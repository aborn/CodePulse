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
        public static readonly Guid GuidWakatimeOutputPane = new Guid("c74266cb-ac06-4f69-915e-503598095b0e");
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
