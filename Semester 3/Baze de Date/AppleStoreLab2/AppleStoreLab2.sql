USE [AppleStore]

INSERT INTO Furnizori(nume, tara, nr_telefon, email)
VALUES
('Gigi','Romania','0711111111','gigi@gmail.com'),
('Micu','Rusia','0722222222','micu@gmail.com'),
('Ionela','India','0733333333','ionela@gmai.com'),
('Florin','America','0744444444','florin@gmail.com'),
('Marcel','Indonezia','0755555555','marcel@gmail.com'),
('Bobi','China','0766666666','bobi@gmail.com');



INSERT INTO Produse(id_furnizor, tip, nume, culoare, capacitate, pret,stoc)
VALUES
(1, 'IPhone', 'IPhone 13 Pro Max', 'Negru', '256GB', 1000, 10000),
(1, 'IPhone', 'IPhone 12 Pro', 'Albastru', '128GB', 800, 5000),
(1, 'IPhone', 'IPhone 15 Pro Max', 'Negru', '1TB', 1500, 50000),
(1, 'IPhone', 'IPhone 14 Pro', 'Alb', '512GB', 1200, 30000),
(2, 'IPad', 'IPad Pro', 'Negru', '512GB', 1800, 25000),
(2, 'IPad', 'IPad Mini', 'Mov', '128GB', 900, 5000),
(2, 'IPad', 'IPad Gen 10', 'Verde', '512GB', 1200, 20000),
(3, 'Apple Watch', 'Apple Watch 10', 'Negru', '64GB', 500, 50000),
(3, 'Apple Watch', 'Apple Watch 9', 'Albastru', '64GB', 400, 50000),
(3, 'Apple Watch', 'Apple Watch Ultra', 'Portocaliu', '128GB', 1000, 70000),
(4, 'AirPods', 'AirPods Pro', 'Alb', '12GB', 300, 80000),
(4, 'AirPods', 'AirPods Max', 'Negru', '12GB', 500, 100000),
(4, 'AirPods', 'AirPods 3', 'Alb', '12GB', 200, 70000),
(4, 'AirPods', 'AirPods 2', 'Alb', '12GB', 150, 80000),
(5, 'MacBook', 'MacBook Pro 16 inch', 'Negru', '4TB', 6000, 100000),
(5, 'MacBook', 'MacBook Pro 14 inch', 'Argintiu', '1TB', 2000, 70000),
(5, 'MacBook', 'MacBook Air 15 inch', 'Albastru', '512GB', 1500, 100000),
(6, 'Display', 'Studio Display', 'Alb', '512GB', 2000, 20000),
(6, 'Display', 'Pro Display', 'Negru', '2TB', 4000, 40000);



INSERT INTO Promotii(nume, procent)
VALUES
('Smart Sale', 30),
('Apple Smart Choice', 25),
('Deal of the Day', 10),
('iDeal', 50);



INSERT INTO Produse_Promotii(id_produs, id_promotie, durata)
VALUES (6,4,'10 zile');

INSERT INTO Produse_Promotii(id_produs, id_promotie, durata)
VALUES 
(5,4,'o luna'),
(4,3,'15 zile'),
(3,3,'20 zile'),
(3,2,'5 zile'),
(2,4,'25 zile'),
(2,1,'o luna'),
(1,3,'10 zile');



INSERT INTO Clienti(nume, email, nr_telefon)
VALUES
('Gabi', 'gabi@yahoo.com', '0712121212'),
('Marius', 'marius@gamil.com', '0713131313'),
('Flavian', 'flavian@gmail.com', '0748484848'),
('Cristiana', 'cristiana@gmail.com', '0739393939'),
('Ale', 'ale.shooter@hotmail.ro', '0769696969'),
('Bogdan', 'bogdi@yahoo.com', '0747474747'),
('Bianca', 'bibirusu@gmail.com', '0739584762'),
('Ilinca', 'ilinca@yahoo.com', '0712345678');



INSERT INTO Comenzi(id_client, cod_comanda, descriere, adresa)
VALUES
(1, '0001', 'Mare', 'Strada Libertății 25'),
(1, '0002', 'Faina', 'Bulevardul Unirii 47'),
(1, '0003', 'Misto', 'Strada Ștefan Cel Mare 12'),
(2, '0004', 'Smekera', 'Calea Victoriei 90'),
(3, '0005', 'Top', 'Strada Moșilor 3'),
(4, '0006', 'Frumoasa', 'Bulevardul Nicolae Titulescu 34'),
(5, '0007', 'Mica', 'Strada Traian 18'),
(5, '0008', 'Gigantica', 'Calea Floreasca 56');



INSERT INTO Produse_Comenzi(id_produs, id_comanda, status, cantitate)
VALUES
(1, 1, 'Livrata', 2),
(1, 2, 'Plasata', 1),
(2, 3, 'Livrata', 3),
(3, 4, 'Plasata', 4),
(4, 5, 'Plasata', 1),
(4, 6, 'Livrata', 4),
(5, 7, 'Plasata', 2),
(6, 8, 'Plasata', 1);



INSERT INTO Magazine(nume, adresa)
VALUES
('Apple Spectrum', '789 Digital Way'),
('iTech World', '987 Technology Plaza'),
('iStore Central', '234 Apple Square'),
('Apple Hub', '321 Future St'),
('Apple Spectrum', '564 Mac Lane');



INSERT INTO Departamente(nume, nr_angajati)
VALUES
('IT', 1000),
('HR', 200),
('Engineering', 700),
('Design', 500);


INSERT INTO Angajati(id_magazin, id_departament, nume, functie, email, nr_telefon)
VALUES
(1, 1, 'Bibi', 'CEO', 'bibi@gmail.com', '0700000000'),
(2, 2, 'Bogdan', 'Manager', 'bogdan@gmail.com', '0751236479'),
(3, 3, 'Ale', 'Curatenie', 'shooterale@yahoo.com', '0716752983'),
(4, 4, 'Gugu', 'IT', 'gucci@yahoo.com', '0741528976'),
(5, 1, 'Vasile', 'Design', 'vasilica@hotmail.com', '0711224455'),
(1, 2, 'HotMan', 'Curatenie', 'hotmain@gmail.com', '0735748928');



INSERT INTO Produse_Magazine(id_produs, id_magazin, nr_produse_din_magazin)
VALUES
(1, 1, 10000),
(2, 2, 3000),
(3, 3, 2000),
(4, 4, 4000),
(5, 5, 500),
(6, 1, 7000);

INSERT INTO Produse_Magazine(id_produs, id_magazin, nr_produse_din_magazin)
VALUES
(19, 1, 10000),
(18, 2, 3000),
(15, 3, 2000),
(13, 4, 4000),
(12, 5, 500),
(17, 1, 7000);



--interogari WHERE

SELECT * FROM Produse
WHERE tip = 'IPhone';


SELECT * FROM Produse
WHERE pret < 1000;


SELECT * FROM Produse 
WHERE pret BETWEEN 1000 and 3000;


SELECT * FROM Produse_Comenzi
WHERE status = 'Livrata' OR status = 'Plasata';


SELECT * FROM Angajati
WHERE functie IN ('CEO', 'Manager');


SELECT * FROM Angajati
WHERE email LIKE '%@gmail.com'



-- afiseaza numarul de produse de fiecare tip
SELECT tip, COUNT(*)
FROM Produse
GROUP BY tip 


-- afiseaza care e stocul total de produse pentru fiecare tip
SELECT tip, SUM(stoc)
FROM Produse 
GROUP BY tip;


-- furnizorii care ofera mai mult de 3 produse
SELECT id_furnizor, COUNT(*)
FROM Produse
GROUP BY id_furnizor
HAVING COUNT(*) > 3;


--pretul mediu pentru fiecare tip de produs
SELECT tip, AVG(pret)
FROM Produse
GROUP BY tip;




-- fiecare tip de produs
SELECT DISTINCT tip 
FROM Produse;




SELECT Produse.nume, Furnizori.nume, Produse_Comenzi.cantitate, Produse_Comenzi.status
FROM Produse
JOIN Furnizori ON Produse.id_furnizor = Furnizori.id_furnizor
JOIN Produse_Comenzi On Produse.id_produs = Produse_Comenzi.id_produs
WHERE status IN ('Plasata')


SELECT Angajati.nume, Magazine.nume, Departamente.nume
FROM Angajati 
JOIN Magazine ON Angajati.id_magazin = Magazine.id_magazin
JOIN Departamente ON Departamente.id_departament = Angajati.id_departament
WHERE Angajati.functie IN ('Curatenie')



SELECT DISTINCT Produse.nume, Produse_Comenzi.cantitate, Comenzi.cod_comanda
FROM Produse 
JOIN Produse_Comenzi ON Produse.id_produs = Produse_Comenzi.id_produs
JOIN Comenzi ON Produse_Comenzi.id_comanda = Comenzi.cod_comanda
WHERE Produse.tip = 'IPhone';



SELECT Produse.tip, Magazine.adresa, Produse_Magazine.nr_produse_din_magazin
FROM Produse  -- legatura dintre tabele si tabelul de legatura
JOIN Produse_Magazine ON Produse.id_produs = Produse_Magazine.id_produs
JOIN Magazine ON Produse_Magazine.id_magazin = Magazine.id_magazin
WHERE Produse.stoc > 40000



SELECT Produse.id_produs, Promotii.nume, Produse_Promotii.durata
FROM Produse 
JOIN Produse_Promotii ON Produse.id_produs = Produse_Promotii.id_produs
JOIN Promotii On Produse_Promotii.id_promotie = Promotii.id_promotie
WHERE Produse.id_furnizor = 2


--comenzile cu mai mult de 3 produse
SELECT id_comanda, SUM(cantitate)
FROM Produse_Comenzi
GROUP BY id_comanda
HAVING SUM(cantitate) > 3



--tipul de produse cu stoc mai mare decat 100000
SELECT tip, SUM(stoc) 
FROM Produse 
GROUP BY tip 
HAVING SUM(stoc) > 100000;


SELECT DISTINCT tip 
FROM Produse 
WHERE Produse.capacitate IN ('1TB')



SELECT nume FROM Comenzi C
INNER JOIN Produse_Comenzi PC ON C.cod_comanda=PC.id_comanda
INNER JOIN Produse P ON P.id_produs = PC.id_produs
WHERE PC.status IN ('Livrata')



SELECT Departamente.nume, COUNT(Angajati.id_angajat) AS Nr_angajati
FROM Angajati 
JOIN Departamente ON Angajati.id_departament = Departamente.id_departament
JOIN Magazine ON Angajati.id_magazin = Magazine.id_magazin
WHERE Departamente.nume IN ('IT', 'HR')
GROUP BY Departamente.nume



