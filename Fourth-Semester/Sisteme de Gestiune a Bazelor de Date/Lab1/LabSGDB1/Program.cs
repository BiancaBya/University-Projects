using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using Microsoft.Data.SqlClient;


namespace LabSGDB1
{
    class Program
    {
        static void Main(string[] args)
        {
            string connectionString = @"Server=DESKTOP-5LU00PG\SQLEXPRESS;Database=AppleStore;Integrated Security=true;TrustServerCertificate=true";
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    Console.WriteLine("Connecting to database");
                    connection.Open();
                    Console.WriteLine("Connected");

                    

                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                throw;
            }

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainWindow());
        }
    }
}


