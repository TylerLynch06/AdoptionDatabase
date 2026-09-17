import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class UserInterface {
    private Scanner scanner;

    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
    }

    public int inputPositiveInt(String prompt, String error) {
        int number = -1;
        String err = "";
        do { 
            outputMessage(err + prompt);
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
            }
            scanner.nextLine();
            err = error + "\n";
        }
        while (number < 0);
        return number;
    }

    public String inputString(String prompt, String error) {
        String value;
        String err = "";
        do { 
            outputMessage(err + prompt);
            value = scanner.nextLine().trim();
            err = error + "\n";
        }
        while (value.equals(""));
        return value;
    }

    public LocalDate inputDate(String prompt) {
        LocalDate value = null;
        do { 
            outputMessage("Please enter " + prompt.toLowerCase() + ":");
            String input = scanner.nextLine().trim();
            try {
                value = LocalDate.parse(input);
            }
            catch (Exception e) {
                outputMessage("Invalid date. Use YYYY-MM-DD.");
            }
        }
        while (value == null);
        return value;
    }

    public int inputPositiveInt(String text) {
        return inputPositiveInt("Please enter " + text.toLowerCase() + ":", "Invalid " + text.toLowerCase() + ".");
    }

    public String inputString(String text) {
        return inputString("Please enter " + text.toLowerCase() + ":", text + " cannot be blank.");
    }

    public Adopter inputAdopter() throws Exception {
        String name = inputString("Adopter name");
        String phone = inputString("Adopter phone number");
        String email = inputString("Adopter email address");
        String address = inputString("Adopter address"); 
        Adopter adopter = new Adopter(name, phone, email, address);
        return adopter;
    }

    public List<String> inputAdopterPreferences() {
        String species = inputString("Please enter species the adopter is interested in adopting:", "Must specify at least one species.");
        List<String> speciesNames = Arrays.stream(species.split(",+"))
            .collect(Collectors.toList());
        return speciesNames;
    }

    public Animal inputAnimal() {
        String id = inputString("Animal id");
        String name = inputString("Animal name");
        String species = inputString("Animal species"); 
        String breed = inputString("Animal breed");
        String sex = inputString("Please enter animal sex [male/female]:", "Animal sex cannot be blank.");
        String personality = inputString("Animal personality");
        LocalDate arrival = inputDate("Animal arrival date"); 
        return new Animal(name, id, sex, species, breed, arrival, personality);
    }

    public Adoption inputAdoption(int adopterId, String animalId) throws Exception {
        LocalDate adoptionDate = inputDate("adoption date");
        Adoption adoption = new Adoption(adopterId, animalId, adoptionDate, null);
        return adoption;
    }

    //Will repeatedly prompt until the given string is contained within the supplied list
    public String getStringWithinList(String prompt, String err, List<String> list) {
        String input = null;
        while (true) {
            input = inputString(prompt);
            //System.out.println("Animal ID "+animalId);
            if (list.contains(input)) {
                break;
            }
            System.out.println(err);
        }  
        return input;      
    }

    public int getIntWithinList(String prompt, String err, List<Integer> list) {
        //If the value we're working with is an int, then the comparison list will be a list of ints
        //Hence convert to a list of strings
        List<String> listString = list.stream()
            .map(i -> String.valueOf(i))
            .collect(Collectors.toList());
        return Integer.parseInt(getStringWithinList(prompt, err, listString));
    }

    public int inputAdopterId() {
        return inputPositiveInt("adopter id");
    }

    public String inputAnimalId() {
        return inputString("animal id");
    }

    public void outputMessage(String msg) {
        System.out.println(msg);
    }
}
