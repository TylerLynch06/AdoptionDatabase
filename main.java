import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;

//export CLASSPATH=${CLASSPATH}:./libs/*

public class main {
    public static void main(String[] args) {
        UserInterface ui = new UserInterface(new Scanner(System.in));

        if (args.length != 2) {
            ui.outputMessage("Usage: java Practical3 <database_file_name> <task>");
            return;
        }

        //Initialise DAOs
        AdopterDAO adopterDAO = null;
        AdoptionDAO adoptionDAO = null;
        AnimalDAO animalDAO = null;

        try {
            animalDAO = new AnimalDAO(args[0]);
            adopterDAO = new AdopterDAO(args[0]);
            adoptionDAO = new AdoptionDAO(args[0]);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Could not connect to database");
            return;
        } 

        switch (args[1]) {
            case ("1") -> { // add animal
                Animal animal = ui.inputAnimal();
                try {
                    animalDAO.store(animal);
                    System.out.println("Stored " + animal.toString());
                }
                catch (Exception e) {
                    System.out.println("Could not add animal.");
                }
                break;
            }
            case ("2") -> { // add adopter
                try {
                    Adopter adopter = ui.inputAdopter();
                    //adopterDAO.store(adopter);
                    List<String> speciesPreferences = ui.inputAdopterPreferences();
                    adopterDAO.store(adopter);
                    adopter.setPreferredSpecies(speciesPreferences);
                    adopterDAO.update(adopter);
                    System.out.println("Stored " + adopter.toString());
                }
                catch (Exception e) {
                    System.out.println("Could not add adopter.");
                    //System.out.println("Error\n"+e.getMessage());
                }
            }
            case ("3") -> { // list all adopters (including their preferences)
                try {
                    for (Adopter adopter : adopterDAO.getAll()) {
                        //Made a to string method for brevity/readability
                        System.out.println(adopter.toString());
                        System.out.println("    Preferred species: " + adopter.preferencesToString());
                    }
                }
                catch (Exception e) {
                    System.out.println("Could not display adopters.");
                }
            }
            case ("4") -> { // list all animals (including adoptions)
                try {
                    for (Animal animal : animalDAO.getAll()) {
                        System.out.println(animal.toString());
                        List<Object[]> adoptionInfo = animalDAO.getAdoptionInfo(animal);
                        for (Object[] adoptionRecordInfo : adoptionInfo) {
                            //If end is null, set it to present
                            if (adoptionRecordInfo[1] == null) {
                                adoptionRecordInfo[1] = "present";
                            }
                            System.out.printf("    Adopted %s - %s by %s\n", 
                                adoptionRecordInfo);
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("Could not display animals.");
                }

            }
            case ("5") -> { // find matches (given an adopter, find matches)
                List<Integer> existingAdopterIds = adopterDAO.getExistingIds();
                int adopterId = ui.getIntWithinList("adopter id",
                    "Invalid adopter id.", 
                    existingAdopterIds);     
                try {
                    Adopter adopter = adopterDAO.get(adopterId);
                    List<Animal> animals = adopterDAO.findPreferredAnimals(adopter);
                    System.out.println(adopter.toString());
                    System.out.println("    Preferred species: " + adopter.preferencesToString());
                    for (Animal animal : animals) {
                        System.out.println(animal.toString());
                    }
                }
                catch (Exception e) {
                    System.out.println("Could not display matches.");
                    //System.out.println("Error\n"+e.getMessage());
                }
            }

            case ("6") -> { // adopt (enter adopter id and animal id)   
                String placeholderString = "%s adopted %s on %s\n";
                try {
                    List<Integer> existingAdopterIds = adopterDAO.getExistingIds(); 
                    int adopterId = ui.getIntWithinList("adopter id", 
                        "Invalid adopter id.",
                        existingAdopterIds); 
                    Adopter adopter = adopterDAO.get(adopterId);
                    List<String> adoptableAnimalIds = adopterDAO.findAvailableAnimalIds(adopter);

                    String animalId = ui.getStringWithinList("an animal id",
                        "Invalid animal id.", 
                        adoptableAnimalIds);
                    Animal animal = animalDAO.get(animalId);
                    Adoption adoption = ui.inputAdoption(adopterId, animalId);

                    adopterDAO.addAdoption(adopter, adoption);
                    adopterDAO.update(adopter);
                    animalDAO.addAdoption(animal, adoption);
                    animalDAO.update(animal);

                    System.out.printf(placeholderString,
                        adopter.getName(), 
                        animal.getName(),
                        adoption.getStart().toString());
                }
                catch (Exception e) {
                    System.out.println("Could not adopt animal.");
                }
            }
            case ("7") -> { // unadopt (enter animal id)
                try {
                    List<String> availableIds = animalDAO.getAdoptedAnimalIds();
                    String animalId = ui.getStringWithinList("an animal id", "Invalid animal id.", availableIds);
                    LocalDate end = ui.inputDate("return date");
                    String placeholderString = "%s returned to centre on %s\n";
                    Animal animal = animalDAO.get(animalId);
                    int adopterId = animalDAO.getCurrentAdopterId(animal);
                    Adoption adoption = adoptionDAO.get(adopterId, animalId);
                    Adopter adopter = adopterDAO.get(adopterId);
                    adopter.removeAdoption(adoption);
                    adopterDAO.update(adopter);
                    adoption.setEnd(end);
                    adoptionDAO.update(adoption);
                    System.out.printf(placeholderString, 
                        animal.getName(),
                        adoption.getEnd());
                }
                catch (Exception e) {
                    System.out.println("Could not return animal.");
                    //System.out.println(e.getMessage());
                }
            }
            default -> {
                ui.outputMessage("Invalid option.");
                return;
            }
        }

    }
}
