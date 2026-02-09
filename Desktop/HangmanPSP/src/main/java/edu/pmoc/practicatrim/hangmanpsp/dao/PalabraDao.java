package edu.pmoc.practicatrim.hangmanpsp.dao;

import com.google.gson.Gson;
import edu.pmoc.practicatrim.hangmanpsp.model.Palabra;
import edu.pmoc.practicatrim.hangmanpsp.util.CryptoUtil;
import edu.pmoc.practicatrim.hangmanpsp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class PalabraDao {

    public static void importarDesdeJson() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long cuenta = session.createQuery("select count(p) from Palabra p", Long.class).uniqueResult();

            if (cuenta == 0) {
                Transaction tx = session.beginTransaction();
                Gson gson = new Gson();

                try (InputStream is = PalabraDao.class.getClassLoader().getResourceAsStream("palabras-ahorcado.json")) {
                    if (is == null) throw new IOException("Archivo no encontrado");
                    Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                    Palabra[] palabrasArray = gson.fromJson(reader, Palabra[].class);

                    for (Palabra p : palabrasArray) {
                        p.setPalabra(CryptoUtil.encrypt(p.getPalabra()));
                        session.persist(p);
                    }
                    tx.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPalabraSecreta() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT p.palabra FROM Palabra p ORDER BY RAND()";
            Query<String> query = session.createQuery(hql, String.class);
            query.setMaxResults(1);

            String resultadoCifrado = query.uniqueResult();

            if (resultadoCifrado != null) {
                return CryptoUtil.decrypt(resultadoCifrado);
            }

            importarDesdeJson();
            return "NATURALEZA";

        } catch (Exception e) {
            e.printStackTrace();
            return "AHORCADO";
        }
    }
}