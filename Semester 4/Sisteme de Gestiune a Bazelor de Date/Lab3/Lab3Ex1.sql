USE AppleStore

-- Tabele: Produse-Promotii

-- Produse: 
--  id_furnizor, tip, nume, culoare, capacitate, stoc, pret

-- Promotii:
-- nume, procent

-- Produse_Promotii:
-- id_produs, id_promotie, durata

GO
CREATE OR ALTER FUNCTION ValidarePromotie(
	@nume VARCHAR(200),
	@procent INT
)
RETURNS BIT
AS
BEGIN
	IF @nume IS NULL OR
	   @procent IS NULL
		RETURN 0;

	IF (@procent < 0 OR @procent > 100)
	RETURN 0;

	RETURN 1;
END;


GO
CREATE OR ALTER FUNCTION ValidareDurata(
	@durata VARCHAR(200)
)
RETURNS BIT
AS
BEGIN
	IF @durata IS NULL
		RETURN 0;

	RETURN 1;
END;


GO
CREATE OR ALTER PROCEDURE AddProdusPromotie
	@idFurnizor INT,
	@tipProdus VARCHAR(200),
	@numeProdus VARCHAR(200),
	@culoareProdus VARCHAR(200),
	@capacitateProdus VARCHAR(200),
	@pretProdus INT,
	@stocProdus INT,

	@numePromotie VARCHAR(200),
	@procent INT,

	@durata VARCHAR(200)
AS
BEGIN
	EXEC TraceEntry 'AddProductAndPromotion';

	BEGIN TRAN
	PRINT @@TRANCOUNT
	BEGIN TRY
		DECLARE @currentStep INT = 1;

		DECLARE @ok INT = dbo.ValidareAddProduse(@idFurnizor, @tipProdus, @numeProdus, @culoareProdus, @capacitateProdus, @pretProdus, @stocProdus);
		IF (@ok = 0)
			THROW 50001, 'Produs invalid!', 1;

		INSERT INTO Produse(id_furnizor, tip, nume, culoare, capacitate, pret, stoc) 
		VALUES(@idFurnizor, @tipProdus, @numeProdus, @culoareProdus, @capacitateProdus, @pretProdus, @stocProdus)

		DECLARE @productId INT = SCOPE_IDENTITY();
		EXEC LogInfo 'Produs adaugat', 'INSERT', 'Produse', 'AddProductAndPromotion', NULL, @productId;

		SET @currentStep = 2;
		SET @ok = dbo.ValidarePromotie(@numePromotie, @procent);
		IF (@ok = 0)
			THROW 50001, 'Promotie invalida!', 1;

		INSERT INTO Promotii(nume, procent)
		VALUES(@numePromotie, @procent);

		DECLARE @promotieId INT = SCOPE_IDENTITY();
		EXEC LogInfo 'Promotie adaugata', 'INSERT', 'Promotii', 'AddProductAndPromotion', NULL, @promotieId;

		SET @currentStep = 3;
		SET @ok = dbo.ValidareDurata(@durata);
		IF (@ok = 0)
			THROW 50001, 'Durata Invalida!', 1;

		INSERT INTO Produse_Promotii(id_produs, id_promotie, durata)
		VALUES(@productId, @promotieId, @durata);

		EXEC LogInfo 'Produs si Promotie adaugate', 'INSERT', 'Produse_Promotii', 'AddProductAndPromotion', NULL, NULL;

		COMMIT TRAN
		PRINT @@TRANCOUNT

		EXEC TraceExit 'AddProductAndPromotion';
	END TRY
	BEGIN CATCH
		DECLARE @tableName VARCHAR(30) = 'Produse';
		IF @currentStep = 1 
			SET @tableName = 'Promotii';
		ELSE IF @currentStep = 2
			SET @tableName = 'Produse_Promotii';

		DECLARE @errMsg VARCHAR(300) = ERROR_MESSAGE();
		DECLARE @errProc VARCHAR(100) = ERROR_PROCEDURE();
		DECLARE @errLine INT = ERROR_LINE();

		EXEC LogError @errMsg, 'INSERT', @tableName, @errProc, @errLine, NULL;
		ROLLBACK TRAN;

		THROW 50001, @errMsg, 1;

		EXEC TraceExit 'AddProductAndCategory';
	END CATCH
END;


GO
CREATE OR ALTER PROCEDURE TestAddFullTranRB
AS
BEGIN
	DECLARE @produsCount INT = (SELECT COUNT(*) FROM Produse);
	DECLARE @promotieCount INT = (SELECT COUNT(*) FROM Promotii);
	DECLARE @produsPromotieCount INT = (SELECT COUNT(*) FROM Produse_Promotii);

	EXEC AddProdusPromotie 6, 'Tip Produs', 'Nume Produs', 'Culoare Produs', '256 GB', 10, 10, 'Nume Promotie', 10, 'durata promotie'

	IF NOT((SELECT COUNT(*) FROM Produse) = @produsCount + 1 AND 
		(SELECT COUNT(*) FROM Promotii) = @promotieCount + 1 AND
		(SELECT COUNT(*) FROM Produse_Promotii) = @produsPromotieCount + 1)
			RAISERROR('Test1 - failed', 16, 1);
	ELSE
		PRINT('Test1 passed')

	SET @produsCount = (SELECT COUNT(*) FROM Produse);
	SET @promotieCount = (SELECT COUNT(*) FROM Promotii);
	SET @produsPromotieCount = (SELECT COUNT(*) FROM Produse_Promotii);

	BEGIN TRY
		EXEC AddProdusPromotie 0, '', NULL, NULL, '',  -1, -1, 'Nume Promotie', 10, 'durata promotie'
		RAISERROR('Test2 - failed: produs', 16, 1);
	END TRY
	BEGIN CATCH
	DECLARE @errorMsg VARCHAR(300) = ERROR_MESSAGE();
	DECLARE @expectedErrMsg VARCHAR(300)= 'Produs invalid!';

	IF (@errorMsg <> @expectedErrMsg)
		RAISERROR('Test2 - failed: validare' , 16, 1);
	ELSE
		PRINT('Test2 passed - validare')

	IF NOT((SELECT COUNT(*) FROM Produse) = @produsCount AND 
		(SELECT COUNT(*) FROM Promotii) = @promotieCount AND
		(SELECT COUNT(*) FROM Produse_Promotii) = @produsPromotieCount)
			
			RAISERROR('Test2 - failed: date adaugate', 16, 1);
	ELSE
		PRINT('Test2 passed: produs')
	END CATCH



	BEGIN TRY
		EXEC AddProdusPromotie 6, 'Tip Produs', 'Nume Produs', 'Culoare Produs', '256 GB', 10, 10, NULL, 120, 'durata promotie'
		RAISERROR('Test3 - failed: promotie', 16, 1);
	END TRY
	BEGIN CATCH
	DECLARE @errorMsg2 VARCHAR(300) = ERROR_MESSAGE();
	DECLARE @expectedErrMsg2 VARCHAR(300)= 'Promotie invalida!';

	IF (@errorMsg2 <> @expectedErrMsg2)
		RAISERROR('Test3 - failed: validare' , 16, 1);
	ELSE
		PRINT('Test3 passed - validare')

	IF NOT((SELECT COUNT(*) FROM Produse) = @produsCount AND 
		(SELECT COUNT(*) FROM Promotii) = @promotieCount AND
		(SELECT COUNT(*) FROM Produse_Promotii) = @produsPromotieCount)
			
			RAISERROR('Test3 - failed: date adaugate', 16, 1);
	ELSE
		PRINT('Test3 passed: promotie')
	END CATCH


	BEGIN TRY
		EXEC AddProdusPromotie 6, 'Tip Produs', 'Nume Produs', 'Culoare Produs', '256 GB', 10, 10, 'Nume Promotie', 10, NULL
		RAISERROR('Test4 - failed: durata', 16, 1);
	END TRY
	BEGIN CATCH
	DECLARE @errorMsg3 VARCHAR(300) = ERROR_MESSAGE();
	DECLARE @expectedErrMsg3 VARCHAR(300)= 'Durata invalida!';

	IF (@errorMsg2 <> @expectedErrMsg2)
		RAISERROR('Test4 - failed: validare' , 16, 1);
	ELSE
		PRINT('Test4 passed - validare')

	IF NOT((SELECT COUNT(*) FROM Produse) = @produsCount AND 
		(SELECT COUNT(*) FROM Promotii) = @promotieCount AND
		(SELECT COUNT(*) FROM Produse_Promotii) = @produsPromotieCount)
			
			RAISERROR('Test4 - failed: date adaugate', 16, 1);
	ELSE
		PRINT('Test4 passed: durata')
	END CATCH

END;


EXEC TestAddFullTranRB;
SELECT * FROM LogTable;

SELECT * FROM Produse;
SELECT * FROM Promotii;
SELECT * FROM Produse_Promotii;


DELETE FROM Promotii WHERE nume = 'Nume Promotie';
DELETE FROM Produse WHERE tip = 'Tip Produs';



