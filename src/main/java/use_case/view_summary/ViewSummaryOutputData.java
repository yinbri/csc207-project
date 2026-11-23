package use_case.view_summary;

import java.util.List;
import java.util.Map;

/**
 * Output Data for the View Monthly Summary use case.
 */
public class ViewSummaryOutputData {
    private final String month;
    private final boolean hasBudget;
    private final double budget;
    private final double totalSpent;
    private final double remaining;
    private final boolean overBudget;
    private final Map<String, Double> categoryTotals;
    private final List<ExpenseRow> expenses;

    public ViewSummaryOutputData(String month, boolean hasBudget, double budget, double totalSpent,
                                 double remaining, boolean overBudget,
                                 Map<String, Double> categoryTotals, List<ExpenseRow> expenses) {
        this.month = month;
        this.hasBudget = hasBudget;
        this.budget = budget;
        this.totalSpent = totalSpent;
        this.remaining = remaining;
        this.overBudget = overBudget;
        this.categoryTotals = categoryTotals;
        this.expenses = expenses;
    }

    public String getMonth() {
        return month;
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

    public boolean isOverBudget() {
        return overBudget;
    }

    public Map<String, Double> getCategoryTotals() {
        return categoryTotals;
    }

    public List<ExpenseRow> getExpenses() {
        return expenses;
    }
}
