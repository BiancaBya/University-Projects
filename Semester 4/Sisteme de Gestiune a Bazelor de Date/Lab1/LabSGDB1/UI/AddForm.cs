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
    public partial class AddForm: Form
    {
        private MainService service;
        private int providerID;
        public AddForm()
        {
            InitializeComponent();
        }

        public void setService(MainService service, int idProvider)
        {
            this.service = service;
            this.providerID = idProvider;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string tip = textBoxTip.Text;
            string nume = textBoxNume.Text;
            string culoare = textBoxCuloare.Text;
            string capacitate = textBoxCapacitate.Text;
            string pret = textBoxPret.Text;
            string stoc = textBoxStoc.Text;

            try
            {
                service.addProduct(providerID, tip, nume, culoare, capacitate, pret, stoc);
            } catch(Exception exception)
            {
                MessageBox.Show(exception.Message);
            }

            this.Close();
        }
    }
}
