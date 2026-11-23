package use_case.view_summary;

/**
 * Input Data for the View Monthly Summary use case.
 */
public class ViewSummaryInputData {
    private final String month;

    public ViewSummaryInputData(String month) {
        this.month = month;
    }

    public String getMonth() {
        return month;
    }
}
