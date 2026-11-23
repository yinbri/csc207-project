package use_case.view_summary;

/**
 * Output Boundary for the View Monthly Summary use case.
 */
public interface ViewSummaryOutputBoundary {
    /**
     * Prepares the view to show a month's summary.
     * @param outputData the summary
     */
    void prepareSuccessView(ViewSummaryOutputData outputData);

    /**
     * Prepares the view when the summary could not be produced.
     * @param errorMessage why it failed
     */
    void prepareFailView(String errorMessage);
}
