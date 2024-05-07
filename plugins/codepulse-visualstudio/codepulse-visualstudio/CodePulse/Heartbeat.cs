using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodePulse
{
    public class Heartbeat
    {
        public string Entity { get; set; }

        public string Timestamp { get; set; }

        public DateTime DT { get; set; }

        public string Project { get; set; }

        public bool IsWrite { get; set; }

        public HeartbeatCategory? Category { get; set; }

        public EntityType? EntityType { get; set; }

        public override string ToString() => "{\"entity\":\"" + this.Entity.Replace("\\", "\\\\").Replace("\"", "\\\"") + "\",\"timestamp\":" + this.Timestamp + ",\"alternate_project\":\"" + this.Project.Replace("\"", "\\\"") + "\",\"is_write\":" + this.IsWrite.ToString().ToLower() + ",\"category\":\"" + ((Enum)(ValueType)this.Category).GetDescription() + "\",\"entity_type\":\"" + ((Enum)(ValueType)this.EntityType).GetDescription() + "\"}";
    }
}
