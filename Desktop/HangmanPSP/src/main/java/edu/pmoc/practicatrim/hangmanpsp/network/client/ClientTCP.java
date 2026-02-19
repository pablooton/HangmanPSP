package edu.pmoc.practicatrim.hangmanpsp.network.client;

import edu.pmoc.practicatrim.hangmanpsp.util.ConfigLoader;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.SocketException;

public class ClientTCP {
    private SSLSocket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void conectar() {
        String trustStorePath = ConfigLoader.getProperty("ssl.truststore.path");
        String trustStorePass = ConfigLoader.getProperty("ssl.truststore.pass");

        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePass);

        try {
            String host = ConfigLoader.getProperty("server.host");
            int port = ConfigLoader.getIntProperty("server.port");

            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) sslSocketFactory.createSocket(host, port);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }

    public void enviarDatos(Object datos) {
        try {
            if (out != null && socket != null && !socket.isClosed()) {
                out.writeObject(datos);
                out.flush();
                out.reset();
            }
        } catch (IOException e) {
            System.out.println("[INFO] No se pudo enviar datos: Conexión cerrada.");
        }
    }

    public Object recibirDatos() {
        try {
            if (in != null) {
                return in.readObject();
            }
        } catch (EOFException | SocketException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en recepción de datos: " + e.getMessage());
        }
        return null;
    }

    public void desconectar() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
}