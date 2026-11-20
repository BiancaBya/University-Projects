USE AppleStore

SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
BEGIN TRAN;
UPDATE Departamente SET nr_angajati=135 WHERE nume='HR';
WAITFOR DELAY '00:00:05';
UPDATE Magazine SET nume='NUME' WHERE 
adresa='Adresa';
COMMIT TRAN;


SELECT * FROM Departamente
SELECT * FROM Magazine

GO 
CREATE OR ALTER PROCEDURE Deadlock1
AS 
BEGIN
	SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
	BEGIN TRAN;
	UPDATE Departamente SET nr_angajati=135 WHERE nume='HR';
	WAITFOR DELAY '00:00:05';
	UPDATE Magazine SET nume='NUME' WHERE 
	adresa='Adresa';
	COMMIT TRAN;
END
GO


-- Deadlock -> tranzactiile isi asteapta reciproc resurse blocate fara sa existe o cale de iesire
-- si SGDB-ul decide sa ucida una din tranzactii pentru a elibera resursele

-- solutii:
-- toate tranzactii care lucreaza cu aceleasi tabele le acceseaza în aceeasi ordine, ca sa nu se astepte una pe alta
-- folosirea nivelului de izolare SERIALIZABLE pe ambele tabele si nu pot interveni alte tranzacţii între lock-uri



