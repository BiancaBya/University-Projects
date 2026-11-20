using LabSGDB1.Repository;
using LabSGDB1.Service;
using LabSGDB1.UI;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;


namespace LabSGDB1
{
    public partial class MainWindow: Form
    {
        string connectionString = @"Server=DESKTOP-5LU00PG\SQLEXPRESS;Database=AppleStore;Integrated Security=true;TrustServerCertificate=true";
        private RepoProduct productRepo;
        private RepoProvider providerRepo;
        private MainService service;
        private DataSet dataSet;
        public MainWindow()
        {
            InitializeComponent();
            productRepo = new RepoProduct(connectionString);
            providerRepo = new RepoProvider(connectionString);
            service = new MainService(providerRepo, productRepo);

            initModel();
        }

        private void initModel()
        {
            dataSet = new DataSet();

            productRepo.findAllProducts(dataSet);
            providerRepo.findAllProviders(dataSet);

            BindingSource productBindingSource = new BindingSource();
            BindingSource providerBindingSource = new BindingSource();

            providerBindingSource.DataSource = dataSet.Tables["Furnizori"];


            DataColumn providerPK = dataSet.Tables["Furnizori"].Columns["id_furnizor"];
            DataColumn productFK = dataSet.Tables["Produse"].Columns["id_furnizor"];
            DataRelation relation = new DataRelation("fk_FurnizorProduse", providerPK, productFK);
            dataSet.Relations.Add(relation);

            productBindingSource.DataSource = providerBindingSource;
            productBindingSource.DataMember = "fk_FurnizorProduse";

            ProductGridView.DataSource = productBindingSource;
            ProviderGridView.DataSource = providerBindingSource;
        }

        private void AddBtn_Click(object sender, EventArgs e)
        {
            if (ProviderGridView.SelectedRows.Count > 0)
            {
                DataGridViewRow selectedRow = ProviderGridView.SelectedRows[0];
                int providerId = int.Parse(selectedRow.Cells[0].Value.ToString());

                AddForm addForm = new AddForm();
                addForm.setService(this.service, providerId);
                addForm.Show();
            }

            else
            {
                MessageBox.Show("Alegeti un furnizor!");
            }
        }

        private void DeleteBtn_Click(object sender, EventArgs e)
        {
            if (ProductGridView.SelectedRows.Count > 0)
            {
                DataGridViewRow selectedRow = ProductGridView.SelectedRows[0];
                int productId = int.Parse(selectedRow.Cells[0].Value.ToString()); 

                try
                {
                    service.deleteProduct(productId);
                } catch(Exception exception)
                {
                    MessageBox.Show(exception.Message);
                }
            }

            else
            {
                MessageBox.Show("Alegeti un produs!");
            }
        }

        private void UpdateBtn_Click(object sender, EventArgs e)
        {
            if (ProviderGridView.SelectedRows.Count > 0 && ProductGridView.SelectedRows.Count > 0)
            {
                DataGridViewRow selectedProvider = ProviderGridView.SelectedRows[0];
                int providerId = int.Parse(selectedProvider.Cells[0].Value.ToString());

                DataGridViewRow selectedProduct = ProductGridView.SelectedRows[0];
                int productId = int.Parse(selectedProduct.Cells[0].Value.ToString());

                UpdateForm updateForm = new UpdateForm();
                updateForm.setService(this.service, providerId, productId);
                updateForm.Show();
            }
            else
            {
                MessageBox.Show("Alegeti furnizorul si produsul!");
            }
        }
    }
}

