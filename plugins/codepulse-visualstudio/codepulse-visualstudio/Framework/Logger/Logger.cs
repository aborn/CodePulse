using System;
using System.IO;

namespace CodePulse
{
    public class Logger : ILogger
    {
        private readonly bool _isDebugEnabled;

        public Logger(string configFilepath)
        {
            var configFile = new ConfigFile(configFilepath);

            _isDebugEnabled = true; //configFile.GetSettingAsBoolean("debug");
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
            WriteLogToFile(message);
        }


        public static void WriteLogToFile(string str)
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
