package interface_adapter.dashboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Everything the Dashboard View displays, already formatted as strings.
 */
public class DashboardState {
    private String month = "";
    private String budgetText = "No budget set";
    private String spentText = "";
    private String remainingText = "";
    private boolean overBudget;
    private List<String[]> expenseRows = new ArrayList<>();
    private List<String> categoryLines = new ArrayList<>();
    private String convertedText = "";
    private String message = "";
    private String error = "";

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getBudgetText() {
        return budgetText;
    }

    public void setBudgetText(String budgetText) {
        this.budgetText = budgetText;
    }

    public String getSpentText() {
        return spentText;
    }

    public void setSpentText(String spentText) {
        this.spentText = spentText;
    }

    public String getRemainingText() {
        return remainingText;
    }

    public void setRemainingText(String remainingText) {
        this.remainingText = remainingText;
    }

    public boolean isOverBudget() {
        return overBudget;
    }

    public void setOverBudget(boolean overBudget) {
        this.overBudget = overBudget;
    }

    public List<String[]> getExpenseRows() {
        return expenseRows;
    }

    public void setExpenseRows(List<String[]> expenseRows) {
        this.expenseRows = expenseRows;
    }

    public List<String> getCategoryLines() {
        return categoryLines;
    }

    public void setCategoryLines(List<String> categoryLines) {
        this.categoryLines = categoryLines;
    }

    public String getConvertedText() {
        return convertedText;
    }

    public void setConvertedText(String convertedText) {
        this.convertedText = convertedText;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
