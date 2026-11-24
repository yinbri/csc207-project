package use_case.convert_currency;

import java.util.Map;

/**
 * Output Data for the View Spending in Home Currency use case. All amounts are in the home currency.
 */
public class ConvertCurrencyOutputData {
    private final String localCurrency;
    private final String homeCurrency;
    private final double rate;
    private final boolean hasBudget;
    private final double budget;
    private final double totalSpent;
    private final double remaining;
    private final Map<String, Double> categoryTotals;

    public ConvertCurrencyOutputData(String localCurrency, String homeCurrency, double rate, boolean hasBudget,
                                     double budget, double totalSpent, double remaining,
                                     Map<String, Double> categoryTotals) {
        this.localCurrency = localCurrency;
        this.homeCurrency = homeCurrency;
        this.rate = rate;
        this.hasBudget = hasBudget;
        this.budget = budget;
        this.totalSpent = totalSpent;
        this.remaining = remaining;
        this.categoryTotals = categoryTotals;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public String getHomeCurrency() {
        return homeCurrency;
    }

    public double getRate() {
        return rate;
    }

    public boolean hasBudget() {
        return hasBudget;
    }

    public double getBudget() {
        return budget;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public double getRemaining() {
        return remaining;
    }

    public Map<String, Double> getCategoryTotals() {
        return categoryTotals;
    }
}
