using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LabSGDB1.Repository
{
    public class RepoProvider
    {
        private string _connectionString;

        public RepoProvider(string connectionString)
        {
            _connectionString = connectionString;
        }

        public void findAllProviders(DataSet dataSet)
        {
            using(SqlConnection connection = new SqlConnection(_connectionString))
            {
                string query = "SELECT * FROM Furnizori";
                SqlDataAdapter dataAdapter = new SqlDataAdapter(query, connection);
                dataAdapter.Fill(dataSet, "Furnizori");
            }
        }
    }
}
