package edu.pmoc.practicatrim.hangmanpsp.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.pmoc.practicatrim.hangmanpsp.model.Palabra;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

public class JsonLoader {

    public static void loadPalabras(String relativePath) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("select count(p) from Palabra p", Long.class);
            Long count = query.uniqueResult();

            if (count > 0) {
                return;
            }

            Gson gson = new Gson();
            try (Reader reader = new FileReader(relativePath)) {
                Type listType = new TypeToken<List<Palabra>>() {}.getType();
                List<Palabra> listaPalabras = gson.fromJson(reader, listType);

                if (listaPalabras != null && !listaPalabras.isEmpty()) {
                    Transaction tx = session.beginTransaction();
                    for (Palabra p : listaPalabras) {
                        session.persist(p);
                    }
                    tx.commit();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}