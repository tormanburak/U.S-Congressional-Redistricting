package webapp.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {

    private static EntityManagerFactory emf;
    private static EntityManager em = null;

    public static EntityManager getEntityManager() {
        if (em == null) {
            emf = Persistence.createEntityManagerFactory("persistence");
            em = emf.createEntityManager();
        }
        return em;
    }
}
