package edu.pmoc.practicatrim.hangmanpsp.dao;

import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.util.List;

public class UsuarioDao {
    public Jugador cargarrUser(String username){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "Select j from jugador j where j.nombre = :nombre";
            Query<Jugador> query = session.createQuery(hql, Jugador.class);
            query.setParameter("nombre",username);
            Jugador jugador = query.uniqueResult();

            if(jugador == null){
                Transaction tx = session.beginTransaction();
                jugador = new Jugador();
                jugador.setNombre(username);
                session.persist(jugador);
                tx.commit();
            }
            return jugador;

        }
    }
}
