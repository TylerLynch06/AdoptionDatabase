import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class AnimalDAO {

    private EntityManagerFactory factory;
    private EntityManager entityManager;

    public AnimalDAO(String databasename) throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + databasename);
        this.factory = Persistence.createEntityManagerFactory("ARCService", properties);
        this.entityManager = factory.createEntityManager();        
    }

    public void store(Animal animal) throws Exception {
        entityManager.getTransaction().begin();
        entityManager.persist(animal);
        entityManager.getTransaction().commit();
    }

    public void update(Animal animal) throws Exception {
        entityManager.getTransaction().begin();
        entityManager.merge(animal);
        entityManager.getTransaction().commit();
    }

    public void remove(Animal animal) throws Exception {
        entityManager.getTransaction().begin();
        entityManager.remove(animal);
        entityManager.getTransaction().commit();
    }

    public Animal get(String id) throws Exception {
        Animal animal = entityManager.find(Animal.class, id);
        return animal;
    }

    public int getCurrentAdopterId(Animal animal) {
        return getCurrentAdopter(animal).getId();
    }

    public Adopter getCurrentAdopter(Animal animal) {
        TypedQuery<Adopter> adopterIdQuery = entityManager.createQuery("SELECT a1 FROM adopters a1"
            + " JOIN a1.adoptions a2"
            + " WHERE a2.end IS null"
            + " AND a2.animal_id = ?1", 
            Adopter.class);
        Adopter adopter = adopterIdQuery.setParameter(1, animal.getId())
            .getResultList()
            .getFirst();
        return adopter;
    }

    public Adoption addAdoption(Animal animal, Adoption adoption) {
        animal.addAdoption(adoption);
        return adoption;
    }

    public List<Animal> getAll() throws Exception {
        TypedQuery<Animal> query = entityManager.createQuery("SELECT a FROM animals a"
            + " ORDER BY a.id", Animal.class);
        return query.getResultList();
    }

    public List<String> getAdoptedAnimalIds() throws Exception {
        TypedQuery<String> query = entityManager.createQuery("SELECT a.id FROM animals a"
            + " WHERE a.id IN"
                + " (SELECT a2.id FROM animals a2" 
                + " JOIN a2.adoptions ad" 
                + " WHERE ad.end IS NULL)"
            + " ORDER BY a.id", String.class);
        return query.getResultList();
    }

    //Source for returning mutlplie values from an sql query
    //https://www.baeldung.com/jpa-return-multiple-entities
    public List<Object[]> getAdoptionInfo(Animal animal) {
        TypedQuery<Object[]> adoptionInfoQuery = entityManager.createQuery("SELECT ad2.start, ad2.end, ad1.name FROM adopters ad1"
            + " JOIN ad1.adoptions ad2"
            + " WHERE ad2.animal_id = :animalId"
            + " ORDER BY ad2.start", 
            Object[].class);
        List<Object[]> adoptionData = adoptionInfoQuery.setParameter("animalId", 
                animal.getId())
            .getResultList();
        return adoptionData;
    }

    public void close() {
        // close down the entity manager and factory so we do not have resource leaks
        entityManager.close();
        factory.close();
    }

}
