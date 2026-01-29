SQL

CREATE DATABASE IF NOT EXISTS hangman_db;
USE hangman_db;


CREATE TABLE IF NOT EXISTS palabras (
    id INT PRIMARY KEY,
    palabra VARCHAR(15) NOT NULL
);


CREATE TABLE IF NOT EXISTS jugadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS partidas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    jugador_id INT,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    acertado BOOLEAN,
    puntuacion_obtenida INT,
    FOREIGN KEY (jugador_id) REFERENCES jugadores(id)
);