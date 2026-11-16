package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExchangeRateTest {

    @Test
    void exchangeRateConverts() {
        final ExchangeRate rate = new ExchangeRate("CAD", "CNY", 5.0);
        assertEquals("CAD", rate.getFromCurrency());
        assertEquals("CNY", rate.getToCurrency());
        assertEquals(5.0, rate.getRate());
        assertEquals(50.0, rate.convert(10));
        assertThrows(IllegalArgumentException.class, () -> new ExchangeRate("CAD", "CNY", 0));
    }
}
