package main.userInterface.BackgroundWorkers;

import main.logic.Report;

import javax.swing.*;

/**
 * Worker that performs an ordered report by runway latitude occurrences
 */
public class ListOfOrderedRunwaysLatitudeWorker extends SwingWorker<String, String> {

    private final int numberOfResults;

    public ListOfOrderedRunwaysLatitudeWorker(final int numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    @Override
    protected String doInBackground() throws Exception {
        return Report.getOrderedRunwayLatitudes(numberOfResults);
    }
}
