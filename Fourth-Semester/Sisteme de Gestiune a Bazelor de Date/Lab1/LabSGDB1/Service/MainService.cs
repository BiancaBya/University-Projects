using LabSGDB1.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using System.Data.SqlClient;
using Microsoft.IdentityModel.Tokens;

namespace LabSGDB1.Service
{
    public class MainService
    {
        private RepoProvider providerRepo;
        private RepoProduct productRepo;

        public MainService(RepoProvider providerRepo, RepoProduct productRepo)
        {
            this.providerRepo = providerRepo;
            this.productRepo = productRepo;
        }

        public void loadAllProduct(DataSet dataSet)
        {
            productRepo.findAllProducts(dataSet);
        }

        public void loadAllProviders(DataSet dataSet)
        {
            providerRepo.findAllProviders(dataSet);
        }

        public void addProduct(int id_furnizor, string tip, string nume, string culoare, string capacitate, string pret, string stoc) 
        {
            if (!int.TryParse(pret, out _) || !int.TryParse(stoc, out _))
                throw new Exception("Capacitatea si Stocul trebuie sa fie numere intregi");
            if (tip.IsNullOrEmpty() || nume.IsNullOrEmpty() || culoare.IsNullOrEmpty() || capacitate.IsNullOrEmpty() || pret.IsNullOrEmpty() || stoc.IsNullOrEmpty())
                throw new Exception("Campurile nu pot fi nule");

            productRepo.AddProduct(id_furnizor, tip, nume, culoare, capacitate, pret, stoc);
        }

        public void deleteProduct(int id_product)
        {
            productRepo.DeleteProduct(id_product);
        }

        public void updateProduct(int id_produs, int id_furnizor, string tip, string nume, string culoare, string capacitate, string pret, string stoc)
        {
            if (!int.TryParse(pret, out _) || !int.TryParse(stoc, out _))
                throw new Exception("Capacitatea si Stocul trebuie sa fie numere intregi");
            if (tip.IsNullOrEmpty() || nume.IsNullOrEmpty() || culoare.IsNullOrEmpty() || capacitate.IsNullOrEmpty() || pret.IsNullOrEmpty() || stoc.IsNullOrEmpty())
                throw new Exception("Campurile nu pot fi nule");

            productRepo.updateProduct(id_produs, id_furnizor, tip, nume, culoare, capacitate, pret, stoc);
        }

    }
}


