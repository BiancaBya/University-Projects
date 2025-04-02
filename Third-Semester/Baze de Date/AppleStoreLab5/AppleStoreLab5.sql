/*
Tabele:

-Produse
Add, Delete, Update, FindOne, FindAll

-Comenzi
Add, Delete, Update, FindOne, FindAll

-Produse_Comenzi
Add, Delete, Update, FindOne, FindAll

Magazine
Add, Delete, Update, FindOne, FindAll

Angajati
Add, Delete, Update, FindOne, FindAll

*/


GO
CREATE OR ALTER FUNCTION ValidareAddProduse(
	@idFurnizor INT,
	@tipProdus VARCHAR(200),
	@numeProdus VARCHAR(200),
	@culoareProdus VARCHAR(200),
	@capacitateProdus VARCHAR(200),
	@pretProdus INT,
	@stocProdus INT
)
RETURNS BIT
AS
BEGIN

	IF @idFurnizor IS NULL OR
	   @tipProdus IS NULL OR
	   @numeProdus IS NULL OR
	   @culoareProdus IS NULL OR
	   @capacitateProdus IS NULL OR
	   @pretProdus IS NULL OR
	   @stocProdus IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Furnizori
		WHERE id_furnizor = @idFurnizor
	)
		RETURN 0;

	
	IF (@pretProdus <= 0)
		RETURN 0;

	IF (@stocProdus < 0)
		RETURN 0;

	IF NOT (RIGHT(@capacitateProdus, 2) = 'TB' OR RIGHT(@capacitateProdus, 2) = 'GB')
		RETURN 0;

	RETURN 1;

END
GO


GO
CREATE OR ALTER PROCEDURE AddInProduse
	@idFurnizor INT,
	@tipProdus VARCHAR(200),
	@numeProdus VARCHAR(200),
	@culoareProdus VARCHAR(200),
	@capacitateProdus VARCHAR(200),
	@pretProdus INT,
	@stocProdus INT
AS
BEGIN
	
	IF dbo.ValidareAddProduse(@idFurnizor, @tipProdus, @numeProdus, @culoareProdus, @capacitateProdus, @pretProdus, @stocProdus) = 0
	BEGIN
		PRINT 'Date invalide!';
		RETURN;
	END
	
	INSERT INTO Produse(id_furnizor, tip, nume, culoare, capacitate, pret, stoc) 
	VALUES(@idFurnizor, @tipProdus, @numeProdus, @culoareProdus, @capacitateProdus, @pretProdus, @stocProdus)

	PRINT 'Produsul a fost inserat!';

END
GO


EXEC AddInProduse 1, 'IPhone', 'IPhone 10', 'Negru', '256GB', 100, 100

SELECT * FROM Produse






GO
CREATE OR ALTER FUNCTION ValidareDeleteProduse(
	@idFurnizor INT,
	@tipProdus VARCHAR(200),
	@numeProdus VARCHAR(200),
	@culoareProdus VARCHAR(200),
	@capacitateProdus VARCHAR(200),
	@pretProdus INT,
	@stocProdus INT
)
RETURNS BIT
AS
BEGIN

	IF @idFurnizor IS NULL OR
	   @tipProdus IS NULL OR
	   @numeProdus IS NULL OR
	   @culoareProdus IS NULL OR
	   @capacitateProdus IS NULL OR
	   @pretProdus IS NULL OR
	   @stocProdus IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Produse
		WHERE id_furnizor = @idFurnizor AND tip = @tipProdus AND nume = @numeProdus AND
				culoare = @culoareProdus AND capacitate = @capacitateProdus AND pret = @pretProdus AND stoc = @stocProdus
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE DeleteFromProduse
	@idFurnizor INT,
	@tipProdus VARCHAR(200),
	@numeProdus VARCHAR(200),
	@culoareProdus VARCHAR(200),
	@capacitateProdus VARCHAR(200),
	@pretProdus INT,
	@stocProdus INT
AS
BEGIN
	
	IF dbo.ValidareDeleteProduse(@idFurnizor, @tipProdus, @numeProdus, @culoareProdus, @capacitateProdus, @pretProdus, @stocProdus) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	DELETE FROM Produse WHERE id_furnizor = @idFurnizor AND tip = @tipProdus AND nume = @numeProdus AND
						culoare = @culoareProdus AND capacitate = @capacitateProdus AND pret = @pretProdus AND stoc = @stocProdus

	PRINT 'Produsul a fost sters!'

END
GO


EXEC DeleteFromProduse 1, 'IPhone', 'IPhone 10', 'Negru', '256GB', 100, 100

SELECT * FROM Produse






GO
CREATE OR ALTER FUNCTION ValidareUpdateProduse(
	@idProdus INT,
	@idFurnizor INT,
	@tipProdus VARCHAR(200),
	@numeProdus VARCHAR(200),
	@culoareProdus VARCHAR(200),
	@capacitateProdus VARCHAR(200),
	@pretProdus INT,
	@stocProdus INT
)
RETURNS BIT
AS
BEGIN

	IF @idProdus IS NULL OR
	   @idFurnizor IS NULL OR 	
	   @tipProdus IS NULL OR
	   @numeProdus IS NULL OR
	   @culoareProdus IS NULL OR
	   @capacitateProdus IS NULL OR
	   @pretProdus IS NULL OR
	   @stocProdus IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Produse
		WHERE id_produs = @idProdus
	)
		RETURN 0;

	IF dbo.ValiareAddProduse(@idFurnizor, @tipProdus, @numeProdus, @culoareProdus, @capacitateProdus, @pretProdus, @stocProdus) = 0
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE UpdateInProduse
	@idProdus INT,
	@idFurnizor INT,
	@tipProdus VARCHAR(200),
	@numeProdus VARCHAR(200),
	@culoareProdus VARCHAR(200),
	@capacitateProdus VARCHAR(200),
	@pretProdus INT,
	@stocProdus INT
AS
BEGIN
	
	IF dbo.ValidareUpdateProduse(@idProdus, @idFurnizor, @tipProdus, @numeProdus, @culoareProdus, @capacitateProdus, @pretProdus, @stocProdus) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	UPDATE Produse
	SET id_furnizor = @idFurnizor, tip = @tipProdus, nume = @numeProdus, culoare = @culoareProdus,
		capacitate = @capacitateProdus, pret = @pretProdus, stoc = @stocProdus
	WHERE id_produs = @idProdus

	PRINT 'Produs modificat cu succes!'

END
GO


EXEC AddInProduse 1, 'IPhone', 'IPhone 10', 'Negru', '256GB', 100, 100
EXEC DeleteFromProduse 1, 'Mac', 'Mac 10', 'Negru', '1TB', 1000, 100
EXEC UpdateInProduse 23, 1, 'Mac', 'Mac 10', 'Negru', '1TB', 1000, 100

SELECT * FROM Produse





GO
CREATE OR ALTER FUNCTION ValidareFindOneProduse(
	@idProdus INT
)
RETURNS BIT
AS
BEGIN

	IF @idProdus IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Produse
		WHERE id_produs = @idProdus
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE FindOneProduse
	@idProdus INT
AS
BEGIN
	
	IF dbo.ValidareFindOneProduse(@idProdus) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	SELECT * FROM Produse WHERE id_produs = @idProdus

END
GO


EXEC FindOneProduse 5






GO
CREATE OR ALTER PROCEDURE FindAllProduse
AS
BEGIN

	SELECT * FROM Produse 

END
GO


EXEC FindAllProduse







-------------------------------------------------------------------------------------------------------------------------------






GO
CREATE OR ALTER FUNCTION ValidareAddComenzi(
	@idClient INT,
	@codComanda INT,
	@descriereComanda VARCHAR(200),
	@adresaComanda VARCHAR(200)
)
RETURNS BIT
AS
BEGIN

	IF @idClient IS NULL OR
	   @codComanda IS NULL OR
	   @descriereComanda IS NULL OR
	   @adresaComanda IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Clienti
		WHERE id_client = @idClient
	)
		RETURN 0;

	IF EXISTS(
		SELECT 1
		FROM Comenzi
		WHERE cod_comanda = @codComanda
	)
		RETURN 0;

	RETURN 1;

END
GO


GO
CREATE OR ALTER PROCEDURE AddInComenzi
	@idClient INT,
	@codComanda INT,
	@descriereComanda VARCHAR(200),
	@adresaComanda VARCHAR(200)
AS
BEGIN
	
	IF dbo.ValidareAddComenzi(@idClient, @codComanda, @descriereComanda, @adresaComanda) = 0
	BEGIN
		PRINT 'Date invalide!';
		RETURN;
	END
	
	INSERT INTO Comenzi(id_client, cod_comanda, descriere, adresa)
	VALUES(@idClient, @codComanda, @descriereComanda, @adresaComanda)

	PRINT 'Comanda a fost inserata!';

END
GO


EXEC AddInComenzi 2, 11, 'desc', 'adr'

SELECT * FROM Comenzi





GO
CREATE OR ALTER FUNCTION ValidareDeleteComenzi(
	@idClient INT,
	@codComanda INT,
	@descriereComanda VARCHAR(200),
	@adresaComanda VARCHAR(200)
)
RETURNS BIT
AS
BEGIN

	IF @idClient IS NULL OR
	   @codComanda IS NULL OR
	   @descriereComanda IS NULL OR
	   @adresaComanda IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Comenzi
		WHERE id_client = @idClient AND cod_comanda = @codComanda AND descriere = @descriereComanda AND adresa = @adresaComanda 
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE DeleteFromComenzi
	@idClient INT,
	@codComanda INT,
	@descriereComanda VARCHAR(200),
	@adresaComanda VARCHAR(200)
AS
BEGIN
	
	IF dbo.ValidareDeleteComenzi(@idClient, @codComanda, @descriereComanda, @adresaComanda) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	DELETE FROM Comenzi
	WHERE id_client = @idClient AND cod_comanda = @codComanda AND descriere = @descriereComanda AND adresa = @adresaComanda

	PRINT 'Comanda a fost stearsa!'

END
GO


EXEC DeleteFromComenzi 2, 11, 'desc', 'adr'

SELECT * FROM Comenzi





GO
CREATE OR ALTER FUNCTION ValidareUpdateComenzi(
	@idComanda INT,
	@idClient INT,
	@codComanda INT,
	@descriereComanda VARCHAR(200),
	@adresaComanda VARCHAR(200)
)
RETURNS BIT
AS
BEGIN

	IF @idComanda IS NULL OR
	   @idClient IS NULL OR
	   @codComanda IS NULL OR
	   @descriereComanda IS NULL OR
	   @adresaComanda IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Comenzi
		WHERE id_comanda = @idComanda
	)
		RETURN 0;

	IF NOT EXISTS(
		SELECT 1
		FROM Clienti
		WHERE id_client = @idClient
	)
		RETURN 0;

	IF EXISTS(
		SELECT 1
		FROM Comenzi
		WHERE cod_comanda = @codComanda AND id_comanda != @idComanda
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE UpdateInComenzi
	@idComanda INT,
	@idClient INT,
	@codComanda INT,
	@descriereComanda VARCHAR(200),
	@adresaComanda VARCHAR(200)
AS
BEGIN
	
	IF dbo.ValidareUpdateComenzi(@idComanda, @idClient, @codComanda, @descriereComanda, @adresaComanda) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	UPDATE Comenzi
	SET id_client = @idClient, cod_comanda = @codComanda, descriere = @descriereComanda, adresa = @adresaComanda
	WHERE id_comanda = @idComanda

	PRINT 'Comanda modificata cu succes!'

END
GO


EXEC AddInComenzi 2, 11, 'desc', 'adr'
EXEC UpdateInComenzi 13, 3, 11, 'desc1', 'adr1'
EXEC DeleteFromComenzi 3, 11, 'desc1', 'adr1'

SELECT * FROM Comenzi




GO
CREATE OR ALTER FUNCTION ValidareFindOneComenzi(
	@idComanda INT
)
RETURNS BIT
AS
BEGIN

	IF @idComanda IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Comenzi
		WHERE id_comanda = @idComanda
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE FindOneComenzi
	@idComanda INT
AS
BEGIN
	
	IF dbo.ValidareFindOneComenzi(@idComanda) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	SELECT * FROM Comenzi WHERE id_comanda = @idComanda

END
GO


EXEC FindOneComenzi 5







GO
CREATE OR ALTER PROCEDURE FindAllComenzi
AS
BEGIN

	SELECT * FROM Comenzi

END
GO


EXEC FindAllComenzi








-------------------------------------------------------------------------------------------------------------------------------







GO
CREATE OR ALTER FUNCTION ValidareAddProduseComenzi(
	@idProdus INT,
	@idComanda INT,
	@status VARCHAR(200),
	@cantitate INT
)
RETURNS BIT
AS
BEGIN

	IF @idProdus IS NULL OR
	   @idComanda IS NULL OR
	   @status IS NULL OR
	   @cantitate IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Produse
		WHERE id_produs = @idProdus
	)
		RETURN 0;

	IF NOT EXISTS(
		SELECT 1
		FROM Comenzi
		WHERE id_comanda = @idComanda
	)
		RETURN 0;

	IF EXISTS(
		SELECT 1
		FROM Produse_Comenzi
		WHERE id_produs = @idProdus AND id_comanda = @idComanda
	)
		RETURN 0;

	IF @status NOT IN ('Livrata', 'In curs de Livrare', 'Plasata')
		RETURN 0;

	IF @cantitate <= 0
		RETURN 0;

	RETURN 1;

END
GO


GO
CREATE OR ALTER PROCEDURE AddInProduseComenzi
	@idProdus INT,
	@idComanda INT,
	@status VARCHAR(200),
	@cantitate INT
AS
BEGIN
	
	IF dbo.ValidareAddProduseComenzi(@idProdus, @idComanda, @status, @cantitate) = 0
	BEGIN
		PRINT 'Date invalide!';
		RETURN;
	END
	
	INSERT INTO Produse_Comenzi(id_produs, id_comanda, status, cantitate)
	VALUES (@idProdus, @idComanda, @status, @cantitate)

	PRINT 'Inserare cu succes!';

END
GO


EXEC AddInProduseComenzi 10, 6, 'Livrata', 3

SELECT * FROM Produse_Comenzi



GO
CREATE OR ALTER FUNCTION ValidareDeleteProduseComenzi(
	@idProdus INT,
	@idComanda INT,
	@status VARCHAR(200),
	@cantitate INT
)
RETURNS BIT
AS
BEGIN

	IF @idProdus IS NULL OR
	   @idComanda IS NULL OR
	   @status IS NULL OR
	   @cantitate IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Produse_Comenzi
		WHERE id_produs = @idProdus AND id_comanda = @idComanda AND status = @status AND cantitate = @cantitate 
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE DeleteFromProduseComenzi
	@idProdus INT,
	@idComanda INT,
	@status VARCHAR(200),
	@cantitate INT
AS
BEGIN
	
	IF dbo.ValidareDeleteProduseComenzi(@idProdus, @idComanda, @status, @cantitate) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	DELETE FROM Produse_Comenzi
	WHERE id_produs = @idProdus AND id_comanda = @idComanda AND status = @status AND cantitate = @cantitate

	PRINT 'Stergere cu succes!'

END
GO


EXEC DeleteFromProduseComenzi 10, 6, 'Livrata', 3

SELECT * FROM Produse_Comenzi





GO
CREATE OR ALTER FUNCTION ValidareUpdateProduseComenzi(
	@idProdusOld INT,
	@idComandaOld INT,
	@idProdusNew INT,
	@idComandaNew INT,
	@status VARCHAR(200),
	@cantitate INT
)
RETURNS BIT
AS
BEGIN

	IF @idProdusOld IS NULL OR
	   @idComandaOld IS NULL OR
	   @idProdusNew IS NULL OR
	   @idComandaNew IS NULL OR
	   @status IS NULL OR
	   @cantitate IS NULL
		RETURN 0;
	
	IF NOT EXISTS(
		SELECT 1
		FROM Produse
		WHERE id_produs = @idProdusOld
	)
		RETURN 0;

	IF NOT EXISTS(
		SELECT 1
		FROM Produse
		WHERE id_produs = @idProdusNew
	)
		RETURN 0;

	IF NOT EXISTS(
		SELECT 1
		FROM Comenzi
		WHERE id_comanda = @idComandaOld
	)
		RETURN 0;

	IF NOT EXISTS(
		SELECT 1
		FROM Comenzi
		WHERE id_comanda = @idComandaNew
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE UpdateInProduseComenzi
	@idProdusOld INT,
	@idComandaOld INT,
	@idProdusNew INT,
	@idComandaNew INT,
	@status VARCHAR(200),
	@cantitate INT
AS
BEGIN
	
	IF dbo.ValidareUpdateProduseComenzi(@idProdusOld, @idComandaOld, @idProdusNew, @idComandaNew, @status, @cantitate) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	UPDATE Produse_Comenzi
	SET id_produs = @idProdusNew, id_comanda = @idComandaNew, status = @status, cantitate = @cantitate
	WHERE id_produs = @idProdusOld AND id_comanda = @idComandaOld

	PRINT 'Modificare cu succes!'

END
GO


EXEC AddInProduseComenzi 10, 6, 'Livrata', 3
EXEC UpdateInProduseComenzi 10, 6, 11, 7, 'Plasata', 4
EXEC DeleteFromProduseComenzi 11, 7, 'Plasata', 4

SELECT * FROM Produse_Comenzi






GO
CREATE OR ALTER FUNCTION ValidareFindOneProduseComenzi(
	@idProdus INT,
	@idComanda INT
)
RETURNS BIT
AS
BEGIN

	IF @idProdus IS NULL OR
	   @idComanda IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Produse
		WHERE id_produs = @idProdus
	)
		RETURN 0;

	IF NOT EXISTS(
		SELECT 1
		FROM Comenzi
		WHERE id_comanda = @idComanda
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE FindOneProduseComenzi
	@idProdus INT,
	@idComanda INT
AS
BEGIN
	
	IF dbo.ValidareFindOneProduseComenzi(@idProdus, @idComanda) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	SELECT * FROM Produse_Comenzi WHERE id_produs = @idProdus AND id_comanda = @idComanda

END
GO


EXEC FindOneProduseComenzi 2, 3





GO
CREATE OR ALTER PROCEDURE FindAllProduseComenzi
AS
BEGIN

	SELECT * FROM Produse_Comenzi 

END
GO


EXEC FindAllProduseComenzi







-------------------------------------------------------------------------------------------------------------------------------







GO
CREATE OR ALTER FUNCTION ValidareAddMagazine(
	@numeMagazin VARCHAR(200),
	@adresaMagazin VARCHAR(200)
)
RETURNS BIT
AS
BEGIN

	IF @numeMagazin IS NULL OR
	   @adresaMagazin IS NULL
		RETURN 0;

	RETURN 1;

END
GO


GO
CREATE OR ALTER PROCEDURE AddInMagazine
	@numeMagazin VARCHAR(200),
	@adresaMagazin VARCHAR(200)
AS
BEGIN
	
	IF dbo.ValidareAddMagazine(@numeMagazin, @adresaMagazin) = 0
	BEGIN
		PRINT 'Date invalide!';
		RETURN;
	END
	
	INSERT INTO Magazine(nume, adresa)
	VALUES(@numeMagazin, @adresaMagazin)

	PRINT 'Magazinul a fost inserat!';

END
GO


EXEC AddInMagazine 'nume', 'adresa'

SELECT * FROM Magazine





GO
CREATE OR ALTER FUNCTION ValidareDeleteMagazine(
	@numeMagazin VARCHAR(200),
	@adresaMagazin VARCHAR(200)
)
RETURNS BIT
AS
BEGIN

	IF @numeMagazin IS NULL OR
	   @adresaMagazin IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Magazine
		WHERE nume = @numeMagazin AND adresa = @adresaMagazin 
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE DeleteFromMagazine
	@numeMagazin VARCHAR(200),
	@adresaMagazin VARCHAR(200)
AS
BEGIN
	
	IF dbo.ValidareDeleteMagazine(@numeMagazin, @adresaMagazin) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	DELETE FROM Magazine
	WHERE nume = @numeMagazin AND adresa = @adresaMagazin 

	PRINT 'Magazinul a fost sters!'

END
GO


EXEC DeleteFromMagazine 'nume', 'adresa'

SELECT * FROM Magazine





GO
CREATE OR ALTER FUNCTION ValidareUpdateMagazine(
	@idMagazin INT,
	@numeMagazin VARCHAR(200),
	@adresaMagazin VARCHAR(200)
)
RETURNS BIT
AS
BEGIN

	IF @idMagazin IS NULL OR
	   @numeMagazin IS NULL OR
	   @adresaMagazin IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Magazine
		WHERE id_magazin = @idMagazin
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE UpdateInMagazine
	@idMagazin INT,
	@numeMagazin VARCHAR(200),
	@adresaMagazin VARCHAR(200)
AS
BEGIN
	
	IF dbo.ValidareUpdateMagazine(@idMagazin, @numeMagazin, @adresaMagazin) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	UPDATE Magazine
	SET nume = @numeMagazin, adresa = @adresaMagazin
	WHERE id_magazin = @idMagazin

	PRINT 'Magazin modificat cu succes!'

END
GO


EXEC AddInMagazine 'nume',  'adresa'
EXEC UpdateInMagazine 7, 'nume1', 'adresa1'
EXEC DeleteFromMagazine 'nume1', 'adresa1'

SELECT * FROM Magazine







GO
CREATE OR ALTER FUNCTION ValidareFindOneMagazine(
	@idMagazin INT
)
RETURNS BIT
AS
BEGIN

	IF @idMagazin IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Magazine
		WHERE id_magazin = @idMagazin
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE FindOneMagazine
	@idMagazin INT
AS
BEGIN
	
	IF dbo.ValidareFindOneMagazine(@idMagazin) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	SELECT * FROM Magazine WHERE id_magazin = @idMagazin

END
GO


EXEC FindOneMagazine 5





GO
CREATE OR ALTER PROCEDURE FindAllMagazine
AS
BEGIN

	SELECT * FROM Magazine

END
GO


EXEC FindAllMagazine








-------------------------------------------------------------------------------------------------------------------------------









GO
CREATE OR ALTER FUNCTION ValidareAddAngajati(
	@idMagazin INT,
	@idDepartament INT,
	@nume VARCHAR(200),
	@functie VARCHAR(200),
	@email VARCHAR(200),
	@telefon VARCHAR(200)
)
RETURNS BIT
AS
BEGIN

	IF @idMagazin IS NULL OR
	   @idDepartament IS NULL OR
	   @nume IS NULL OR
	   @functie IS NULL OR
	   @email IS NULL OR
	   @telefon IS NULL
		RETURN 0;

	
	IF NOT EXISTS(
		SELECT 1
		FROM Magazine
		WHERE id_magazin = @idMagazin
	)
		RETURN 0;

	IF NOT EXISTS(
		SELECT 1
		FROM Departamente
		WHERE id_departament = @idDepartament
	)
		RETURN 0;

	IF EXISTS(
		SELECT 1
		FROM Angajati
		WHERE nume = @nume
	)
		RETURN 0;

	IF EXISTS(
		SELECT 1
		FROM Angajati
		WHERE email = @email
	)
		RETURN 0;

	IF EXISTS(
		SELECT 1
		FROM Angajati
		WHERE nr_telefon = @telefon
	)
		RETURN 0;

	IF LEN(@telefon) != 10
		RETURN 0;

	RETURN 1;

END
GO


GO
CREATE OR ALTER PROCEDURE AddInAngajati
	@idMagazin INT,
	@idDepartament INT,
	@nume VARCHAR(200),
	@functie VARCHAR(200),
	@email VARCHAR(200),
	@telefon VARCHAR(200)
AS
BEGIN
	
	IF dbo.ValidareAddAngajati(@idMagazin, @idDepartament, @nume, @functie, @email, @telefon) = 0
	BEGIN
		PRINT 'Date invalide!';
		RETURN;
	END
	
	INSERT INTO Angajati(id_magazin, id_departament, nume, functie, email, nr_telefon)
	VALUES(@idMagazin, @idDepartament, @nume, @functie, @email, @telefon)

	PRINT 'Angajatul a fost inserat!';

END
GO


EXEC AddInAngajati  1, 1, 'nume', 'fucntie', 'email', '1111111111'

SELECT * FROM Angajati





GO
CREATE OR ALTER FUNCTION ValidareDeleteAngajati(
	@idMagazin INT,
	@idDepartament INT,
	@nume VARCHAR(200),
	@functie VARCHAR(200),
	@email VARCHAR(200),
	@telefon VARCHAR(200)
)
RETURNS BIT
AS
BEGIN

	IF @idMagazin IS NULL OR
	   @idDepartament IS NULL OR
	   @nume IS NULL OR
	   @functie IS NULL OR
	   @email IS NULL OR
	   @telefon IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Angajati
		WHERE id_magazin = @idMagazin AND id_departament = @idDepartament AND nume = @nume AND functie = @functie
			AND email = @email AND nr_telefon = @telefon
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE DeleteFromAngajati
	@idMagazin INT,
	@idDepartament INT,
	@nume VARCHAR(200),
	@functie VARCHAR(200),
	@email VARCHAR(200),
	@telefon VARCHAR(200)
AS
BEGIN
	
	IF dbo.ValidareDeleteAngajati(@idMagazin, @idDepartament, @nume, @functie, @email, @telefon) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	DELETE FROM Angajati
		WHERE id_magazin = @idMagazin AND id_departament = @idDepartament AND nume = @nume AND functie = @functie
			AND email = @email AND nr_telefon = @telefon

	PRINT 'Angajatul a fost sters!'

END
GO


EXEC DeleteFromAngajati 1, 1, 'nume', 'fucntie', 'email', '1111111111'

SELECT * FROM Angajati





GO
CREATE OR ALTER FUNCTION ValidareUpdateAngajati(
	@idAngajat INT,
	@idMagazin INT,
	@idDepartament INT,
	@nume VARCHAR(200),
	@functie VARCHAR(200),
	@email VARCHAR(200),
	@telefon VARCHAR(200)
)
RETURNS BIT
AS
BEGIN

	IF @idAngajat IS NULL OR
	   @idMagazin IS NULL OR
	   @idDepartament IS NULL OR
	   @nume IS NULL OR
	   @functie IS NULL OR
	   @email IS NULL OR
	   @telefon IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Angajati
		WHERE id_angajat = @idAngajat
	)
		RETURN 0;


	IF NOT EXISTS(
		SELECT 1
		FROM Magazine
		WHERE id_magazin = @idMagazin
	)
		RETURN 0;

	IF NOT EXISTS(
		SELECT 1
		FROM Departamente
		WHERE id_departament = @idDepartament
	)
		RETURN 0;


	IF EXISTS(
		SELECT 1
		FROM Angajati
		WHERE (nume = @nume OR email = @email OR nr_telefon = @telefon)
			AND id_angajat != @idAngajat
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE UpdateInAngajati
	@idAngajat INT,
	@idMagazin INT,
	@idDepartament INT,
	@nume VARCHAR(200),
	@functie VARCHAR(200),
	@email VARCHAR(200),
	@telefon VARCHAR(200)
AS
BEGIN
	
	IF dbo.ValidareUpdateAngajati(@idAngajat, @idMagazin, @idDepartament, @nume, @functie, @email, @telefon) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	UPDATE Angajati
	SET id_magazin = @idMagazin, id_departament = @idDepartament, nume = @nume, functie = @functie, email = @email, nr_telefon = @telefon
	WHERE id_angajat = @idAngajat

	PRINT 'Angajat modificat cu succes!'

END
GO


EXEC AddInAngajati  1, 1, 'nume', 'fucntie', 'email', '1111111111'
EXEC UpdateInAngajati 7, 2, 2, 'nume1', 'fucntie1', 'email1', '2222222222'
EXEC DeleteFromAngajati 2, 2, 'nume1', 'fucntie1', 'email1', '2222222222'

SELECT * FROM Angajati







GO
CREATE OR ALTER FUNCTION ValidareFindOneAngajati(
	@idAngajat INT
)
RETURNS BIT
AS
BEGIN

	IF @idAngajat IS NULL
		RETURN 0;
	

	IF NOT EXISTS(
		SELECT 1
		FROM Angajati
		WHERE id_angajat = @idAngajat
	)
		RETURN 0;

	RETURN 1;

END
GO



GO
CREATE OR ALTER PROCEDURE FindOneAngajati
	@idAngajat INT
AS
BEGIN
	
	IF dbo.ValidareFindOneAngajati(@idAngajat) = 0
	BEGIN
		PRINT 'Date invalide!'
		RETURN;
	END

	SELECT * FROM Angajati WHERE id_angajat = @idAngajat

END
GO


EXEC FindOneAngajati 5





GO
CREATE OR ALTER PROCEDURE FindAllAngajati
AS
BEGIN

	SELECT * FROM Angajati

END
GO

EXEC FindAllAngajati




GO
CREATE OR ALTER VIEW View_Produse
AS
	SELECT nume, stoc FROM Produse
	WHERE stoc >= 70000
GO

CREATE NONCLUSTERED INDEX N_indx_Produse_stoc ON Produse(nume, stoc)

SELECT * FROM View_Produse



GO
CREATE OR ALTER VIEW View_Produse_comenzi
AS
	SELECT P.tip, P.nume, P.stoc FROM Produse P
	JOIN Produse_Comenzi Pc ON P.id_produs = Pc.id_produs
	JOIN Comenzi C ON Pc.id_comanda = C.id_comanda
GO

CREATE NONCLUSTERED INDEX N_indx_produse_comenzi ON Produse_Comenzi(id_produs, id_comanda)


SELECT * FROM View_Produse_comenzi













