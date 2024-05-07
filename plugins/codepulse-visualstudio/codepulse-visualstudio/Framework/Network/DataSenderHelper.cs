using CodePulse.Framework.Network;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Text;

namespace CodePulse
{
    public class DataSenderHelper
    {
        public static string API = "https://cp.popkit.org/api/v1/codepulse/userAction";
        public static string API_TEST = "http://192.168.31.154:8001/api/v1/codepulse/userAction";
        public static void Main(string[] args)
        {
            // GET http://192.168.31.154:8001/api/v1/codepulse/status
            // POST http://192.168.31.154:8001/api/v1/codepulse/userAction
            string url = "http://192.168.31.154:8001/api/v1/codepulse/userAction";
            Dictionary<string, object> dictionary = new Dictionary<string, object>();
            string token = "0xa";
            DateTime dt = DateTime.Now;
            string day = dt.ToString("yyyy-MM-dd");
            DayBitSet dayBitSet = new DayBitSet(day);
            dayBitSet.set(111);
            dayBitSet.set(DayBitSet.SLOT_SIZE - 1);
            dayBitSet.set(1);
            int slot = dayBitSet.setSlotByCurrentTime();

            string postJSONData = dayBitSet.toJSONString(token);

            byte[] dBytes = dayBitSet.getDayBitSetByteArray();
            DayBitSet dayBitSet2 = new DayBitSet(day, dBytes);
            bool b1 = dayBitSet2.get(1);
            bool b2 = dayBitSet2.get(2);
            bool b111 = dayBitSet2.get(111);
            bool bMax = dayBitSet2.get(DayBitSet.SLOT_SIZE - 1);
            bool bMax2 = dayBitSet2.get(DayBitSet.SLOT_SIZE - 2);
            bool bMax3 = dayBitSet2.get(DayBitSet.SLOT_SIZE - 3);
            bool slotV = dayBitSet2.get(slot);

            //dictionary.Add("dayBitSetArray", dBytes);
            try
            {
                string result = Post(url, postJSONData);
                Console.WriteLine(result);
                JObject jObject = JObject.Parse(result);
                string data = (string)jObject.GetValue("data");
                bool status = (bool)jObject.GetValue("status");   // true表示成功
                string msg = (string)jObject.GetValue("msg");     // 
                int code = (int)jObject.GetValue("code");         // 200表示成功
                string a = "";
            }
            catch (Exception e)
            {
                string msg = e.Message;
            }
        }

        public static SimpleResult Post(DayBitSet dayBitSet, string token, string url)
        {
            SimpleResult simpleResult = new SimpleResult();
            try
            {
                string api = string.IsNullOrWhiteSpace(url) ? API : url;
                string postJSONData = dayBitSet.toJSONString(token);
                string result = Post(api, postJSONData);
                Console.WriteLine(result);
                JObject jObject = JObject.Parse(result);
                simpleResult.data = (string)jObject.GetValue("data");
                simpleResult.status = (bool)jObject.GetValue("status");    // true表示成功
                simpleResult.msg = (string)jObject.GetValue("msg");        // 消息
                simpleResult.code = (int)jObject.GetValue("code");         // 200表示成功
            }
            catch (Exception e)
            {
                simpleResult.msg = e.Message;
                simpleResult.status = false;
                simpleResult.code = 555;
            }
            return simpleResult;
        }

        private static string Post(string url, string postData)
        {
            string result = "";
            HttpWebRequest req = (HttpWebRequest)WebRequest.Create(url);
            req.Method = "POST";
            req.ContentType = "application/json; charset=utf-8";
            req.Accept = "application/json";
            req.Timeout = 10000;


            byte[] data = Encoding.UTF8.GetBytes(postData);

            req.ContentLength = data.Length;

            using (Stream reqStream = req.GetRequestStream())
            {
                reqStream.Write(data, 0, data.Length);

                reqStream.Close();
            }

            HttpWebResponse resp = (HttpWebResponse)req.GetResponse();

            Stream stream = resp.GetResponseStream();

            //获取响应内容
            using (StreamReader reader = new StreamReader(stream, Encoding.UTF8))
            {
                result = reader.ReadToEnd();
            }

            return result;
        }
    }
}
