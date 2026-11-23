package use_case.set_budget;

import entity.MonthlyBudget;

/**
 * Data access needed by the Set Budget use case.
 */
public interface SetBudgetDataAccessInterface {
    /**
     * Saves a budget, replacing any existing budget for the same month.
     * @param budget the budget to save
     */
    void saveBudget(MonthlyBudget budget);
}
