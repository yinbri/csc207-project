package data_access;

import entity.Category;
import entity.Expense;
import entity.MonthlyBudget;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Stores expenses and budgets in a JSON file so they survive restarting the app.
 * The file is read once on startup and rewritten after every change.
 */
public class FileBudgetDataAccessObject extends InMemoryBudgetDataAccessObject {

    private final Path filePath;

    /**
     * Creates the DAO, loading any data already in the file.
     * @param filePath path to the JSON file (created if it does not exist)
     */
    public FileBudgetDataAccessObject(String filePath) {
        this.filePath = Paths.get(filePath);
        if (Files.exists(this.filePath)) {
            load();
        }
    }

    @Override
    public void saveExpense(Expense expense) {
        super.saveExpense(expense);
        save();
    }

    @Override
    public void saveBudget(MonthlyBudget budget) {
        super.saveBudget(budget);
        save();
    }

    private void load() {
        try {
            final String text = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            final JSONObject root = new JSONObject(text);

            final JSONArray expenseArray = root.optJSONArray("expenses");
            if (expenseArray != null) {
                for (int i = 0; i < expenseArray.length(); i++) {
                    final JSONObject e = expenseArray.getJSONObject(i);
                    expenses.add(new Expense(
                            e.getString("id"),
                            e.getDouble("amount"),
                            Category.valueOf(e.getString("category")),
                            e.optString("description", ""),
                            LocalDate.parse(e.getString("date"))));
                }
            }

            final JSONObject budgetObject = root.optJSONObject("budgets");
            if (budgetObject != null) {
                for (String key : budgetObject.keySet()) {
                    final YearMonth month = YearMonth.parse(key);
                    budgets.put(month, new MonthlyBudget(month, budgetObject.getDouble(key)));
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Could not read " + filePath, ex);
        }
    }

    private void save() {
        final JSONArray expenseArray = new JSONArray();
        for (Expense expense : expenses) {
            final JSONObject e = new JSONObject();
            e.put("id", expense.getId());
            e.put("amount", expense.getAmount());
            e.put("category", expense.getCategory().name());
            e.put("description", expense.getDescription());
            e.put("date", expense.getDate().toString());
            expenseArray.put(e);
        }

        final JSONObject budgetObject = new JSONObject();
        for (MonthlyBudget budget : budgets.values()) {
            budgetObject.put(budget.getMonth().toString(), budget.getLimit());
        }

        final JSONObject root = new JSONObject();
        root.put("expenses", expenseArray);
        root.put("budgets", budgetObject);

        try {
            Files.write(filePath, root.toString(2).getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException ex) {
            throw new RuntimeException("Could not write " + filePath, ex);
        }
    }
}
