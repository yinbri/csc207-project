package data_access;

import entity.Category;
import entity.Expense;
import entity.MonthlyBudget;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBudgetDataAccessObjectTest {
    private static final YearMonth NOV = YearMonth.of(2026, 11);

    @TempDir
    Path tempDir;

    @Test
    void startsEmptyWhenFileDoesNotExist() {
        final Path file = tempDir.resolve("missing.json");
        final FileBudgetDataAccessObject dao = new FileBudgetDataAccessObject(file.toString());
        assertTrue(dao.getExpensesForMonth(NOV).isEmpty());
        assertFalse(dao.hasBudget(NOV));
        assertFalse(Files.exists(file));
    }

    @Test
    void dataSurvivesReopening() {
        final String file = tempDir.resolve("budget_data.json").toString();
        final FileBudgetDataAccessObject first = new FileBudgetDataAccessObject(file);
        first.saveBudget(new MonthlyBudget(NOV, 1500));
        first.saveExpense(new Expense("e1", 42.5, Category.FOOD, "Groceries", LocalDate.of(2026, 11, 3)));

        // A brand-new object reads the same file, like reopening the app.
        final FileBudgetDataAccessObject reopened = new FileBudgetDataAccessObject(file);
        assertTrue(reopened.hasBudget(NOV));
        assertEquals(1500, reopened.getBudget(NOV).getLimit());

        final List<Expense> expenses = reopened.getExpensesForMonth(NOV);
        assertEquals(1, expenses.size());
        final Expense expense = expenses.get(0);
        assertEquals("e1", expense.getId());
        assertEquals(42.5, expense.getAmount());
        assertEquals(Category.FOOD, expense.getCategory());
        assertEquals("Groceries", expense.getDescription());
        assertEquals(LocalDate.of(2026, 11, 3), expense.getDate());
    }

    @Test
    void loadsFileWithMissingSections() throws IOException {
        final Path file = tempDir.resolve("empty.json");
        Files.write(file, "{}".getBytes(StandardCharsets.UTF_8));
        final FileBudgetDataAccessObject dao = new FileBudgetDataAccessObject(file.toString());
        assertTrue(dao.getExpensesForMonth(NOV).isEmpty());
        assertFalse(dao.hasBudget(NOV));
    }
}
