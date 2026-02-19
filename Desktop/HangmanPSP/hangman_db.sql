CREATE DATABASE  IF NOT EXISTS hangman_db /!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /!80016 DEFAULT ENCRYPTION='N' */;
USE hangman_db;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: hangman_db
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table jugadores
--

DROP TABLE IF EXISTS jugadores;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE jugadores (
  id int NOT NULL AUTO_INCREMENT,
  nombre varchar(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UKpfkibd3gr9w7p5bbim4ho80qi (nombre)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table jugadores
--

LOCK TABLES jugadores WRITE;
/*!40000 ALTER TABLE jugadores DISABLE KEYS */;
INSERT INTO jugadores VALUES (2,'3EQp5cNtbSHN60g0GHbx6w=='),(1,'4mXjgBIfkhz5O//QlT/pbA==');
/*!40000 ALTER TABLE jugadores ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table palabras
--

DROP TABLE IF EXISTS palabras;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE palabras (
  id int NOT NULL,
  palabra text,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table palabras
--

LOCK TABLES palabras WRITE;
/*!40000 ALTER TABLE palabras DISABLE KEYS */;
INSERT INTO palabras VALUES (1,'gnLvfrEq5VTzzsEQZBz6tQ=='),(2,'n/p5j+rr1UuBS37UhcLmRg=='),(3,'AnBLOFvLLgMx7ZnFlIObcQ=='),(4,'J96YDKpWlX0hGBrj4bo8NQ=='),(5,'Aez8jLjiRNMsEzGpaHeY8Q=='),(6,'2YF/uCanOvxOmcZJau71kQ=='),(7,'1lsmn8NS9LpDjHCZDrYE1Q=='),(8,'JDwuHXbpQZab/LS3fyymOw=='),(9,'vQOGWwyo/h/dN8jqWMNQlg=='),(10,'uBfTJPqXqX+5nIW6K4K0bw=='),(11,'f0eDSGAZEOzq1nQknky8Dw=='),(12,'zZlINwZVZTz7N4dp3NC3nw=='),(13,'kdNbKVT8DdfTPoWaBBWgnA=='),(14,'/43a31WDmekhms4hZmMHgQ=='),(15,'dwC/aMyRljixdJhm7Quf0A=='),(16,'HfAY7IxKKfNi2rwluMa17A=='),(17,'B4mxPawbxZB0KHX58A5KSQ=='),(18,'e2CowbTthICJs5/59mtn+g=='),(19,'6AmhKqWvHm3QV9CixEu35Q=='),(20,'5zBc1e63hHSzSKGhjgtVdw=='),(21,'gm7zYKYUFqLveSORGBXgTw=='),(22,'HA99kcdYXedLbCJeAoHBYQ=='),(23,'d4Iw0qCx2F50B7MOIQ0Nbw=='),(24,'LHA2ZZk/HpDXv9ImtXpHEQ=='),(25,'wL7EgdBjabMWlKfG/e/8FQ=='),(26,'fpJaOD/VktyNStkxw1Yn+A=='),(27,'pq60MOFIt1N4II7omp+4FQ=='),(28,'k4D97SaXDNPb9N1LFVSXGQ=='),(29,'P5Ddp24ltaTXKIsjj3fzyw=='),(30,'gn5n6tzOpb2DeoTgPWeRkQ=='),(31,'u8PiOPylal3ww9NccglosA=='),(32,'7reb6DqFTY/WY+r8WXA6Eg=='),(33,'5yi1wx1/WwQ00ZcOaz02gA=='),(34,'cAVXZXNJzKMnjBafR8tofw=='),(35,'TTKhbKWNJzC2sKOC+UOZhg=='),(36,'eRMOB/syklzXxri5+hwehA=='),(37,'ZZfXfF5I0WYXE6JHeK6mmQ=='),(38,'bslQtE+cpVBfVahg5fIWjg=='),(39,'Yu5K6Pe9zPSdSnKVvZXgrQ=='),(40,'Xk4pXzX0V0XD5VW6uRkKnw=='),(41,'hga5qvTjG1LkLNBzO4qm2Q=='),(42,'SwhwkYWUgVmMpu1EHEiFDw=='),(43,'BuMXQKC/VOR09nofXPWOCA=='),(44,'3LM9MLynHAdIFDJbG77ytg=='),(45,'ltA0ZaDbAOMwLVvs3mU0Eg=='),(46,'Vbky1NKPn2pYcpTGHUqA8w=='),(47,'PtoMrQRtci2UZa6hqIh4zA=='),(48,'Du0IpHj3HM2U7hGeZVBmCg=='),(49,'LETj96msyy0YOURuKMoKgg=='),(50,'bQ9A8SFzA+bRIoiIbzw7RA==');
/*!40000 ALTER TABLE palabras ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table partidas
--

DROP TABLE IF EXISTS partidas;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE partidas (
  acertado bit(1) DEFAULT NULL,
  id int NOT NULL AUTO_INCREMENT,
  jugador_id int DEFAULT NULL,
  puntuacion_obtenida int DEFAULT NULL,
  fecha_partida datetime(6) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKoo07b7j39sbw9k2lxectuf690 (jugador_id),
  CONSTRAINT FKoo07b7j39sbw9k2lxectuf690 FOREIGN KEY (jugador_id) REFERENCES jugadores (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table partidas
--

LOCK TABLES partidas WRITE;
/*!40000 ALTER TABLE partidas DISABLE KEYS */;
/*!40000 ALTER TABLE partidas ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-12 12:34:53