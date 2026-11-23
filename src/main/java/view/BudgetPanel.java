package view;

import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.set_budget.SetBudgetController;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.GridLayout;
import java.time.YearMonth;

/**
 * The form for the Set Monthly Budget use case: a month and a spending limit.
 */
public class BudgetPanel extends JPanel {
    private SetBudgetController setBudgetController;

    private final JTextField monthField = new JTextField(YearMonth.now().toString(), 8);
    private final JTextField limitField = new JTextField(8);

    public BudgetPanel() {
        setLayout(new GridLayout(0, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Monthly budget (" + DashboardViewModel.LOCAL_CURRENCY + ")"));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        final JButton setBudget = new JButton("Set budget");
        setBudget.addActionListener(evt -> submit());

        add(new JLabel("Month (YYYY-MM):"));
        add(monthField);
        add(new JLabel("Limit:"));
        add(limitField);
        add(new JLabel());
        add(setBudget);
    }

    private void submit() {
        setBudgetController.execute(monthField.getText(), limitField.getText());
    }

    public void setSetBudgetController(SetBudgetController controller) {
        this.setBudgetController = controller;
    }
}
