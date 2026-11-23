package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Starts the Budget Abroad application.
 */
public class Main {
    /**
     * Builds and shows the app.
     * @param args optional: path of the data file (default budget_data.json)
     */
    public static void main(String[] args) {
        final String dataFile = args.length > 0 ? args[0] : "budget_data.json";
        SwingUtilities.invokeLater(() -> {
            final JFrame application = new AppBuilder(dataFile)
                    .addDashboardView()
                    .addAddExpenseUseCase()
                    .addSetBudgetUseCase()
                    .addViewSummaryUseCase()
                    .build();
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }
}
