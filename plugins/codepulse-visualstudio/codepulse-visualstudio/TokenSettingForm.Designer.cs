using Microsoft.Build.Framework;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using System;

namespace CodePulse
{
    partial class TokenSettingForm
    {
        private System.Windows.Forms.Label lblAPIKey;
        private System.Windows.Forms.TextBox txtAPIKey;
        private System.Windows.Forms.Button btnOk;
        private System.Windows.Forms.LinkLabel linkLabel1;
        private System.Windows.Forms.Label label2;

        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;
        

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.lblAPIKey = new System.Windows.Forms.Label();
            this.txtAPIKey = new System.Windows.Forms.TextBox();
            this.btnOk = new System.Windows.Forms.Button();
            this.linkLabel1 = new System.Windows.Forms.LinkLabel();
            this.label2 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // lblAPIKey
            // 
            this.lblAPIKey.AutoSize = true;
            this.lblAPIKey.Location = new System.Drawing.Point(12, 45);
            this.lblAPIKey.Name = "lblAPIKey";
            this.lblAPIKey.Size = new System.Drawing.Size(143, 25);
            this.lblAPIKey.TabIndex = 0;
            this.lblAPIKey.Text = "码脉账号Token :";
            // 
            // txtAPIKey
            // 
            this.txtAPIKey.Location = new System.Drawing.Point(161, 42);
            this.txtAPIKey.MaxLength = 41;
            this.txtAPIKey.Name = "txtAPIKey";
            this.txtAPIKey.Size = new System.Drawing.Size(394, 31);
            this.txtAPIKey.TabIndex = 1;
            // 
            // btnOk
            // 
            this.btnOk.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.btnOk.DialogResult = System.Windows.Forms.DialogResult.OK;
            this.btnOk.Location = new System.Drawing.Point(286, 87);
            this.btnOk.Name = "btnOk";
            this.btnOk.Size = new System.Drawing.Size(87, 35);
            this.btnOk.TabIndex = 2;
            this.btnOk.Text = "确定";
            this.btnOk.UseVisualStyleBackColor = true;
            this.btnOk.Click += new System.EventHandler(this.BtnOk_Click);
            // 
            // linkLabel1
            // 
            this.linkLabel1.AutoSize = true;
            this.linkLabel1.LinkArea = new System.Windows.Forms.LinkArea(0, 90);
            this.linkLabel1.LinkBehavior = System.Windows.Forms.LinkBehavior.AlwaysUnderline;
            this.linkLabel1.Location = new System.Drawing.Point(163, 9);
            this.linkLabel1.Name = "linkLabel1";
            this.linkLabel1.Size = new System.Drawing.Size(182, 30);
            this.linkLabel1.TabIndex = 4;
            this.linkLabel1.TabStop = true;
            this.linkLabel1.Text = "https://cp.popkit.org/";
            this.linkLabel1.UseCompatibleTextRendering = true;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(13, 11);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(144, 25);
            this.label2.TabIndex = 5;
            this.label2.Text = "账号申请? 访问 ";
            // 
            // TokenSettingForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(144F, 144F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
            this.ClientSize = new System.Drawing.Size(587, 134);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.linkLabel1);
            this.Controls.Add(this.btnOk);
            this.Controls.Add(this.txtAPIKey);
            this.Controls.Add(this.lblAPIKey);
            this.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "TokenSettingForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "码脉";
            this.Load += new System.EventHandler(this.ApiKeyForm_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

       
    }

}