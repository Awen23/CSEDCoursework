import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class MainMenu {
    public JFrame mainFrame;

    public MainMenu(){

    }

    public void mainMenu(){
        boolean loop = true;
        String input;

        displayAll();

        while (loop){
            System.out.println("\n\nWhat would you like to do?: 1) view details 2) set up 3) adjust amount spent 4) get categories in sorted order 5)save and exit");
            System.out.print("Option number: ");
            input = BudgetingMain.getInputFromConsole().trim();
            if(!input.isEmpty()) {
                switch (input.charAt(0)) {
                    case '1':
                        displayAll();
                        break;
                    case '2':
                        new SetupMenu().setUp();
                        displayAll();
                        break;
                    case '3':
                        addToAmountSpent();
                        break;
                    case'4':
                        sortCategoriesAfter(BudgetingMain.getBudgetStart().atTime(23,59,59));
                        break;
                    case '5':
                        BudgetingMain.saveToInformationFile();
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

    public void draw(){
        mainFrame = new JFrame("Budgeting Application");
        mainFrame.setSize(500,800);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() { //allows saving when the window is closed via the x
            @Override
            public void windowClosing(WindowEvent event) {
                BudgetingMain.saveToInformationFile();
                mainFrame.dispose();
                System.exit(0);
            }
        });

        mainFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel welcomeMessage = new JLabel("Hello, " + BudgetingMain.getUsername());
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        mainFrame.add(welcomeMessage, c);

        JPanel pieArea = new JPanel();
        pieArea.setPreferredSize(new Dimension(500,500));
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 1;
        mainFrame.add(pieArea, c);

        JButton butSetup = new JButton("Setup");
        butSetup.setPreferredSize(new Dimension(100,100));
        butSetup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                new SetupMenu(mainFrame).draw();
            }
        });
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        mainFrame.add(butSetup, c);

        JButton butAddSpent = new JButton("Input Amount Spent");
        butAddSpent.setPreferredSize(new Dimension(100,100));
        butAddSpent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField textInput = new JTextField();
                textInput.setPreferredSize(new Dimension(100,25));
                JComboBox<String> comboBox = new JComboBox(BudgetingMain.getCategories().toArray());
                comboBox.setEditable(false);
                JPanel amountSpentPanel = new JPanel();
                amountSpentPanel.add(textInput);
                amountSpentPanel.add(comboBox);
                int result = 2; //2 means cancelled
                double tempSpent = 0;
                do {
                    result = JOptionPane.showConfirmDialog(null, amountSpentPanel, "Add to amount spent", JOptionPane.OK_CANCEL_OPTION);
                    if (result != JOptionPane.OK_OPTION){
                        break;
                    }
                    try {
                        tempSpent = Math.round(Float.parseFloat(textInput.getText()) * 100.0) / 100.0;
                        if (tempSpent <= 0){
                            JOptionPane.showConfirmDialog(null, "Input must be greater than 0.01", "", JOptionPane.DEFAULT_OPTION);

                        }
                    } catch (NumberFormatException ex){
                        tempSpent = 0;
                        JOptionPane.showConfirmDialog(null, "Input must be a number", "", JOptionPane.DEFAULT_OPTION);
                    }
                } while(tempSpent <= 0 || result != JOptionPane.OK_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    addToAmountSpentParam(comboBox.getSelectedItem().toString() , (float) tempSpent);
                    System.out.println("haha");
                }
            }
        });
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        mainFrame.add(butAddSpent, c);

        JButton butGoal = new JButton("Data Trends");
        butGoal.setPreferredSize(new Dimension(100,100));
        butGoal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                new DataTrendsMenu().draw();
                mainFrame.setVisible(true);
            }
        });
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 2;
        mainFrame.add(butGoal, c);

        JButton butSaveExit = new JButton("Save and Exit");
        butSaveExit.setPreferredSize(new Dimension(100,100));
        butSaveExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BudgetingMain.saveToInformationFile();
                mainFrame.dispose();
                System.exit(0);
            }
        });
        c.gridwidth = 1;
        c.gridx = 3;
        c.gridy = 2;
        mainFrame.add(butSaveExit, c);

        mainFrame.setVisible(true);
    }

    private void addToAmountSpent(){
        String inputCategory;
        double tempSpent;
        System.out.print("Which category is the expenditure in? ");
        BudgetingMain.viewCategories();
        do {
            inputCategory = BudgetingMain.validateInputString();
            if (!BudgetingMain.getCategories().contains(inputCategory)){
                System.out.println(inputCategory + " is not an existing category, would you like to add "+inputCategory+" to the list of categories? ");
                if (BudgetingMain.YesNo()){
                    BudgetingMain.addCategoryParameter(inputCategory, false);
                }else {
                    System.out.print("\nWhich category is the expenditure in? ");
                }
            }
        } while (!BudgetingMain.getCategories().contains(inputCategory));

        System.out.print("\nHow much was spent: ");
        do {
            tempSpent = Math.round(BudgetingMain.validateInputStringToFloat()*100.0)/100.0;
            if (tempSpent <= 0){
                System.out.println("Amount spent must be greater than 0.01");
            }
        } while (tempSpent <= 0);
        System.out.println("You spent "+tempSpent+" on "+inputCategory);
        addToAmountSpentParam(inputCategory, (float) tempSpent);
        System.out.printf("You have spent a total of:\t%.2f", BudgetingMain.getAmountSpent());
    }

    private void addToAmountSpentParam(String category,float spent){
        appendToSpendingHistoryFile(category, spent);
        BudgetingMain.setAmountSpent(BudgetingMain.getAmountSpent() + spent);
    }


    private void appendToSpendingHistoryFile(String category, Float amount){
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File("Budgeting\\SpendingHistory"),true)));
            out.println(category+";"+amount+";"+ LocalDateTime.now());
            out.close();
        }catch (FileNotFoundException e){
            System.out.println(e + "Unable to save");
        }catch (IOException e){
            System.out.println(e + "saving failed");
        }
    }


    private void displayAll(){
        System.out.printf("\nHello %s!", BudgetingMain.getUsername());
        System.out.printf("\nYour Budget is:\t%.2f", BudgetingMain.getBudget());
        System.out.printf("\nYou have spent:\t%.2f", BudgetingMain.getAmountSpent());
        System.out.printf("\nYour balance:\t%.2f", BudgetingMain.getBudget()- BudgetingMain.getAmountSpent());
        System.out.printf("\nYour budget refreshes every:\t%d %s\n", BudgetingMain.getTimePeriod(), (BudgetingMain.getTimePeriod()==1)? BudgetingMain.getTimeUnits():BudgetingMain.getTimeUnits()+'s');
        System.out.println(returnWeeklyTarget());
        BudgetingMain.viewCategories();
    }

    private String returnWeeklyTarget(){
        LocalDate budgetStart = BudgetingMain.getBudgetStart();
        int timePeriod = BudgetingMain.getTimePeriod();

        LocalDate current = LocalDate.now();
        LocalDate endDate;
        switch (BudgetingMain.getTimeUnits()){
            case "day":
                endDate = budgetStart.plusDays(timePeriod);
                while (endDate.isBefore(current)){
                    endDate = endDate.plusDays(timePeriod);
                }
                break;
            case "week":
                endDate = budgetStart.plusWeeks(timePeriod);
                while (endDate.isBefore(current)){
                    endDate = endDate.plusWeeks(timePeriod);
                }
                break;
            case "month":
                endDate = budgetStart.plusMonths(timePeriod);
                while (endDate.isBefore(current)){
                    endDate = endDate.plusMonths(timePeriod);
                }
                break;
            case "year":
                endDate = budgetStart.plusYears(timePeriod);
                while (endDate.isBefore(current)){
                    endDate = endDate.plusYears(timePeriod);
                }
                break;
            default:
                endDate = budgetStart.plusMonths(1);
        }

        ArrayList<Expenditure> thisWeekExpenditures = BudgetingMain.getExpendituresBetween(current.with(ChronoField.DAY_OF_WEEK,1).atStartOfDay(), LocalDateTime.now()); //finds the date of the first of this week

        float spentThisWeek = 0;
        for (Expenditure expense:thisWeekExpenditures) {
            spentThisWeek += expense.getAmount();
        }

        float weeklyTarget = (BudgetingMain.getBudget()- BudgetingMain.getAmountSpent()+spentThisWeek)/(Math.floorDiv(ChronoUnit.DAYS.between(current,endDate),7) + 1);
        return String.format("You have a target to stay under £%.2f this week, you have spent £%.2f so far.",weeklyTarget, spentThisWeek);
    }

    private void sortCategoriesAfter(LocalDateTime start){
        ArrayList<Expenditure> expenditures = BudgetingMain.getExpendituresBetween(start, LocalDateTime.now());
        HashMap<String, Float> categoryValues = new HashMap();

        for (String category:BudgetingMain.getCategories()) {
            categoryValues.put(category, new Float(0)); //intellij says this isn't needed but apparently it is
        }
        for (Expenditure expense:expenditures) {
            categoryValues.replace(expense.getCategory(),categoryValues.get(expense.getCategory()) + expense.getAmount());
        }

        Map<String, Float> sorted = sortByValue(categoryValues);

        System.out.println();
        for (String key:sorted.keySet()) {
            System.out.println(key+":"+categoryValues.get(key));
        }


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
}
