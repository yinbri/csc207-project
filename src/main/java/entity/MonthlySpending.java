package entity;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * All of the expenses for one month, with totals overall and per category.
 */
public class MonthlySpending {
    private final YearMonth month;
    private final List<Expense> expenses = new ArrayList<>();

    /**
     * Creates the spending record for a month. Expenses outside the month are ignored.
     * @param month the month
     * @param expenses candidate expenses
     */
    public MonthlySpending(YearMonth month, List<Expense> expenses) {
        this.month = month;
        for (Expense expense : expenses) {
            if (expense.isIn(month)) {
                this.expenses.add(expense);
            }
        }
    }

    public YearMonth getMonth() {
        return month;
    }

    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(expenses);
    }

    /**
     * Returns the total amount spent this month.
     * @return the total
     */
    public double getTotal() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    /**
     * Returns the amount spent in each category, including categories with no spending.
     * @return a map from category to amount, in category order
     */
    public Map<Category, Double> getTotalsByCategory() {
        final Map<Category, Double> totals = new EnumMap<>(Category.class);
        for (Category category : Category.values()) {
            totals.put(category, 0.0);
        }
        for (Expense expense : expenses) {
            totals.put(expense.getCategory(), totals.get(expense.getCategory()) + expense.getAmount());
        }
        return totals;
    }
}
