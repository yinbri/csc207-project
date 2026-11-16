package use_case.convert_currency;

import entity.ExchangeRate;

/**
 * Source of exchange rates (implemented by a class that calls an exchange rate API).
 */
public interface ExchangeRateDataAccessInterface {
    /**
     * Gets the current exchange rate between two currencies.
     * @param fromCurrency ISO code, e.g. "CAD"
     * @param toCurrency ISO code, e.g. "CNY"
     * @return the exchange rate
     * @throws ExchangeRateUnavailableException if the rate cannot be retrieved
     */
    ExchangeRate getRate(String fromCurrency, String toCurrency) throws ExchangeRateUnavailableException;
}
