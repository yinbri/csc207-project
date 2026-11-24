package app;

import data_access.ExchangeRateApiDataAccessObject;
import data_access.FileBudgetDataAccessObject;
import interface_adapter.add_expense.AddExpenseController;
import interface_adapter.add_expense.AddExpensePresenter;
import interface_adapter.convert_currency.ConvertCurrencyController;
import interface_adapter.convert_currency.ConvertCurrencyPresenter;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.set_budget.SetBudgetController;
import interface_adapter.set_budget.SetBudgetPresenter;
import interface_adapter.view_summary.ViewSummaryController;
import interface_adapter.view_summary.ViewSummaryPresenter;
import use_case.add_expense.AddExpenseInteractor;
import use_case.convert_currency.ConvertCurrencyInteractor;
import use_case.set_budget.SetBudgetInteractor;
import use_case.view_summary.ViewSummaryInteractor;
import view.DashboardView;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.time.YearMonth;

/**
 * Builds the application by creating each object and wiring them together (the "Main component"
 * from Clean Architecture). Each addXUseCase method added later plugs in one use case.
 */
public class AppBuilder {
    private final FileBudgetDataAccessObject budgetDataAccess;
    private final ExchangeRateApiDataAccessObject exchangeRateDataAccess = new ExchangeRateApiDataAccessObject();

    private DashboardViewModel dashboardViewModel;
    private DashboardView dashboardView;
    private ViewSummaryController viewSummaryController;

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
     * Adds the View Monthly Summary use case.
     * @return this builder
     */
    public AppBuilder addViewSummaryUseCase() {
        final ViewSummaryInteractor interactor = new ViewSummaryInteractor(budgetDataAccess,
                new ViewSummaryPresenter(dashboardViewModel));
        viewSummaryController = new ViewSummaryController(interactor);
        dashboardView.getSummaryPanel().setController(viewSummaryController);
        return this;
    }

    /**
     * Adds the View Spending in Home Currency use case.
     * @return this builder
     */
    public AppBuilder addConvertCurrencyUseCase() {
        final ConvertCurrencyInteractor interactor = new ConvertCurrencyInteractor(budgetDataAccess,
                exchangeRateDataAccess, new ConvertCurrencyPresenter(dashboardViewModel),
                DashboardViewModel.LOCAL_CURRENCY);
        dashboardView.getCurrencyPanel().setConvertCurrencyController(new ConvertCurrencyController(interactor));
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
        if (viewSummaryController != null) {
            viewSummaryController.execute(YearMonth.now().toString());
        }
        return application;
    }
}
