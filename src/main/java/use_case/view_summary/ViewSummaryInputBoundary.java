package use_case.view_summary;

/**
 * Input Boundary for the View Monthly Summary use case.
 */
public interface ViewSummaryInputBoundary {
    /**
     * Shows the budget, spending and remaining money for a month.
     * @param inputData the month to summarize
     */
    void execute(ViewSummaryInputData inputData);
}
