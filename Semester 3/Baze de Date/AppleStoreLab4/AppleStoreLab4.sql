CREATE VIEW vw_Departamente_Angajati
AS

SELECT Departamente.nume, COUNT(Angajati.id_angajat) AS Nr_angajati
FROM Angajati 
JOIN Departamente ON Angajati.id_departament = Departamente.id_departament
JOIN Magazine ON Angajati.id_magazin = Magazine.id_magazin
WHERE Departamente.nume IN ('IT', 'HR')
GROUP BY Departamente.nume

GO


CREATE VIEW vw_Produse_Promotii
AS

SELECT Produse.id_produs, Promotii.nume, Produse_Promotii.durata
FROM Produse 
JOIN Produse_Promotii ON Produse.id_produs = Produse_Promotii.id_produs
JOIN Promotii On Produse_Promotii.id_promotie = Promotii.id_promotie
WHERE Produse.id_furnizor = 2

GO


CREATE VIEW vw_Produse_TipUnic
AS

SELECT DISTINCT tip 
FROM Produse;

GO

SELECT * FROM vw_Departamente_Angajati;

SELECT * FROM vw_Produse_Promotii;

SELECT * FROM vw_Produse_TipUnic;


INSERT INTO Views(Name)
VALUES
('vw_Departamente_Angajati'),
('vw_Produse_Promotii'), 
('vw_Produse_TipUnic');


SELECT * FROM Views;


INSERT INTO TestViews(TestID, ViewID)
VALUES
(1,1),
(1,2),
(1,3);

SELECT * FROM TestViews;


-- tabel1 magazine -> 1 cheie primara, 0 chei straine
-- tabel 2 comenzi -> 1 cheie primara, cel putin 1 cheie straina
-- tabel3 produse-promotii -> cheie primara din 2 chei straine


CREATE TABLE MagazineCopy
(
	id_magazin INT IDENTITY(1,1),
	CONSTRAINT pk_magazinCopy PRIMARY KEY(id_magazin),

	adresa VARCHAR(200) NOT NULL
);


INSERT INTO MagazineCopy(adresa)
SELECT M.adresa
FROM Magazine M;




CREATE TABLE ComenziCopy
(
	id_comanda INT IDENTITY(1,1),
	CONSTRAINT pk_copyComanda PRIMARY KEY(id_comanda),

	cod_comanda VARCHAR(200) NOT NULL UNIQUE,
	descriere VARCHAR(200) NOT NULL,
	adresa VARCHAR(200) NOT NULL,

	id_client INT,
	CONSTRAINT fk_CopyComenziClient FOREIGN KEY(id_client) REFERENCES Clienti(id_client) 
);


INSERT INTO ComenziCopy(cod_comanda, descriere, adresa, id_client)
SELECT C.cod_comanda, C.descriere, C.adresa, C.id_client
FROM Comenzi C;



CREATE TABLE Produse_Copy
(
	id_produs INT IDENTITY(1,1),
	CONSTRAINT pk_produsCopy PRIMARY KEY(id_produs),

	id_furnizor INT,
	CONSTRAINT fk_FurnizorProduseCopy FOREIGN KEY(id_furnizor) REFERENCES Furnizori(id_furnizor),

	tip VARCHAR(200) NOT NULL,
	nume VARCHAR(200) NOT NULL,
	culoare VARCHAR(200) NOT NULL,
	capacitate VARCHAR(200) NOT NULL,
	pret INT,
	stoc INT,
)

INSERT INTO Produse_Copy(id_furnizor, tip, nume, culoare, capacitate, pret, stoc)
SELECT P.id_furnizor, P.tip, P.nume, P.culoare, P.capacitate, P.pret, P.stoc
FROM Produse P;




DECLARE @i_copy INT
SET @i_copy = 0
WHILE @i_copy < 1000
BEGIN
	INSERT INTO Produse_Copy(id_furnizor, tip, nume, culoare, capacitate, pret, stoc)
	VALUES (1, 'IPhone', 'IPhone1', 'Negru', '256GB', 1000, 1000);
	SET @i_copy = @i_copy + 1
END


CREATE TABLE Promotii_Copy
(
	id_promotie INT IDENTITY(1,1),
	CONSTRAINT pk_promotieCopy PRIMARY KEY(id_promotie),

	nume VARCHAR(200) NOT NULL,
	procent INT
)

INSERT INTO Promotii_Copy(nume, procent)
SELECT P.nume, P.procent
FROM Promotii P;



CREATE TABLE Produse_Promotii_Copy
(
	id_produs INT,
	CONSTRAINT fk_CopyProdus FOREIGN KEY(id_produs) REFERENCES Produse_Copy(id_produs),

	id_promotie INT,
	CONSTRAINT fk_CopyPromotie FOREIGN KEY(id_promotie) REFERENCES Promotii_Copy(id_promotie),

	CONSTRAINT pk_Copy_produse_promotii PRIMARY KEY(id_produs, id_promotie),

	durata VARCHAR(100) NOT NULL
)



INSERT INTO Produse_Promotii_Copy(id_produs, id_promotie, durata)
SELECT P.id_produs, P.id_promotie, P.durata
FROM Produse_Promotii P;


SELECT * FROM MagazineCopy;
SELECT * FROM ComenziCopy;
SELECT * FROM Produse_Copy;
SELECT * FROM Promotii_Copy;
SELECT * FROM Produse_Promotii_Copy;



INSERT INTO Tables(Name)
VALUES
('MagazineCopy'),
('ComenziCopy'),
('Produse_Promotii_Copy');



SELECT * FROM Tables;



INSERT INTO Tests(Name)
VALUES
('Test1')


SELECT * FROM Tests


INSERT INTO TestTables(TestID, TableID, NoOfRows, Position)
VALUES
(1, 1, 1000, 1),
(1, 2, 1000, 2),
(1, 3, 1000, 3);


SELECT * FROM TestTables




GO
CREATE PROCEDURE DeleteFromTable
	@currentPosition INT
AS
BEGIN

	DECLARE @tableName VARCHAR(50);
	SET @tableName = (SELECT T.Name FROM Tables T WHERE T.TableID = @currentPosition);
	EXEC('DELETE FROM ' + @tableName);

	IF(OBJECTPROPERTY(OBJECT_ID(@tableName), 'TableHasIdentity') = 1)
		DBCC CHECKIDENT(@tableName, RESEED, 0);

END
GO




GO
CREATE PROCEDURE InsertIntoMagazine
	@numberOfRows INT
AS
BEGIN

	DECLARE @index INT;
	SET @index = 0;

	WHILE @index < @numberOfRows
	BEGIN
		INSERT INTO MagazineCopy(adresa)
		VALUES ('Adresa');
		SET @index = @index + 1;
	END
	
END
GO


GO
CREATE PROCEDURE InsertIntoComenzi
	@numberOfRows INT
AS
BEGIN

	DECLARE @index INT;
	SET @index = 0;

	WHILE @index < @numberOfRows
	BEGIN
		INSERT INTO ComenziCopy(cod_comanda, descriere, adresa, id_client)
		VALUES ('Cod' + CAST(@index AS VARCHAR(5)), 'Descriere', 'Adresa', 1);
		SET @index = @index + 1;
	END
	
END
GO


GO
CREATE PROCEDURE InsertIntoProdusePromotii
	@numberOfRows INT
AS
BEGIN

	DECLARE @index INT;
	SET @index = 1;

	WHILE @index <= @numberOfRows
	BEGIN
		INSERT INTO Produse_Promotii_Copy(id_produs, id_promotie, durata)
		VALUES(@index, 1, 'durata')
		SET @index = @index + 1;
	END

END
GO


EXEC DeleteFromTable 1;
EXEC DeleteFromTable 2;
EXEC DeleteFromTable 3;

EXEC InsertIntoMagazine 10;
EXEC InsertIntoComenzi 10;
EXEC InsertIntoProdusePromotii 10;



SELECT * FROM MagazineCopy;
SELECT * FROM ComenziCopy;
SELECT * FROM Produse_Copy;
SELECT * FROM Promotii_Copy;
SELECT * FROM Produse_Promotii_Copy;



GO
CREATE PROCEDURE Test1
	
AS
BEGIN

	DECLARE @startTime DATETIME;
	DECLARE @endTime DATETIME;
	DECLARE @descriere VARCHAR(100);

	DECLARE @tableName VARCHAR(100);
	DECLARE @TestID INT;
	DECLARE @TestRunID INT;

	DECLARE @TableID INT;
	DECLARE @NoOfRows INT;
	DECLARE @Position INT;

	DECLARE @ViewName VARCHAR(100);
	DECLARE @ViewID INT;

	
	SELECT @TestID = TestID
	FROM Tests
	WHERE Name = 'Test1';


	INSERT INTO TestRuns(Description, StartAt, EndAt)
	VALUES ('Running test with id ' + CAST(@TestID AS CHAR(5)), GETDATE(), NULL)


	DECLARE TestCursorStergere CURSOR FOR
	SELECT TsT.TableID, TsT.NoOfRows, TsT.Position
	FROM TestTables TsT
	INNER JOIN Tests T ON TsT.TestID = T.TestID
	WHERE T.Name = 'Test1'
	ORDER BY TsT.Position;

	OPEN TestCursorStergere;

	FETCH NEXT FROM TestCursorStergere INTO @TableID, @NoOfRows, @Position;

	WHILE @@FETCH_STATUS = 0
	BEGIN

		SELECT @tableName = Tables.Name FROM Tables WHERE Tables.TableID = @TableID;

		SET @startTime = GETDATE();

		
		IF @tableName = 'MagazineCopy'
		BEGIN
			EXEC DeleteFromTable @Position;
		END

		IF @tableName = 'ComenziCopy'
		BEGIN
			EXEC DeleteFromTable @Position;
		END

		IF @tableName = 'Produse_Promotii_Copy'
		BEGIN
			EXEC DeleteFromTable @Position;
		END


		SET @endTime = GETDATE();

		FETCH NEXT FROM TestCursorStergere INTO @TableID, @NoOfRows, @Position;

	END;


	CLOSE TestCursorStergere;
	DEALLOCATE TestCursorStergere;







	DECLARE TestCursorInserare CURSOR FOR
	SELECT TsT.TableID, TsT.NoOfRows, TsT.Position
	FROM TestTables TsT
	INNER JOIN Tests T ON TsT.TestID = T.TestID
	WHERE T.Name = 'Test1'
	ORDER BY TsT.Position DESC;

	OPEN TestCursorInserare;

	FETCH NEXT FROM TestCursorInserare INTO @TableID, @NoOfRows, @Position;

	WHILE @@FETCH_STATUS = 0
	BEGIN

		SELECT @tableName = Tables.Name FROM Tables WHERE Tables.TableID = @TableID;

		SET @startTime = GETDATE();

		
		IF @tableName = 'MagazineCopy'
		BEGIN
			EXEC InsertIntoMagazine @NoOfRows;
		END

		IF @tableName = 'ComenziCopy'
		BEGIN
			EXEC InsertIntoComenzi @NoOfRows;
		END

		IF @tableName = 'Produse_Promotii_Copy'
		BEGIN
			EXEC InsertIntoProdusePromotii @NoOfRows;
		END


		SET @endTime = GETDATE();

		SELECT @TestRunID = TestRuns.TestRunID FROM TestRuns WHERE TestRuns.Description = 'Running test with id ' + CAST(@TestID AS CHAR(5));

		INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt)
		VALUES (@TestRunID, @TableID, @startTime, @endTime)

		
		FETCH NEXT FROM TestCursorInserare INTO @TableID, @NoOfRows, @Position;

	END;


	CLOSE TestCursorInserare;
	DEALLOCATE TestCursorInserare;





	
	DECLARE TestCursorViews CURSOR FOR
	SELECT TsW.ViewID
	FROM TestViews TsW
	INNER JOIN Tests T ON TsW.TestID = T.TestID
	WHERE T.Name = 'Test1';

	OPEN TestCursorViews;

	FETCH NEXT FROM TestCursorViews INTO @ViewID;

	WHILE @@FETCH_STATUS = 0
	BEGIN

		SELECT @ViewName = Views.Name FROM Views WHERE Views.ViewID = @ViewID;

		SET @startTime = GETDATE();

		DECLARE @comanda NVARCHAR(55) = N'SELECT * FROM ' + @viewName;	
		EXECUTE (@comanda);

		SET @endTime = GETDATE();

		SELECT @TestRunID = TestRuns.TestRunID FROM TestRuns WHERE TestRuns.Description = 'Running test with id ' + CAST(@TestID AS CHAR(5));

		INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt)
		VALUES (@TestRunID, @ViewID, @startTime, @endTime)

		
		FETCH NEXT FROM TestCursorViews INTO @ViewID;

	END;

	
	CLOSE TestCursorViews;
	DEALLOCATE TestCursorViews;


	UPDATE TestRuns SET EndAt =  GETDATE()
	WHERE Description = 'Running test with id ' + CAST(@TestID AS CHAR(5))


END
GO





GO
CREATE PROCEDURE RunTest
	@idTest INT
AS
BEGIN

	DECLARE @TestName VARCHAR(100);
	SELECT @TestName = Tests.Name FROM Tests WHERE Tests.TestID = @idTest;
	EXEC @TestName

END
GO

EXEC RunTest 1


SELECT * FROM TestRuns;
SELECT * FROM TestRunTables;
SELECT * FROM TestRunViews;

DBCC CHECKIDENT(TestRuns, RESEED, 0);

DELETE FROM TestRuns;
DELETE FROM TestRunTables;
DELETE FROM TestRunViews;

