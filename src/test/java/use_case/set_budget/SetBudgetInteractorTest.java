package use_case.set_budget;

import data_access.InMemoryBudgetDataAccessObject;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class SetBudgetInteractorTest {
    private static final YearMonth NOV = YearMonth.of(2026, 11);

    private void assertFails(SetBudgetInputData input, String expectedError) {
        final InMemoryBudgetDataAccessObject dao = new InMemoryBudgetDataAccessObject();
        new SetBudgetInteractor(dao, new SetBudgetOutputBoundary() {
            @Override
            public void prepareSuccessView(SetBudgetOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(expectedError, errorMessage);
            }
        }).execute(input);
        assertFalse(dao.hasBudget(NOV));
    }

    @Test
    void successSavesAndReplacesBudget() {
        final InMemoryBudgetDataAccessObject dao = new InMemoryBudgetDataAccessObject();
        final SetBudgetOutputBoundary presenter = new SetBudgetOutputBoundary() {
            @Override
            public void prepareSuccessView(SetBudgetOutputData outputData) {
                assertEquals("2026-11", outputData.getMonth());
                assertTrue(outputData.getLimit() > 0);
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };
        final SetBudgetInteractor interactor = new SetBudgetInteractor(dao, presenter);
        interactor.execute(new SetBudgetInputData("2026-11", "1500"));
        interactor.execute(new SetBudgetInputData("2026-11", "1800"));
        assertEquals(1800, dao.getBudget(NOV).getLimit());
    }

    @Test
    void failsWhenMonthInvalid() {
        assertFails(new SetBudgetInputData("November", "1500"), "Month must be in YYYY-MM format.");
    }

    @Test
    void failsWhenLimitNotANumber() {
        assertFails(new SetBudgetInputData("2026-11", "lots"), "Budget must be a number, e.g. 1500");
    }

    @Test
    void failsWhenLimitNotPositive() {
        assertFails(new SetBudgetInputData("2026-11", "0"), "Budget must be greater than zero.");
    }
}
