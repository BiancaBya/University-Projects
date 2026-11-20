using LabSGDB1.Service;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace LabSGDB1.UI
{
    public partial class UpdateForm: Form
    {
        private MainService service;
        private int providerID;
        private int productID;
        public UpdateForm()
        {
            InitializeComponent();
        }

        public void setService(MainService service, int idProvider, int idProduct)
        {
            this.service = service;
            this.providerID = idProvider;
            this.productID = idProduct;
        }

        private void Update_Click(object sender, EventArgs e)
        {
            string tip = textBoxTipUpdate.Text;
            string nume = textBoxNumeUpdate.Text;
            string culoare = textBoxCuloareUpdate.Text;
            string capacitate = textBoxCapacitateUpdate.Text;
            string pret = textBoxPretUpdate.Text;
            string stoc = textBoxStocUpdate.Text;

            try
            {
                service.updateProduct(productID, providerID, tip, nume, culoare, capacitate, pret, stoc);
            } catch(Exception exception)
            {
                MessageBox.Show(exception.Message);
            }
            this.Close();
        }
    }
}
