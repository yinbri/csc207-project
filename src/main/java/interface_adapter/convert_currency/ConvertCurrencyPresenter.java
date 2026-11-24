package interface_adapter.convert_currency;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import use_case.convert_currency.ConvertCurrencyOutputBoundary;
import use_case.convert_currency.ConvertCurrencyOutputData;

import java.util.Map;

/**
 * Presenter for the View Spending in Home Currency use case.
 */
public class ConvertCurrencyPresenter implements ConvertCurrencyOutputBoundary {
    private final DashboardViewModel viewModel;

    public ConvertCurrencyPresenter(DashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ConvertCurrencyOutputData outputData) {
        final String home = outputData.getHomeCurrency();
        final StringBuilder text = new StringBuilder();
        text.append(String.format("1 %s = %.4f %s%n%n", outputData.getLocalCurrency(), outputData.getRate(), home));
        if (outputData.hasBudget()) {
            text.append("Budget: ").append(DashboardViewModel.formatMoney(home, outputData.getBudget())).append('\n');
        }
        text.append("Spent: ").append(DashboardViewModel.formatMoney(home, outputData.getTotalSpent())).append('\n');
        if (outputData.hasBudget()) {
            text.append("Remaining: ").append(DashboardViewModel.formatMoney(home, outputData.getRemaining()))
                    .append('\n');
        }
        text.append('\n');
        for (Map.Entry<String, Double> entry : outputData.getCategoryTotals().entrySet()) {
            text.append(entry.getKey()).append(": ")
                    .append(DashboardViewModel.formatMoney(home, entry.getValue())).append('\n');
        }

        final DashboardState state = viewModel.getState();
        state.setError("");
        state.setMessage("Converted to " + home + " using today's exchange rate.");
        state.setConvertedText(text.toString());
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
