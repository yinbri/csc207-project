package view;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.dashboard.DashboardViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * The main screen. Panels that are still empty placeholders are replaced by each use case
 * with its own panel.
 */
public class DashboardView extends JPanel implements DashboardViewModelListener {
    private static final Color OK_GREEN = new Color(0, 128, 0);
    private static final Color OVER_RED = new Color(200, 0, 0);

    private final DashboardViewModel viewModel;

    private final AddExpensePanel addExpensePanel;
    private final BudgetPanel budgetPanel;
    private final SummaryPanel summaryPanel;
    private final CurrencyPanel currencyPanel;

    private final JLabel messageLabel = new JLabel(" ");

    public DashboardView(DashboardViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addListener(this);
        this.addExpensePanel = new AddExpensePanel(viewModel);
        this.budgetPanel = new BudgetPanel();
        this.summaryPanel = new SummaryPanel(viewModel);
        this.currencyPanel = new CurrencyPanel(() -> viewModel.getState().getMonth());

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildControls(), BorderLayout.WEST);
        add(summaryPanel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        final JLabel title = new JLabel("Budget Abroad");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        final JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JPanel buildControls() {
        final JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.add(budgetPanel);
        controls.add(Box.createVerticalStrut(10));
        controls.add(addExpensePanel);
        controls.add(Box.createVerticalStrut(10));
        controls.add(currencyPanel);
        controls.add(Box.createVerticalGlue());
        return controls;
    }

    public SummaryPanel getSummaryPanel() {
        return summaryPanel;
    }

    public AddExpensePanel getAddExpensePanel() {
        return addExpensePanel;
    }

    public BudgetPanel getBudgetPanel() {
        return budgetPanel;
    }

    public CurrencyPanel getCurrencyPanel() {
        return currencyPanel;
    }

    @Override
    public void stateChanged() {
        final DashboardState state = viewModel.getState();
        currencyPanel.showConvertedText(state.getConvertedText());
        if (!state.getError().isEmpty()) {
            messageLabel.setForeground(OVER_RED);
            messageLabel.setText(state.getError());
        }
        else {
            messageLabel.setForeground(OK_GREEN);
            messageLabel.setText(state.getMessage().isEmpty() ? " " : state.getMessage());
        }
    }
}
