package entity;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * A single expense the student has recorded, in their local currency.
 */
public class Expense {
    private final String id;
    private final double amount;
    private final Category category;
    private final String description;
    private final LocalDate date;

    /**
     * Creates a new expense.
     * @param id a unique identifier
     * @param amount the amount spent; must be positive
     * @param category the spending category
     * @param description a short description (may be empty)
     * @param date the day the money was spent
     * @throws IllegalArgumentException if amount is not positive
     */
    public Expense(String id, double amount, Category category, String description, LocalDate date) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive.");
        }
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns whether this expense happened in the given month.
     * @param month the month to check
     * @return true if the expense date falls in month
     */
    public boolean isIn(YearMonth month) {
        return YearMonth.from(date).equals(month);
    }
}
