import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BudgetingMain {
    private static String username;
    private static float amountSpent;
    private static float budget;
    private static String timeUnits; //days, weeks, months, years
    private static int timePeriod;
    private static LocalDate budgetStart;
    private static ArrayList<String> categories = new ArrayList<>();

    private BudgetingMain(){
        readFromFile();
    }

    private void run(){
        //new MainMenu().mainMenu();
        new MainMenu().draw();
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
                if (!timeUnits.equals("day") && !timeUnits.equals("week") && !timeUnits.equals("month") && !timeUnits.equals("year")){
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
            try {
                budgetStart = LocalDate.parse(input.readLine());
            } catch (NullPointerException | DateTimeParseException e) {
                System.out.println("Start date not found - default start date in use");
                budgetStart = LocalDate.of(2019,1,1);
            }

            while (line != null) {
                line = input.readLine(); //read requested line
                if (line != null) {
                    categories.add(line);
                }
            }
            if (categories.size() == 0){
                System.out.println("Categories is empty - adding default categories");
                addCategoryParameter("Food", false);
                addCategoryParameter("Entertainment", false);
                addCategoryParameter("Study", false);
            }
            input.close(); //close the readers
            saveToInformationFile();
        }
        catch (IOException e){
            System.out.println("Budget file not found, using default values and creating file.");
            username = "username";
            amountSpent = 0;
            budget = 500;
            timeUnits = "month";
            timePeriod = 1;
            budgetStart = LocalDate.of(2019,1,1);
            addCategoryParameter("Food", false);
            addCategoryParameter("Entertainment", false);
            addCategoryParameter("Study",false);
            saveToInformationFile();
        }
    }


    public static boolean areYouSure(String messageEnd){
        System.out.println("Are you sure you would like to "+messageEnd +"?");
        return YesNo();
    }

    public static boolean YesNo(){
        String response;
        do {
            response = getInputFromConsole();
            if (!response.equals("Yes") && !response.equals("No")){
                System.out.println("Invalid input, please try again!");
            }
        } while(!response.equals("Yes") && !response.equals("No"));
        return response.equals("Yes");
    }



    public static String getInputFromConsole() {
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


    public static String validateInputString() {
        String s;
        do {
            s = getInputFromConsole().trim();
            if (!validateString(s)) {
                System.out.println("Invalid input, please try again!");
            }
        } while (!validateString(s));
        return s;
    }

    public static Boolean validateString(String s){
        return s.matches("^[a-zA-Z0-9 ]+");
    }


    public static float validateInputStringToFloat(){
        final float limit = 1000000000;
        boolean loop = true;
        float value = 0;
        do{
            try {
                value = Float.parseFloat(getInputFromConsole());
                if (value > limit){
                    System.out.println("Value is too large, please try again");
                } else if (value < -limit){
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

    public static int validateInputStringToInteger(){
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

    public static boolean validateStringToNumberGUI(String input){
        final float limit = 1000000000;
        float value = 0;
        try {
            value = Float.parseFloat(input);
            if (value > limit){
                JOptionPane.showConfirmDialog(null, "Value is too large, please try again", "", JOptionPane.DEFAULT_OPTION);
            } else if (value < -limit){
                JOptionPane.showConfirmDialog(null, "Value is too small, please try again", "", JOptionPane.DEFAULT_OPTION);
            } else {
                return true;
            }
            return false;
        }
        catch(NumberFormatException e){
            JOptionPane.showConfirmDialog(null, "Input must be number", "", JOptionPane.DEFAULT_OPTION);
            return false;
        }
    }



    public static ArrayList<Expenditure> getExpendituresBetween(LocalDateTime start, LocalDateTime end){
        ArrayList<Expenditure> expenditures = new ArrayList<>();
        File historyFile = new File("Budgeting\\SpendingHistory");
        String line = "Uh oh"; //compiler needed something

        try {
            FileReader fileIn = new FileReader(historyFile);
            BufferedReader input = new BufferedReader(fileIn);

            while (line != null) {
                line = input.readLine(); //read requested line
                if (line != null) {
                    String[] details = line.split(";");
                    if (details.length == 3) {
                        LocalDateTime expenditureDate = LocalDateTime.parse(details[2]);
                        if (expenditureDate.isAfter(start) && expenditureDate.isBefore(end)) {
                            expenditures.add(new Expenditure(details[0], Float.parseFloat(details[1]), expenditureDate));
                        }
                    }
                }
            }
        }catch (IOException e){
            System.out.println("History file not found");
        }
        return expenditures;
    }

    public static Map<String, Float> sortCategoriesAfter(LocalDateTime start){
        ArrayList<Expenditure> expenditures = BudgetingMain.getExpendituresBetween(start, LocalDateTime.now());
        HashMap<String, Float> categoryValues = new HashMap();

        for (String category:BudgetingMain.getCategories()) {
            categoryValues.put(category, new Float(0)); //intellij says this isn't needed but apparently it is
        }
        for (Expenditure expense:expenditures) {
            categoryValues.replace(expense.getCategory(),categoryValues.get(expense.getCategory()) + expense.getAmount());
        }

        Map<String, Float> sorted = sortByValue(categoryValues);

        return sorted;


    }

    //Hashmap sorting code found at https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
    public static HashMap<String, Float> sortByValue(HashMap<String, Float> map) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Float> > list = new LinkedList<Map.Entry<String, Float> >(map.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Float> >() {
            public int compare(Map.Entry<String, Float> entry1, Map.Entry<String, Float> entry2){
                return (entry2.getValue()).compareTo(entry1.getValue()); //entry2 first for descending
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Float> temp = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    public static boolean saveToInformationFile(){
        try {
            PrintWriter out = new PrintWriter("Budgeting\\BudgetInfo");
            out.println(getUsername());
            out.println(getAmountSpent());
            out.println(getBudget());
            out.println(getTimeUnits());
            out.println(getTimePeriod());
            out.println(getBudgetStart());
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

    public static void viewCategories() {
        System.out.println("\nCategories: " + categories);
    }


    public static float getAmountSpent(){
        return amountSpent;
    }

    public static void setAmountSpent(float a){
        amountSpent = a;
    }


    public static float getBudget(){
        return budget;
    }

    public static void setBudget(float a){
        budget = a;
    }


    public static String getUsername(){
        return username;
    }

    public static void setUsername(String a){
        username = a;
    }


    public static String getTimeUnits(){
        return timeUnits;
    }

    public static void setTimeUnits(String a){
        timeUnits = a;
    }


    public static int getTimePeriod(){
        return timePeriod;
    }

    public static void setTimePeriod(int a){
        timePeriod = a;
    }


    public static LocalDate getBudgetStart() {
        return budgetStart;
    }

    public static void setBudgetStart(LocalDate a){
        budgetStart = a;
    }


    public static ArrayList<String> getCategories(){
        return categories;
    }

    public static void addCategoryParameter(String s, Boolean GUI){
        if (!categories.contains(s)){
            categories.add(s);
            saveToInformationFile();
        } else{
            if (GUI){
                JOptionPane.showConfirmDialog(null, "Category already exists", "", JOptionPane.DEFAULT_OPTION);
            } else {
                System.out.println("Category already exists");
            }
        }
    }

    public static void removeCategoryParameter(String s, boolean GUI){
        if (categories.contains(s)){
            if (categories.size() > 1) {
                categories.remove(s);
                saveToInformationFile();
            } else {
                if(GUI){
                    JOptionPane.showConfirmDialog(null, "Cannot remove last category", "", JOptionPane.DEFAULT_OPTION);
                } else {
                    System.out.println("Cannot remove last category");
                }
            }
        } else{
            System.out.println("Category doesn't exist");
        }
    }



    public static void main(String[] args) {
        BudgetingMain budget = new BudgetingMain();
        budget.run();
    }
}
