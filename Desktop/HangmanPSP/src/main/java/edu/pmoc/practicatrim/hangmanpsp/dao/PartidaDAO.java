package edu.pmoc.practicatrim.hangmanpsp.dao;

import edu.pmoc.practicatrim.hangmanpsp.model.Partida;
import edu.pmoc.practicatrim.hangmanpsp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PartidaDAO {
    public void guardarPartida(Partida partida) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(partida);
            tx.commit();
            System.out.println("Partida guardada en el historial");
        } catch (Exception e) {
            System.err.println("Error al guardar la partida: " + e.getMessage());
        }
    }
}
