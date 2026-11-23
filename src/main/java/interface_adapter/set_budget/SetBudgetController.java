package interface_adapter.set_budget;

import use_case.set_budget.SetBudgetInputBoundary;
import use_case.set_budget.SetBudgetInputData;

/**
 * Controller for the Set Budget use case.
 */
public class SetBudgetController {
    private final SetBudgetInputBoundary interactor;

    public SetBudgetController(SetBudgetInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Starts the Set Budget use case.
     * @param month the month as typed (YYYY-MM)
     * @param limit the budget as typed
     */
    public void execute(String month, String limit) {
        interactor.execute(new SetBudgetInputData(month, limit));
    }
}
