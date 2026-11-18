package data_access;

import entity.Category;
import entity.Expense;
import entity.MonthlyBudget;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryBudgetDataAccessObjectTest {
    private static final YearMonth NOV = YearMonth.of(2026, 11);

    @Test
    void returnsOnlyExpensesInTheRequestedMonth() {
        final InMemoryBudgetDataAccessObject dao = new InMemoryBudgetDataAccessObject();
        dao.saveExpense(new Expense("1", 20, Category.FOOD, "Lunch", LocalDate.of(2026, 11, 5)));
        dao.saveExpense(new Expense("2", 900, Category.RENT, "Rent", LocalDate.of(2026, 10, 1)));

        final List<Expense> november = dao.getExpensesForMonth(NOV);
        assertEquals(1, november.size());
        assertEquals("1", november.get(0).getId());
        assertTrue(dao.getExpensesForMonth(NOV.plusMonths(1)).isEmpty());
    }

    @Test
    void savingABudgetReplacesThePreviousOne() {
        final InMemoryBudgetDataAccessObject dao = new InMemoryBudgetDataAccessObject();
        assertFalse(dao.hasBudget(NOV));
        assertNull(dao.getBudget(NOV));

        dao.saveBudget(new MonthlyBudget(NOV, 1500));
        dao.saveBudget(new MonthlyBudget(NOV, 1800));
        assertTrue(dao.hasBudget(NOV));
        assertEquals(1800, dao.getBudget(NOV).getLimit());
        assertFalse(dao.hasBudget(NOV.minusMonths(1)));
    }
}
