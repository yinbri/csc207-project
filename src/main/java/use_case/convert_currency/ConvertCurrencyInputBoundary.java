package use_case.convert_currency;

/**
 * Input Boundary for the View Spending in Home Currency use case.
 */
public interface ConvertCurrencyInputBoundary {
    /**
     * Converts a month's budget and spending into the student's home currency.
     * @param inputData the month and home currency
     */
    void execute(ConvertCurrencyInputData inputData);
}
