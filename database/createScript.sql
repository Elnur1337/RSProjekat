CREATE DATABASE rsprojekat CHARACTER SET "utf8mb4";
USE rsprojekat;

CREATE TABLE mjesto (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL
);

CREATE TABLE lokacija (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL,
    id_mjesto INT NOT NULL,
    img_path VARCHAR(200) NOT NULL,
    FOREIGN KEY (id_mjesto) REFERENCES mjesto(id)
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
    email VARCHAR(24) NOT NULL UNIQUE,
    wallet DECIMAL(6, 2) NOT NULL DEFAULT 0,
    admin TINYINT(1) NOT NULL DEFAULT 0,
    organizator TINYINT(1) NOT NULL DEFAULT 0,
    datum_rod DATE,
    approved TINYINT(1) NOT NULL DEFAULT 0,
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
    available TINYINT(1) NOT NULL DEFAULT 0,
    approved TINYINT(1) NOT NULL DEFAULT 0,
    id_organizator INT NOT NULL,
    id_podkategorija INT NOT NULL,
    id_lokacija INT NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (id_organizator) REFERENCES korisnik(id),
    FOREIGN KEY (id_podkategorija) REFERENCES podkategorija(id),
    FOREIGN KEY (id_lokacija) REFERENCES lokacija(id)
);

