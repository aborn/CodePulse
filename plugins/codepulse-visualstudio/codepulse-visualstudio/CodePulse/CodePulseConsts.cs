using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodePulse
{
    public class CodePulseConsts
    {
        public static string CODE_PULSE = "CodePulse";
        public static string ROOT_PATH = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), CODE_PULSE);
        public static string CONFIG_FILE = Path.Combine(ROOT_PATH,  CODE_PULSE.ToLower() + ".cfg");
        public static string LOG_FILE = Path.Combine(ROOT_PATH, CODE_PULSE.ToLower() + ".log");
    }
}
