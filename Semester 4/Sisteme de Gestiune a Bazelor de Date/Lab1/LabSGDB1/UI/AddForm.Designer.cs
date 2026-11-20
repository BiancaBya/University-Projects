namespace LabSGDB1.UI
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
            this.textBoxTip = new System.Windows.Forms.TextBox();
            this.textBoxNume = new System.Windows.Forms.TextBox();
            this.textBoxCuloare = new System.Windows.Forms.TextBox();
            this.textBoxCapacitate = new System.Windows.Forms.TextBox();
            this.textBoxPret = new System.Windows.Forms.TextBox();
            this.textBoxStoc = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.AddBtnForm = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // textBoxTip
            // 
            this.textBoxTip.AcceptsReturn = true;
            this.textBoxTip.Location = new System.Drawing.Point(246, 62);
            this.textBoxTip.Name = "textBoxTip";
            this.textBoxTip.Size = new System.Drawing.Size(188, 22);
            this.textBoxTip.TabIndex = 0;
            // 
            // textBoxNume
            // 
            this.textBoxNume.Location = new System.Drawing.Point(246, 117);
            this.textBoxNume.Name = "textBoxNume";
            this.textBoxNume.Size = new System.Drawing.Size(188, 22);
            this.textBoxNume.TabIndex = 1;
            // 
            // textBoxCuloare
            // 
            this.textBoxCuloare.Location = new System.Drawing.Point(246, 182);
            this.textBoxCuloare.Name = "textBoxCuloare";
            this.textBoxCuloare.Size = new System.Drawing.Size(188, 22);
            this.textBoxCuloare.TabIndex = 2;
            // 
            // textBoxCapacitate
            // 
            this.textBoxCapacitate.Location = new System.Drawing.Point(246, 243);
            this.textBoxCapacitate.Name = "textBoxCapacitate";
            this.textBoxCapacitate.Size = new System.Drawing.Size(188, 22);
            this.textBoxCapacitate.TabIndex = 3;
            // 
            // textBoxPret
            // 
            this.textBoxPret.Location = new System.Drawing.Point(246, 306);
            this.textBoxPret.Name = "textBoxPret";
            this.textBoxPret.Size = new System.Drawing.Size(188, 22);
            this.textBoxPret.TabIndex = 4;
            // 
            // textBoxStoc
            // 
            this.textBoxStoc.Location = new System.Drawing.Point(246, 365);
            this.textBoxStoc.Name = "textBoxStoc";
            this.textBoxStoc.Size = new System.Drawing.Size(188, 22);
            this.textBoxStoc.TabIndex = 5;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(134, 68);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(27, 16);
            this.label1.TabIndex = 6;
            this.label1.Text = "Tip";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(134, 123);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(43, 16);
            this.label2.TabIndex = 7;
            this.label2.Text = "Nume";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(134, 188);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(54, 16);
            this.label3.TabIndex = 8;
            this.label3.Text = "Culoare";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(134, 249);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(72, 16);
            this.label4.TabIndex = 9;
            this.label4.Text = "Capacitate";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(134, 312);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(31, 16);
            this.label5.TabIndex = 10;
            this.label5.Text = "Pret";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(134, 371);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(34, 16);
            this.label6.TabIndex = 11;
            this.label6.Text = "Stoc";
            // 
            // AddBtnForm
            // 
            this.AddBtnForm.Location = new System.Drawing.Point(589, 197);
            this.AddBtnForm.Name = "AddBtnForm";
            this.AddBtnForm.Size = new System.Drawing.Size(112, 42);
            this.AddBtnForm.TabIndex = 12;
            this.AddBtnForm.Text = "Add";
            this.AddBtnForm.UseVisualStyleBackColor = true;
            this.AddBtnForm.Click += new System.EventHandler(this.button1_Click);
            // 
            // AddForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.ActiveCaption;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.AddBtnForm);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.textBoxStoc);
            this.Controls.Add(this.textBoxPret);
            this.Controls.Add(this.textBoxCapacitate);
            this.Controls.Add(this.textBoxCuloare);
            this.Controls.Add(this.textBoxNume);
            this.Controls.Add(this.textBoxTip);
            this.Name = "AddForm";
            this.Text = "AddForm";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox textBoxTip;
        private System.Windows.Forms.TextBox textBoxNume;
        private System.Windows.Forms.TextBox textBoxCuloare;
        private System.Windows.Forms.TextBox textBoxCapacitate;
        private System.Windows.Forms.TextBox textBoxPret;
        private System.Windows.Forms.TextBox textBoxStoc;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Button AddBtnForm;
    }
}