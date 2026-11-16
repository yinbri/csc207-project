package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MonthlySpendingTest {
    private static final YearMonth NOV = YearMonth.of(2026, 11);

    private static Expense expense(double amount, Category category, String date) {
        return new Expense("id", amount, category, "desc", LocalDate.parse(date));
    }

    @Test
    void keepsOnlyExpensesInTheMonth() {
        final MonthlySpending spending = new MonthlySpending(NOV, Arrays.asList(
                expense(100, Category.FOOD, "2026-11-01"),
                expense(900, Category.RENT, "2026-11-30"),
                expense(999, Category.RENT, "2026-10-31"),
                expense(999, Category.RENT, "2026-12-01")));
        assertEquals(NOV, spending.getMonth());
        assertEquals(2, spending.getExpenses().size());
        assertEquals(1000, spending.getTotal());
    }

    @Test
    void totalsByCategoryIncludeEveryCategory() {
        final MonthlySpending spending = new MonthlySpending(NOV, Arrays.asList(
                expense(100, Category.FOOD, "2026-11-01"),
                expense(50, Category.FOOD, "2026-11-20"),
                expense(900, Category.RENT, "2026-11-01")));
        final Map<Category, Double> totals = spending.getTotalsByCategory();
        assertEquals(150.0, totals.get(Category.FOOD));
        assertEquals(900.0, totals.get(Category.RENT));
        assertEquals(0.0, totals.get(Category.TUITION));
        assertEquals(Category.values().length, totals.size());
    }

    @Test
    void emptyMonthHasZeroTotal() {
        final MonthlySpending spending = new MonthlySpending(NOV, Collections.<Expense>emptyList());
        assertEquals(0, spending.getTotal());
        assertEquals(0.0, spending.getTotalsByCategory().get(Category.FOOD));
    }

    @Test
    void expenseListCannotBeModified() {
        final MonthlySpending spending = new MonthlySpending(NOV,
                Arrays.asList(expense(100, Category.FOOD, "2026-11-01")));
        assertThrows(UnsupportedOperationException.class,
                () -> spending.getExpenses().add(expense(1, Category.FOOD, "2026-11-02")));
    }
}
