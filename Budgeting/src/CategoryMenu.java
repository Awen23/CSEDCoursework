import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class CategoryMenu {
    JFrame setupFrame;

    public CategoryMenu(){

    }

    public CategoryMenu(JFrame setupFrame){
        this.setupFrame = setupFrame;
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

    public void draw(){
        JFrame categoryFrame = new JFrame();
        categoryFrame.setSize(800,500);
        categoryFrame.setTitle("Category Settings");
        categoryFrame.setLocationRelativeTo(null);
        categoryFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        categoryFrame.addWindowListener(new WindowAdapter() { //allows saving when the window is closed via the x
            @Override
            public void windowClosing(WindowEvent event) {
                BudgetingMain.saveToInformationFile();
                setupFrame.setVisible(true);
                categoryFrame.dispose();
            }
        });

        categoryFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JTextArea tCategoryList = new JTextArea();
        tCategoryList.setEditable(false);
        tCategoryList.setLineWrap(true);
        for (String category:BudgetingMain.getCategories()) {
            tCategoryList.append(category + "\n");
        }
        JScrollPane categoryScroll = new JScrollPane(tCategoryList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.ipady = 300;
        c.ipadx = 300;
        c.fill = GridBagConstraints.BOTH;
        categoryFrame.add(categoryScroll, c);

        JButton bAdd = new JButton("Add Category");
        bAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField textInput = new JTextField();
                textInput.setPreferredSize(new Dimension(300,25));
                JPanel panel = new JPanel();
                panel.add(textInput);
                int result = JOptionPane.showConfirmDialog(null, panel, "Input new category name", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION){
                    String tempCategory = textInput.getText().trim();
                    if (BudgetingMain.validateString(tempCategory)) {
                        tempCategory = tempCategory.substring(0, 1).toUpperCase() + tempCategory.substring(1).toLowerCase();
                        BudgetingMain.addCategoryParameter(tempCategory, true);
                        tCategoryList.setText("");
                        for (String category:BudgetingMain.getCategories()) {
                            tCategoryList.append(category + "\n");
                        }
                    } else{
                        JOptionPane.showConfirmDialog(null, "Category name uses invalid characters", "", JOptionPane.DEFAULT_OPTION);
                    }
                }
            }
        });
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.fill = GridBagConstraints.BOTH;
        categoryFrame.add(bAdd, c);

        JButton bRemove = new JButton("Remove Category");
        bRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> comboBox = new JComboBox(BudgetingMain.getCategories().toArray());
                comboBox.setEditable(false);
                JPanel panel = new JPanel();
                panel.add(comboBox);
                int result = JOptionPane.showConfirmDialog(null, panel, "Select category to remove", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION){
                    BudgetingMain.removeCategoryParameter(comboBox.getSelectedItem().toString(), true);
                    tCategoryList.setText("");
                    for (String category:BudgetingMain.getCategories()) {
                        tCategoryList.append(category + "\n");
                    }
                }
            }
        });
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        categoryFrame.add(bRemove, c);


        categoryFrame.setVisible(true);
    }

    private void removeCategory(){

        System.out.print("Category to remove: ");
        BudgetingMain.removeCategoryParameter(BudgetingMain.getInputFromConsole(), false);
    }

    private void addCategory(){
        String tempCategory;

        System.out.print("New category name: ");
        tempCategory = BudgetingMain.validateInputString();
        tempCategory = tempCategory.substring(0,1).toUpperCase() + tempCategory.substring(1).toLowerCase();
        BudgetingMain.addCategoryParameter(tempCategory, false);
    }
}
