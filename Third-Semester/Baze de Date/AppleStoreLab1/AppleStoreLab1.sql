CREATE DATABASE [AppleStore]
GO
USE [AppleStore]

--1
CREATE TABLE Furnizori
(
	id_furnizor INT IDENTITY(1,1),
	CONSTRAINT pk_furnizor PRIMARY KEY(id_furnizor),

	nume VARCHAR(100) NOT NULL UNIQUE,
	tara VARCHAR(100) NOT NULL,
	nr_telefon VARCHAR(20) NOT NULL,
	email VARCHAR(100) NOT NULL
	
);


--2
CREATE TABLE Produse
(

	id_produs INT IDENTITY(1,1),
	CONSTRAINT pk_produs PRIMARY KEY(id_produs),

	id_furnizor INT,
	CONSTRAINT fk_FurnizorProduse FOREIGN KEY(id_furnizor) REFERENCES Furnizori(id_furnizor) ON UPDATE CASCADE    ON DELETE CASCADE,

	tip VARCHAR(200) NOT NULL,
	nume VARCHAR(200) NOT NULL,
	culoare VARCHAR(200) NOT NULL,
	capacitate VARCHAR(200) NOT NULL,
	pret INT,
	stoc INT,

);



--3
CREATE TABLE Promotii
(
	id_promotie INT IDENTITY(1,1),
	CONSTRAINT pk_promotie PRIMARY KEY(id_promotie),

	nume VARCHAR(200) NOT NULL,
	procent INT

);


--4
CREATE TABLE Produse_Promotii
(
	id_produs INT,
	CONSTRAINT fk_Produs FOREIGN KEY(id_produs) REFERENCES Produse(id_produs) ON UPDATE CASCADE    ON DELETE CASCADE,

	id_promotie INT,
	CONSTRAINT fk_Promotie FOREIGN KEY(id_promotie) REFERENCES Promotii(id_promotie) ON UPDATE CASCADE    ON DELETE CASCADE,

	CONSTRAINT pk_produse_promotii PRIMARY KEY(id_produs, id_promotie),

	durata VARCHAR(100) NOT NULL

);


--5
CREATE TABLE Clienti
(
	id_client INT IDENTITY(1,1),
	CONSTRAINT pk_client PRIMARY KEY(id_client),

	nume VARCHAR(200) NOT NULL UNIQUE,
	email VARCHAR(200) NOT NULL UNIQUE,
	nr_telefon VARCHAR(200) NOT NULL UNIQUE

);


--6
CREATE TABLE Comenzi
(
	id_comanda INT IDENTITY(1,1),
	CONSTRAINT pk_comanda PRIMARY KEY(id_comanda),

	cod_comanda VARCHAR(200) NOT NULL UNIQUE,
	descriere VARCHAR(200) NOT NULL,
	adresa VARCHAR(200) NOT NULL,

	id_client INT,
	CONSTRAINT fk_client FOREIGN KEY(id_client) REFERENCES Clienti(id_client) ON UPDATE CASCADE    ON DELETE CASCADE

);



--7
CREATE TABLE Produse_Comenzi
(
	id_produs INT,
	CONSTRAINT fk_Produs_comanda FOREIGN KEY(id_produs) REFERENCES Produse(id_produs) ON UPDATE CASCADE    ON DELETE CASCADE,

	id_comanda INT,
	CONSTRAINT fk_Comanda_produs FOREIGN KEY(id_comanda) REFERENCES Comenzi(id_comanda) ON UPDATE CASCADE    ON DELETE CASCADE,

	CONSTRAINT pk_produse_comenzi PRIMARY KEY(id_produs, id_comanda),

	status VARCHAR(200),

	cantitate INT NOT NULL,

	CONSTRAINT ck_status CHECK (status IN ('Livrata', 'In curs de Livrare', 'Plasata'))

);


--8
CREATE TABLE Magazine 
(
	id_magazin INT IDENTITY(1,1),
	CONSTRAINT pk_magazin PRIMARY KEY(id_magazin),

	nume VARCHAR(200) NOT NULL,
	adresa VARCHAR(200) NOT NULL
);




--9
CREATE TABLE Departamente
(
	id_departament INT IDENTITY(1,1),
	CONSTRAINT pk_departament PRIMARY KEY(id_departament),

	nume VARCHAR(200) NOT NULL UNIQUE,
	nr_angajati INT NOT NULL
);




--10
CREATE TABLE Angajati
(
	id_angajat INT IDENTITY(1,1),
	CONSTRAINT pk_angajat PRIMARY KEY(id_angajat), 

	nume VARCHAR(200) NOT NULL UNIQUE,
	functie VARCHAR(200) NOT NULL,
	email VARCHAR(200) NOT NULL UNIQUE,
	nr_telefon VARCHAR(200) NOT NULL UNIQUE,

	CONSTRAINT chk_nr_telefon CHECK (LEN(nr_telefon) = 10),

	id_magazin INT,
	CONSTRAINT fk_magazin FOREIGN KEY(id_magazin) REFERENCES Magazine(id_magazin) ON UPDATE CASCADE    ON DELETE CASCADE,

	id_departament INT,
	CONSTRAINT fk_departament FOREIGN KEY(id_departament) REFERENCES Departamente(id_departament) ON UPDATE CASCADE    ON DELETE CASCADE

);




--11
CREATE TABLE Produse_Magazine
(
	id_produs INT,
	CONSTRAINT fk_produse_magazine FOREIGN KEY(id_produs) REFERENCES Produse(id_produs) ON UPDATE CASCADE    ON DELETE CASCADE,

	id_magazin INT,
	CONSTRAINT fk_magazine_produse FOREIGN KEY(id_magazin) REFERENCES Magazine(id_magazin) ON UPDATE CASCADE    ON DELETE CASCADE,

	CONSTRAINT pk_produse_magazine PRIMARY KEY(id_produs, id_magazin),

	nr_produse_din_magazin INT NOT NULL
);


