package UserInterface.BackgroundWorkers;

import Logic.Query;
import javax.swing.*;

/**
 * Worker that perform a query of airports and runways per country name
 */
public class CountryNameQueryWorker extends SwingWorker<String, String> {

    private final String countryName;

    public CountryNameQueryWorker(final String countryName) {
        this.countryName = countryName;
    }

    @Override
    protected String doInBackground() throws Exception {
        return Query.queryFromCountryName(countryName);
    }
}
