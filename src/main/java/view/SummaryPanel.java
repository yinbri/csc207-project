package view;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.dashboard.DashboardViewModelListener;
import interface_adapter.view_summary.ViewSummaryController;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * The View Monthly Summary part of the dashboard: pick a month, then see that month's expenses,
 * budget, remaining amount, and category totals.
 */
public class SummaryPanel extends JPanel implements DashboardViewModelListener {
    private static final Color OK_GREEN = new Color(0, 128, 0);
    private static final Color OVER_RED = new Color(200, 0, 0);

    private final DashboardViewModel viewModel;
    private ViewSummaryController viewSummaryController;

    private final JTextField monthField = new JTextField(8);
    private final JLabel budgetLabel = new JLabel();
    private final JLabel spentLabel = new JLabel();
    private final JLabel remainingLabel = new JLabel();
    private final JTextArea categoryArea = new JTextArea(7, 22);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[] {"Date", "Category", "Description", "Amount"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    /**
     * Creates the panel and redraws it whenever the dashboard state changes.
     * @param viewModel the dashboard view model
     */
    public SummaryPanel(DashboardViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addListener(this);

        setLayout(new BorderLayout(10, 10));

        final JButton showMonth = new JButton("Show month");
        showMonth.addActionListener(evt -> {
            if (viewSummaryController != null) {
                viewSummaryController.execute(monthField.getText());
            }
        });

        final JPanel monthPanel = new JPanel();
        monthPanel.add(new JLabel("Month (YYYY-MM):"));
        monthPanel.add(monthField);
        monthPanel.add(showMonth);
        add(monthPanel, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
        add(buildTotals(), BorderLayout.EAST);
    }

    private JPanel buildTotals() {
        categoryArea.setEditable(false);
        remainingLabel.setFont(remainingLabel.getFont().deriveFont(Font.BOLD));

        final JPanel totals = new JPanel(new GridLayout(0, 2, 5, 5));
        totals.add(new JLabel("Budget:"));
        totals.add(budgetLabel);
        totals.add(new JLabel("Spent:"));
        totals.add(spentLabel);
        totals.add(new JLabel("Remaining:"));
        totals.add(remainingLabel);

        final JPanel summary = new JPanel(new BorderLayout(0, 8));
        summary.setPreferredSize(new Dimension(260, 0));
        summary.add(titled(totals, "This month"), BorderLayout.NORTH);
        summary.add(titled(new JScrollPane(categoryArea), "By category"), BorderLayout.CENTER);
        return summary;
    }

    private static JPanel titled(java.awt.Component content, String title) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Connects the Show month button to the use case.
     * @param controller the view summary controller
     */
    public void setController(ViewSummaryController controller) {
        this.viewSummaryController = controller;
    }

    @Override
    public void stateChanged() {
        final DashboardState state = viewModel.getState();
        monthField.setText(state.getMonth());
        budgetLabel.setText(state.getBudgetText());
        spentLabel.setText(state.getSpentText());
        remainingLabel.setText(state.getRemainingText());
        remainingLabel.setForeground(state.isOverBudget() ? OVER_RED : OK_GREEN);

        tableModel.setRowCount(0);
        for (String[] row : state.getExpenseRows()) {
            tableModel.addRow(row);
        }
        categoryArea.setText(String.join("\n", state.getCategoryLines()));
    }
}
