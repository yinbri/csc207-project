package use_case.set_budget;

/**
 * Input Boundary for the Set Budget use case.
 */
public interface SetBudgetInputBoundary {
    /**
     * Sets (or replaces) the budget for a month.
     * @param inputData the month and the limit
     */
    void execute(SetBudgetInputData inputData);
}
