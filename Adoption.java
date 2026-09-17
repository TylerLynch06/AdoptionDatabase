import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Column;

@Entity(name = "adoptions")
@IdClass(AdoptionId.class)
public class Adoption {

    @Id
    @Column
    private int adopter_id;
    @Id
    @Column
    private String animal_id;
    @Column
    private Date start;
    @Column
    private Date end;

    public Adoption() {

    }

    public Adoption(int adopter_id, String animal_id, LocalDate start, LocalDate end) {
        this.adopter_id = adopter_id;
        this.animal_id = animal_id;
        this.start = Date.valueOf(start);
        this.end = (end != null) ? Date.valueOf(end) : null;
    }

    public int getAdopterId() {
        return adopter_id;
    }

    public String getAnimalId() {
        return animal_id;
    }

    public LocalDate getStart() {
        return start.toLocalDate();
    }

    public LocalDate getEnd() {
        if (end == null) {
            return null;
        }
        return end.toLocalDate();
    }

    public void setEnd(LocalDate end) {
        this.end = Date.valueOf(end);
    }
}
