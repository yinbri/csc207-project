package data_access;

import entity.Expense;
import entity.MonthlyBudget;
import use_case.add_expense.AddExpenseDataAccessInterface;
import use_case.set_budget.SetBudgetDataAccessInterface;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps expenses and budgets in memory only. Useful for tests; nothing is saved.
 *
 * <p>Each use case PR makes this class implement that use case's data access interface
 * (e.g. {@code AddExpenseDataAccessInterface}). Because {@link FileBudgetDataAccessObject}
 * extends this class, it picks those interfaces up automatically.</p>
 */
public class InMemoryBudgetDataAccessObject implements AddExpenseDataAccessInterface,
        SetBudgetDataAccessInterface {

    protected final List<Expense> expenses = new ArrayList<>();
    protected final Map<YearMonth, MonthlyBudget> budgets = new HashMap<>();

    @Override
    public void saveExpense(Expense expense) {
        expenses.add(expense);
    }

    @Override
    public void saveBudget(MonthlyBudget budget) {
        budgets.put(budget.getMonth(), budget);
    }

    /**
     * Returns the expenses recorded in a month.
     * @param month the month
     * @return the month's expenses (possibly empty)
     */
    public List<Expense> getExpensesForMonth(YearMonth month) {
        final List<Expense> result = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.isIn(month)) {
                result.add(expense);
            }
        }
        return result;
    }

    /**
     * Returns whether a budget has been set for a month.
     * @param month the month
     * @return true if a budget exists
     */
    public boolean hasBudget(YearMonth month) {
        return budgets.containsKey(month);
    }

    /**
     * Returns the budget for a month. Only call this if hasBudget(month) is true.
     * @param month the month
     * @return the budget, or null if none has been set
     */
    public MonthlyBudget getBudget(YearMonth month) {
        return budgets.get(month);
    }
}
