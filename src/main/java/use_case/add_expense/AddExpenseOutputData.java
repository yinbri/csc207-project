package use_case.add_expense;

/**
 * Output Data for the Add Expense use case.
 */
public class AddExpenseOutputData {
    private final double amount;
    private final String category;
    private final String date;

    public AddExpenseOutputData(double amount, String category, String date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }
}
