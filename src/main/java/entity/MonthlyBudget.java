package entity;

import java.time.YearMonth;

/**
 * The spending limit a student has set for one month, in their local currency.
 */
public class MonthlyBudget {
    private final YearMonth month;
    private final double limit;

    /**
     * Creates a budget.
     * @param month the month the budget applies to
     * @param limit the spending limit; must be positive
     * @throws IllegalArgumentException if limit is not positive
     */
    public MonthlyBudget(YearMonth month, double limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Budget must be positive.");
        }
        this.month = month;
        this.limit = limit;
    }

    public YearMonth getMonth() {
        return month;
    }

    public double getLimit() {
        return limit;
    }

    /**
     * Returns how much money is left this month (negative if over budget).
     * @param spending the month's spending
     * @return limit minus total spent
     */
    public double getRemaining(MonthlySpending spending) {
        return limit - spending.getTotal();
    }

    /**
     * Returns whether the student has spent more than their budget.
     * @param spending the month's spending
     * @return true if over budget
     */
    public boolean isOverBudget(MonthlySpending spending) {
        return getRemaining(spending) < 0;
    }
}
