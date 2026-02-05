package edu.pmoc.practicatrim.hangmanpsp.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {

            System.err.println("Error en la creación de la SessionFactory inicial." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public void shutdown(){
        if(sessionFactory!=null){
            getSessionFactory().close();
        }
    }
}
