CREATE DATABASE IF not exists hangman_db;
use hangman_db;


CREATE TABLE IF NOT EXISTS usuarios(
jugador VARCHAR(50) PRIMARY KEY,
puntuacion int);

INSERT INTO usuarios (jugador) VALUES
	("Jugador1"),
    ("Jugador2");
    
    


