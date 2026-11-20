USE AppleStore

BEGIN TRAN;
SELECT * FROM Magazine WHERE id_magazin BETWEEN 1 AND 100;
WAITFOR DELAY '00:00:06';
SELECT * FROM Magazine WHERE id_magazin BETWEEN 1 AND 100;
COMMIT TRAN;

-- Phantom Reads -> intre doua selecturi din acceasi tranzactie apar randuri noi

-- folosim IZOLARE SERIALIZABLE -> nicio alta tranzactie care se executa in acelasi timp
-- nu poate modifica randurile folosite in tranzactia in care se fac select-urile




