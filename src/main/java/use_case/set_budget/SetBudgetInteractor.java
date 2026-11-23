package use_case.set_budget;

import entity.MonthlyBudget;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

/**
 * The Set Budget Interactor: validates and saves a monthly budget.
 */
public class SetBudgetInteractor implements SetBudgetInputBoundary {
    private final SetBudgetDataAccessInterface dataAccess;
    private final SetBudgetOutputBoundary presenter;

    public SetBudgetInteractor(SetBudgetDataAccessInterface dataAccess,
                               SetBudgetOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SetBudgetInputData inputData) {
        final YearMonth month;
        try {
            month = YearMonth.parse(inputData.getMonth().trim());
        }
        catch (DateTimeParseException ex) {
            presenter.prepareFailView("Month must be in YYYY-MM format.");
            return;
        }

        final double limit;
        try {
            limit = Double.parseDouble(inputData.getLimit().trim());
        }
        catch (NumberFormatException ex) {
            presenter.prepareFailView("Budget must be a number, e.g. 1500");
            return;
        }
        if (limit <= 0) {
            presenter.prepareFailView("Budget must be greater than zero.");
            return;
        }

        dataAccess.saveBudget(new MonthlyBudget(month, limit));
        presenter.prepareSuccessView(new SetBudgetOutputData(month.toString(), limit));
    }
}
