package use_case.view_summary;

/**
 * A simple, display-friendly description of one expense, passed out of the use case
 * so that outer layers never need to touch the Expense entity.
 */
public class ExpenseRow {
    private final String date;
    private final String category;
    private final String description;
    private final double amount;

    public ExpenseRow(String date, String category, String description, double amount) {
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }
}
