package edu.pmoc.practicatrim.hangmanpsp.dao;

import com.google.gson.Gson;
import edu.pmoc.practicatrim.hangmanpsp.model.Palabra;
import edu.pmoc.practicatrim.hangmanpsp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PalabraDao {

    public static void importarDesdeJson() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Long cuenta = session.createQuery("select count(p) from Palabra p", Long.class).uniqueResult();

            if (cuenta == 0) {
                System.out.println("Base de datos de palabras vacía. Iniciando importación...");
                Transaction tx = session.beginTransaction();

                Gson gson = new Gson();

                try (InputStream is = PalabraDao.class.getClassLoader().getResourceAsStream("palabras-ahorcado.json")) {
                    if (is == null) throw new IOException("Archivo no encontrado en resources");
                    Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);

                    Palabra[] palabrasArray = gson.fromJson(reader, Palabra[].class);

                    for (Palabra p : palabrasArray) {
                        session.persist(p);
                    }

                    tx.commit();
                    System.out.println("Se han importado " + palabrasArray.length + " palabras con éxito.");
                }
            } else {
                System.out.println("La tabla palabras ya contiene datos");
            }
        } catch (Exception e) {
            System.err.println("Error al importar palabras: " + e.getMessage());
        }
    }
    public static String getPalabraSecreta() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT p.palabra FROM Palabra p ORDER BY RAND()";
            Query<String> query = session.createQuery(hql, String.class);
            query.setMaxResults(1);

            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("Error al obtener la palabra secreta: " + e.getMessage());
            return "HIBERNATE"; // Chicos hay que poner una palabra por defecto en caso de error
        }
    }

}
