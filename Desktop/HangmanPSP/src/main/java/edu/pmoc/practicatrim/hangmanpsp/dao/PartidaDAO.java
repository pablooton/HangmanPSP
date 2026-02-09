package edu.pmoc.practicatrim.hangmanpsp.dao;

import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.model.Partida;
import edu.pmoc.practicatrim.hangmanpsp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query; // Import necesario si usas consultas

public class PartidaDAO {

    public void guardarPartida(Partida partida) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            if (partida.getJugador() != null) {
                Jugador jugadorManaged = session.find(Jugador.class, partida.getJugador().getId());
                partida.setJugador(jugadorManaged);
            }

            session.persist(partida);
            tx.commit();
            System.out.println("Partida guardada correctamente con Jugador ID: " +
                    (partida.getJugador() != null ? partida.getJugador().getId() : "NULL"));

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al guardar la partida: " + e.getMessage());
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