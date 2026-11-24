package interface_adapter.convert_currency;

import use_case.convert_currency.ConvertCurrencyInputBoundary;
import use_case.convert_currency.ConvertCurrencyInputData;

/**
 * Controller for the View Spending in Home Currency use case.
 */
public class ConvertCurrencyController {
    private final ConvertCurrencyInputBoundary interactor;

    public ConvertCurrencyController(ConvertCurrencyInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Starts the View Spending in Home Currency use case.
     * @param month the month as typed (YYYY-MM)
     * @param homeCurrency the chosen home currency code
     */
    public void execute(String month, String homeCurrency) {
        interactor.execute(new ConvertCurrencyInputData(month, homeCurrency));
    }
}
