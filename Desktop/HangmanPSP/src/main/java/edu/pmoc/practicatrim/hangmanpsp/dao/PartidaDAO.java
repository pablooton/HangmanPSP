package edu.pmoc.practicatrim.hangmanpsp.dao;

import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.model.Partida;
import edu.pmoc.practicatrim.hangmanpsp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query; // Import necesario si usas consultas

public class PartidaDAO {
    public void guardarPartida(Partida partida) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(partida);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Long obtenerPuntuacionTotal(int jugadorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT SUM(p.puntuacionObtenida) FROM Partida p WHERE p.jugador.id = :jid";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("jid", jugadorId);
            Long total = query.uniqueResult();
            return (total != null) ? total : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}