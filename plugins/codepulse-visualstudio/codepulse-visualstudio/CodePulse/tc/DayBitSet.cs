using Newtonsoft.Json.Linq;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodePulse
{
    public class DayBitSet
    {
        private string day;
        private BitArray codingBitSet;
        public static int SLOT_SIZE = 24 * 60 * 2;

        public DayBitSet()
        {
            this.day = DateTime.Now.ToString("yyyy-MM-dd");
            codingBitSet = new BitArray(SLOT_SIZE);
        }

        public DayBitSet(string day)
        {
            this.day = day;
            codingBitSet = new BitArray(SLOT_SIZE);
        }

        public DayBitSet(string day, byte[] bytes)
        {
            this.day = day;
            codingBitSet = new BitArray(bytes);
        }

        public void set(int slot)
        {
            this.codingBitSet.Set(slot, true);
        }

        public bool get(int slot)
        {
            return this.codingBitSet.Get(slot);
        }

        public bool isToday()
        {
            string today = DateTime.Now.ToString("yyyy-MM-dd");
            return today.Equals(this.day);
        }

        public void clearIfNotToday()
        {
            if (isToday())
            {
                return;
            }

            this.day = DateTime.Now.ToString("yyyy-MM-dd");
            this.codingBitSet.SetAll(false);
        }

        public int setSlotByCurrentTime()
        {
            int slot = DateBitSlotUtils.getSlotIndex(DateTime.Now);
            this.set(slot);
            return slot;
        }

        public int countOfCodingSlot()
        {
            int count = 0;
            for (int i = 0; i < SLOT_SIZE; i++)
            {
                if (this.codingBitSet.Get(i)) count++;
            }
            return count;
        }

        public byte[] getDayBitSetByteArray()
        {
            byte[] ret = new byte[(codingBitSet.Length - 1) / 8 + 1];
            codingBitSet.CopyTo(ret, 0);
            return ret;
        }

        public string toJSONString(string token)
        {
            JObject jObject = new JObject();
            jObject.Add("day", this.day);
            jObject.Add("token", token);
            jObject.Add("dayBitSetArray", this.getDayBitSetByteArray());
            jObject.Add("ide", 3);  // 1-表示intellij, 2-表示vscode, 3-表示vs

            return jObject.ToString();
        }
    }
}
