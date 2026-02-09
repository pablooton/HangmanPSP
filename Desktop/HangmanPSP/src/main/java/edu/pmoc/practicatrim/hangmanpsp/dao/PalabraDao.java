package edu.pmoc.practicatrim.hangmanpsp.dao;

import com.google.gson.Gson;
import edu.pmoc.practicatrim.hangmanpsp.model.Palabra;
import edu.pmoc.practicatrim.hangmanpsp.util.CryptoUtil;
import edu.pmoc.practicatrim.hangmanpsp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class PalabraDao {
    public static void importarDesdeJson() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long cuenta = session.createQuery("select count(p) from Palabra p", Long.class).uniqueResult();
            if (cuenta == 0) {
                session.beginTransaction();
                InputStream is = PalabraDao.class.getClassLoader().getResourceAsStream("palabras-ahorcado.json");
                Palabra[] palabras = new Gson().fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), Palabra[].class);
                for (Palabra p : palabras) {
                    p.setPalabra(CryptoUtil.encrypt(p.getPalabra()));
                    session.persist(p);
                }
                session.getTransaction().commit();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static String getPalabraSecreta() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT p.palabra FROM Palabra p ORDER BY RAND()";
            Query<String> q = session.createQuery(hql, String.class).setMaxResults(1);
            String res = q.uniqueResult();
            return (res != null) ? CryptoUtil.decrypt(res) : "NATURALEZA";
        } catch (Exception e) { return "AHORCADO"; }
    }
}