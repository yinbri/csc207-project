package use_case.add_expense;

import entity.Category;
import entity.Expense;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 * The Add Expense Interactor: validates the user's input and records the expense.
 */
public class AddExpenseInteractor implements AddExpenseInputBoundary {
    private final AddExpenseDataAccessInterface dataAccess;
    private final AddExpenseOutputBoundary presenter;

    public AddExpenseInteractor(AddExpenseDataAccessInterface dataAccess,
                                AddExpenseOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddExpenseInputData inputData) {
        final double amount;
        try {
            amount = Double.parseDouble(inputData.getAmount().trim());
        }
        catch (NumberFormatException ex) {
            presenter.prepareFailView("Amount must be a number, e.g. 12.50");
            return;
        }
        if (amount <= 0) {
            presenter.prepareFailView("Amount must be greater than zero.");
            return;
        }

        final Category category;
        try {
            category = Category.fromDisplayName(inputData.getCategory());
        }
        catch (IllegalArgumentException ex) {
            presenter.prepareFailView("Please choose a valid category.");
            return;
        }

        final LocalDate date;
        try {
            date = LocalDate.parse(inputData.getDate().trim());
        }
        catch (DateTimeParseException ex) {
            presenter.prepareFailView("Date must be in YYYY-MM-DD format.");
            return;
        }

        final Expense expense = new Expense(UUID.randomUUID().toString(), amount, category,
                inputData.getDescription().trim(), date);
        dataAccess.saveExpense(expense);

        presenter.prepareSuccessView(new AddExpenseOutputData(amount, category.getDisplayName(),
                date.toString()));
    }
}
