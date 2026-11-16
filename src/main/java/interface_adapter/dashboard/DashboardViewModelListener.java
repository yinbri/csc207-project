package interface_adapter.dashboard;

/**
 * Something (the Dashboard View) that wants to know when the dashboard state changes.
 */
public interface DashboardViewModelListener {
    /**
     * The dashboard state was updated.
     */
    void stateChanged();
}
