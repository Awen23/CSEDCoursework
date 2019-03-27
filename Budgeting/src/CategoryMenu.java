import java.util.ArrayList;

public class CategoryMenu {

    public CategoryMenu(){

    }

    public void categoryMenu(){
        boolean loop = true;
        String input;
        while(loop){
            System.out.println("\nAdd, Remove or view categories?: 1) add category 2) remove category 3) view categories 4) go back");
            System.out.print("Option number: ");
            input = BudgetingMain.getInputFromConsole().trim();
            if (!input.isEmpty()){
                switch (input.charAt(0)) {
                    case '1':
                        if(BudgetingMain.areYouSure("add a category")){
                            addCategory();
                            BudgetingMain.viewCategories();
                        }
                        break;
                    case '2':
                        if(BudgetingMain.areYouSure("remove a category")) {
                            removeCategory();
                            BudgetingMain.viewCategories();
                        }
                        break;
                    case '3':
                        BudgetingMain.viewCategories();
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

    private void removeCategory(){

        System.out.print("Category to remove: ");
        BudgetingMain.removeCategoryParameter(BudgetingMain.getInputFromConsole());
    }

    private void addCategory(){
        String tempCategory;

        System.out.print("New category name: ");
        tempCategory = BudgetingMain.validateInputString();
        tempCategory = tempCategory.substring(0,1).toUpperCase() + tempCategory.substring(1).toLowerCase();
        BudgetingMain.addCategoryParameter(tempCategory);
    }
}
