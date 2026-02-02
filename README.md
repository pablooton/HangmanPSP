# HangmanPSP
Markdown

# Hangman Game - Secure Multi-threaded Client/Server

A professional multiplayer Hangman game developed for the **Programming Services and Processes** module (2nd year DAM). This project demonstrates advanced Java concepts: multi-threading, secure socket communication (SSL/TLS), and data persistence using Hibernate with AES encryption.

## 🚀 Key Features

* **Multi-threaded Server:** Handles multiple concurrent clients and game sessions.
* **Secure Transport Layer (TLS):** All network communication is encrypted using hybrid cryptography (`SSLSocket`).
* **Data Persistence (Hibernate):** Automatic database management for players and scores.
* **Application Layer Security (AES):** Sensitive database fields (like player names) are encrypted using the AES algorithm.
* **JSON Integration:** Word bank imported dynamically from JSON files.
* **Modern GUI:** User interface developed with JavaFX.

## 🛠️ Tech Stack

* **Language:** Java 21
* **ORM:** Hibernate 6.x
* **UI:** JavaFX 25
* **Security:** JCE (Java Cryptography Extension) for AES & JSSE for SSL/TLS.
* **Build Tool:** Maven

## 📋 Configuration & Setup

### 1. SSL/TLS Certificates
To ensure secure communication, you must generate a Java KeyStore (`.jks`). Run the following command in your terminal:

bash
keytool -genkey -alias serverKey -keyalg RSA -keystore serverKeys.jks -validity 365
2. Environment Configuration
The application uses a config.properties file located in the resources folder. Ensure the following properties are set:

Properties

ssl.truststore.path=path/to/serverKeys.jks
ssl.truststore.pass=your_password
server.host=127.0.0.1
server.port=5555
3. Database
Update hibernate.cfg.xml with your database credentials (URL, username, and password).

🏗️ Project Structure
network.server: Server implementation and ClientHandler threads.

network.client: ClientTCP service managing the SSL connection.

dao: Data Access Objects using Hibernate sessions.

model: Entity classes (Jugador, Palabra) with Hibernate annotations.

controller: JavaFX controllers for the Login and Game views.

util: Logic for AES encryption (CryptoUtil) and Config loading.

🔐 Security Implementation Details
Transport Layer (SSL/TLS)
The connection is established using SSLSocketFactory. This ensures that data packets cannot be intercepted (Sniffing) during transmission between the client and the server.

Application Layer (AES)
We use AES (Advanced Encryption Standard) to encrypt data before it hits the database.

Algorithm: AES/ECB/PKCS5Padding

Key Size: 128-bit (16 characters)

📝 Authors
Student: PABLO MANUEL OTÓN CARIDAD
Student: MANUEL GARCÍA REY
Student: ANTÓN FARALDO MOSQUERA

Institution: Liceo La Paz

Subject: Programming Services and Processes

Year: 2026


---
