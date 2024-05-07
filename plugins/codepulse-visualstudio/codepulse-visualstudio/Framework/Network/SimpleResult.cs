using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodePulse.Framework.Network
{
    public class SimpleResult
    {
        public string data { get; set; }
        public bool status { get; set; }     // true表示成功
        public string msg { get; set; }      // 
        public int code { get; set; }     // 200表示成功
    }
}
