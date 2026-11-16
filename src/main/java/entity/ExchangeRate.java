package entity;

/**
 * An exchange rate from one currency to another, e.g. 1 CAD = 5.2 CNY.
 */
public class ExchangeRate {
    private final String fromCurrency;
    private final String toCurrency;
    private final double rate;

    /**
     * Creates an exchange rate.
     * @param fromCurrency ISO code of the source currency, e.g. "CAD"
     * @param toCurrency ISO code of the target currency, e.g. "CNY"
     * @param rate units of toCurrency per 1 unit of fromCurrency; must be positive
     * @throws IllegalArgumentException if rate is not positive
     */
    public ExchangeRate(String fromCurrency, String toCurrency, double rate) {
        if (rate <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive.");
        }
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getRate() {
        return rate;
    }

    /**
     * Converts an amount from the source currency to the target currency.
     * @param amount the amount in fromCurrency
     * @return the amount in toCurrency
     */
    public double convert(double amount) {
        return amount * rate;
    }
}
