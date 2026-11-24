package use_case.convert_currency;

import entity.Expense;
import entity.MonthlyBudget;

import java.time.YearMonth;
import java.util.List;

/**
 * Budget data needed by the View Spending in Home Currency use case.
 */
public interface ConvertCurrencyDataAccessInterface {
    /**
     * Returns the expenses recorded in a month.
     * @param month the month
     * @return the month's expenses (possibly empty)
     */
    List<Expense> getExpensesForMonth(YearMonth month);

    /**
     * Returns whether a budget has been set for a month.
     * @param month the month
     * @return true if a budget exists
     */
    boolean hasBudget(YearMonth month);

    /**
     * Returns the budget for a month. Only call this if hasBudget(month) is true.
     * @param month the month
     * @return the budget
     */
    MonthlyBudget getBudget(YearMonth month);
}
