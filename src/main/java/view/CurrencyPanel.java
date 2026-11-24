package view;

import interface_adapter.convert_currency.ConvertCurrencyController;
import interface_adapter.dashboard.DashboardViewModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.function.Supplier;

/**
 * The View Spending in Home Currency part of the dashboard: pick a home currency, press Convert,
 * and see the month's budget and spending in that currency.
 */
public class CurrencyPanel extends JPanel {
    private final Supplier<String> monthSupplier;
    private ConvertCurrencyController convertCurrencyController;

    private final JComboBox<String> homeCurrencyBox = new JComboBox<>(DashboardViewModel.HOME_CURRENCY_OPTIONS);
    private final JTextArea convertedArea = new JTextArea(10, 22);

    /**
     * Creates the panel.
     * @param monthSupplier gives the month (YYYY-MM) to convert, read when Convert is pressed
     */
    public CurrencyPanel(Supplier<String> monthSupplier) {
        this.monthSupplier = monthSupplier;

        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createTitledBorder("View in home currency"));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        final JButton convert = new JButton("Convert");
        convert.addActionListener(evt -> submit());

        final JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Home currency:"));
        form.add(homeCurrencyBox);
        form.add(new JLabel());
        form.add(convert);

        convertedArea.setEditable(false);
        add(form, BorderLayout.NORTH);
        add(new JScrollPane(convertedArea), BorderLayout.CENTER);
    }

    private void submit() {
        if (convertCurrencyController != null) {
            convertCurrencyController.execute(monthSupplier.get(), (String) homeCurrencyBox.getSelectedItem());
        }
    }

    /**
     * Connects the Convert button to the use case.
     * @param controller the convert currency controller
     */
    public void setConvertCurrencyController(ConvertCurrencyController controller) {
        this.convertCurrencyController = controller;
    }

    /**
     * Shows the converted amounts.
     * @param convertedText the text prepared by the presenter
     */
    public void showConvertedText(String convertedText) {
        convertedArea.setText(convertedText);
    }
}
