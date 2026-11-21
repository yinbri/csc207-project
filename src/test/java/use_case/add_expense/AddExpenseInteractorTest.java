package use_case.add_expense;

import data_access.InMemoryBudgetDataAccessObject;
import entity.Expense;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class AddExpenseInteractorTest {

    /** Runs the interactor and asserts it fails with the given message. */
    private void assertFails(AddExpenseInputData input, String expectedError) {
        final InMemoryBudgetDataAccessObject dao = new InMemoryBudgetDataAccessObject();
        new AddExpenseInteractor(dao, new AddExpenseOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExpenseOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(expectedError, errorMessage);
            }
        }).execute(input);
        assertTrue(dao.getExpensesForMonth(YearMonth.of(2026, 11)).isEmpty());
    }

    @Test
    void successSavesExpense() {
        final InMemoryBudgetDataAccessObject dao = new InMemoryBudgetDataAccessObject();
        final AddExpenseOutputBoundary presenter = new AddExpenseOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExpenseOutputData outputData) {
                assertEquals(12.5, outputData.getAmount());
                assertEquals("Food", outputData.getCategory());
                assertEquals("2026-11-03", outputData.getDate());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };
        new AddExpenseInteractor(dao, presenter)
                .execute(new AddExpenseInputData(" 12.50 ", "Food", " Groceries ", "2026-11-03"));

        final List<Expense> saved = dao.getExpensesForMonth(YearMonth.of(2026, 11));
        assertEquals(1, saved.size());
        assertEquals("Groceries", saved.get(0).getDescription());
    }

    @Test
    void failsWhenAmountNotANumber() {
        assertFails(new AddExpenseInputData("abc", "Food", "", "2026-11-03"),
                "Amount must be a number, e.g. 12.50");
    }

    @Test
    void failsWhenAmountNotPositive() {
        assertFails(new AddExpenseInputData("-3", "Food", "", "2026-11-03"),
                "Amount must be greater than zero.");
    }

    @Test
    void failsWhenCategoryUnknown() {
        assertFails(new AddExpenseInputData("3", "Pets", "", "2026-11-03"),
                "Please choose a valid category.");
    }

    @Test
    void failsWhenDateInvalid() {
        assertFails(new AddExpenseInputData("3", "Food", "", "Nov 3"),
                "Date must be in YYYY-MM-DD format.");
    }
}
