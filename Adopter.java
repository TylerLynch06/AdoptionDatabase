import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;

@Entity(name = "adopters")
public class Adopter {

    @Column
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private String address;
    //https://wiki.eclipse.org/EclipseLink/Examples/JPA/2.0/ElementCollections#:~:text=In%20JPA%20a%20ElementCollection%20relationship,element.
    @ElementCollection
    @CollectionTable(
        name = "preferred_species",
        joinColumns = @JoinColumn(name = "adopter_id")
    )
    @Column(name = "species")
    private List<String> preferences = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "adopter_id")
    private List<Adoption> adoptions = new ArrayList<>();

    public Adopter() {

    } 

    public Adopter(String name, String phone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setPreferredSpecies(List<String> speciesNames) {
        speciesNames.parallelStream().forEach(p -> preferences.add(p));
    }

    public void addAdoption(Adoption adoption) {
        adoptions.add(adoption);
    }

    @Override
    public String toString() {
        String result = String.format("%s %s %s %s %s", 
            id, name, phone, email, address);
        return result;
    }

    public String preferencesToString() {
        return preferences.stream()
        .sorted()
        .collect(Collectors.joining(", "));
    }

    public List<String> getPreferredSpecies() {
        return preferences;
    }

    public void removeAdoption(Adoption adoption) {
        adoptions.remove(adoption);
    }
}
