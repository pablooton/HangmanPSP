package edu.pmoc.practicatrim.hangmanpsp.network.server;

import edu.pmoc.practicatrim.hangmanpsp.dao.PalabraDao;
import edu.pmoc.practicatrim.hangmanpsp.util.ConfigLoader;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class ServerSSL {
    public void iniciar() {
        try {
            PalabraDao.importarDesdeJson();
            SSLContext sc = crearContextoSSL();
            SSLServerSocket serverSocket = (SSLServerSocket) sc.getServerSocketFactory().createServerSocket(ConfigLoader.getIntProperty("server.port"));
            System.out.println("[SERVIDOR] Listo.");

            while (true) {
                List<SSLSocket> clientes = new ArrayList<>();
                SSLSocket s1 = (SSLSocket) serverSocket.accept();
                clientes.add(s1);
                serverSocket.setSoTimeout(10000);
                try { clientes.add((SSLSocket) serverSocket.accept()); } catch (Exception e) {}
                serverSocket.setSoTimeout(0);

                LogicaPartida logica = new LogicaPartida(PalabraDao.getPalabraSecreta(), clientes.size());
                for (int i = 0; i < clientes.size(); i++) {
                    new Thread(new HiloCliente(clientes.get(i), i, logica)).start();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private SSLContext crearContextoSSL() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(ConfigLoader.getProperty("ssl.keystore.path")), ConfigLoader.getProperty("ssl.keystore.pass").toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, ConfigLoader.getProperty("ssl.keystore.pass").toCharArray());
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);
        return sc;
    }

    public static void main(String[] args) { new ServerSSL().iniciar(); }
}