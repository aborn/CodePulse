using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.IO;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using System.Timers;
using CodePulse.Framework.Network;

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
        public static string WINDOWN_ACTIVED_EVENT = "WindowEventsOnWindowActivated";
        private string _lastFile;

        public readonly ConcurrentQueue<Heartbeat> HeartbeatQueue;
        private readonly Timer _heartbeatsProcessTimer;
        private readonly Timer _postDataTimer; // 数据上报的Timer
        private readonly CliParameters _cliParameters;

        public ILogger Logger { get; }
        public readonly ConfigFile Config;
        private string _token;

        private DateTime _lastHeartbeat;  // 上一次的心跳时间
        private DateTime _lastPostTime;   // 上一次数据上报成功的时间
        private int _lastPostCountOfCoding = 0;  // 上一次上报数据的count数
        private DateTime _ideActivedTime;        // IDE最近一次打开的时间，但当前没这个事件
        private DayBitSet _currentDayBitSet;

        public CodePulse(ILogger logger)
        {
            this.Logger = logger != null ? logger : throw new ArgumentNullException(nameof(logger));

            this.Config = new ConfigFile(CodePulseConsts.CONFIG_FILE);
            this.HeartbeatQueue = new ConcurrentQueue<Heartbeat>();
            this._token = this.Config.GetSetting("api_key");
            this._heartbeatsProcessTimer = new Timer(10000.0);
            this._postDataTimer = new Timer(30000.0);  // 每30s上报一次数据
            this._lastHeartbeat = DateTime.UtcNow.AddMinutes(-3.0);
            this._cliParameters = new CliParameters();
            this._currentDayBitSet = new DayBitSet();
        }


        public async Task InitializeAsync()
        {
            CodePulse codePulse = this;
            codePulse.Logger.Info("Initializing CodePulse...");
            try
            {
                codePulse._heartbeatsProcessTimer.Elapsed += new ElapsedEventHandler(codePulse.ProcessHeartbeats);
                codePulse._heartbeatsProcessTimer.Start();

                codePulse._postDataTimer.Elapsed += new ElapsedEventHandler(codePulse.PostCodingTimeToServer);
                codePulse._postDataTimer.Start();


                codePulse.Logger.Info("Finished initializing CodePulse.");
            }
            catch (WebException ex)
            {
                codePulse.Logger.Error("Are you behind a proxy? Try setting a proxy in WakaTime Settings with format https://user:pass@host:port", (Exception)ex);
            }
            catch (Exception ex)
            {
                codePulse.Logger.Error("Error installing dependencies", ex);
            }
        }

        public void HandleActivity(string currentFile, bool isWrite, string project, HeartbeatCategory? category = null, EntityType? entityType = null, string eventName = "")
        {
            this.Logger.Info("HandleActivity...:" + eventName + ", fileName:" + currentFile);
            if (currentFile == null)
                return;

            this.Record();
            DateTime utcNow = DateTime.UtcNow;
            if (WINDOWN_ACTIVED_EVENT.Equals(eventName))
            {
                this._ideActivedTime = utcNow;
            }

            if (!isWrite && this._lastFile != null && !this.EnoughTimePassed(utcNow) && currentFile.Equals(this._lastFile))
                return;
            this._lastFile = currentFile;
            this._lastHeartbeat = utcNow;
            this.AppendHeartbeat(currentFile, isWrite, utcNow, project, category, entityType);
        }

        // 记录
        private void Record()
        {
            this._currentDayBitSet.clearIfNotToday();
            int currentSlot = this._currentDayBitSet.setSlotByCurrentTime();
            this.Logger.Info("--> 记录...count:" + this._currentDayBitSet.countOfCodingSlot());
            string today = DateTime.Now.ToString("yyyy-MM-dd");
            if (this._ideActivedTime == null)
            {
                return;
            }

            // 往前追加：有这种场景，打开了ide，打了一个点；过了2分钟，又打了个点，中间的点没打上，需要追加
            if (today.Equals(this._ideActivedTime.ToString("yyyy-MM-dd")))
            {
                int slotMinus = (currentSlot - 1) >= 0 ? currentSlot - 1 : 0;
                if (this._currentDayBitSet.get(slotMinus)) return;  // 说明前一个已经打好点了，不需要再向前trace

                int openedSlot = DateSlotUtils.GetSlotIndex(this._ideActivedTime);
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
            this.Logger.Info("--> 记录完成eee...count:" + this._currentDayBitSet.countOfCodingSlot());
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

                if (flag)
                {

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

        private void PostCodingTimeToServer(object sender, ElapsedEventArgs e) => Task.Run((Action)(() => this.PostCodingTimeToServer()));

        private void PostCodingTimeToServer()
        {
            if (!string.IsNullOrWhiteSpace(_token))
            {
                int count = this._currentDayBitSet.countOfCodingSlot();
                if (count <= 0) return;

                // 没有变化不，不再频繁上报
                DateTime nowTime = DateTime.Now;
                if (_lastPostCountOfCoding == count && _lastPostTime != null && DateSlotUtils.DiffSeconds(_lastPostTime, nowTime) < 300)
                {
                    double timeDiff = DateSlotUtils.DiffSeconds(_lastPostTime, nowTime);
                    this.Logger.Info("不上报，时差：" + timeDiff + ", LastCount:" + _lastPostCountOfCoding + ", CurrentCount:" + count);
                    return;
                }

                this.Logger.Info("上报处理...count:" + count);
                SimpleResult simpleResult = DataSenderHelper.Post(_currentDayBitSet, this._token);
                if (simpleResult.status && simpleResult.code == 200)
                {
                    _lastPostCountOfCoding = count;
                    _lastPostTime = DateTime.Now;
                }

                this.Logger.Info("上报结果：" + simpleResult.status + ", code:" + simpleResult.code + ", msg:" +
                                 simpleResult.msg + ", data:" + simpleResult.data);
            }
        }

        public void Dispose()
        {
            if (this._heartbeatsProcessTimer != null)
            {
                this._heartbeatsProcessTimer.Stop();
                this._heartbeatsProcessTimer.Elapsed -= new ElapsedEventHandler(this.ProcessHeartbeats);
                this._heartbeatsProcessTimer.Dispose();
            }
            if (this._postDataTimer != null)
            {
                this._postDataTimer.Stop();
                this._postDataTimer.Elapsed -= new ElapsedEventHandler(this.PostCodingTimeToServer);
                this._postDataTimer.Dispose();
            }
            this.ProcessHeartbeats();
        }
    }
}
