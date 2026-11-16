package interface_adapter.dashboard;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Dashboard View. Presenters update the state and then call
 * notifyStateChanged(), which tells the View to redraw (Observer pattern).
 */
public class DashboardViewModel {
    public static final String VIEW_NAME = "dashboard";
    public static final String LOCAL_CURRENCY = "CAD";
    public static final String[] HOME_CURRENCY_OPTIONS = {
        "CNY", "INR", "KRW", "JPY", "VND", "PHP", "PKR", "BDT", "NGN", "BRL", "MXN", "EUR", "GBP", "USD",
    };

    private final List<DashboardViewModelListener> listeners = new ArrayList<>();
    private DashboardState state = new DashboardState();

    public DashboardState getState() {
        return state;
    }

    public void setState(DashboardState state) {
        this.state = state;
    }

    public void addListener(DashboardViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener that the state changed.
     */
    public void notifyStateChanged() {
        for (DashboardViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

    /**
     * Formats an amount of money for display, e.g. "CAD 1,234.50".
     * @param currency ISO currency code
     * @param amount the amount
     * @return the formatted string
     */
    public static String formatMoney(String currency, double amount) {
        return String.format("%s %,.2f", currency, amount);
    }
}
