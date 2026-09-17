public class AdoptionId {
    //These variables are required, despite what the compiler says
    @SuppressWarnings("unused") 
    private int adopter_id;
    @SuppressWarnings("unused") 
    private String animal_id;

    public AdoptionId(int adopter_id, String animal_id) {
        this.adopter_id = adopter_id;
        this.animal_id = animal_id;
    }

    public AdoptionId() {

    }
}
