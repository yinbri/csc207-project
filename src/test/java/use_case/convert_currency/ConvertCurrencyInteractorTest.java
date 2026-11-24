package use_case.convert_currency;

import data_access.InMemoryBudgetDataAccessObject;
import entity.Category;
import entity.ExchangeRate;
import entity.Expense;
import entity.MonthlyBudget;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ConvertCurrencyInteractorTest {
    private static final YearMonth NOV = YearMonth.of(2026, 11);

    /** A fake exchange rate source so tests don't need the internet: 1 CAD = 5 of anything. */
    private static final ExchangeRateDataAccessInterface FAKE_RATES = (from, to) -> new ExchangeRate(from, to, 5.0);

    private static final ExchangeRateDataAccessInterface BROKEN_RATES = (from, to) -> {
        throw new ExchangeRateUnavailableException("check your internet connection.");
    };

    private static InMemoryBudgetDataAccessObject dao() {
        final InMemoryBudgetDataAccessObject dao = new InMemoryBudgetDataAccessObject();
        dao.saveExpense(new Expense("1", 100, Category.FOOD, "", LocalDate.of(2026, 11, 2)));
        return dao;
    }

    private abstract static class SuccessPresenter implements ConvertCurrencyOutputBoundary {
        @Override
        public void prepareFailView(String errorMessage) {
            fail("Use case failure is unexpected: " + errorMessage);
        }
    }

    private static void assertFails(ExchangeRateDataAccessInterface rates, ConvertCurrencyInputData input,
                                    String expectedError) {
        new ConvertCurrencyInteractor(dao(), rates, new ConvertCurrencyOutputBoundary() {
            @Override
            public void prepareSuccessView(ConvertCurrencyOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(expectedError, errorMessage);
            }
        }, "CAD").execute(input);
    }

    @Test
    void convertsWithBudget() {
        final InMemoryBudgetDataAccessObject dao = dao();
        dao.saveBudget(new MonthlyBudget(NOV, 1000));
        new ConvertCurrencyInteractor(dao, FAKE_RATES, new SuccessPresenter() {
            @Override
            public void prepareSuccessView(ConvertCurrencyOutputData out) {
                assertEquals("CAD", out.getLocalCurrency());
                assertEquals("CNY", out.getHomeCurrency());
                assertEquals(5.0, out.getRate());
                assertTrue(out.hasBudget());
                assertEquals(5000, out.getBudget());
                assertEquals(500, out.getTotalSpent());
                assertEquals(4500, out.getRemaining());
                assertEquals(500.0, out.getCategoryTotals().get("Food"));
                assertEquals(0.0, out.getCategoryTotals().get("Rent"));
            }
        }, "CAD").execute(new ConvertCurrencyInputData("2026-11", " cny "));
    }

    @Test
    void convertsWithoutBudget() {
        new ConvertCurrencyInteractor(dao(), FAKE_RATES, new SuccessPresenter() {
            @Override
            public void prepareSuccessView(ConvertCurrencyOutputData out) {
                assertFalse(out.hasBudget());
                assertEquals(500, out.getTotalSpent());
            }
        }, "CAD").execute(new ConvertCurrencyInputData("2026-11", "INR"));
    }

    @Test
    void failsWhenMonthInvalid() {
        assertFails(FAKE_RATES, new ConvertCurrencyInputData("bad", "CNY"), "Month must be in YYYY-MM format.");
    }

    @Test
    void failsWhenCurrencyCodeInvalid() {
        assertFails(FAKE_RATES, new ConvertCurrencyInputData("2026-11", "yuan"),
                "Home currency must be a 3-letter code, e.g. CNY.");
    }

    @Test
    void failsWhenSameAsLocalCurrency() {
        assertFails(FAKE_RATES, new ConvertCurrencyInputData("2026-11", "CAD"),
                "Your home currency is the same as your local currency (CAD).");
    }

    @Test
    void failsWhenRateUnavailable() {
        assertFails(BROKEN_RATES, new ConvertCurrencyInputData("2026-11", "CNY"),
                "Could not get exchange rate: check your internet connection.");
    }
}
