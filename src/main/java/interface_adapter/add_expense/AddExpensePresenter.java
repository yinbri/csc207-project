package interface_adapter.add_expense;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import use_case.add_expense.AddExpenseOutputBoundary;
import use_case.add_expense.AddExpenseOutputData;

/**
 * Presenter for the Add Expense use case.
 */
public class AddExpensePresenter implements AddExpenseOutputBoundary {
    private final DashboardViewModel viewModel;

    public AddExpensePresenter(DashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(AddExpenseOutputData outputData) {
        final DashboardState state = viewModel.getState();
        state.setError("");
        state.setMessage("Added " + DashboardViewModel.formatMoney(DashboardViewModel.LOCAL_CURRENCY,
                outputData.getAmount()) + " for " + outputData.getCategory() + " on " + outputData.getDate() + ".");
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
