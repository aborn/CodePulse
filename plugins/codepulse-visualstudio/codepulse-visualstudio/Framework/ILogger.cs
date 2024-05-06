using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodePulse
{
    public interface ILogger
    {
        void Debug(string message);

        void Error(string message, Exception ex = null);

        void Warning(string message);

        void Info(string message);
    }
}
