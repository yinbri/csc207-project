package use_case.add_expense;

/**
 * Output Boundary for the Add Expense use case.
 */
public interface AddExpenseOutputBoundary {
    /**
     * Prepares the view after an expense was recorded.
     * @param outputData the recorded expense
     */
    void prepareSuccessView(AddExpenseOutputData outputData);

    /**
     * Prepares the view when the expense could not be recorded.
     * @param errorMessage why it failed
     */
    void prepareFailView(String errorMessage);
}
