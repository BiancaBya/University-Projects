using System;
using System.Configuration;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;

namespace LabSGDB2.Repository
{
    public class GenericRepository
    {
        private readonly String connectionString;
        public GenericRepository(String connectionString)
        {
            this.connectionString = connectionString;
        }

        public void AddChild(string tableName, Dictionary<string, object> values, int parentId)
        {
            using (SqlConnection connection = new SqlConnection(connectionString))
            {
                string insertQuery = ConfigurationManager.AppSettings["ChildInsertQuery"];

                SqlCommand command = new SqlCommand(insertQuery, connection);

                command.Parameters.AddWithValue("@ParentId", parentId);

                foreach (var kvp in values)
                {
                    command.Parameters.AddWithValue("@" + kvp.Key, kvp.Value);
                }

                connection.Open();
                command.ExecuteNonQuery();
            }
        }

        public void DeleteChild(string tableName, string primaryKeyColumnName, object primaryKeyValue)
        {
            using (SqlConnection connection = new SqlConnection(connectionString))
            {
                string query = $"DELETE FROM {tableName} WHERE {primaryKeyColumnName} = @PrimaryKeyValue";
                SqlCommand command = new SqlCommand(query, connection);
                command.Parameters.AddWithValue("@PrimaryKeyValue", primaryKeyValue);

                connection.Open();
                command.ExecuteNonQuery();
            }
        }

        public void UpdateChild(string tableName, Dictionary<string, object> updatedValues, string primaryKeyColumnName, int selectedParentId)
        {
            string childPrimaryKey = ConfigurationManager.AppSettings["ChildPrimaryKey"];
            if (updatedValues.ContainsKey(childPrimaryKey))
            {
                updatedValues.Remove(childPrimaryKey);
            }

            string updateQuery = ConfigurationManager.AppSettings["ChildUpdateQuery"];

            using (SqlConnection connection = new SqlConnection(connectionString))
            using (SqlCommand command = new SqlCommand(updateQuery, connection))
            {
                foreach (var kvp in updatedValues)
                {
                    command.Parameters.AddWithValue($"@{kvp.Key}", kvp.Value);
                }
                command.Parameters.AddWithValue("@PrimaryKeyValue", selectedParentId);

                connection.Open();
                command.ExecuteNonQuery();
            }
        }

        public DataTable GetChildrenByParentId(string tableName, string foreignKeyColumnName, int parentId)
        {
            DataTable dataTable = new DataTable();
            string[] childColumns = ConfigurationManager.AppSettings["ChildColumns"].Split(',');
            using (SqlConnection connection = new SqlConnection(connectionString))
            {
                string columnList = string.Join(",", childColumns);
                string query = $"SELECT {columnList} FROM {tableName} WHERE {foreignKeyColumnName} = @ParentId";
                SqlCommand command = new SqlCommand(query, connection);
                command.Parameters.AddWithValue("@ParentId", parentId);

                SqlDataAdapter adapter = new SqlDataAdapter(command);
                adapter.Fill(dataTable);
            }
            return dataTable;
        }
    }
}
