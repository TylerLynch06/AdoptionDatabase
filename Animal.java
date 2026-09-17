import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;

@Entity(name = "animals")
public class Animal {
    @Column
    private String name;
    @Id
    @Column
    private String id;
    @Column
    private String sex;
    @Column
    private String species;
    @Column
    private String breed;
    @Column
    private Date arrival;
    @Column
    private String personality;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "animal_id")
    private List<Adoption> adoptions = new ArrayList<>();

    public Animal() {

    }

    public Animal(String name, String id, String sex, String species, String breed, LocalDate arrivalDate, String personality) {
        this.name = name;
        this.id = id;
        this.sex = sex;
        this.species = species;
        this.breed = breed;
        this.arrival = Date.valueOf(arrivalDate);
        this.personality = personality;
    }

    @Override
    public String toString() {
        String date = arrival.toString();
        return String.format("%s %s %s %s (%s) arrived %s %s",
            name,
            id,
            sex,
            species,
            breed,
            date,
            personality);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addAdoption(Adoption adoption) {
        adoptions.add(adoption);
    }

    public List<Adoption> getAdoptions() {
        return adoptions;
    }
}
