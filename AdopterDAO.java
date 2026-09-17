import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class AdopterDAO {

    private EntityManagerFactory factory;
    private EntityManager entityManager;

    public AdopterDAO(String databasename) throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + databasename);
        this.factory = Persistence.createEntityManagerFactory("ARCService", properties);
        this.entityManager = factory.createEntityManager();     
    }

    public void store(Adopter adopter) {
        entityManager.getTransaction().begin();
        entityManager.persist(adopter);
        entityManager.getTransaction().commit();
    }

    public void update(Adopter adopter) throws Exception {
        entityManager.getTransaction().begin();
        entityManager.merge(adopter);
        entityManager.getTransaction().commit();       
    }

    public Adopter get(int id) throws Exception {
        Adopter adopter = entityManager.find(Adopter.class, id);
        return adopter;
    }

    public List<Adopter> getAll() throws Exception {
        TypedQuery<Adopter> query = entityManager.createQuery("SELECT a FROM adopters a ORDER BY a.id", 
            Adopter.class);
        return query.getResultList();
    }

    public List<Animal> findPreferredAnimals(Adopter adopter) {
        int adopterId = adopter.getId();    

        //Preferemce animal is a subset of available animals
        //Hence, use available animals as base
        List<String> availableAnimalIds = findAvailableAnimalIds(adopter);
        TypedQuery<Animal> adoptableAnimalQuery = entityManager.createQuery("SELECT ani FROM animals ani" 
            + " JOIN adopters ad1 ON ad1.id = :adopterId" 
            + " WHERE ani.id IN :availableAnimalIds"
            + " AND ani.species IN"
                + " (SELECT prefs FROM ad1.preferences prefs)"
            + " ORDER BY ani.id", 
            Animal.class);

        List<Animal> animals = (List<Animal>) adoptableAnimalQuery
            .setParameter("adopterId", adopterId)
            .setParameter("availableAnimalIds", availableAnimalIds)
            .getResultList();
        return animals;
    }

    public List<String> findAvailableAnimalIds(Adopter adopter) {
        int adopterId = adopter.getId();    
        TypedQuery<String> adoptableAnimalQuery = entityManager.createQuery("SELECT ani.id FROM animals ani"
            + " JOIN adopters ad1 ON ad1.id = :adopterId"
            //Animal must not have an active adoption
            + " WHERE ani.id NOT IN"
                + " (SELECT ani2.id FROM animals ani2" 
                + " JOIN ani2.adoptions ad1" 
                + " WHERE ad1.end IS NULL)"
            //The animal can have no existing adoption record (active or otherwise) with the adopter
            + " AND ani.id NOT IN"
                + " (SELECT ad2.animal_id FROM adopters ad1" 
                + " JOIN ad1.adoptions ad2" 
                + " WHERE ad2.adopter_id = :adopterId)"
            + " ORDER BY ani.id",
            String.class);

        List<String> animalIds = adoptableAnimalQuery
            .setParameter("adopterId", adopterId)
            .getResultList();
        return animalIds;
    }

    public List<Integer> getExistingIds() {
        TypedQuery<Integer> adopterQuery = entityManager.createQuery("SELECT a.id FROM adopters a", 
            Integer.class);
        List<Integer> existingIds = adopterQuery.getResultList();
        return existingIds;
    }

    public void addAdoption(Adopter adopter, Adoption adoption) {
        adopter.addAdoption(adoption);
    }

    public void unadoptAnimal(Adopter adopter, Adoption adoption) {
        adopter.removeAdoption(adoption);
    }
}
