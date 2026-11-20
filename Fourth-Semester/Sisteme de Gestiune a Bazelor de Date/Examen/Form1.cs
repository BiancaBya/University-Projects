using System.Data;
using System.Windows.Forms;
using Microsoft.Data.SqlClient;

namespace Examen
{
    public partial class Form1 : Form
    {
        private static string _connectionString = @"Server=DESKTOP-5LU00PG\SQLEXPRESS;Database=Practic;Integrated Security=true;TrustServerCertificate=true";
        private SqlConnection _connection = new SqlConnection(_connectionString);
        private SqlDataAdapter _adapter = new SqlDataAdapter();
        private SqlDataAdapter _adapterCopil = new SqlDataAdapter();
        private DataSet _dataSet = new DataSet();

        private int _idGrupa = -1;
        private int _idCopil = -1;

        private void LoadDataParent()
        {
            try
            {
                _connection.Open();
                string query = "SELECT * FROM Grupe;";
                _adapter.SelectCommand = new SqlCommand(query, _connection);
                if (_dataSet.Tables.Contains("Grupe"))
                    _dataSet.Tables["Grupe"].Clear();
                _adapter.Fill(_dataSet, "Grupe");
                dataGridView1.DataSource = _dataSet.Tables["Grupe"];
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message);
            }
            finally
            {
                _connection.Close();
            }
        }

        private void LoadDataCopil()
        {
            try
            {
                _connection.Open();
                string query = "SELECT * FROM Copii WHERE cod_grupa = @cod_grupa";
                _adapterCopil.SelectCommand = new SqlCommand(query, _connection);
                _adapterCopil.SelectCommand.Parameters.AddWithValue("@cod_grupa", _idGrupa);
                if (_dataSet.Tables.Contains("Copii"))
                    _dataSet.Tables["Copii"].Clear();
                _adapterCopil.Fill(_dataSet, "Copii");
                dataGridView2.DataSource = _dataSet.Tables["Copii"];
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message);
            }
            finally
            {
                _connection.Close();
            }
        }
        public Form1()
        {
            InitializeComponent();

            LoadDataParent();
        }

        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                int grupaId = Convert.ToInt32(dataGridView1.Rows[e.RowIndex].Cells["cod_grupa"].Value);
                _idGrupa = grupaId;
                Console.WriteLine(grupaId);

                LoadDataCopil();
            }
            else
            {
                _idGrupa = -1;
            }
        }

        private void dataGridView2_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                int copilID = Convert.ToInt32(dataGridView2.Rows[e.RowIndex].Cells["cod_copil"].Value);
                _idCopil = copilID;
                Console.WriteLine(copilID);
            }
            else
            {
                _idCopil = -1;
            }
        }

        private void AddBtn_Click(object sender, EventArgs e)
        {
            try
            {
                _connection.Open();

                string query =
                    "INSERT INTO Copii (nume,varsta,cod_grupa) " +
                    "VALUES (@nume,@varsta, @cod_grupa)";

                SqlCommand command = new SqlCommand(query, _connection);

                string nume = textBoxNume.Text;
                int varsta = int.Parse(textBoxVarsta.Text);
                int cod_grupa = int.Parse(textBoxCodGrupa.Text);

                command.Parameters.AddWithValue("@nume", nume);
                command.Parameters.AddWithValue("@varsta", varsta);
                command.Parameters.AddWithValue("@cod_grupa", cod_grupa);

                command.ExecuteNonQuery();

                MessageBox.Show("Copilul a fost adaugat cu succes!");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Eroare: " + ex.Message);
            }
            finally
            {
                _connection.Close();
                LoadDataCopil();
            }
        }

        private void DeleteBtn_Click(object sender, EventArgs e)
        {
            if (_idCopil == -1)
            {
                return;
            }
            try
            {
                _connection.Open();
                string query = "DELETE FROM Copii WHERE cod_copil = @id";
                _adapter.DeleteCommand = new SqlCommand(query, _connection);

                _adapter.DeleteCommand.Parameters.AddWithValue("@id", _idCopil);

                _adapter.DeleteCommand.ExecuteNonQuery();

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                _connection.Close();
                LoadDataCopil();
            }
        }

        private void UpdateBtn_Click(object sender, EventArgs e)
        {
            try
            {
                _connection.Open();
                string query = "UPDATE Copii SET nume = @nume, varsta = @varsta, cod_grupa = @cod_grupa WHERE cod_copil = @cod_copil";
                _adapter.UpdateCommand = new SqlCommand(query, _connection);

                _adapter.UpdateCommand.Parameters.AddWithValue("@cod_copil", textBoxCodCopil.Text);
                _adapter.UpdateCommand.Parameters.AddWithValue("@nume", textBoxNume.Text);
                _adapter.UpdateCommand.Parameters.AddWithValue("@varsta", textBoxVarsta.Text);
                _adapter.UpdateCommand.Parameters.AddWithValue("@cod_grupa", textBoxCodGrupa.Text);

                int x = _adapter.UpdateCommand.ExecuteNonQuery();
                if (x >= 1)
                    MessageBox.Show("The record has been updated");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                _connection.Close();
                LoadDataCopil();
            }
        }
    }
}
