package use_case.set_budget;

/**
 * Output Data for the Set Budget use case.
 */
public class SetBudgetOutputData {
    private final String month;
    private final double limit;

    public SetBudgetOutputData(String month, double limit) {
        this.month = month;
        this.limit = limit;
    }

    public String getMonth() {
        return month;
    }

    public double getLimit() {
        return limit;
    }
}
