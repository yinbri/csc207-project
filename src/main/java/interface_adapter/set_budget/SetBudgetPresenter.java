package interface_adapter.set_budget;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import use_case.set_budget.SetBudgetOutputBoundary;
import use_case.set_budget.SetBudgetOutputData;

/**
 * Presenter for the Set Budget use case.
 */
public class SetBudgetPresenter implements SetBudgetOutputBoundary {
    private final DashboardViewModel viewModel;

    public SetBudgetPresenter(DashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SetBudgetOutputData outputData) {
        final DashboardState state = viewModel.getState();
        state.setError("");
        state.setMessage("Budget for " + outputData.getMonth() + " set to "
                + DashboardViewModel.formatMoney(DashboardViewModel.LOCAL_CURRENCY, outputData.getLimit()) + ".");
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
