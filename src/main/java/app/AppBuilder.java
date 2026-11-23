package app;

import data_access.FileBudgetDataAccessObject;
import interface_adapter.add_expense.AddExpenseController;
import interface_adapter.add_expense.AddExpensePresenter;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.set_budget.SetBudgetController;
import interface_adapter.set_budget.SetBudgetPresenter;
import use_case.add_expense.AddExpenseInteractor;
import use_case.set_budget.SetBudgetInteractor;
import view.DashboardView;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Builds the application by creating each object and wiring them together (the "Main component"
 * from Clean Architecture). Each addXUseCase method added later plugs in one use case.
 */
public class AppBuilder {
    private final FileBudgetDataAccessObject budgetDataAccess;

    private DashboardViewModel dashboardViewModel;
    private DashboardView dashboardView;

    /**
     * Creates the builder.
     * @param dataFilePath where budget data is saved
     */
    public AppBuilder(String dataFilePath) {
        this.budgetDataAccess = new FileBudgetDataAccessObject(dataFilePath);
    }

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
     * Adds the Add Expense use case.
     * @return this builder
     */
    public AppBuilder addAddExpenseUseCase() {
        final AddExpenseInteractor interactor = new AddExpenseInteractor(budgetDataAccess,
                new AddExpensePresenter(dashboardViewModel));
        dashboardView.getAddExpensePanel().setAddExpenseController(new AddExpenseController(interactor));
        return this;
    }

    /**
     * Adds the Set Monthly Budget use case.
     * @return this builder
     */
    public AppBuilder addSetBudgetUseCase() {
        final SetBudgetInteractor interactor = new SetBudgetInteractor(budgetDataAccess,
                new SetBudgetPresenter(dashboardViewModel));
        dashboardView.getBudgetPanel().setSetBudgetController(new SetBudgetController(interactor));
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
