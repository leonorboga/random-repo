package main.userInterface;

import main.userInterface.BackgroundWorkers.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker.StateValue;

/**
 * Interface class that prints queries and reports made to the Airport Information System
 */
public class AirportInformation extends JFrame{
    final private ResourceBundle resBundle = ResourceBundle.getBundle("main/resources/applicationMessages", Locale.getDefault());
    private JTextField countryName;
    private JTextField countryCode;
    private JButton queryCountriesByNameButton;
    private JTextArea outputConsole;
    private JButton typeRunwaysCountryButton;
    private JButton runwayLatitudeButton;
    private JButton airportsWithCountryPerButton;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JPanel rootPanel;
    private JLabel labelCountryCode;
    private JLabel labelCountryName;
    private JLabel labelNumberResults;
    private JLabel LabelNumberResults2;
    private JLabel ReportLabel;
    private JLabel QueryLabel;
    private JButton clearConsoleButton;
    private JButton queryCountriesByCodeButton;
    private JScrollPane scrollPane;
    private JButton cancelQueryReportButton;

    private final int minimumCountryNameCharacters = 3;
    private final int minimumCountryCodeCharacters = 2;
    private SwingWorker<String, String> currentWorker;

    public AirportInformation() {

        super("Airport Information");
        setConfiguration();

        /**
         *  query airport and runways information using country name
         */
        queryCountriesByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (isInputForQueryByCountryNameValid()) {
                    final CountryNameQueryWorker queryWorker = new CountryNameQueryWorker(countryName.getText());

                    runWorkerAndGetResult(queryWorker,
                            String.format(resBundle.getString("ListAirportInformationCountryName"), countryName.getText()),
                            resBundle.getString("ListAirportInformationCountryNameCancelled"));
                }
            }
        });

        /**
         *  query airport and runways information using country code
         */
        queryCountriesByCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (isInputForQueryByCountryCodeValid()) {

                    final CountryCodeQueryWorker queryWorker = new CountryCodeQueryWorker(countryCode.getText());

                    runWorkerAndGetResult(queryWorker,
                            String.format(resBundle.getString("ListAirportInformationCountryCode"), countryCode.getText()),
                            resBundle.getString("ListAirportInformationCountryCodeCancelled"));
                }
            }
        });


        /**
         * report about the highest number of airports (with count) and
         countries with lowest number of airports.
         */
        airportsWithCountryPerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int numberOfResults = Integer.parseInt(spinner1.getValue().toString());
                final ListOfOrderedAirportsPerCountryWithCountWorker reportWorker =
                        new ListOfOrderedAirportsPerCountryWithCountWorker(numberOfResults);

                runWorkerAndGetResult(reportWorker,
                        String.format(resBundle.getString("ReportAirportsPerCountry"), numberOfResults),
                        resBundle.getString("ReportAirportsPerCountryCancelled"));
            }
        });


        /**
         * Report about the top most common runway latitude
         */
        runwayLatitudeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int numberOfResults = Integer.parseInt(spinner2.getValue().toString());
                final ListOfOrderedRunwaysLatitudeWorker reportWorker =
                        new ListOfOrderedRunwaysLatitudeWorker(numberOfResults);

                runWorkerAndGetResult(reportWorker,
                        String.format(resBundle.getString("ReportMostCommonAirportLatitudes"), numberOfResults),
                        resBundle.getString("ReportMostCommonAirportLatitudesCancelled"));
            }
        });

        /**
         * Report for the type of runways per country
         */
        typeRunwaysCountryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final TypeOfRunwayPerCountryWorker reportWorker = new TypeOfRunwayPerCountryWorker();

                runWorkerAndGetResult(reportWorker,
                        resBundle.getString("ReportTypeOfRunwaysPerCountry"),
                        resBundle.getString("ReportTypeOfRunwaysPerCountryCancelled"));
            }
        });

        clearConsoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputConsole.setText("");
            }
        });

        cancelQueryReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentWorker != null)
                {
                    currentWorker.cancel(true);
                    currentWorker = null;
                }
            }
        });
        setVisible(true);
    }

    private void startWorker(SwingWorker<String, String> workerToStart)
    {
        if(canQueryBeStarted()) {
            outputConsole.append(resBundle.getString("QueryReportBeingPerformedPleaseWait"));
            outputConsole.append(System.lineSeparator());
            setCurrentWorker(workerToStart);
            workerToStart.execute();
        }
        else{
            outputConsole.append(resBundle.getString("PleaseWaitCurrentQueryReport"));
        }
    }

    private void printWorkerResults(String description, String result){
        outputConsole.append(System.lineSeparator());
        outputConsole.append(description);
        outputConsole.append(System.lineSeparator());
        outputConsole.append(System.lineSeparator());
        outputConsole.append(result);
        outputConsole.append(System.lineSeparator());
        outputConsole.append(System.lineSeparator());
        outputConsole.append(resBundle.getString("QueryCompleted"));
        outputConsole.append(System.lineSeparator());
    }

    private boolean canQueryBeStarted(){
        return currentWorker == null;
    }

    private void setCurrentWorker(SwingWorker<String, String> worker) {
        currentWorker = worker;
    }

    private boolean isInputForQueryByCountryNameValid()
    {
        if(!isCountryNameFilledIn() || !hasCountryNameMinimunOfCharacters())
        {
            outputConsole.append(String.format(resBundle.getString("CountryNameMinCharacters"), minimumCountryNameCharacters));
            outputConsole.append(System.lineSeparator());
            return false;
        }

        return true;
    }

    private boolean isInputForQueryByCountryCodeValid()
    {
        if(!isCountryCodeFilledIn() || !hasCountryCodeExactNumOfCharacters())
        {
            outputConsole.append(String.format(resBundle.getString("CountryCodeNumberCharacters"), minimumCountryCodeCharacters));
            outputConsole.append(System.lineSeparator());
            return false;
        }

        return true;
    }

    private boolean isCountryNameFilledIn()
    {
        return ! countryName.getText().trim().isEmpty();
    }

    private boolean isCountryCodeFilledIn()
    {
        return ! countryCode.getText().trim().isEmpty();
    }

    private boolean hasCountryNameMinimunOfCharacters()
    {
        return countryName.getText().trim().length() >= minimumCountryNameCharacters;
    }

    private boolean hasCountryCodeExactNumOfCharacters()
    {
        return countryCode.getText().trim().length() == minimumCountryCodeCharacters;
    }

    private void setConfiguration() {
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void createUIComponents() {
        int min = 1;
        int max = 100;
        int step = 1;
        int initValue = 10;
        SpinnerModel model1 = new SpinnerNumberModel(initValue, min, max, step);
        SpinnerModel model2 = new SpinnerNumberModel(initValue, min, max, step);
        spinner1 = new JSpinner(model1);
        spinner2 = new JSpinner(model2);
    }

    private void runWorkerAndGetResult(final SwingWorker<String, String> worker,
                                       final String workerDescription,
                                       final String workerCancelledDescription){

        worker.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent event) {
                switch (event.getPropertyName()) {
                    case "state":
                        switch ((StateValue) event.getNewValue()) {
                            case DONE:
                                try {
                                    printWorkerResults(workerDescription, worker.get());
                                    setCurrentWorker(null);
                                } catch (CancellationException e1) {
                                    outputConsole.append(workerCancelledDescription);
                                    outputConsole.append(System.lineSeparator());
                                    setCurrentWorker(null);
                                } catch (ExecutionException e1) {
                                    outputConsole.append(System.lineSeparator());
                                    outputConsole.append(resBundle.getString("queryProducedNoResults"));
                                    outputConsole.append(System.lineSeparator());
                                    setCurrentWorker(null);
                                }catch (Exception e1) {
                                    outputConsole.append(e1.getMessage());
                                    setCurrentWorker(null);
                                }
                                break;
                        }
                        break;
                }
            }
        });

        try {
            startWorker(worker);
        } catch (Exception e1) {
            outputConsole.append(e1.getMessage());
        }
    }
}

