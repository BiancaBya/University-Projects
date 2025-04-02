USE [AppleStore]

CREATE TABLE VersiuneCurenta(
	cod_v INT PRIMARY KEY IDENTITY,
	numar INT
);


INSERT INTO VersiuneCurenta (numar) VALUES(0);


GO
CREATE PROCEDURE V1
AS
BEGIN
    CREATE TABLE Categorii (
        id_categorie INT PRIMARY KEY,
        Nume NVARCHAR(50),
    );
END;
GO


GO
CREATE PROCEDURE V2
AS
BEGIN
	ALTER TABLE Produse
    ADD CONSTRAINT df_pret DEFAULT 100 FOR pret;
END;
GO


GO 
CREATE PROCEDURE V3
AS
BEGIN
	ALTER TABLE Produse
	ALTER COLUMN stoc DECIMAL(10, 2);
END;
GO


GO 
CREATE PROCEDURE V4
AS 
BEGIN
	ALTER TABLE Produse
	ADD descriere VARCHAR(255);
END;
GO


GO
CREATE PROCEDURE V5
AS 
BEGIN
	ALTER TABLE Produse
	ADD id_categorie INT
	ALTER TABLE Produse
	ADD CONSTRAINT fk_CategorieProduse FOREIGN KEY (id_categorie) REFERENCES Categorii(id_categorie);

END;
GO



GO
CREATE PROCEDURE UpdateDatabaseVersion
AS
BEGIN
    DECLARE @currentVersion INT;
    SELECT @currentVersion = numar FROM VersiuneCurenta;

    IF @currentVersion = 0
    BEGIN
        EXEC V1;
        UPDATE VersiuneCurenta SET numar = 1;
    END;

	IF @currentVersion = 1
	BEGIN
		EXEC V2;
		UPDATE VersiuneCurenta SET numar = 2;
	END;

	IF @currentVersion = 2
	BEGIN
		EXEC V3;
		UPDATE VersiuneCurenta SET numar = 3;
	END;

	IF @currentVersion = 3
	BEGIN
		EXEC V4;
		UPDATE VersiuneCurenta SET numar = 4;
	END;

	IF @currentVersion = 4
	BEGIN
		EXEC V5;
		UPDATE VersiuneCurenta SET numar = 5;
	END;

END;
GO




GO
CREATE PROCEDURE UndoV1
AS
BEGIN
    IF OBJECT_ID('Categorii', 'U') IS NOT NULL
    BEGIN
        DROP TABLE Categorii;
    END
END;
GO


GO
CREATE PROCEDURE UndoV2
AS
BEGIN
    ALTER TABLE Produse
    DROP CONSTRAINT df_pret
END;
GO


GO
CREATE PROCEDURE UndoV3
AS
BEGIN
    ALTER TABLE Produse
	ALTER COLUMN stoc INT;
END;
GO


GO
CREATE PROCEDURE UndoV4
AS
BEGIN
    ALTER TABLE Produse
	DROP COLUMN descriere;
END;
GO


CREATE PROCEDURE UndoV5
AS
BEGIN
    ALTER TABLE Produse
	DROP CONSTRAINT fk_CategorieProduse; 
	ALTER TABLE Produse
	DROP COLUMN id_categorie; 
END;
GO


CREATE PROCEDURE UndoDatabase
AS
BEGIN
    DECLARE @currentVersion INT;
    SELECT @currentVersion = numar FROM VersiuneCurenta;

    IF @currentVersion = 5
    BEGIN
        EXEC UndoV5;
        UPDATE VersiuneCurenta SET numar = 4;
    END;

    IF @currentVersion = 4
    BEGIN
        EXEC UndoV4;
        UPDATE VersiuneCurenta SET numar = 3;
    END;

    IF @currentVersion = 3
    BEGIN
        EXEC UndoV3;
        UPDATE VersiuneCurenta SET numar = 2;
    END;

    IF @currentVersion = 2
    BEGIN
        EXEC UndoV2;
        UPDATE VersiuneCurenta SET numar = 1;
    END;

    IF @currentVersion = 1
    BEGIN
        EXEC UndoV1;
        UPDATE VersiuneCurenta SET numar = 0;
    END;
END;
GO


EXEC UpdateDatabaseVersion;
EXEC UndoDatabase;



GO 
CREATE PROCEDURE go_to_version(@version INT)
AS
BEGIN

	IF @version < 0 OR @version > 5
	BEGIN
		PRINT 'Versiune invalida!';
		RETURN;
	END;

	DECLARE @currentVersion INT;
    SELECT @currentVersion = numar FROM VersiuneCurenta;

	IF @version < @currentVersion
	BEGIN
		WHILE @version != @currentVersion
		BEGIN
			EXEC UndoDatabase;
			SELECT @currentVersion = numar FROM VersiuneCurenta;
		END
	END;

	IF @version > @currentVersion
	BEGIN
		WHILE @version != @currentVersion
		BEGIN
			EXEC UpdateDatabaseVersion;
			SELECT @currentVersion = numar FROM VersiuneCurenta;
		END
	END;

END;
GO


SELECT numar FROM VersiuneCurenta;


EXEC go_to_version @version = 0;

EXEC go_to_version @version = 1;

EXEC go_to_version @version = 2;

EXEC go_to_version @version = 3;

EXEC go_to_version @version = 4;

EXEC go_to_version @version = 5;

EXEC go_to_version @version = 100;

EXEC go_to_version @version = -3;









