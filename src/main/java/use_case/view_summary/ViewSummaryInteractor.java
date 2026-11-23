package use_case.view_summary;

import entity.Category;
import entity.Expense;
import entity.MonthlyBudget;
import entity.MonthlySpending;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The View Monthly Summary Interactor: works out how much has been spent and how much is left.
 */
public class ViewSummaryInteractor implements ViewSummaryInputBoundary {
    private final ViewSummaryDataAccessInterface dataAccess;
    private final ViewSummaryOutputBoundary presenter;

    public ViewSummaryInteractor(ViewSummaryDataAccessInterface dataAccess,
                                 ViewSummaryOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewSummaryInputData inputData) {
        final YearMonth month;
        try {
            month = YearMonth.parse(inputData.getMonth().trim());
        }
        catch (DateTimeParseException ex) {
            presenter.prepareFailView("Month must be in YYYY-MM format.");
            return;
        }

        final MonthlySpending spending = new MonthlySpending(month, dataAccess.getExpensesForMonth(month));

        final boolean hasBudget = dataAccess.hasBudget(month);
        double budgetLimit = 0;
        double remaining = 0;
        boolean overBudget = false;
        if (hasBudget) {
            final MonthlyBudget budget = dataAccess.getBudget(month);
            budgetLimit = budget.getLimit();
            remaining = budget.getRemaining(spending);
            overBudget = budget.isOverBudget(spending);
        }

        final Map<String, Double> categoryTotals = new LinkedHashMap<>();
        for (Map.Entry<Category, Double> entry : spending.getTotalsByCategory().entrySet()) {
            categoryTotals.put(entry.getKey().getDisplayName(), entry.getValue());
        }

        final List<Expense> sorted = new ArrayList<>(spending.getExpenses());
        sorted.sort(Comparator.comparing(Expense::getDate));
        final List<ExpenseRow> rows = new ArrayList<>();
        for (Expense expense : sorted) {
            rows.add(new ExpenseRow(expense.getDate().toString(), expense.getCategory().getDisplayName(),
                    expense.getDescription(), expense.getAmount()));
        }

        presenter.prepareSuccessView(new ViewSummaryOutputData(month.toString(), hasBudget, budgetLimit,
                spending.getTotal(), remaining, overBudget, categoryTotals, rows));
    }
}
