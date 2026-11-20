using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LabSGDB1.Repository
{
    public class RepoProduct
    {
        private string _connectionString;
        private SqlDataAdapter dataAdapter;
        private DataSet dataSet;

        public RepoProduct(string connectionString)
        {
            _connectionString = connectionString;
        }

        public void findAllProducts(DataSet dataSet)
        {
            using (SqlConnection connection = new SqlConnection(_connectionString))
            {
                string query = "SELECT * FROM Produse";
                this.dataAdapter = new SqlDataAdapter(query, connection);
                this.dataSet = dataSet;
                dataAdapter.Fill(dataSet, "Produse");
            }
        }

        public int GetIdentity()
        {
            int identityValue = 0;
            string query = $"SELECT IDENT_CURRENT('Produse')";

            using (SqlConnection connection = new SqlConnection(_connectionString))
            {
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    connection.Open();
                    object result = command.ExecuteScalar();
                    if (result != DBNull.Value)
                    {
                        identityValue = Convert.ToInt32(result);
                    }
                }
            }

            return identityValue;
        }

        public void AddProduct(int id_furnizor, string tip, string nume, string culoare, string capacitate, string pret, string stoc)
        {
            using (SqlConnection connection = new SqlConnection(_connectionString))
            {
                string query = "INSERT INTO Produse(id_furnizor, tip, nume, culoare, capacitate, pret, stoc) " +
                    "VALUES (@id_furnizor, @tip, @nume, @culoare, @capacitate, @pret, @stoc)";

                SqlCommand insertCommand = new SqlCommand(query, connection);

                insertCommand.Parameters.AddWithValue("@id_furnizor", id_furnizor);
                insertCommand.Parameters.AddWithValue("@tip", tip);
                insertCommand.Parameters.AddWithValue("@nume", nume);
                insertCommand.Parameters.AddWithValue("@culoare", culoare);
                insertCommand.Parameters.AddWithValue("@capacitate", capacitate);
                insertCommand.Parameters.AddWithValue("@pret", int.Parse(pret));
                insertCommand.Parameters.AddWithValue("@stoc", int.Parse(stoc));

                this.dataAdapter.InsertCommand = insertCommand;

                DataTable productsTable = dataSet.Tables["Produse"];
                DataRow newRow = productsTable.NewRow();

                newRow["id_furnizor"] = id_furnizor;
                newRow["tip"] = tip;
                newRow["nume"] = nume;
                newRow["culoare"] = culoare;
                newRow["capacitate"] = capacitate;
                newRow["pret"] = pret;
                newRow["stoc"] = stoc;

                productsTable.Rows.Add(newRow);
                dataAdapter.Update(dataSet, "Produse");

                int identity = GetIdentity();
                newRow["id_produs"] = identity;
            }
        }


        public void DeleteProduct(int id_produs)
        {
            using (SqlConnection connection = new SqlConnection(_connectionString))
            {
                string query = "DELETE FROM Produse WHERE id_produs = @id_produs";

                SqlCommand deleteCommand = new SqlCommand(query, connection);
                deleteCommand.Parameters.AddWithValue("@id_produs", id_produs);

                this.dataAdapter.DeleteCommand = deleteCommand;

                DataTable productsTable = dataSet.Tables["Produse"];
                DataRow rowToDelete = productsTable.Rows.Cast<DataRow>().FirstOrDefault(row => (int)row["id_produs"] == id_produs);

                if (rowToDelete != null)
                {
                    rowToDelete.Delete();

                    dataAdapter.Update(dataSet, "Produse");
                }

            }
        }

        public void updateProduct(int id_produs, int id_furnizor, string tip, string nume, string culoare, string capacitate, string pret, string stoc)
        {
            using (SqlConnection connection = new SqlConnection(_connectionString))
            {
                string query = "UPDATE Produse " +
                    "SET id_furnizor = @id_furnizor, tip = @tip, nume = @nume, culoare = @culoare, capacitate = @capacitate, pret = @pret, stoc = @stoc " +
                    "WHERE id_produs = @id_produs";

                SqlCommand updateCommand = new SqlCommand(query, connection);

                updateCommand.Parameters.AddWithValue("@id_produs", id_produs);
                updateCommand.Parameters.AddWithValue("@id_furnizor", id_furnizor);
                updateCommand.Parameters.AddWithValue("@tip", tip);
                updateCommand.Parameters.AddWithValue("@nume", nume);
                updateCommand.Parameters.AddWithValue("@culoare", culoare);
                updateCommand.Parameters.AddWithValue("@capacitate", capacitate);
                updateCommand.Parameters.AddWithValue("@pret", int.Parse(pret));
                updateCommand.Parameters.AddWithValue("@stoc", int.Parse(stoc));

                this.dataAdapter.UpdateCommand = updateCommand;

                DataTable productsTable = dataSet.Tables["Produse"];
                DataRow rowToUpdate = productsTable.Rows.Cast<DataRow>().FirstOrDefault(row => (int)row["id_produs"] == id_produs);

                if (rowToUpdate != null)
                {
                    rowToUpdate["id_furnizor"] = id_furnizor;
                    rowToUpdate["tip"] = tip;
                    rowToUpdate["nume"] = nume;
                    rowToUpdate["culoare"] = culoare;
                    rowToUpdate["capacitate"] = capacitate;
                    rowToUpdate["pret"] = pret;
                    rowToUpdate["stoc"] = stoc;

                    dataAdapter.Update(dataSet, "Produse");

                    rowToUpdate.AcceptChanges();
                }

            }
        }
    }
}



