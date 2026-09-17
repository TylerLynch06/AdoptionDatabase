import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AdoptionDAO {

    private EntityManagerFactory factory;
    private EntityManager entityManager;

    public AdoptionDAO(String databasename) throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + databasename);
        this.factory = Persistence.createEntityManagerFactory("ARCService", properties);
        this.entityManager = factory.createEntityManager();        
    }

    public void update(Adoption adoption) throws Exception {
        entityManager.getTransaction().begin();
        entityManager.merge(adoption);
        entityManager.getTransaction().commit();       
    }

    public Adoption get(int adopter_id, String animal_id) throws Exception {
        AdoptionId id = new AdoptionId(adopter_id, animal_id);
        Adoption adoption = entityManager.find(Adoption.class, id);
        return adoption;
    }
}
