package main.userInterface.BackgroundWorkers;

import main.logic.Report;
import javax.swing.*;

/**
 * Worker that performs a query of the runways types per country
 */
public class TypeOfRunwayPerCountryWorker extends SwingWorker<String, String> {

    public TypeOfRunwayPerCountryWorker() {
    }
    @Override
    protected String doInBackground() throws Exception {
        return Report.getTypeOfRunwaysPerCountry();
    }
}


