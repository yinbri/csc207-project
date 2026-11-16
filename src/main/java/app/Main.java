package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Starts the Budget Abroad application.
 */
public class Main {
    /**
     * Builds and shows the app.
     * @param args unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JFrame application = new AppBuilder()
                    .addDashboardView()
                    .build();
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }
}
