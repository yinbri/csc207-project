package use_case.set_budget;

/**
 * Output Boundary for the Set Budget use case.
 */
public interface SetBudgetOutputBoundary {
    /**
     * Prepares the view after the budget was saved.
     * @param outputData the saved budget
     */
    void prepareSuccessView(SetBudgetOutputData outputData);

    /**
     * Prepares the view when the budget could not be saved.
     * @param errorMessage why it failed
     */
    void prepareFailView(String errorMessage);
}
