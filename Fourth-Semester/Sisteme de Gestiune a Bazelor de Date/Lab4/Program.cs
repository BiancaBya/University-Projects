using System;
using System.Data;
using System.Data.SqlClient;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Generic;

namespace AppleStoreDeadlockDemo
{
    static class Program
    {
        private static readonly string ConnectionString =
            @"Server=DESKTOP-5LU00PG\SQLEXPRESS;Database=AppleStore;Integrated Security=true;TrustServerCertificate=true";
        private static readonly RetryPolicy RetryPolicy = new RetryPolicy(maxRetries: 5, retryDelayMs: 2000);

        static void Main()
        {
            Console.WriteLine("Deadlock starting...");

            var procedures = new List<string> { "Deadlock1", "Deadlock2" };

            Parallel.ForEach(procedures, procedureName =>
            {
                RetryPolicy.Execute(
                    () => ExecuteStoredProcedure(procedureName),
                    operationName: procedureName
                );
            });

            Console.WriteLine("Finished!");
        }

        private static void ExecuteStoredProcedure(string procedureName)
        {
            using (var connection = new SqlConnection(ConnectionString))
            {
                connection.Open();
                using (var command = new SqlCommand(procedureName, connection))
                {
                    command.CommandType = CommandType.StoredProcedure;
                    command.CommandTimeout = 10;
                    command.ExecuteNonQuery();
                }
            }
        }
    }

    class RetryPolicy
    {
        private readonly int _maxRetries;
        private readonly int _delayMilliseconds;

        public RetryPolicy(int maxRetries, int retryDelayMs)
        {
            _maxRetries = maxRetries;
            _delayMilliseconds = retryDelayMs;
        }

        public void Execute(Action action, string operationName = "")
        {
            for (int attempt = 1; attempt <= _maxRetries; attempt++)
            {
                try
                {
                    action();
                    Console.WriteLine($"Operation '{operationName}' succeeded on attempt {attempt}.");
                    return;
                }
                catch (SqlException ex) when (ex.Number == 1205)
                {
                    Console.WriteLine($"Deadlock detected for '{operationName}', retry {attempt}/{_maxRetries}...");
                    if (attempt == _maxRetries)
                        Console.WriteLine($"Operation '{operationName}' failed after {_maxRetries} attempts.");
                    else
                        Thread.Sleep(_delayMilliseconds);
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error during '{operationName}': {ex.Message}");
                    break;
                }
            }
        }
    }
}
