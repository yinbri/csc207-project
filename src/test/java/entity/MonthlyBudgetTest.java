package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MonthlyBudgetTest {
    private static final YearMonth NOV = YearMonth.of(2026, 11);

    private static MonthlySpending spent(double amount) {
        return new MonthlySpending(NOV, Arrays.asList(
                new Expense("id", amount, Category.FOOD, "desc", LocalDate.of(2026, 11, 1))));
    }

    @Test
    void storesMonthAndLimit() {
        final MonthlyBudget budget = new MonthlyBudget(NOV, 1000);
        assertEquals(NOV, budget.getMonth());
        assertEquals(1000, budget.getLimit());
    }

    @Test
    void underBudget() {
        final MonthlyBudget budget = new MonthlyBudget(NOV, 1000);
        assertEquals(600, budget.getRemaining(spent(400)));
        assertFalse(budget.isOverBudget(spent(400)));
    }

    @Test
    void exactlyAtBudgetIsNotOver() {
        final MonthlyBudget budget = new MonthlyBudget(NOV, 1000);
        assertEquals(0, budget.getRemaining(spent(1000)));
        assertFalse(budget.isOverBudget(spent(1000)));
    }

    @Test
    void overBudget() {
        final MonthlyBudget budget = new MonthlyBudget(NOV, 1000);
        assertEquals(-200, budget.getRemaining(spent(1200)));
        assertTrue(budget.isOverBudget(spent(1200)));
    }

    @Test
    void rejectsNonPositiveLimit() {
        assertThrows(IllegalArgumentException.class, () -> new MonthlyBudget(NOV, 0));
        assertThrows(IllegalArgumentException.class, () -> new MonthlyBudget(NOV, -5));
    }
}
