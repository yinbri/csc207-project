package interface_adapter.add_expense;

import use_case.add_expense.AddExpenseInputBoundary;
import use_case.add_expense.AddExpenseInputData;

/**
 * Controller for the Add Expense use case.
 */
public class AddExpenseController {
    private final AddExpenseInputBoundary interactor;

    public AddExpenseController(AddExpenseInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Starts the Add Expense use case.
     * @param amount the amount as typed
     * @param category the chosen category name
     * @param description a short description
     * @param date the date as typed (YYYY-MM-DD)
     */
    public void execute(String amount, String category, String description, String date) {
        interactor.execute(new AddExpenseInputData(amount, category, description, date));
    }
}
