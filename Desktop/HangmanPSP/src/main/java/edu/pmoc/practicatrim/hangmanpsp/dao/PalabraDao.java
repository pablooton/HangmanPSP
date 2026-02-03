package edu.pmoc.practicatrim.hangmanpsp.dao;

import com.google.gson.Gson;
import edu.pmoc.practicatrim.hangmanpsp.model.Palabra;
import edu.pmoc.practicatrim.hangmanpsp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Reader;
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

                try (Reader reader = Files.newBufferedReader(Paths.get("palabras-ahorcado.json"))) {


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
}
