CREATE DATABASE rsprojekat CHARACTER SET "utf8mb4";
USE rsprojekat;

CREATE TABLE mjesto (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE lokacija (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL,
    img_path VARCHAR(200) NOT NULL,
    id_mjesto INT NOT NULL,
    FOREIGN KEY (id_mjesto) REFERENCES mjesto(id)
);

CREATE TABLE sektor (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL,
    price_modifier INT NOT NULL,
    kapacitet INT NOT NULL,
    id_lokacija INT NOT NULL,
    FOREIGN KEY (id_lokacija) REFERENCES lokacija(id)
);

CREATE TABLE sjedalo (
	id INT NOT NULL PRIMARY KEY,
    id_sektor INT NOT NULL,
    FOREIGN KEY (id_sektor) REFERENCES sektor(id)
);

CREATE TABLE kategorija (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL
);

CREATE TABLE podkategorija (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL,
    id_kategorija INT NOT NULL,
    FOREIGN KEY (id_kategorija) REFERENCES kategorija(id)
);

CREATE TABLE korisnik (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    ime VARCHAR(20) NOT NULL,
    prezime VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    pass VARCHAR(250) NOT NULL,
    wallet DECIMAL(6, 2) NOT NULL DEFAULT 0,
    admin TINYINT NOT NULL DEFAULT 0,
    organizator TINYINT NOT NULL DEFAULT 0,
    datum_rod DATE,
    approved TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE dogadjaj (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(150) NOT NULL,
    opis VARCHAR(500) NOT NULL,
    img_path VARCHAR(200) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    base_price DECIMAL(5, 2) NOT NULL,
    available TINYINT NOT NULL DEFAULT 0,
    approved TINYINT NOT NULL DEFAULT 0,
    id_organizator INT NOT NULL,
    id_podkategorija INT NOT NULL,
    id_lokacija INT NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (id_organizator) REFERENCES korisnik(id),
    FOREIGN KEY (id_podkategorija) REFERENCES podkategorija(id),
    FOREIGN KEY (id_lokacija) REFERENCES lokacija(id)
);

CREATE TABLE karta (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    bought TINYINT NOT NULL DEFAULT 0,
    reserved TINYINT NOT NULL DEFAULT 0,
    reserved_to DATE,
    id_dogadjaj INT NOT NULL,
    id_sektor INT NOT NULL,
    id_sjedalo INT,
    id_kupac INT NOT NULL,
    FOREIGN KEY (id_dogadjaj) REFERENCES dogadjaj(id),
    FOREIGN KEY (id_sektor) REFERENCES sektor(id),
    FOREIGN KEY (id_sjedalo) REFERENCES sjedalo(id),
    FOREIGN KEY (id_kupac) REFERENCES korisnik(id)
);
