using Microsoft.VisualStudio.Shell.Interop;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.Shell;

namespace CodePulse
{
    public class Logger : ILogger
    {
        private IVsOutputWindowPane _wakatimeOutputWindowPane;
        private IVsOutputWindowPane WakatimeOutputWindowPane =>
            _wakatimeOutputWindowPane ?? (_wakatimeOutputWindowPane = GetWakatimeOutputWindowPane());
        private readonly bool _isDebugEnabled;

        public Logger(string configFilepath)
        {
            var configFile = new ConfigFile(configFilepath);

            _isDebugEnabled = true; //configFile.GetSettingAsBoolean("debug");
        }

        private static IVsOutputWindowPane GetWakatimeOutputWindowPane()
        {
            if (!(Package.GetGlobalService(typeof(SVsOutputWindow)) is IVsOutputWindow outputWindow)) return null;

            var outputPaneGuid = new Guid(GuidList.GuidWakatimeOutputPane.ToByteArray());

            outputWindow.CreatePane(ref outputPaneGuid, "CodePulse", 1, 1);
            outputWindow.GetPane(ref outputPaneGuid, out var windowPane);

            return windowPane;
        }

        public void Debug(string message)
        {
            if (!_isDebugEnabled)
                return;

            Log(LogLevel.Debug, message);
        }

        public void Error(string message, Exception ex = null)
        {
            var exceptionMessage = $"{message}: {ex}";

            Log(LogLevel.HandledException, exceptionMessage);
        }

        public void Warning(string message)
        {
            Log(LogLevel.Warning, message);
        }

        public void Info(string message)
        {
            Log(LogLevel.Info, message);
        }

        private void Log(LogLevel level, string message)
        {
            write_log(message);
            var outputWindowPane = WakatimeOutputWindowPane;
            if (outputWindowPane == null) return;

            var outputMessage =
                $"[CodePulse {Enum.GetName(level.GetType(), level)} {DateTime.Now.ToString("hh:mm:ss tt", CultureInfo.InvariantCulture)}] {message}{Environment.NewLine}";
            outputWindowPane.OutputString(outputMessage);
        }


        public static void write_log(string str)
        {
            string strlog = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss") + ": " + str + "\r\n";
            
            using (FileStream fs = new FileStream(CodePulseConst.LOG_FILE, FileMode.Append, FileAccess.Write, FileShare.ReadWrite))
            {
                using (StreamWriter sw = new StreamWriter(fs))
                {
                    sw.Write(strlog);
                    sw.Flush();
                    sw.Close();
                    fs.Close();
                }
            }
        }
    }
}
