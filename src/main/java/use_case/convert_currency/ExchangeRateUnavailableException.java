package use_case.convert_currency;

/**
 * Thrown when an exchange rate cannot be retrieved (e.g. no internet, unknown currency).
 */
public class ExchangeRateUnavailableException extends Exception {
    public ExchangeRateUnavailableException(String message) {
        super(message);
    }
}
