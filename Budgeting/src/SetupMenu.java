import com.sun.tools.javac.Main;
import jdk.jfr.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SetupMenu {
    private JFrame mainFrame;

    public SetupMenu(){

    }

    public SetupMenu(JFrame mainFrame){
        this.mainFrame = mainFrame;
    }


    public void setUp(){
        boolean loop = true;
        String input;
        while(loop){
            System.out.println("\nWhat setting would you like to change?: 1) username 2) budget 3) time period 4) category options 5) go back");
            System.out.print("Option number: ");
            input = BudgetingMain.getInputFromConsole().trim();
            if (!input.isEmpty()){
                switch (input.charAt(0)) {
                    case '1':
                        if (BudgetingMain.areYouSure("change your username")) {
                            changeUsername();
                        }
                        break;
                    case '2':
                        if (BudgetingMain.areYouSure("change the budget")) {
                            setBudget();
                        }
                        break;
                    case '3':
                        if (BudgetingMain.areYouSure("change the time period")) {
                            changeTimePeriod();
                        }
                        break;
                    case '4':
                        new CategoryMenu().categoryMenu();
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

    public void draw(){
        JFrame setupFrame = new JFrame();
        setupFrame.setSize(800,500);
        setupFrame.setTitle("Settings");
        setupFrame.setLocationRelativeTo(null);
        setupFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setupFrame.addWindowListener(new WindowAdapter() { //allows saving when the window is closed via the x
            @Override
            public void windowClosing(WindowEvent event) {
                BudgetingMain.saveToInformationFile();
//                mainFrame.setVisible(true);
                new MainMenu().draw();
                setupFrame.dispose();
            }
        });

        setupFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel lUsername = new JLabel("Current username:");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        setupFrame.add(lUsername, c);

        JTextField tUsername = new JTextField(BudgetingMain.getUsername());
        tUsername.setPreferredSize(new Dimension(300, 25));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        setupFrame.add(tUsername, c);

        JButton bUsername = new JButton("Change Username");
        bUsername.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tempUsername = tUsername.getText().trim();
                if (BudgetingMain.validateString(tempUsername) && !tempUsername.equals(BudgetingMain.getUsername())){
                    changeUsernameParam(tempUsername);
                    JOptionPane.showConfirmDialog(null, "Username has been changed to " + BudgetingMain.getUsername(), "", JOptionPane.DEFAULT_OPTION);
                } else if (tempUsername.equals(BudgetingMain.getUsername())){
                    JOptionPane.showConfirmDialog(null, "New username is the same as the original, returning to setup page", "", JOptionPane.DEFAULT_OPTION);
                } else{
                    JOptionPane.showConfirmDialog(null, "New username contains invalid characters", "", JOptionPane.DEFAULT_OPTION);
                }
                tUsername.setText(BudgetingMain.getUsername());
            }
        });
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        setupFrame.add(bUsername, c);


        JLabel lBudget = new JLabel("Current Budget:");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        setupFrame.add(lBudget, c);

        JTextField tBudget = new JTextField(String.format("%.2f",BudgetingMain.getBudget()));
        tBudget.setPreferredSize(new Dimension(300, 25));
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        setupFrame.add(tBudget, c);

        JButton bBudget = new JButton("Change Budget");
        bBudget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strBudget = tBudget.getText().trim();
                if (BudgetingMain.validateStringToNumberGUI(strBudget)) {
                    double dobBudget = Float.parseFloat(strBudget);
                    dobBudget = Math.round(dobBudget * 100.0) / 100.0;
                    float budget = (float) dobBudget;
                    if (budget > 0) {
                        if (budget != BudgetingMain.getBudget()) {
                            setBudgetParam(budget);
                            JOptionPane.showConfirmDialog(null, "Budget has been changed to " + BudgetingMain.getBudget(), "", JOptionPane.DEFAULT_OPTION);
                        } else {
                            JOptionPane.showConfirmDialog(null, "New budget is the same as the original, returning to setup page", "", JOptionPane.DEFAULT_OPTION);
                        }
                    } else {
                        JOptionPane.showConfirmDialog(null, "Budget must be greater than 0.01", "", JOptionPane.DEFAULT_OPTION);
                    }
                }
                tBudget.setText(String.format("%.2f",BudgetingMain.getBudget()));
            }
        });
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        setupFrame.add(bBudget, c);

        JLabel lTime = new JLabel("Current Time Period:");
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        setupFrame.add(lTime, c);

        JTextField tTimeP = new JTextField((String.format("%d",BudgetingMain.getTimePeriod())));
        tTimeP.setPreferredSize(new Dimension(150, 25));
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        setupFrame.add(tTimeP, c);

        String[] units = {"day","week", "month", "year"};
        JComboBox<String> tTimeU = new JComboBox<String>(units);
        tTimeU.setPreferredSize(new Dimension(150, 25));
        tTimeU.setEditable(false);
        tTimeU.setSelectedItem(BudgetingMain.getTimeUnits());
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        setupFrame.add(tTimeU, c);


        JButton bTime = new JButton("Change Time Period");
        bTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strTimePeriod = tTimeP.getText().trim();
                if (BudgetingMain.validateStringToNumberGUI(strTimePeriod)) {
                    int timePeriod = Integer.parseInt(strTimePeriod);
                    if (timePeriod > 0) {
                        String timeUnits = tTimeU.getSelectedItem().toString();
                        if (timePeriod != BudgetingMain.getTimePeriod() || !timeUnits.equals(BudgetingMain.getTimeUnits())) {
                            changeTimePeriodParam(timeUnits, timePeriod);
                            JOptionPane.showConfirmDialog(null, String.format("Your budget now refreshes every: %d %s", BudgetingMain.getTimePeriod(), (BudgetingMain.getTimePeriod()==1)?BudgetingMain.getTimeUnits():BudgetingMain.getTimeUnits()+'s'), "", JOptionPane.DEFAULT_OPTION);
                        } else {
                            JOptionPane.showConfirmDialog(null, "New time period is the same as the original", "", JOptionPane.DEFAULT_OPTION);
                        }
                    } else {
                        JOptionPane.showConfirmDialog(null, "Time period must be positive", "", JOptionPane.DEFAULT_OPTION);
                    }
                }
                tTimeP.setText(String.format("%d",BudgetingMain.getTimePeriod()));
                tTimeU.setSelectedItem(BudgetingMain.getTimeUnits());
            }
        });
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        setupFrame.add(bTime, c);

        JButton bCategories = new JButton("Category Menu");
        bCategories.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupFrame.setVisible(false);
                new CategoryMenu(setupFrame).draw();
            }
        });
        c.gridx = 2;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        setupFrame.add(bCategories, c);


        setupFrame.setVisible(true);
    }

    private void changeUsername(){
        String tempUsername;
        String username = BudgetingMain.getUsername();

        System.out.print("Your username is " + username + ", what is your new username? ");
        tempUsername = BudgetingMain.validateInputString();
        if (!username.equals(tempUsername)){
            changeUsernameParam(tempUsername);
            System.out.println("Your username is now "+ BudgetingMain.getUsername());
        } else {
            System.out.println("New username is the same as the original, returning to setup page");
        }
    }

    private void changeUsernameParam(String username){
        if (!BudgetingMain.getUsername().equals(username)){
            BudgetingMain.setUsername(username);
            BudgetingMain.saveToInformationFile();
        }
    }

    protected void setBudget(){
        float budget = BudgetingMain.getBudget();
        double amount = -1;
        do {
            System.out.print("\nSet Budget to: ");
            amount = Math.round(BudgetingMain.validateInputStringToFloat()*100.0)/100.0;
            if (amount <= 0){
                System.out.println("Budget must be greater than 0.01");
            }
        } while (amount <= 0);
        //rounds to whole numbers therefore time by 100 then round it uses whole number rounding
        if (amount == budget){
            System.out.println("New budget is the same as the original, returning to setup page");
        } else {
            setBudgetParam((float) amount);
            System.out.println("New budget is " + BudgetingMain.getBudget());
        }
    }

    private void setBudgetParam(Float budget){
        if (BudgetingMain.getBudget() != budget) {
            BudgetingMain.setBudget(budget);
            BudgetingMain.saveToInformationFile();
        }
    }

    private void changeTimePeriod(){
        String timeUnits = BudgetingMain.getTimeUnits();
        int timePeriod = BudgetingMain.getTimePeriod();

        String input;
        boolean loop = true;
        System.out.println("Your current time period is " + timePeriod + " " + ((timePeriod==1)?timeUnits:timeUnits+'s'));
        do{
            System.out.println("what are the new units? 1) days 2) weeks 3) months 4) years");
            System.out.print("Option number: ");
            input = BudgetingMain.getInputFromConsole().trim();
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
        BudgetingMain.setTimeUnits(timeUnits);

        System.out.print("how many " + timeUnits + "s? ");
        timePeriod = BudgetingMain.validateInputStringToInteger();
        BudgetingMain.setTimePeriod(timePeriod);
        BudgetingMain.saveToInformationFile();
        System.out.printf("\nYour budget now refreshes every:\t%d %s", timePeriod, (timePeriod==1)?timeUnits:timeUnits+'s');
    }

    private void changeTimePeriodParam(String units, int period){
        BudgetingMain.setTimePeriod(period);
        BudgetingMain.setTimeUnits(units);
        BudgetingMain.saveToInformationFile();
    }
}
