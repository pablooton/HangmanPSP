package edu.pmoc.practicatrim.hangmanpsp.network.client;

import edu.pmoc.practicatrim.hangmanpsp.util.ConfigLoader;

import javax.net.ssl.SSLSocketFactory;

public class ClientTCP {
    private final static String COD_TEXTO = "UTF-8";



    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.trustStore", ConfigLoader.getProperty("ssl.truststore.path"));
        System.setProperty("javax.net.ssl.trustStorePassword", ConfigLoader.getProperty("ssl.truststore.pass"));

        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        String host = ConfigLoader.getProperty("server.host");
        int port = ConfigLoader.getIntProperty("server.port");
    }
}
