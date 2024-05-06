using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace CodePulse
{
    public static class GuidList
    {
        public const string GuidWakaTimePkgString = "52d9c3ff-c893-408e-95e4-d7484ec7fa47";
        public const string GuidWakaTimeUIString = "ADFC4E64-0397-11D1-9F4E-00A0C911004F";
        public const string GuidWakaTimeCmdSetString = "054caf12-7fba-40d1-8dc8-bd69f838b910";
        public static readonly Guid GuidWakatimeOutputPane = new Guid("a635ec18-1b8f-468d-832e-8e5eda489815");
    }

    internal static class NativeMethods
    {
        [DllImport("kernel32", CharSet = CharSet.Auto, SetLastError = true)]
        internal static extern bool WritePrivateProfileString(
            [MarshalAs(UnmanagedType.LPWStr)] string section,
            [MarshalAs(UnmanagedType.LPWStr)] string key,
            [MarshalAs(UnmanagedType.LPWStr)] string val,
            [MarshalAs(UnmanagedType.LPWStr)] string filePath);

        [DllImport("kernel32", CharSet = CharSet.Auto, SetLastError = true)]
        internal static extern uint GetPrivateProfileString(
            [MarshalAs(UnmanagedType.LPWStr)] string section,
            [MarshalAs(UnmanagedType.LPWStr)] string key,
            [MarshalAs(UnmanagedType.LPWStr)] string def,
            [MarshalAs(UnmanagedType.LPWStr)] StringBuilder retVal,
            int size,
            [MarshalAs(UnmanagedType.LPWStr)] string filePath);

        [DllImport("kernel32.dll", SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        internal static extern bool IsWow64Process([In] IntPtr hProcess, out bool wow64Process);
    }
}
