using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using System.Timers;

namespace CodePulse
{
    public enum HeartbeatCategory
    {
        [Description("coding")] Coding,
        [Description("building")] Building,
        [Description("indexing")] Indexing,
        [Description("debugging")] Debugging,
        [Description("running tests")] RunningTests,
        [Description("writing tests")] WritingTests,
        [Description("manual testing")] ManualTesting,
        [Description("code reviewing")] CodeReviewing,
        [Description("browsing")] Browsing,
        [Description("designing")] Designing,
    }
    public static class EnumExtensions
    {
        public static string GetDescription(this Enum e) => e == null ? (string)null : ((DescriptionAttribute)((IEnumerable<object>)e.GetType().GetField(e.ToString()).GetCustomAttributes(typeof(DescriptionAttribute), false)).FirstOrDefault<object>())?.Description ?? e.ToString();
    }

    public enum EntityType
    {
        [Description("file")] File,
        [Description("domain")] Domain,
        [Description("app")] App,
    }

    public class CodePulse : IDisposable
    {
        public static string CONFIG_FILE = "D:\\temp\\codepulse.cfg";
        public static string LOG_FILE = "D:\\temp\\codepulse.log";
        public static string WINDOWN_ACTIVED_EVENT = "WindowEventsOnWindowActivated";
        private string _lastFile;

        public readonly ConcurrentQueue<Heartbeat> HeartbeatQueue;
        private readonly Timer _heartbeatsProcessTimer;
        private readonly Timer _totalTimeTodayUpdateTimer;
        private readonly CliParameters _cliParameters;

        public ILogger Logger { get; }
        public readonly ConfigFile Config;
        private string _token;

        private DateTime _lastHeartbeat;  // 上一次的心跳时间
        private DateTime _ideOpendedTime; // IDE最近一次打开的时间
        private DayBitSet _currentDayBitSet;

        public CodePulse(ILogger logger)
        {
            this.Logger = logger != null ? logger : throw new ArgumentNullException(nameof(logger));
            this.Config = new ConfigFile(CONFIG_FILE);
            this.HeartbeatQueue = new ConcurrentQueue<Heartbeat>();
            this._token = this.Config.GetSetting("api_key");
            this._heartbeatsProcessTimer = new Timer(10000.0);
            this._totalTimeTodayUpdateTimer = new Timer(60000.0);
            this._lastHeartbeat = DateTime.UtcNow.AddMinutes(-3.0);
            this._cliParameters = new CliParameters();
            this._currentDayBitSet = new DayBitSet();
        }

        public async Task InitializeAsync()
        {
            CodePulse wakaTime = this;
            wakaTime.Logger.Info("Initializing CodePulse...");
            try
            {
                wakaTime._heartbeatsProcessTimer.Elapsed += new ElapsedEventHandler(wakaTime.ProcessHeartbeats);
                wakaTime._heartbeatsProcessTimer.Start();
                wakaTime.UpdateTotalTimeToday((object)null, (ElapsedEventArgs)null);
                wakaTime._totalTimeTodayUpdateTimer.Elapsed += new ElapsedEventHandler(wakaTime.UpdateTotalTimeToday);
                wakaTime._totalTimeTodayUpdateTimer.Start();
                wakaTime.Logger.Info("Finished initializing CodePulse.");
            }
            catch (WebException ex)
            {
                wakaTime.Logger.Error("Are you behind a proxy? Try setting a proxy in WakaTime Settings with format https://user:pass@host:port", (Exception)ex);
            }
            catch (Exception ex)
            {
                wakaTime.Logger.Error("Error installing dependencies", ex);
            }
        }

        public void HandleActivity(string currentFile, bool isWrite, string project, HeartbeatCategory? category = null, EntityType? entityType = null, string eventName = "")
        {
            if (currentFile == null)
                return;
            DateTime utcNow = DateTime.UtcNow;
            if (!isWrite && this._lastFile != null && !this.EnoughTimePassed(utcNow) && currentFile.Equals(this._lastFile))
                return;
            this._lastFile = currentFile;
            this._lastHeartbeat = utcNow;
            if (WINDOWN_ACTIVED_EVENT.Equals(eventName))
            {
                this._ideOpendedTime = utcNow;
            }
            this.AppendHeartbeat(currentFile, isWrite, utcNow, project, category, entityType);
            this.Record();
        }

        // 记录
        private void Record()
        {
            this._currentDayBitSet.clearIfNotToday();
            int currentSlot = this._currentDayBitSet.setSlotByCurrentTime();

            string today = DateTime.Now.ToString("yyyy-MM-dd");
            if (this._ideOpendedTime == null)
            {
                return;
            }

            if (today.Equals(this._ideOpendedTime.ToString("yyyy-MM-dd")))
            {
                int slotMinus = (currentSlot - 1) >= 0 ? currentSlot - 1 : 0;
                if (this._currentDayBitSet.get(slotMinus)) return;  // 说明前一个已经打好点了，不需要再向前trace

                int openedSlot = DateBitSlotUtils.getSlotIndex(this._ideOpendedTime);
                int findVerIndex = -1;
                for (int i = currentSlot - 1; i >= openedSlot; i--)
                {
                    if (_currentDayBitSet.get(i))
                    {
                        findVerIndex = i;
                        break;
                    }
                }

                // 只往前追踪5分钟，间隔10个Slot
                if (findVerIndex >= 0 && findVerIndex < currentSlot && (currentSlot - findVerIndex) < 10)
                {
                    for (int j = findVerIndex + 1; j < currentSlot; j++)
                    {
                        _currentDayBitSet.set(j);
                    }
                }
            }
        }

        private bool EnoughTimePassed(DateTime now) => this._lastHeartbeat < now.AddMinutes(-2.0);

        private static string ToUnixEpoch(DateTime date)
        {
            DateTime dateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0, DateTimeKind.Utc);
            TimeSpan timeSpan = date - dateTime;
            return string.Format("{0}.{1}", (object)Convert.ToInt64(Math.Floor(timeSpan.TotalSeconds)), (object)timeSpan.ToString("ffffff"));
        }

        private void ProcessHeartbeats(object sender, ElapsedEventArgs e) => Task.Run((Action)(() => this.ProcessHeartbeats()));

        private void ProcessHeartbeats()
        {
            try
            {
                Heartbeat result1;
                if (!this.HeartbeatQueue.TryDequeue(out result1))
                    return;
                Collection<Heartbeat> instance = new Collection<Heartbeat>();
                Heartbeat result2;
                while (this.HeartbeatQueue.TryDequeue(out result2))
                    instance.Add(result2);
                bool flag = instance.Count > 0;

                this._cliParameters.File = result1.Entity;
                this._cliParameters.Time = result1.Timestamp;
                this._cliParameters.IsWrite = result1.IsWrite;
                this._cliParameters.Project = result1.Project;
                this._cliParameters.Category = result1.Category;
                this._cliParameters.EntityType = result1.EntityType;
                this._cliParameters.HasExtraHeartbeats = flag;
                string currentToken = _token;
                string stdin = (string)null;

                if (flag && !string.IsNullOrEmpty(this._token))
                {
                    // 处理中
                    this.Logger.Info("上报处理...count:" + this._currentDayBitSet.countOfCodingSlot());
                    DataSenderHelper.Post(_currentDayBitSet, this._token);
                }

            }
            catch (Exception ex)
            {
                this.Logger.Error("Error processing heartbeat(s).", ex);
            }
        }


        private void AppendHeartbeat(string fileName, bool isWrite, DateTime time, string project, HeartbeatCategory? category, EntityType? entityType)
        {
            this.HeartbeatQueue.Enqueue(new Heartbeat()
            {
                Entity = fileName,
                Timestamp = ToUnixEpoch(time),
                DT = time,
                IsWrite = isWrite,
                Project = project,
                Category = category,
                EntityType = entityType
            });
        }

        private void UpdateTotalTimeToday(object sender, ElapsedEventArgs e) => Task.Run((Action)(() => this.UpdateTotalTimeToday()));

        private void UpdateTotalTimeToday()
        {

        }

        public void Dispose()
        {
            if (this._heartbeatsProcessTimer != null)
            {
                this._heartbeatsProcessTimer.Stop();
                this._heartbeatsProcessTimer.Elapsed -= new ElapsedEventHandler(this.ProcessHeartbeats);
                this._heartbeatsProcessTimer.Dispose();
            }
            if (this._totalTimeTodayUpdateTimer != null)
            {
                this._totalTimeTodayUpdateTimer.Stop();
                this._totalTimeTodayUpdateTimer.Elapsed -= new ElapsedEventHandler(this.UpdateTotalTimeToday);
                this._totalTimeTodayUpdateTimer.Dispose();
            }
            this.ProcessHeartbeats();
        }
    }
}
