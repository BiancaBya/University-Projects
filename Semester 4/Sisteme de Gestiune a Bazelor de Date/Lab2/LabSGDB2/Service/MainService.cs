using LabSGDB2.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using System.Data.SqlClient;

namespace LabSGDB2.Service
{
    public class MainService
    {
        private readonly GenericRepository repository;
        public MainService(GenericRepository repository)
        {
            this.repository = repository;
        }

        public void AddChild(string tableName, Dictionary<string, object> values, int parentId)
        {
            repository.AddChild(tableName, values, parentId);
        }

        public void DeleteChild(string tableName, string primaryKeyColumnName, object primaryKeyValue)
        {
            repository.DeleteChild(tableName, primaryKeyColumnName, primaryKeyValue);
        }

        public void UpdateChild(string tableName, Dictionary<string, object> updatedValues, string primaryKeyColumnName, int primaryKeyValue)
        {
            repository.UpdateChild(tableName, updatedValues, primaryKeyColumnName, primaryKeyValue);
        }

        public DataTable GetChildrenByParentId(string tableName, string foreignKeyColumnName, int parentId)
        {
            return repository.GetChildrenByParentId(tableName, foreignKeyColumnName, parentId);
        }


    }
}




