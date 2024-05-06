using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodePulse
{
    public class CliParameters
    {
        public string Key { get; set; }

        public string File { get; set; }

        public string Time { get; set; }

        public string Plugin { get; set; }

        public HeartbeatCategory? Category { get; set; }

        public EntityType? EntityType { get; set; }

        public bool IsWrite { get; set; }

        public string Project { get; set; }

        public bool HasExtraHeartbeats { get; set; }

        public string[] ToArray(bool obfuscate = false)
        {
            Collection<string> source = new Collection<string>()
            {
                "--key",
                obfuscate ? "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXX" + this.Key.Substring(this.Key.Length - 4) : this.Key,
                "--entity",
                this.File,
                "--time",
                this.Time,
                "--plugin",
                this.Plugin
            };
            if (this.Category.HasValue)
            {
                source.Add("--category");
                source.Add(((Enum)(ValueType)this.Category).GetDescription());
            }
            if (this.EntityType.HasValue)
            {
                source.Add("--entity-type");
                source.Add(((Enum)(ValueType)this.EntityType).GetDescription());
            }
            if (!string.IsNullOrEmpty(this.Project))
            {
                source.Add("--alternate-project");
                source.Add(this.Project);
            }
            if (this.IsWrite)
                source.Add("--write");
            if (this.HasExtraHeartbeats)
                source.Add("--extra-heartbeats");
            return source.ToArray<string>();
        }
    }
}
