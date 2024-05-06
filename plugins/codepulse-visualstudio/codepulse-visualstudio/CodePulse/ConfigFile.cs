using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodePulse
{
    public class ConfigFile
    {
        public readonly string ConfigFilepath;

        public ConfigFile(string configFilepath) => this.ConfigFilepath = configFilepath;

        public string GetSetting(string key, string section = "settings")
        {
            StringBuilder retVal = new StringBuilder((int)byte.MaxValue);
            return NativeMethods.GetPrivateProfileString(section, key, "", retVal, (int)byte.MaxValue, this.ConfigFilepath) <= 0U ? string.Empty : retVal.ToString();
        }

        public bool GetSettingAsBoolean(string key, bool @default = false, string section = "settings")
        {
            StringBuilder retVal = new StringBuilder((int)byte.MaxValue);
            bool result;
            return NativeMethods.GetPrivateProfileString(section, key, @default.ToString(), retVal, (int)byte.MaxValue, this.ConfigFilepath) > 0U && bool.TryParse(retVal.ToString(), out result) ? result : @default;
        }

        public void SaveSetting(string section, string key, string value)
        {
            if (bool.TryParse(value.Trim(), out bool _))
                NativeMethods.WritePrivateProfileString(section, key, value.Trim().ToLower(), this.ConfigFilepath);
            else
                NativeMethods.WritePrivateProfileString(section, key, value.Trim(), this.ConfigFilepath);
        }
    }
}