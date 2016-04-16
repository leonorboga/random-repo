package UserInterface.BackgroundWorkers;

import Logic.Report;

import javax.swing.*;

/**
 * Worker that performs an ordered report of the countries with highest number of airports (with count) and
 countries with lowest number of airports.
 */
public class ListOfOrderedAirportsPerCountryWithCountWorker extends SwingWorker<String, String> {

    private final int numberOfResults;

    public ListOfOrderedAirportsPerCountryWithCountWorker(final int numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    @Override
    protected String doInBackground() throws Exception {
        return Report.airportsPerCountryWithCount(numberOfResults);
    }
}