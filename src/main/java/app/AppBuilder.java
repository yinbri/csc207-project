package app;

import interface_adapter.dashboard.DashboardViewModel;
import view.DashboardView;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Builds the application by creating each object and wiring them together (the "Main component"
 * from Clean Architecture). Each addXUseCase method added later plugs in one use case.
 */
public class AppBuilder {
    private DashboardViewModel dashboardViewModel;
    private DashboardView dashboardView;

    /**
     * Adds the dashboard view.
     * @return this builder
     */
    public AppBuilder addDashboardView() {
        dashboardViewModel = new DashboardViewModel();
        dashboardView = new DashboardView(dashboardViewModel);
        return this;
    }

    /**
     * Creates the application window.
     * @return the application window
     */
    public JFrame build() {
        final JFrame application = new JFrame("Budget Abroad");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(dashboardView);
        application.setSize(1100, 650);
        return application;
    }
}
