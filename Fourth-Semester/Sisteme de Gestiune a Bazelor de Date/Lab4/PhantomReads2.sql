USE AppleStore

BEGIN TRAN;
WAITFOR DELAY '00:00:03';
INSERT INTO Magazine(adresa, nume) VALUES 
('Adresa','Nume');
COMMIT TRAN;

