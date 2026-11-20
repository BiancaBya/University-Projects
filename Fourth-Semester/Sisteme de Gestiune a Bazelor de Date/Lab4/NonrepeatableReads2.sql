USE AppleStore

BEGIN TRAN;
WAITFOR DELAY '00:00:03';
UPDATE Departamente SET nr_angajati=500 WHERE nume='HR';
COMMIT TRAN;

