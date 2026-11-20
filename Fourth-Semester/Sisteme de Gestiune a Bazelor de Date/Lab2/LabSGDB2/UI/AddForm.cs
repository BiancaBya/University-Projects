using LabSGDB2.Service;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace LabSGDB2.UI
{
    public partial class AddForm: Form
    {
        private MainService service;
        private int parentID;
        private readonly string[] childColumns;
        private readonly string tableName;
        private Dictionary<string, TextBox> fields = new Dictionary<string, TextBox>();

        public AddForm()
        {
            InitializeComponent();
            try
            {
                childColumns = ConfigurationManager.AppSettings["ChildColumns"]
                                    .Split(',')
                                    .Select(s => s.Trim())
                                    .ToArray();

                tableName = ConfigurationManager.AppSettings["ChildTable"];
            }
            catch (Exception ex)
            {
                MessageBox.Show("Failed to load configuration from file.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        public void setService(MainService service, int idParent)
        {
            this.service = service;
            this.parentID = idParent;
        }

        private void UI_Load(object sender, EventArgs e)
        {
            int startY = 30; 
            int labelX = 50; 
            int textBoxX = 150;
            string identityColumn = ConfigurationManager.AppSettings["ChildPrimaryKey"];
            string foreignKeyColumn = ConfigurationManager.AppSettings["ChildForeignKey"];

            foreach (var columnName in childColumns)
            {
                if(columnName == identityColumn || columnName == foreignKeyColumn) 
                    continue;

                Label label = new Label();
                label.Text = columnName;
                label.Location = new Point(labelX, startY);
                label.AutoSize = true;

                TextBox textBox = new TextBox();
                textBox.Name = $"textBox_{columnName}";
                textBox.Location = new Point(textBoxX, startY);
                textBox.Width = 150;

                this.Controls.Add(label);
                this.Controls.Add(textBox);

                fields[columnName] = textBox;

                startY += 40;
            }
        }

        private void Add_Click(object sender, EventArgs e)
        {
            
            Dictionary<string, object> values = new Dictionary<string, object>();
            
            foreach (var field in fields)
            {
                string key = field.Key;
                string value = field.Value.Text.Trim();
                values[key] = value;
            }

            values[ConfigurationManager.AppSettings["ChildForeignKey"]] = this.parentID;

            try
            {
                service.AddChild(tableName, values, this.parentID);
                MessageBox.Show("Adaugare realizată cu succes!");
            }
            catch (Exception exception)
            {
                MessageBox.Show(exception.Message);
            }

            this.Close();
        }
    }
}
