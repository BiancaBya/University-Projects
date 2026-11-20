namespace LabSGDB2.UI
{
    partial class AddForm
    {
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
            this.AddBtnForm = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // AddBtnForm
            // 
            this.AddBtnForm.Location = new System.Drawing.Point(589, 197);
            this.AddBtnForm.Name = "AddBtnForm";
            this.AddBtnForm.Size = new System.Drawing.Size(112, 42);
            this.AddBtnForm.TabIndex = 12;
            this.AddBtnForm.Text = "Add";
            this.AddBtnForm.UseVisualStyleBackColor = true;
            this.AddBtnForm.Click += new System.EventHandler(this.Add_Click);
            // 
            // AddForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.ActiveCaption;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.AddBtnForm);
            this.Name = "AddForm";
            this.Text = "AddForm";
            this.Load += new System.EventHandler(this.UI_Load);
            this.ResumeLayout(false);

        }

        #endregion
        private System.Windows.Forms.Button AddBtnForm;
    }
}