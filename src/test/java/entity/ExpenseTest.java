package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpenseTest {
    private static final YearMonth NOV = YearMonth.of(2026, 11);

    private static Expense expense(double amount, Category category, String date) {
        return new Expense("id", amount, category, "desc", LocalDate.parse(date));
    }

    @Test
    void categoryLookupIgnoresCase() {
        assertEquals(Category.FOOD, Category.fromDisplayName("food"));
        assertEquals("Transportation", Category.TRANSPORTATION.getDisplayName());
        assertThrows(IllegalArgumentException.class, () -> Category.fromDisplayName("Pets"));
    }

    @Test
    void expenseStoresFieldsAndRejectsNonPositiveAmount() {
        final Expense e = expense(12.5, Category.FOOD, "2026-11-03");
        assertEquals("id", e.getId());
        assertEquals(12.5, e.getAmount());
        assertEquals(Category.FOOD, e.getCategory());
        assertEquals("desc", e.getDescription());
        assertEquals(LocalDate.of(2026, 11, 3), e.getDate());
        assertTrue(e.isIn(NOV));
        assertFalse(e.isIn(NOV.plusMonths(1)));
        assertThrows(IllegalArgumentException.class, () -> expense(0, Category.FOOD, "2026-11-03"));
    }
}
