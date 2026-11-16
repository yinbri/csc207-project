package data_access;

import entity.ExchangeRate;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.convert_currency.ExchangeRateDataAccessInterface;
import use_case.convert_currency.ExchangeRateUnavailableException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Gets live exchange rates from the free ExchangeRate-API "open access" endpoint
 * (https://www.exchangerate-api.com/docs/free). No API key is needed.
 */
public class ExchangeRateApiDataAccessObject implements ExchangeRateDataAccessInterface {
    private static final String BASE_URL = "https://open.er-api.com/v6/latest/";

    private final OkHttpClient client = new OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .build();

    @Override
    public ExchangeRate getRate(String fromCurrency, String toCurrency) throws ExchangeRateUnavailableException {
        final Request request = new Request.Builder().url(BASE_URL + fromCurrency).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                throw new ExchangeRateUnavailableException("empty response from exchange rate service.");
            }
            final JSONObject json = new JSONObject(response.body().string());
            if (!"success".equals(json.optString("result"))) {
                throw new ExchangeRateUnavailableException("unknown currency " + fromCurrency + ".");
            }
            final JSONObject rates = json.getJSONObject("rates");
            if (!rates.has(toCurrency)) {
                throw new ExchangeRateUnavailableException("unknown currency " + toCurrency + ".");
            }
            return new ExchangeRate(fromCurrency, toCurrency, rates.getDouble(toCurrency));
        }
        catch (IOException ex) {
            throw new ExchangeRateUnavailableException("check your internet connection.");
        }
        catch (JSONException ex) {
            throw new ExchangeRateUnavailableException("unexpected response from exchange rate service.");
        }
    }
}
