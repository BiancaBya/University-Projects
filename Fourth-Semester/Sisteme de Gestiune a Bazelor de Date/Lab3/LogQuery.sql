USE AppleStore;

CREATE TABLE LogTable(
	id INT IDENTITY,
	log_type VARCHAR(15),
	log_description VARCHAR(300),
	date_time DATETIME CONSTRAINT df_logTable_date_time DEFAULT GETDATE(),
	action_type VARCHAR(15),
	table_name VARCHAR(100),
	procedure_name VARCHAR(100),
	line INT,
	key_index INT,
	CONSTRAINT pk_logTable PRIMARY KEY (id),
	CONSTRAINT ck_logTable_log_type CHECK (log_type IN ('INFO', 'DEBUG', 'TRACE')),
	CONSTRAINT ck_logTable_action_type CHECK (action_type IS NULL OR action_type IN ('INSERT', 'DELETE', 'UPDATE'))
);

GO
CREATE OR ALTER PROCEDURE InsertLog
	@type VARCHAR(15),
	@description VARCHAR(300),
	@action_type VARCHAR(15),
	@table_name VARCHAR(100),
	@procedure_name VARCHAR(100),
	@line INT,
	@key_index INT
AS
BEGIN
	INSERT INTO LogTable(log_type, log_description, action_type, table_name, procedure_name, line, key_index)
	VALUES
	(@type, @description, @action_type, @table_name, @procedure_name, @line, @key_index);
	RETURN;
END;

GO
CREATE OR ALTER PROCEDURE TraceEntry
	@procedureName VARCHAR(100)
AS
BEGIN
	DECLARE @logMsg VARCHAR(150) = 'Enter - ' + @procedureName;
	EXEC InsertLog 'TRACE', @logMsg , NULL, NULL, @procedureName, NULL, NULL; 
END;

GO
CREATE OR ALTER PROCEDURE TraceExit
	@procedureName VARCHAR(100)
AS
BEGIN
	DECLARE @logMsg VARCHAR(150) = 'Exit - ' + @procedureName;
	EXEC InsertLog 'TRACE', @logMsg , NULL, NULL, @procedureName, NULL, NULL; 
END;

GO 
CREATE OR ALTER PROCEDURE LogError
	@description VARCHAR(300),
	@action_type VARCHAR(15),
	@table_name VARCHAR(100),
	@procedure_name VARCHAR(100),
	@line INT,
	@key_index INT
AS
BEGIN
	EXEC InsertLog 'DEBUG', @description, @action_type, @table_name, @procedure_name, @line, @key_index;
END;

GO 
CREATE OR ALTER PROCEDURE LogInfo
	@description VARCHAR(300),
	@action_type VARCHAR(15),
	@table_name VARCHAR(100),
	@procedure_name VARCHAR(100),
	@line INT,
	@key_index INT
AS
BEGIN
	EXEC InsertLog 'INFO', @description, @action_type, @table_name, @procedure_name, @line, @key_index;
END;
	






