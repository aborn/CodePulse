using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodePulse
{
    public class DateSlotUtils
    {
        public static int GetSlotIndex(DateTime dateTime)
        {
            DateTime dt = dateTime == null ? DateTime.Now : dateTime;
            // 获取小时数
            int hour = dt.Hour;
            // 获取分钟数
            int minute = dt.Minute;
            // 获取秒数
            int second = dt.Second;
            return hour * 60 * 2 + minute * 2 + (second / 30);
        }

        public static double DiffSeconds(DateTime startTime, DateTime endTime)
        {
            TimeSpan secondSpan = new TimeSpan(endTime.Ticks - startTime.Ticks);
            return secondSpan.TotalSeconds;
        }
    }
}
