USE AppleStore


GO
CREATE OR ALTER PROCEDURE AddProdusPromotieSave
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
	EXEC TraceEntry 'AddProdusPromotieSave';

	BEGIN TRAN
	BEGIN TRY
		DECLARE @currentStep INT = 0;

		DECLARE @ok INT = dbo.ValidareAddProduse(@idFurnizor, @tipProdus, @numeProdus, @culoareProdus, @capacitateProdus, @pretProdus, @stocProdus);
		IF (@ok = 0)
			THROW 50001, 'Produs invalid!', 1;

		INSERT INTO Produse(id_furnizor, tip, nume, culoare, capacitate, pret, stoc) 
		VALUES(@idFurnizor, @tipProdus, @numeProdus, @culoareProdus, @capacitateProdus, @pretProdus, @stocProdus)

		DECLARE @productId INT = SCOPE_IDENTITY();
		EXEC LogInfo 'Produs adaugat', 'INSERT', 'Produse', 'AddProdusPromotieSave', NULL, @productId;

		SAVE TRAN savepoint1
		SET @currentStep = 1;

		SET @ok = dbo.ValidarePromotie(@numePromotie, @procent);
		IF (@ok = 0)
			THROW 50001, 'Promotie invalida!', 1;

		INSERT INTO Promotii(nume, procent)
		VALUES(@numePromotie, @procent);

		DECLARE @promotieId INT = SCOPE_IDENTITY();
		EXEC LogInfo 'Category added', 'INSERT', 'Categorii', 'AddProdusPromotieSave', NULL, @promotieId;

		SAVE TRAN savepoint2
		SET @currentStep = 2;

		SET @ok = dbo.ValidareDurata(@durata);
		IF (@ok = 0)
			THROW 50001, 'Durata Invalida!', 1;

		INSERT INTO Produse_Promotii(id_produs, id_promotie, durata)
		VALUES(@productId, @promotieId, @durata);

		EXEC LogInfo 'Produs si Promotie adaugate', 'INSERT', 'Produse_Promotii', 'AddProdusPromotieSave', NULL, NULL;

		COMMIT TRAN

		EXEC TraceExit 'AddProdusPromotieSave';
	END TRY
	BEGIN CATCH
		DECLARE @tableName VARCHAR(30);
		IF @currentStep = 0
		BEGIN 
			SET @tableName = 'Produse';
			ROLLBACK TRAN;
		END
		ELSE IF @currentStep = 1 
		BEGIN
			SET @tableName = 'Promotii';
			ROLLBACK TRAN savepoint1;
			COMMIT TRAN;
		END
		ELSE IF @currentStep = 2
		BEGIN
			SET @tableName = 'Produse_Promotii';
			ROLLBACK TRAN savepoint2;
			COMMIT TRAN;
		END
		DECLARE @errMsg VARCHAR(300) = ERROR_MESSAGE();
		DECLARE @errProc VARCHAR(100) = ERROR_PROCEDURE();
		DECLARE @errLine INT = ERROR_LINE();

		EXEC LogError @errMsg, 'INSERT', @tableName, @errProc, @errLine, NULL;

		THROW 50001, @errMsg, 1;

		EXEC TraceExit 'AddProdusPromotieSave';
	END CATCH
END;




GO
CREATE OR ALTER PROCEDURE TestAddSavepointRB
AS
BEGIN
	DECLARE @produsCount INT = (SELECT COUNT(*) FROM Produse);
	DECLARE @promotieCount INT = (SELECT COUNT(*) FROM Promotii);
	DECLARE @produsPromotieCount INT = (SELECT COUNT(*) FROM Produse_Promotii);

	EXEC AddProdusPromotieSave 6, 'Tip Produs', 'Nume Produs', 'Culoare Produs', '256 GB', 10, 10, 'Nume Promotie', 10, 'durata promotie';

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
		EXEC AddProdusPromotieSave 0, '', NULL, NULL, '',  -1, -1, 'Nume Promotie', 10, 'durata promotie'
		RAISERROR('Test2 - failed: produs', 16, 1);
	END TRY
	BEGIN CATCH
		DECLARE @errorMsg VARCHAR(300) = ERROR_MESSAGE();
		PRINT @errorMsg;
		DECLARE @expectedErrMsg VARCHAR(300)= 'Produs invalid!';

		IF (@errorMsg <> @expectedErrMsg)
			RAISERROR('Test2.1 - failed: validare' , 16, 1);
		ELSE
			PRINT('Test2.1 passed - validare')

		IF NOT((SELECT COUNT(*) FROM Produse) = @produsCount AND 
		(SELECT COUNT(*) FROM Promotii) = @promotieCount AND
		(SELECT COUNT(*) FROM Produse_Promotii) = @produsPromotieCount)
			
			RAISERROR('Test2 - failed: date adaugate', 16, 1);
		ELSE
			PRINT('Test2 passed: produs')
	END CATCH

	
	SET @produsCount = (SELECT COUNT(*) FROM Produse);
	SET @promotieCount = (SELECT COUNT(*) FROM Promotii);
	SET @produsPromotieCount = (SELECT COUNT(*) FROM Produse_Promotii);

	BEGIN TRY
		EXEC AddProdusPromotieSave 6, 'Tip Produs', 'Nume Produs', 'Culoare Produs', '256 GB', 10, 10, NULL, 120, 'durata promotie'
		RAISERROR('Test3 - failed: promotie', 16, 1);
	END TRY
	BEGIN CATCH
		DECLARE @errorMsg2 VARCHAR(300) = ERROR_MESSAGE();
		DECLARE @expectedErrMsg2 VARCHAR(300)= 'Promotie invalida!';

		IF (@errorMsg2 <> @expectedErrMsg2)
			RAISERROR('Test3.1 - failed: validare' , 16, 1);
		ELSE
			PRINT('Test3.1 passed: validare')

		IF NOT((SELECT COUNT(*) FROM Produse) = @produsCount + 1 AND 
		(SELECT COUNT(*) FROM Promotii) = @promotieCount AND
		(SELECT COUNT(*) FROM Produse_Promotii) = @produsPromotieCount)
			RAISERROR('Test3.2 - failed: rollback, produsul nu a fost adaugat', 16, 1);
		ELSE
			PRINT('Test3.2 passed')
	END CATCH

	SET @produsCount = (SELECT COUNT(*) FROM Produse);
	SET @promotieCount = (SELECT COUNT(*) FROM Promotii);
	SET @produsPromotieCount = (SELECT COUNT(*) FROM Produse_Promotii);

	BEGIN TRY
		EXEC AddProdusPromotieSave 6, 'Tip Produs', 'Nume Produs', 'Culoare Produs', '256 GB', 10, 10, 'Nume Promotie', 10, NULL
		RAISERROR('Test3 - failed: durata', 16, 1);
	END TRY
	BEGIN CATCH
		DECLARE @errorMsg3 VARCHAR(300) = ERROR_MESSAGE();
		DECLARE @expectedErrMsg3 VARCHAR(300)= 'Durata invalida!';

		IF (@errorMsg3 <> @expectedErrMsg3)
			RAISERROR('Test3.1 - failed: validare' , 16, 1);
		ELSE
			PRINT('Test3.1 passed: validare')

		IF NOT((SELECT COUNT(*) FROM Produse) = @produsCount + 1 AND 
		(SELECT COUNT(*) FROM Promotii) = @promotieCount + 1 AND
		(SELECT COUNT(*) FROM Produse_Promotii) = @produsPromotieCount)
			RAISERROR('Test3.2 - failed: rollback, produsul/promotia nu au fost adaugate', 16, 1);
		ELSE
			PRINT('Test3.2 passed')
	END CATCH
	
END;


EXEC TestAddSavepointRB;
SELECT * FROM LogTable;


SELECT * FROM Produse;
SELECT * FROM Promotii;
SELECT * FROM Produse_Promotii;


DELETE FROM Promotii WHERE nume = 'Nume Promotie';
DELETE FROM Produse WHERE tip = 'Tip Produs';


DELETE  FROM LogTable

