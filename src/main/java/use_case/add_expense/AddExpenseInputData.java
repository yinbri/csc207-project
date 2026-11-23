package use_case.add_expense;

/**
 * Input Data for the Add Expense use case. Values are kept as the user typed them
 * so the interactor can validate them and report a helpful error.
 */
public class AddExpenseInputData {
    private final String amount;
    private final String category;
    private final String description;
    private final String date;

    public AddExpenseInputData(String amount, String category, String description, String date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
