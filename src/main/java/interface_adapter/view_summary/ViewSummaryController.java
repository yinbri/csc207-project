package interface_adapter.view_summary;

import use_case.view_summary.ViewSummaryInputBoundary;
import use_case.view_summary.ViewSummaryInputData;

/**
 * Controller for the View Monthly Summary use case.
 */
public class ViewSummaryController {
    private final ViewSummaryInputBoundary interactor;

    public ViewSummaryController(ViewSummaryInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Starts the View Monthly Summary use case.
     * @param month the month as typed (YYYY-MM)
     */
    public void execute(String month) {
        interactor.execute(new ViewSummaryInputData(month));
    }
}
