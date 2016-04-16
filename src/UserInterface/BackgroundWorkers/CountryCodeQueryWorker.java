package UserInterface.BackgroundWorkers;

import Logic.Query;
import javax.swing.*;

/**
 * Worker that perform a query of airports and runways per code
 */
public class CountryCodeQueryWorker extends SwingWorker<String, String> {

    private final String countryCode;

    public CountryCodeQueryWorker(final String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    protected String doInBackground() throws Exception {
        return Query.queryFromCountryCode(countryCode.toUpperCase());
    }
}