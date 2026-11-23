package view;

import interface_adapter.add_expense.AddExpenseController;
import interface_adapter.dashboard.DashboardViewModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.GridLayout;
import java.time.LocalDate;

/**
 * The form for the Add Expense use case: amount, category, description and date.
 */
public class AddExpensePanel extends JPanel {
    private final DashboardViewModel viewModel;
    private AddExpenseController addExpenseController;

    private final JTextField amountField = new JTextField(8);
    private final JComboBox<String> categoryBox = new JComboBox<>(DashboardViewModel.getCategoryOptions());
    private final JTextField descriptionField = new JTextField(12);
    private final JTextField dateField = new JTextField(LocalDate.now().toString(), 10);

    public AddExpensePanel(DashboardViewModel viewModel) {
        this.viewModel = viewModel;

        setLayout(new GridLayout(0, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Add an expense (" + DashboardViewModel.LOCAL_CURRENCY + ")"));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        final JButton addExpense = new JButton("Add expense");
        addExpense.addActionListener(evt -> submit());

        add(new JLabel("Amount:"));
        add(amountField);
        add(new JLabel("Category:"));
        add(categoryBox);
        add(new JLabel("Description:"));
        add(descriptionField);
        add(new JLabel("Date (YYYY-MM-DD):"));
        add(dateField);
        add(new JLabel());
        add(addExpense);
    }

    private void submit() {
        addExpenseController.execute(amountField.getText(), (String) categoryBox.getSelectedItem(),
                descriptionField.getText(), dateField.getText());
        if (viewModel.getState().getError().isEmpty()) {
            amountField.setText("");
            descriptionField.setText("");
        }
    }

    public void setAddExpenseController(AddExpenseController controller) {
        this.addExpenseController = controller;
    }
}
