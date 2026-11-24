package use_case.convert_currency;

import entity.Category;
import entity.ExchangeRate;
import entity.MonthlyBudget;
import entity.MonthlySpending;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The View Spending in Home Currency Interactor: converts a month's budget and spending
 * from the local currency into the student's home currency using a live exchange rate.
 */
public class ConvertCurrencyInteractor implements ConvertCurrencyInputBoundary {
    private final ConvertCurrencyDataAccessInterface budgetDataAccess;
    private final ExchangeRateDataAccessInterface exchangeRateDataAccess;
    private final ConvertCurrencyOutputBoundary presenter;
    private final String localCurrency;

    /**
     * Creates the interactor.
     * @param budgetDataAccess where expenses and budgets are stored
     * @param exchangeRateDataAccess where exchange rates come from
     * @param presenter the output boundary
     * @param localCurrency ISO code of the currency expenses are recorded in, e.g. "CAD"
     */
    public ConvertCurrencyInteractor(ConvertCurrencyDataAccessInterface budgetDataAccess,
                                     ExchangeRateDataAccessInterface exchangeRateDataAccess,
                                     ConvertCurrencyOutputBoundary presenter,
                                     String localCurrency) {
        this.budgetDataAccess = budgetDataAccess;
        this.exchangeRateDataAccess = exchangeRateDataAccess;
        this.presenter = presenter;
        this.localCurrency = localCurrency;
    }

    @Override
    public void execute(ConvertCurrencyInputData inputData) {
        final YearMonth month;
        try {
            month = YearMonth.parse(inputData.getMonth().trim());
        }
        catch (DateTimeParseException ex) {
            presenter.prepareFailView("Month must be in YYYY-MM format.");
            return;
        }

        final String homeCurrency = inputData.getHomeCurrency().trim().toUpperCase();
        if (!homeCurrency.matches("[A-Z]{3}")) {
            presenter.prepareFailView("Home currency must be a 3-letter code, e.g. CNY.");
            return;
        }
        if (homeCurrency.equals(localCurrency)) {
            presenter.prepareFailView("Your home currency is the same as your local currency ("
                    + localCurrency + ").");
            return;
        }

        final ExchangeRate rate;
        try {
            rate = exchangeRateDataAccess.getRate(localCurrency, homeCurrency);
        }
        catch (ExchangeRateUnavailableException ex) {
            presenter.prepareFailView("Could not get exchange rate: " + ex.getMessage());
            return;
        }

        final MonthlySpending spending = new MonthlySpending(month,
                budgetDataAccess.getExpensesForMonth(month));

        final boolean hasBudget = budgetDataAccess.hasBudget(month);
        double budget = 0;
        double remaining = 0;
        if (hasBudget) {
            final MonthlyBudget monthlyBudget = budgetDataAccess.getBudget(month);
            budget = rate.convert(monthlyBudget.getLimit());
            remaining = rate.convert(monthlyBudget.getRemaining(spending));
        }

        final Map<String, Double> categoryTotals = new LinkedHashMap<>();
        for (Map.Entry<Category, Double> entry : spending.getTotalsByCategory().entrySet()) {
            categoryTotals.put(entry.getKey().getDisplayName(), rate.convert(entry.getValue()));
        }

        presenter.prepareSuccessView(new ConvertCurrencyOutputData(localCurrency, homeCurrency,
                rate.getRate(), hasBudget, budget, rate.convert(spending.getTotal()), remaining, categoryTotals));
    }
}
