package interface_adapter.view_summary;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import use_case.view_summary.ExpenseRow;
import use_case.view_summary.ViewSummaryOutputBoundary;
import use_case.view_summary.ViewSummaryOutputData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Presenter for the View Monthly Summary use case.
 */
public class ViewSummaryPresenter implements ViewSummaryOutputBoundary {
    private final DashboardViewModel viewModel;

    public ViewSummaryPresenter(DashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ViewSummaryOutputData outputData) {
        final String cur = DashboardViewModel.LOCAL_CURRENCY;
        final DashboardState state = viewModel.getState();
        state.setError("");
        state.setMonth(outputData.getMonth());
        state.setSpentText(DashboardViewModel.formatMoney(cur, outputData.getTotalSpent()));
        if (outputData.hasBudget()) {
            state.setBudgetText(DashboardViewModel.formatMoney(cur, outputData.getBudget()));
            state.setRemainingText(DashboardViewModel.formatMoney(cur, outputData.getRemaining()));
        }
        else {
            state.setBudgetText("No budget set");
            state.setRemainingText("-");
        }
        state.setOverBudget(outputData.isOverBudget());

        final List<String> categoryLines = new ArrayList<>();
        for (Map.Entry<String, Double> entry : outputData.getCategoryTotals().entrySet()) {
            categoryLines.add(entry.getKey() + ": " + DashboardViewModel.formatMoney(cur, entry.getValue()));
        }
        state.setCategoryLines(categoryLines);

        final List<String[]> rows = new ArrayList<>();
        for (ExpenseRow row : outputData.getExpenses()) {
            rows.add(new String[] {row.getDate(), row.getCategory(), row.getDescription(),
                DashboardViewModel.formatMoney(cur, row.getAmount())});
        }
        state.setExpenseRows(rows);

        // Any previous currency conversion is now out of date.
        state.setConvertedText("");
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final DashboardState state = viewModel.getState();
        state.setMessage("");
        state.setError(errorMessage);
        viewModel.notifyStateChanged();
    }
}
