using LabSGDB2.Service;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace LabSGDB2.UI
{
    public partial class UpdateForm : Form
    {
        private readonly string[] childColumns;
        private readonly Dictionary<string, TextBox> inputFields = new Dictionary<string,TextBox>();
        private readonly string tableName;
        private readonly int primaryKeyValue;
        private readonly MainService service;
        private readonly string primaryKeyColumn;
        private readonly Dictionary<string, object> currentValues;

        public UpdateForm(string tableName, string[] childColumns, Dictionary<string, object> currentValues, MainService service)
        {
            this.childColumns = childColumns;
            this.tableName = tableName;
            this.primaryKeyValue = Convert.ToInt32(currentValues[ConfigurationManager.AppSettings["ChildPrimaryKey"]]);
            this.service = service;
            this.primaryKeyColumn = ConfigurationManager.AppSettings["ChildPrimaryKey"];
            this.currentValues = currentValues;

            BuildDynamicUI(); 
        }

        private void BuildDynamicUI()
        {
            this.Text = "Update produs";
            this.Size = new Size(600, 500);
            this.BackColor = Color.Turquoise;
            this.StartPosition = FormStartPosition.CenterScreen;

            int y = 30;

            string identityColumn = ConfigurationManager.AppSettings["ChildPrimaryKey"];
            string foreignKeyColumn = ConfigurationManager.AppSettings["ChildForeignKey"];

            foreach (string column in childColumns)
            {
                if (column == identityColumn || column == foreignKeyColumn)
                    continue;

                Label label = new Label
                {
                    Text = column,
                    Location = new Point(50, y),
                    AutoSize = true
                };
                this.Controls.Add(label);

                TextBox textBox = new TextBox
                {
                    Name = $"txt_{column}",
                    Location = new Point(180, y),
                    Width = 300,
                    Text = currentValues.ContainsKey(column) ? currentValues[column]?.ToString() : ""
                };
                this.Controls.Add(textBox);
                inputFields[column] = textBox;

                y += 40;
            }

            Button updateBtn = new Button
            {
                Text = "Update",
                Location = new Point(180, y + 10),
                Size = new Size(120, 40)
            };
            updateBtn.Click += Update_Click;
            this.Controls.Add(updateBtn);
        }

        private void Update_Click(object sender, EventArgs e)
        {
            Dictionary<string, object> updatedValues = new Dictionary<string, object>();

            foreach (var field in inputFields)
            {
                string key = field.Key;
                string value = field.Value.Text.Trim();
                updatedValues[key] = value;
            }


            service.UpdateChild(tableName, updatedValues, primaryKeyColumn, primaryKeyValue);

            MessageBox.Show("Modificare realizată cu succes!");
            this.Close();
        }
    }

}





