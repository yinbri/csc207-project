package use_case.convert_currency;

/**
 * Input Data for the View Spending in Home Currency use case.
 */
public class ConvertCurrencyInputData {
    private final String month;
    private final String homeCurrency;

    public ConvertCurrencyInputData(String month, String homeCurrency) {
        this.month = month;
        this.homeCurrency = homeCurrency;
    }

    public String getMonth() {
        return month;
    }

    public String getHomeCurrency() {
        return homeCurrency;
    }
}
