package use_case.set_budget;

/**
 * Input Data for the Set Budget use case.
 */
public class SetBudgetInputData {
    private final String month;
    private final String limit;

    public SetBudgetInputData(String month, String limit) {
        this.month = month;
        this.limit = limit;
    }

    public String getMonth() {
        return month;
    }

    public String getLimit() {
        return limit;
    }
}
