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
        private string _lastFile;
        private DateTime _lastHeartbeat;
        public readonly ConcurrentQueue<Heartbeat> HeartbeatQueue;
        private readonly Timer _heartbeatsProcessTimer;
        private readonly Timer _totalTimeTodayUpdateTimer;
        private readonly CliParameters _cliParameters;

        public ILogger Logger { get; }
        public readonly ConfigFile Config;
        private string _token;

        public CodePulse(ILogger logger)
        {
            this.Logger = logger != null ? logger : throw new ArgumentNullException(nameof(logger));
            this.Config = new ConfigFile(CONFIG_FILE);
            this.HeartbeatQueue = new ConcurrentQueue<Heartbeat>();
            this._token = this.Config.GetSetting("api_key");
            this._heartbeatsProcessTimer = new Timer(10000.0);
            this._totalTimeTodayUpdateTimer = new Timer(60000.0);
            this._lastHeartbeat = DateTime.UtcNow.AddMinutes(-3.0);
        }

        public async Task InitializeAsync()
        {
            CodePulse wakaTime = this;
            wakaTime.Logger.Info("Initializing WakaTime v");
            try
            {
                wakaTime._heartbeatsProcessTimer.Elapsed += new ElapsedEventHandler(wakaTime.ProcessHeartbeats);
                wakaTime._heartbeatsProcessTimer.Start();
                wakaTime.UpdateTotalTimeToday((object)null, (ElapsedEventArgs)null);
                wakaTime._totalTimeTodayUpdateTimer.Elapsed += new ElapsedEventHandler(wakaTime.UpdateTotalTimeToday);
                wakaTime._totalTimeTodayUpdateTimer.Start();
                wakaTime.Logger.Info("Finished initializing WakaTime v");
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

        public void HandleActivity(string currentFile, bool isWrite, string project, HeartbeatCategory? category = null, EntityType? entityType = null)
        {
            if (currentFile == null)
                return;
            DateTime utcNow = DateTime.UtcNow;
            if (!isWrite && this._lastFile != null && !this.EnoughTimePassed(utcNow) && currentFile.Equals(this._lastFile))
                return;
            this._lastFile = currentFile;
            this._lastHeartbeat = utcNow;
            this.AppendHeartbeat(currentFile, isWrite, utcNow, project, category, entityType);
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
                string stdin = (string)null;

                if (flag)
                {
                    // 处理中
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
