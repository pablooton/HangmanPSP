package edu.pmoc.practicatrim.hangmanpsp.dao;

import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.util.CryptoUtil;
import edu.pmoc.practicatrim.hangmanpsp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UsuarioDao {
    public Jugador cargarUser(String username){
        String nombreCifrado = CryptoUtil.encrypt(username);
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "Select j from Jugador j where j.nombre = :nombre";
            Query<Jugador> query = session.createQuery(hql, Jugador.class);
            query.setParameter("nombre",nombreCifrado);
            Jugador jugador = query.uniqueResult();

            if(jugador == null){
                Transaction tx = session.beginTransaction();
                jugador = new Jugador();
                jugador.setNombre(nombreCifrado);
                session.persist(jugador);
                tx.commit();
            }
            return jugador;

        }
    }
}
