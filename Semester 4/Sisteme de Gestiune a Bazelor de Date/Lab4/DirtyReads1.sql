USE AppleStore


BEGIN TRAN;
UPDATE Departamente SET nr_angajati=500 WHERE nume='HR';
WAITFOR DELAY '00:00:07';
ROLLBACK TRAN;


-- Dirty Reads -> citirea de date modificate de o alta tranzactie, dar necomise inca

-- solutie: READ COMMITED ISOLATION -> SELECT-ul din DirtyReads2 nu va vedea datele necomise



