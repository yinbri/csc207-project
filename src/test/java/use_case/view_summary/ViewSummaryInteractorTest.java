package use_case.view_summary;

import data_access.InMemoryBudgetDataAccessObject;
import entity.Category;
import entity.Expense;
import entity.MonthlyBudget;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ViewSummaryInteractorTest {
    private static final YearMonth NOV = YearMonth.of(2026, 11);

    private static InMemoryBudgetDataAccessObject daoWithExpenses() {
        final InMemoryBudgetDataAccessObject dao = new InMemoryBudgetDataAccessObject();
        dao.saveExpense(new Expense("1", 900, Category.RENT, "Rent", LocalDate.of(2026, 11, 20)));
        dao.saveExpense(new Expense("2", 40, Category.FOOD, "Groceries", LocalDate.of(2026, 11, 2)));
        dao.saveExpense(new Expense("3", 99, Category.FOOD, "Last month", LocalDate.of(2026, 10, 2)));
        return dao;
    }

    /** A presenter that fails the test if the use case fails. */
    private abstract static class SuccessPresenter implements ViewSummaryOutputBoundary {
        @Override
        public void prepareFailView(String errorMessage) {
            fail("Use case failure is unexpected: " + errorMessage);
        }
    }

    @Test
    void summaryWithBudget() {
        final InMemoryBudgetDataAccessObject dao = daoWithExpenses();
        dao.saveBudget(new MonthlyBudget(NOV, 1000));

        new ViewSummaryInteractor(dao, new SuccessPresenter() {
            @Override
            public void prepareSuccessView(ViewSummaryOutputData out) {
                assertEquals("2026-11", out.getMonth());
                assertTrue(out.hasBudget());
                assertEquals(1000, out.getBudget());
                assertEquals(940, out.getTotalSpent());
                assertEquals(60, out.getRemaining());
                assertFalse(out.isOverBudget());
                assertEquals(40.0, out.getCategoryTotals().get("Food"));
                assertEquals(0.0, out.getCategoryTotals().get("Tuition"));
                // sorted by date
                assertEquals(2, out.getExpenses().size());
                final ExpenseRow first = out.getExpenses().get(0);
                assertEquals("2026-11-02", first.getDate());
                assertEquals("Food", first.getCategory());
                assertEquals("Groceries", first.getDescription());
                assertEquals(40, first.getAmount());
            }
        }).execute(new ViewSummaryInputData("2026-11"));
    }

    @Test
    void summaryOverBudget() {
        final InMemoryBudgetDataAccessObject dao = daoWithExpenses();
        dao.saveBudget(new MonthlyBudget(NOV, 500));

        new ViewSummaryInteractor(dao, new SuccessPresenter() {
            @Override
            public void prepareSuccessView(ViewSummaryOutputData out) {
                assertEquals(-440, out.getRemaining());
                assertTrue(out.isOverBudget());
            }
        }).execute(new ViewSummaryInputData("2026-11"));
    }

    @Test
    void summaryWithoutBudget() {
        new ViewSummaryInteractor(daoWithExpenses(), new SuccessPresenter() {
            @Override
            public void prepareSuccessView(ViewSummaryOutputData out) {
                assertFalse(out.hasBudget());
                assertEquals(940, out.getTotalSpent());
                assertFalse(out.isOverBudget());
            }
        }).execute(new ViewSummaryInputData("2026-11"));
    }

    @Test
    void failsWhenMonthInvalid() {
        new ViewSummaryInteractor(daoWithExpenses(), new ViewSummaryOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewSummaryOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Month must be in YYYY-MM format.", errorMessage);
            }
        }).execute(new ViewSummaryInputData("11/2026"));
    }
}
