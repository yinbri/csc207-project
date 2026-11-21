package use_case.add_expense;

import entity.Expense;

/**
 * Data access needed by the Add Expense use case.
 */
public interface AddExpenseDataAccessInterface {
    /**
     * Saves an expense.
     * @param expense the expense to save
     */
    void saveExpense(Expense expense);
}
