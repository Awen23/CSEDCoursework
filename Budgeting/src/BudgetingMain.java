import jdk.jfr.Category;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BudgetingMain {
    private String username;
    private float amountSpent;
    private float budget;
    private String timeUnits; //days, weeks, months, years
    private int timePeriod;
    private ArrayList<String> categories = new ArrayList<>();

    public BudgetingMain(){
        readFromFile();
    }


    public void mainMenu(){
        boolean loop = true;
        String input;

        displayAll();

        while (loop){
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
                        addToAmountSpent();
                        break;
                    case '4':
                        saveToInformationFile();
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
            System.out.println("\nWhat setting would you like to change?: 1) username 2) budget 3) time period 4) category options 5) go back");
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
                        categoryMenu();
                        break;
                    case '5':
                        return;
                    default:
                        System.out.println("Invalid input, please try again!");
                }
            } else {
                System.out.println("Invalid input, please try again!");
            }
        }
    }

    private void categoryMenu(){
        boolean loop = true;
        String input;
        while(loop){
            System.out.println("\nAdd, Remove or view categories?: 1) add category 2) remove category 3) view categories 4) go back");
            System.out.print("Option number: ");
            input = getInputFromConsole().trim();
            if (!input.isEmpty()){
                switch (input.charAt(0)) {
                    case '1':
                        if(areYouSure("add a category")){
                            addCategory();
                            viewCategories();
                        }
                        break;
                    case '2':
                        if(areYouSure("remove a category")) {
                            removeCategory();
                            viewCategories();
                        }
                        break;
                    case '3':
                        viewCategories();
                        break;
                    case '4':
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

    private void changeUsername(){
        String tempUsername;
        System.out.print("Your username is " + username + ", what is your new username? ");
        tempUsername = validateInputString();
        if (!username.equals(tempUsername)){
            username = tempUsername;
            saveToInformationFile();
            System.out.println("Your username is now "+ username);
        } else {
            System.out.println("New username is the same as the original, returning to setup page");
        }
    }

    protected void setBudget(){
        double amount = -1;
        do {
            System.out.print("\nSet Budget to: ");
            amount = Math.round(validateInputStringToFloat()*100.0)/100.0;
            if (amount <= 0){
                System.out.println("Budget must be greater than 0.01");
            }
        } while (amount <= 0);
        //rounds to whole numbers therefore time by 100 then round it uses whole number rounding
        if (amount == budget){
            System.out.println("New budget is the same as the original, returning to setup page");
        } else {
            budget = (float) amount;
            saveToInformationFile();
            System.out.println("New budget is " + budget);
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
            if (!input.isEmpty()){
                switch (input.charAt(0)) {
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
                    default:
                        System.out.println("Invalid input, please try again!");
                }
            } else {
                System.out.println("Invalid input, please try again!");
            }
        }
        while (loop);
        System.out.print("how many " + timeUnits + "s? ");
        timePeriod = validateInputStringToInteger();
        saveToInformationFile();
        System.out.printf("\nYour budget now refreshes every:\t%d %s", getTimePeriod(), (getTimePeriod()==1)?getTimeUnits():getTimeUnits()+'s');
    }

    private void addCategory(){
        String tempCategory;
        System.out.print("New category name: ");
        tempCategory = validateInputString();
        if (!categories.contains(tempCategory)){
            categories.add(tempCategory);
            saveToInformationFile();
        } else{
            System.out.println("Category already exists");
        }
    }

    private void addCategoryParameter(String s){
        if (!categories.contains(s)){
            categories.add(s);
            saveToInformationFile();
        } else{
            System.out.println("Category already exists");
        }
    }

    private void removeCategory(){
        String tempCategory;
        System.out.print("Category to remove: ");
        tempCategory = getInputFromConsole();
        if (categories.contains(tempCategory)){
            categories.remove(tempCategory);
            saveToInformationFile();
        } else{
            System.out.println("Category doesn't exist");
        }
    }

    private void viewCategories() {
        System.out.println("\nCategories: " + categories);
    }

    protected void addToAmountSpent(){
        String inputCategory;
        double tempSpent;
        System.out.print("Which category is the expenditure in? ");
        viewCategories();
        do {
            inputCategory = validateInputString();
            if (!categories.contains(inputCategory)){
                String response;
                System.out.println(inputCategory + " is not an existing category, would you like to add "+inputCategory+" to the list of categories? ");
                do { //allows the user to add categories on the fly
                    response = getInputFromConsole();
                    if (!response.equals("Yes") && !response.equals("No")){
                        System.out.println("Invalid input, please try again!");
                    }
                } while(!response.equals("Yes") && !response.equals("No"));
                if (response.equals("Yes")){
                    addCategoryParameter(inputCategory);
                }else {
                    System.out.print("\nWhich category is the expenditure in? ");
                }
            }
        } while (!categories.contains(inputCategory));

        System.out.print("\nHow much was spent: ");
        do {
            tempSpent = Math.round(validateInputStringToFloat()*100.0)/100.0;
            if (tempSpent <= 0){
                System.out.println("Amount spent must be greater than 0.01");
            }
        } while (tempSpent <= 0);
        System.out.println("You spent "+tempSpent+" on "+inputCategory);
        appendToSpendingHistoryFile(inputCategory,(float) tempSpent);
        amountSpent += tempSpent;
        System.out.printf("You have spent a total of:\t%.2f", getAmountSpent());
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

    protected String validateInputString() {
        String s;
        do {
            s = getInputFromConsole().trim();
            if (!s.matches("^[a-zA-Z0-9 ]+")) {
                System.out.println("Invalid input, please try again!");
            }
        } while (!s.matches("^[a-zA-Z0-9 ]+"));
        return s;
    }

    protected float validateInputStringToFloat(){
        boolean loop = true;
        float value = 0;
        do{
            try {
                value = Float.parseFloat(getInputFromConsole());
                if (value > Float.MAX_VALUE){
                    System.out.println("Value is too large, please try again");
                } else if (value < -Float.MAX_VALUE){
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

    protected int validateInputStringToInteger(){
        boolean loop = true;
        int value = 0;
        do{
            try {
                value = Integer.parseInt(getInputFromConsole());
                if (value > Integer.MAX_VALUE){
                    System.out.println("Value is too large, please try again");
                } else if (value < -Integer.MAX_VALUE){
                    System.out.println("Value is too small, please try again");
                } else {
                    loop = false;
                }
            }
            catch(NumberFormatException e){
                System.out.print("\nInput must be an integer: ");
                loop = true;
            }
        } while (loop);
        return value;
    }


    private void readFromFile(){
        File budgetFile = new File("Budgeting\\BudgetInfo");
        String line = "Uh oh"; //compiler needed something
        try {
            FileReader fileIn = new FileReader(budgetFile);
            BufferedReader input = new BufferedReader(fileIn);
            try {
                username = input.readLine();
            } catch (NullPointerException e){
                System.out.println("Username not found - default username in use");
                username = "username";
            }
            try {
                amountSpent = Float.parseFloat(input.readLine());
            } catch (NullPointerException | NumberFormatException e){
                System.out.println("Amount spent not found - default amount spent in use");
                amountSpent = 0;
            }
            try {
                budget = Float.parseFloat(input.readLine());
            } catch (NullPointerException | NumberFormatException e) {
                System.out.println("Budget not found - default budget in use");
                budget = 500;
            }
            try {
                timeUnits = input.readLine();
                if (timeUnits != "day" && timeUnits != "week" && timeUnits != "month" && timeUnits != "year"){
                    System.out.println("Time units not found - default time units in use");
                    timeUnits = "month";
                }
            } catch (NullPointerException e) {
                System.out.println("Time units not found - default time units in use");
                timeUnits = "month";
            }
            try {
                timePeriod = Integer.parseInt(input.readLine());
            } catch (NullPointerException | NumberFormatException e) {
                System.out.println("Time period not found - default time period in use");
                timePeriod = 1;
            }

            while (line != null) {
                line = input.readLine(); //read requested line
                if (line != null) {
                    categories.add(line);
                }
            }
            if (categories.size() == 0){
                System.out.println("Categories is empty - adding default categories");
                addCategoryParameter("Food");
                addCategoryParameter("Entertainment");
                addCategoryParameter("Study");
            }
            input.close(); //close the readers
            saveToInformationFile();
        }
        catch (IOException e){
            System.out.println("Budget file not found, make sure the file is called \"BudgetInfo\"");
            System.out.println("Program closing");
            System.exit(1);
        }
    }

    protected boolean saveToInformationFile(){
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

    protected void appendToSpendingHistoryFile(String category, Float amount){
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File("Budgeting\\SpendingHistory"),true)));
            out.println(category+":"+amount+":"+ LocalDateTime.now());
            out.close();
        }catch (FileNotFoundException e){
            System.out.println(e + "Unable to save");
        }catch (IOException e){
            System.out.println(e + "saving failed");
        }
    }


    private void displayAll(){
        System.out.printf("\nHello %s!", getUsername());
        System.out.printf("\nYour Budget is:\t%.2f", getBudget());
        System.out.printf("\nYou have spent:\t%.2f", getAmountSpent());
        System.out.printf("\nYour balance:\t%.2f", getBudget()-getAmountSpent());
        System.out.printf("\nYour budget refreshes every:\t%d %s", getTimePeriod(), (getTimePeriod()==1)?getTimeUnits():getTimeUnits()+'s');
        viewCategories();
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

    protected int getTimePeriod(){
        return timePeriod;
    }


    public static void main(String[] args) {
        BudgetingMain budget = new BudgetingMain();
        budget.mainMenu();
    }
}
