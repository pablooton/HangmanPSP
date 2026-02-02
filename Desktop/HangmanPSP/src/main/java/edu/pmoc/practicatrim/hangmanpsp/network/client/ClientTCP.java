package edu.pmoc.practicatrim.hangmanpsp.network.client;

import edu.pmoc.practicatrim.hangmanpsp.util.ConfigLoader;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientTCP {
    private SSLSocket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void conectar() {
        System.setProperty("javax.net.ssl.trustStore", ConfigLoader.getProperty("ssl.truststore.path"));
        System.setProperty("javax.net.ssl.trustStorePassword", ConfigLoader.getProperty("ssl.truststore.pass"));

        try {
            String host = ConfigLoader.getProperty("server.host");
            int port = ConfigLoader.getIntProperty("server.port");

            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) sslSocketFactory.createSocket(host, port);


            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Conexión SSL establecida con el servidor.");

        } catch (IOException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }

    public void enviarDatos(Object datos) {
        try {
            if (out != null) {
                out.writeObject(datos);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object recibirDatos() {
        try {
            if (in != null) {
                return in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void desconectar() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}