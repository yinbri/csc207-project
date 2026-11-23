package use_case.add_expense;

/**
 * Input Boundary for the Add Expense use case.
 */
public interface AddExpenseInputBoundary {
    /**
     * Records a new expense.
     * @param inputData the expense details entered by the user
     */
    void execute(AddExpenseInputData inputData);
}
