package use_case.convert_currency;

/**
 * Output Boundary for the View Spending in Home Currency use case.
 */
public interface ConvertCurrencyOutputBoundary {
    /**
     * Prepares the view to show converted amounts.
     * @param outputData the converted amounts
     */
    void prepareSuccessView(ConvertCurrencyOutputData outputData);

    /**
     * Prepares the view when conversion failed.
     * @param errorMessage why it failed
     */
    void prepareFailView(String errorMessage);
}
