package src;
import java.io.*;

public class BudgetingMain {
    private float amountSpent;
    private int amountSpentLineNo = 1;
    private float budget;
    private int budgetLineNo = 2;

    public BudgetingMain(){
        boolean loop1 = true;
        String input;
        amountSpent = Float.parseFloat(readFromFile(amountSpentLineNo));
        budget = Float.parseFloat(readFromFile(budgetLineNo));
        displayBudgetAndAmountSpent();
        while (loop1){
            System.out.println("\n\nWhat would you like to do?: 1) view budget and amount spent 2) set budget 3) adjust amount spent 4) save and exit");
            System.out.print("Option number: ");
            input = getInputFromConsole();
            switch (input.charAt(0)){
                case '1':
                    displayBudgetAndAmountSpent();
                    break;
                case '2':
                    do {
                        System.out.print("\nSet Budget to: ");
                    } while (!setBudget(validateInputStringToFloat()));
                    displayBudgetAndAmountSpent();
                    break;
                case '3':
                    System.out.print("\nAdd amount spent: ");
                    addToAmountSpent(validateInputStringToFloat());
                    displayBudgetAndAmountSpent();
                    break;
                case '4':
                    saveToFile();
                    System.exit(0);
            }
        }
    }

    protected float validateInputStringToFloat(){
        boolean loop = true;
        float value = 0;
        do{
            try {
                value = Float.parseFloat(getInputFromConsole());
                loop = false;
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
            return command.toUpperCase(); //Allows user to use any case they want
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }

    /**
     * @param lineNo : Line number in file to be returned
     * @return : String of line at lineNo
     */
    private String readFromFile(int lineNo){
        String line = "Uh oh"; //compiler needed something
        File budgetFile = new File("Budgeting\\BudgetHistory");
        try {
            FileReader fileIn = new FileReader(budgetFile);
            BufferedReader input = new BufferedReader(fileIn);

            for (int i = 1; i < lineNo; i++) { //cycle through the file until it reaches line before requested line
                input.readLine();
            }
            line = input.readLine(); //read requested line
            input.close(); //close the readers
        }
        catch (IOException e){
            System.out.println("Budget file not found, make sure the file is called \"BudgetHistory.txt\"");
            System.out.println("Program closing");
            System.exit(1);
        }
        return line;
    }

    private boolean saveToFile(){
        try {
            PrintWriter out = new PrintWriter("Budgeting\\BudgetHistory");
            out.println(getAmountSpent());
            out.println(getBudget());
            out.close();
            return true;
        }
        catch (FileNotFoundException e){
            System.out.println("Unable to save");
            return false;
        }
    }

    private void displayBudgetAndAmountSpent(){
        System.out.printf("Your Budget is:\t%.2f\nYou have spent:\t%.2f", getBudget(), getAmountSpent());
    }

    protected float getAmountSpent(){
        return amountSpent;
    }

    protected float getBudget(){
        return budget;
    }

    protected void addToAmountSpent(float amount){ //negative for removing from amountSpent
        amountSpent += amount;
    }

    /**
     * @param amount : number to set budget to
     * @return : true if budget was set, false if not
     */
    protected boolean setBudget(float amount){
        if (amount >= 0) {
            budget = amount;
            return true;
        }
        else {
            System.out.println("Budget must be positive");
            return false;
        }
    }

    public static void main(String[] args) {
        BudgetingMain budget = new BudgetingMain();
    }
}
