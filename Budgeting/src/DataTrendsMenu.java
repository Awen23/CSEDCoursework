import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataTrendsMenu {
    private JFrame mainFrame;

    public DataTrendsMenu(){

    }

    public DataTrendsMenu(JFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    public void draw(){

        JFrame trendsFrame = new JFrame();
        trendsFrame.setSize(1000,800);
        trendsFrame.setTitle("Data Trends");
        trendsFrame.setLocationRelativeTo(null);
        trendsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        trendsFrame.addWindowListener(new WindowAdapter() { //allows saving when the window is closed via the x
            @Override
            public void windowClosing(WindowEvent event) {
                mainFrame.setVisible(true);
                trendsFrame.dispose();
            }
        });

        trendsFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createLineChart("Expenses Per Day", "Date","Amount spent (£)", dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        c.gridwidth = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 500;
        c.ipadx = 800;
        c.fill = GridBagConstraints.BOTH;
        trendsFrame.add(chartPanel, c);

        JTextArea tCategoryList = new JTextArea();
        tCategoryList.setEditable(false);
        tCategoryList.setLineWrap(true);
        Map<String, Float> sorted = BudgetingMain.sortCategoriesAfter(BudgetingMain.getBudgetStart().atStartOfDay());
        for (String key:sorted.keySet()) {
            tCategoryList.append(key+": "+sorted.get(key)+"\n");
        }
        JScrollPane categoryScroll = new JScrollPane(tCategoryList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 200;
        c.ipadx = 300;
        c.fill = GridBagConstraints.BOTH;
        trendsFrame.add(categoryScroll, c);

        trendsFrame.setVisible(true);
    }


    private DefaultCategoryDataset createDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<Expenditure> expenses = BudgetingMain.getExpendituresBetween(BudgetingMain.getBudgetStart().atStartOfDay(), LocalDateTime.now());
        Map<String, Float> categoryTotals = new HashMap<>();
        float daytotal = 0;

        for (String category:BudgetingMain.getCategories()) {
            categoryTotals.put(category, new Float(0));
        }

        for (LocalDate i = BudgetingMain.getBudgetStart(); i.isBefore(LocalDate.now()); i = i.plusDays(1)) {
            //daytotal = 0;
            for (String category:BudgetingMain.getCategories()) {
                categoryTotals.put(category, new Float(0));
            }
            for (Expenditure e:expenses) {
                System.out.println(e.getDatetime()+" : "+i);
                if (e.getDatetime().toLocalDate().isEqual(i)){
                    System.out.println("yes");
                    if (categoryTotals.keySet().contains(e.getCategory())){
                        categoryTotals.put(e.getCategory(),categoryTotals.get(e.getCategory()) + e.getAmount());
                    } else {
                        categoryTotals.put(e.getCategory(), e.getAmount());
                    }
                    daytotal += e.getAmount();
                }
            }
            for (String key:categoryTotals.keySet()) {
                dataset.addValue(categoryTotals.get(key),key,i);
            }
            dataset.addValue(daytotal,"Cumulative Total",i);
        }
        return dataset;

    }
}
