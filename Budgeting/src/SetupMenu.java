public class SetupMenu {

    public SetupMenu(){

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

    private void changeUsername(){
        String tempUsername;
        String username = BudgetingMain.getUsername();

        System.out.print("Your username is " + username + ", what is your new username? ");
        tempUsername = BudgetingMain.validateInputString();
        if (!username.equals(tempUsername)){
            username = tempUsername;
            BudgetingMain.setUsername(username);
            BudgetingMain.saveToInformationFile();
            System.out.println("Your username is now "+ BudgetingMain.getUsername());
        } else {
            System.out.println("New username is the same as the original, returning to setup page");
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
            budget = (float) amount;
            BudgetingMain.setBudget(budget);
            BudgetingMain.saveToInformationFile();
            System.out.println("New budget is " + budget);
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
}
