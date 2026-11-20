namespace Examen
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
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
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            dataGridView1 = new DataGridView();
            dataGridView2 = new DataGridView();
            AddBtn = new Button();
            DeleteBtn = new Button();
            UpdateBtn = new Button();
            textBoxCodCopil = new TextBox();
            textBoxCodGrupa = new TextBox();
            textBoxNume = new TextBox();
            textBoxVarsta = new TextBox();
            ((System.ComponentModel.ISupportInitialize)dataGridView1).BeginInit();
            ((System.ComponentModel.ISupportInitialize)dataGridView2).BeginInit();
            SuspendLayout();
            // 
            // dataGridView1
            // 
            dataGridView1.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            dataGridView1.Location = new Point(35, 24);
            dataGridView1.Name = "dataGridView1";
            dataGridView1.RowHeadersWidth = 51;
            dataGridView1.Size = new Size(588, 236);
            dataGridView1.TabIndex = 0;
            dataGridView1.CellClick += dataGridView1_CellClick;
            // 
            // dataGridView2
            // 
            dataGridView2.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            dataGridView2.Location = new Point(35, 303);
            dataGridView2.Name = "dataGridView2";
            dataGridView2.RowHeadersWidth = 51;
            dataGridView2.Size = new Size(588, 226);
            dataGridView2.TabIndex = 1;
            dataGridView2.CellClick += dataGridView2_CellClick;
            // 
            // AddBtn
            // 
            AddBtn.Location = new Point(784, 77);
            AddBtn.Name = "AddBtn";
            AddBtn.Size = new Size(94, 29);
            AddBtn.TabIndex = 2;
            AddBtn.Text = "Add";
            AddBtn.UseVisualStyleBackColor = true;
            AddBtn.Click += AddBtn_Click;
            // 
            // DeleteBtn
            // 
            DeleteBtn.Location = new Point(784, 131);
            DeleteBtn.Name = "DeleteBtn";
            DeleteBtn.Size = new Size(94, 29);
            DeleteBtn.TabIndex = 3;
            DeleteBtn.Text = "Delete";
            DeleteBtn.UseVisualStyleBackColor = true;
            DeleteBtn.Click += DeleteBtn_Click;
            // 
            // UpdateBtn
            // 
            UpdateBtn.Location = new Point(784, 186);
            UpdateBtn.Name = "UpdateBtn";
            UpdateBtn.Size = new Size(94, 29);
            UpdateBtn.TabIndex = 4;
            UpdateBtn.Text = "Update";
            UpdateBtn.UseVisualStyleBackColor = true;
            UpdateBtn.Click += UpdateBtn_Click;
            // 
            // textBoxCodCopil
            // 
            textBoxCodCopil.Location = new Point(793, 303);
            textBoxCodCopil.Name = "textBoxCodCopil";
            textBoxCodCopil.Size = new Size(152, 27);
            textBoxCodCopil.TabIndex = 5;
            textBoxCodCopil.Text = "cod copil";
            // 
            // textBoxCodGrupa
            // 
            textBoxCodGrupa.Location = new Point(793, 475);
            textBoxCodGrupa.Name = "textBoxCodGrupa";
            textBoxCodGrupa.Size = new Size(152, 27);
            textBoxCodGrupa.TabIndex = 6;
            textBoxCodGrupa.Text = "cod grupa";
            // 
            // textBoxNume
            // 
            textBoxNume.Location = new Point(793, 364);
            textBoxNume.Name = "textBoxNume";
            textBoxNume.Size = new Size(152, 27);
            textBoxNume.TabIndex = 7;
            textBoxNume.Text = "nume";
            // 
            // textBoxVarsta
            // 
            textBoxVarsta.Location = new Point(793, 422);
            textBoxVarsta.Name = "textBoxVarsta";
            textBoxVarsta.Size = new Size(152, 27);
            textBoxVarsta.TabIndex = 8;
            textBoxVarsta.Text = "varsta";
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(1058, 570);
            Controls.Add(textBoxVarsta);
            Controls.Add(textBoxNume);
            Controls.Add(textBoxCodGrupa);
            Controls.Add(textBoxCodCopil);
            Controls.Add(UpdateBtn);
            Controls.Add(DeleteBtn);
            Controls.Add(AddBtn);
            Controls.Add(dataGridView2);
            Controls.Add(dataGridView1);
            Name = "Form1";
            Text = "Form1";
            ((System.ComponentModel.ISupportInitialize)dataGridView1).EndInit();
            ((System.ComponentModel.ISupportInitialize)dataGridView2).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private DataGridView dataGridView1;
        private DataGridView dataGridView2;
        private Button AddBtn;
        private Button DeleteBtn;
        private Button UpdateBtn;
        private TextBox textBoxCodCopil;
        private TextBox textBoxCodGrupa;
        private TextBox textBoxNume;
        private TextBox textBoxVarsta;
    }
}
