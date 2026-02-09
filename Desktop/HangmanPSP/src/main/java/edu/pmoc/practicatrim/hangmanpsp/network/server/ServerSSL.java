package edu.pmoc.practicatrim.hangmanpsp.network.server;

import edu.pmoc.practicatrim.hangmanpsp.dao.PalabraDao;
import edu.pmoc.practicatrim.hangmanpsp.util.ConfigLoader;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class ServerSSL {

    private SSLContext crearContextoSSL() throws Exception {
        String path = ConfigLoader.getProperty("ssl.keystore.path");
        String pass = ConfigLoader.getProperty("ssl.keystore.pass");

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(path), pass.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, pass.toCharArray());

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);
        return sc;
    }

    public void iniciar() {
        int puerto = ConfigLoader.getIntProperty("server.port");

        try {
            SSLContext context = crearContextoSSL();
            SSLServerSocketFactory ssf = context.getServerSocketFactory();

            try (SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(puerto)) {
                System.out.println("[SERVIDOR] Listo.");

                while (true) {
                    List<SSLSocket> clientesEnPartida = new ArrayList<>();
                    SSLSocket s1 = (SSLSocket) serverSocket.accept();
                    clientesEnPartida.add(s1);

                    serverSocket.setSoTimeout(10000);
                    try {
                        SSLSocket s2 = (SSLSocket) serverSocket.accept();
                        clientesEnPartida.add(s2);
                    } catch (IOException e) {
                        System.out.println("[SERVIDOR] Individual.");
                    }
                    serverSocket.setSoTimeout(0);

                    String palabra = PalabraDao.getPalabraSecreta();
                    if (palabra == null) palabra = "AHORCADO";

                    LogicaPartida logica = new LogicaPartida(palabra, clientesEnPartida.size());

                    for (int i = 0; i < clientesEnPartida.size(); i++) {
                        new Thread(new HiloCliente(clientesEnPartida.get(i), i, logica)).start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServerSSL().iniciar();
    }
}