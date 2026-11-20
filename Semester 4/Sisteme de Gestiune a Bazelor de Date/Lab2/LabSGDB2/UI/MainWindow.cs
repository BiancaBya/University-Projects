using LabSGDB2.Repository;
using LabSGDB2.Service;
using LabSGDB2.UI;
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


namespace LabSGDB2
{
    public partial class MainWindow: Form
    {
        private string connectionString;
        private string windowTitle;
        private readonly string childTable;
        private readonly string parentTable;
        private readonly string childTableForeignKey;
        private readonly string parentTablePrimaryKey;
        private readonly string[] parentColumns;
        private readonly string[] childColumns;
        private readonly string parentQuery;
        private readonly string childQuery;

        private readonly MainService service;
        private readonly GenericRepository repository;
        public MainWindow()
        {
            InitializeComponent();
            try
            {
                connectionString = ConfigurationManager.AppSettings["ConnectionString"];
                windowTitle = ConfigurationManager.AppSettings["WindowTitle"];
                parentTable = ConfigurationManager.AppSettings["ParentTable"];
                childTable = ConfigurationManager.AppSettings["ChildTable"];
                parentTablePrimaryKey = ConfigurationManager.AppSettings["ParentPrimaryKey"];
                childTableForeignKey = ConfigurationManager.AppSettings["ChildForeignKey"];
                childColumns = ConfigurationManager.AppSettings["ChildColumns"].Split(',');
                parentQuery = ConfigurationManager.AppSettings["ParentQuery"];
                childQuery = ConfigurationManager.AppSettings["ChildQuery"];
            }
            catch (Exception ex)
            {
                MessageBox.Show("Failed to load configuration from file.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }

            this.repository = new GenericRepository(connectionString);
            this.service = new MainService(repository);

            this.CenterToScreen();
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;

            this.ParentDataGridView.SelectionChanged += new System.EventHandler(this.ParentDataGridView_SelectionChanged);
            this.AddButton.Click += new System.EventHandler(this.AddBtn_Click);
            this.DeleteButton.Click += new System.EventHandler(this.DeleteBtn_Click);
            this.UpdateButton.Click += new System.EventHandler(this.UpdateBtn_Click);
        }


        
        private void UI_Load(object sender, EventArgs e)
        {
            using (SqlConnection connection = new SqlConnection(connectionString))
            using (SqlCommand command = new SqlCommand(parentQuery, connection))
            {
                connection.Open();
                SqlDataAdapter adapter = new SqlDataAdapter(command);
                DataTable table = new DataTable();
                adapter.Fill(table);
                ParentDataGridView.DataSource = table;
            }
        }

        
        private void ParentDataGridView_SelectionChanged(object sender, EventArgs e)
        {
            if (ParentDataGridView.SelectedRows.Count > 0)
            {
                int parentId = (int)ParentDataGridView.SelectedRows[0].Cells[parentTablePrimaryKey].Value;
                LoadChildData(parentId);
            }
        }



        private void LoadChildData(int parentId)
        {
            using (SqlConnection connection = new SqlConnection(connectionString))
            using (SqlCommand command = new SqlCommand(childQuery, connection))
            {
                command.Parameters.AddWithValue("@ParentId", parentId);
                connection.Open();
                SqlDataAdapter adapter = new SqlDataAdapter(command);
                DataTable table = new DataTable();
                adapter.Fill(table);
                ChildDataGridView.DataSource = table;
            }
        }





        private void AddBtn_Click(object sender, EventArgs e)
        {
            if (ParentDataGridView.SelectedRows.Count > 0)
            {
                int parentId = (int)ParentDataGridView.SelectedRows[0].Cells[parentTablePrimaryKey].Value;

                AddForm addForm = new AddForm();
                addForm.setService(this.service, parentId);
                addForm.ShowDialog();

                string foreignKeyColumnName = ConfigurationManager.AppSettings["ChildForeignKey"];
                ChildDataGridView.DataSource = service.GetChildrenByParentId(ConfigurationManager.AppSettings["ChildTable"], foreignKeyColumnName, parentId);
            }

            else
            {
                MessageBox.Show("Select the Parent!");
            }
        }



        private void DeleteBtn_Click(object sender, EventArgs e)
        {
            if (ChildDataGridView.SelectedRows.Count == 0)
            {
                MessageBox.Show("Select the child!");
                return;
            }

            string tableName = ConfigurationManager.AppSettings["ChildTable"];
            string primaryKeyColumnName = ConfigurationManager.AppSettings["ChildPrimaryKey"];
            string foreignKeyColumnName = ConfigurationManager.AppSettings["ChildForeignKey"];
            int parentID = Convert.ToInt32(ParentDataGridView.SelectedRows[0].Cells[ConfigurationManager.AppSettings["ParentPrimaryKey"]].Value);
            object primaryKeyValue = ChildDataGridView.SelectedRows[0].Cells[primaryKeyColumnName].Value;

            try
            {
                service.DeleteChild(tableName, primaryKeyColumnName, primaryKeyValue);
                ChildDataGridView.DataSource = service.GetChildrenByParentId(tableName, foreignKeyColumnName, parentID);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Delete Error!");
            }
        }



        private void UpdateBtn_Click(object sender, EventArgs e)
        {

            string[] columns = ConfigurationManager.AppSettings["ChildColumns"]
                                    .Split(',')
                                    .Select(s => s.Trim())
                                    .ToArray();
            string tableName = ConfigurationManager.AppSettings["ChildTable"];
            string primaryKey = ConfigurationManager.AppSettings["ChildPrimaryKey"];
            string foreignKey = ConfigurationManager.AppSettings["ChildForeignKey"];

            if (ChildDataGridView.SelectedRows.Count > 0)
            {
                DataGridViewRow selectedRow = ChildDataGridView.SelectedRows[0];
                Dictionary<string, object> currentValues = new Dictionary<string, object>();

                foreach (var col in columns)
                {
                    currentValues[col] = selectedRow.Cells[col].Value;
                }

                var updateWindow = new UpdateForm(tableName, columns, currentValues, service);
                updateWindow.ShowDialog();

                int parentId = Convert.ToInt32(ParentDataGridView.SelectedRows[0].Cells[parentTablePrimaryKey].Value);
                ChildDataGridView.DataSource = service.GetChildrenByParentId(tableName, foreignKey, parentId);
            }
            else
            {
                MessageBox.Show("Select the Parent and the Child!");
            }
        }
    }
}







