USE AppleStore

BEGIN TRAN;
SELECT * FROM Departamente;
WAITFOR DELAY '00:00:06';
SELECT * FROM Departamente;
COMMIT TRAN;

-- Nonrepeatable Reads -> doua operatii identice in interiorul aceleiasi tranzactii dau rezultate diferite


-- pentru a evita cazul de nonrepeatable reads folosim nivelul de izolare REPEATABLE READ
-- orice rand citit de prima interogare bloceaza orice UPDATE pe acel rand de catre alte tranzactii



