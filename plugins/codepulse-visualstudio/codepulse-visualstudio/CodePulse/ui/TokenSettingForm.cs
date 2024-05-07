using Microsoft.Build.Framework;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CodePulse
{
    public partial class TokenSettingForm : Form
    {
        private readonly ConfigFile _configFile;
        private readonly ILogger _logger;

        public TokenSettingForm(ConfigFile configFile, ILogger logger)
        {
            _configFile = configFile;
            _logger = logger;

            InitializeComponent();
        }

        private void ApiKeyForm_Load(object sender, EventArgs e)
        {
            try
            {
                txtAPIKey.Text = _configFile.GetSetting(CodePulseConst.CONFIG_KEY_TOKEN);
                string apiURL = _configFile.GetSetting(CodePulseConst.CONFIG_KEY_URL);
                txtAPIURL.Text = string.IsNullOrWhiteSpace(apiURL) ? DataSenderHelper.API : apiURL;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void BtnOk_Click(object sender, EventArgs e)
        {
            try
            {
                var matched = true; //Regex.IsMatch(txtAPIKey.Text.Trim(), "(?im)^(waka_)?[0-9A-F]{8}[-]?(?:[0-9A-F]{4}[-]?){3}[0-9A-F]{12}$");

                if (matched)
                {
                    _configFile.SaveSetting("settings", CodePulseConst.CONFIG_KEY_TOKEN, txtAPIKey.Text.Trim());
                    _configFile.SaveSetting("settings", CodePulseConst.CONFIG_KEY_URL, txtAPIURL.Text.Trim());
                }
                else
                {
                    MessageBox.Show("请输入正确的TOKEN.");

                    DialogResult = DialogResult.None; // do not close dialog box
                }
            }
            catch (Exception ex)
            {
                _logger.Error($"Error saving data from ApiKeyForm: {ex}");

                MessageBox.Show(ex.Message);
            }
        }
    }
}
