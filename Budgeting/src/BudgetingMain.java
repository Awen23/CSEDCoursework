import java.io.*;
import java.util.ArrayList;

public class BudgetingMain {
    private String username;
    private float amountSpent;
    private float budget;
    private String timeUnits; //days, weeks, months, years
    private float timePeriod;
    private ArrayList<String> categories = new ArrayList<>();

    public BudgetingMain(){
        readFromFile();
    }

    public void mainMenu(){
        boolean loop1 = true;
        String input;

        displayAll();

        while (loop1){
            System.out.println("\n\nWhat would you like to do?: 1) view details 2) set up 3) adjust amount spent 4) save and exit");
            System.out.print("Option number: ");
            input = getInputFromConsole().trim();
            if(!input.isEmpty()) {
                switch (input.charAt(0)) {
                    case '1':
                        displayAll();
                        break;
                    case '2':
                        setUp();
                        displayAll();
                        break;
                    case '3':
                        System.out.print("\nAdd amount spent: ");
                        addToAmountSpent(validateInputStringToFloat());
                        displayAll();
                        break;
                    case '4':
                        saveToFile();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid input, please try again!");
                }
            } else {
                System.out.println("Invalid input, please try again!");
            }
        }
    }

    private void setUp(){
        boolean loop = true;
        String input;
        while(loop){
            System.out.println("\nWhat setting would you like to change?: 1) username 2) budget 3) time period 4) add category 5) remove category 6) go back");
            System.out.print("Option number: ");
            input = getInputFromConsole().trim();
            if (!input.isEmpty()){
                switch (input.charAt(0)) {
                    case '1':
                        if (areYouSure("change your username")) {
                            changeUsername();
                        }
                        break;
                    case '2':
                        if (areYouSure("change the budget")) {
                            setBudget();
                        }
                        break;
                    case '3':
                        if (areYouSure("change the time period")) {
                            changeTimePeriod();
                        }
                        break;
                    case '4':
                        addCategory();
                        break;
                    case '5':
                        removeCategory();
                        break;
                    case '6':
                        return;
                    default:
                        System.out.println("Invalid input, please try again!");
                }
            } else {
                System.out.println("Invalid input, please try again!");
            }
        }
    }

    private boolean areYouSure(String messageEnd){
        String response = "Don't know";
        System.out.println("Are you sure you would like to "+messageEnd +"?");
        do {
            response = getInputFromConsole();
            if (!response.equals("Yes") && !response.equals("No")){
                System.out.println("Invalid input, please try again!");
            }
        } while(!response.equals("Yes") && !response.equals("No"));

        return response.equals("Yes");
    }

    private void addCategory(){
        String tempCategory;
        System.out.print("New category name: ");
        tempCategory = getInputFromConsole();
        if (!categories.contains(tempCategory)){
            categories.add(tempCategory);
            saveToFile();
        }
    }

    private void removeCategory(){
        String tempCategory;
        System.out.print("Category to remove: ");
        tempCategory = getInputFromConsole();
        if (categories.contains(tempCategory)){
            categories.remove(tempCategory);
            saveToFile();
        }
    }

    private void changeTimePeriod(){
        String input;
        boolean loop = true;
        System.out.println("Your current time period is " + timePeriod + " " + ((getTimePeriod()==1)?getTimeUnits():getTimeUnits()+'s'));
        do{
            System.out.println("what are the new units? 1) days 2) weeks 3) months 4) years");
            System.out.print("Option number: ");
            input = getInputFromConsole().trim();
            switch (input.charAt(0)){
                case '1':
                    timeUnits = "day";
                    loop = false;
                    break;
                case '2':
                    timeUnits = "week";
                    loop = false;
                    break;
                case '3':
                    timeUnits = "month";
                    loop = false;
                    break;
                case '4':
                    timeUnits = "year";
                    loop = false;
                    break;
                default: System.out.println("Invalid input, please try again!");
            }
        }
        while (loop);
        System.out.print("how many " + timeUnits + "s? ");
        timePeriod = validateInputStringToFloat();
        saveToFile();
    }

    private void changeUsername(){
        String tempUsername;
        do {
            System.out.print("Your username is " + username + ", what is your new username? ");
            tempUsername = getInputFromConsole();
            if (!tempUsername.matches("^[a-zA-Z0-9 ]+")) {
                System.out.println("Invalid input, please try again!");
            }
        } while (!tempUsername.matches("^[a-zA-Z0-9 ]+"));
        if (!username.equals(tempUsername)){
            username = tempUsername.trim();
            saveToFile();
            System.out.println("Your username is now "+ username);
        } else {
            System.out.println("New username is the same as the original, returning to setup page");
        }
    }

    protected void setBudget(){
        float amount = -1;
        do {
            System.out.print("\nSet Budget to: ");
            amount = validateInputStringToFloat();
            if (amount < 0){
                System.out.println("Budget must be positive");
            }
        } while (amount < 0);
        if (amount == budget){
            System.out.println("New budget is the same as the original, returning to setup page");
        } else {
            budget = amount;
            saveToFile();
            System.out.println("New budget is " + budget);
        }
    }

    protected float validateInputStringToFloat(){
        boolean loop = true;
        float value = 0;
        do{
            try {
                value = Float.parseFloat(getInputFromConsole());
                if (value > Float.MAX_VALUE){
                    System.out.println("Value is too large, please try again");
                } else if (value < Float.MIN_VALUE){
                    System.out.println("Value is too small, please try again");
                } else {
                    loop = false;
                }
            }
            catch(NumberFormatException e){
                System.out.print("\nInput must be a number: ");
                loop = true;
            }
        } while (loop);
        return value;
    }

    protected String getInputFromConsole() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String command = reader.readLine();

            //Close on an End-of-file (EOF) (Ctrl-D on the terminal)
            /*if (command == null) {
                //Exit code 0 for a graceful exit
                System.exit(0);
            }
            */
            return command; //Allows user to use any case they want
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private void readFromFile(){
        File budgetFile = new File("Budgeting\\BudgetInfo");
        String line = "Uh oh"; //compiler needed something
        try {
            FileReader fileIn = new FileReader(budgetFile);
            BufferedReader input = new BufferedReader(fileIn);

            username = input.readLine();
            amountSpent = Float.parseFloat(input.readLine());
            budget = Float.parseFloat(input.readLine());
            timeUnits = input.readLine();
            timePeriod = Float.parseFloat(input.readLine());

            while (line != null) {
                line = input.readLine(); //read requested line
                if (line != null) {
                    categories.add(line);
                }
            }
            input.close(); //close the readers
        }
        catch (IOException e){
            System.out.println("Budget file not found, make sure the file is called \"BudgetInfo.txt\"");
            System.out.println("Program closing");
            System.exit(1);
        }
    }

    protected boolean saveToFile(){
        try {
            PrintWriter out = new PrintWriter("Budgeting\\BudgetInfo");
            out.println(getUsername());
            out.println(getAmountSpent());
            out.println(getBudget());
            out.println(getTimeUnits());
            out.println(getTimePeriod());
            for (String category:categories) {
                out.println(category);
            }
            out.close();
            return true;
        }
        catch (FileNotFoundException e){
            System.out.println("Unable to save");
            return false;
        }
    }

    private void displayAll(){
        System.out.printf("\nHello %s!", getUsername());
        System.out.printf("\nYour Budget is:\t%.2f", getBudget());
        System.out.printf("\nYou have spent:\t%.2f", getAmountSpent());
        System.out.printf("\nYour balance:\t%.2f", getBudget()-getAmountSpent());
        System.out.printf("\nYour budget refreshes every:\t%.0f %s", getTimePeriod(), (getTimePeriod()==1)?getTimeUnits():getTimeUnits()+'s');
        System.out.println("\nCategories: " + categories);
    }

    protected float getAmountSpent(){
        return amountSpent;
    }

    protected float getBudget(){
        return budget;
    }

    protected String getUsername(){
        return username;
    }

    protected String getTimeUnits(){
        return timeUnits;
    }

    protected float getTimePeriod(){
        return timePeriod;
    }

    protected void addToAmountSpent(float amount){ //negative for removing from amountSpent
        amountSpent += amount;
    }

    public static void main(String[] args) {
        BudgetingMain budget = new BudgetingMain();
        budget.mainMenu();
    }
}
