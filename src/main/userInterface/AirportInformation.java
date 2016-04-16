package main.userInterface;

import main.userInterface.BackgroundWorkers.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker.StateValue;

/**
 * Interface class that prints queries and reports made to the Airport Information System
 */
public class AirportInformation extends JFrame{
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
                    queryWorker.addPropertyChangeListener(new PropertyChangeListener() {

                        public void propertyChange(final PropertyChangeEvent event) {
                            switch (event.getPropertyName()) {
                                case "state":
                                    switch ((StateValue) event.getNewValue()) {
                                        case DONE:
                                            try {
                                                printWorkerResults(String.format("List of airport information that correspond to country name started by '%s': ", countryName.getText()),
                                                        queryWorker.get());
                                                setCurrentWorker(null);
                                            } catch (CancellationException e1) {
                                                outputConsole.append("The query list of airports and runways per country (country name) was cancelled.");
                                                outputConsole.append(System.lineSeparator());
                                                setCurrentWorker(null);
                                            } catch (ExecutionException e1) {
                                                outputConsole.append(System.lineSeparator());
                                                outputConsole.append("The query produced no results");
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
                        startWorker(queryWorker);
                    } catch (Exception e1) {
                        outputConsole.append(e1.getMessage());
                    }
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

                    queryWorker.addPropertyChangeListener(new PropertyChangeListener() {
                        public void propertyChange(final PropertyChangeEvent event) {
                            switch (event.getPropertyName()) {
                                case "state":
                                    switch ((StateValue) event.getNewValue()) {
                                        case DONE:
                                            try {
                                                printWorkerResults(String.format("List of airports information that correspond to country code '%s': ", countryCode.getText()),
                                                        queryWorker.get());
                                                setCurrentWorker(null);
                                            } catch (CancellationException e1) {
                                                outputConsole.append(System.lineSeparator());
                                                outputConsole.append("The query list of airports and runways per country (country code) was cancelled.");
                                                outputConsole.append(System.lineSeparator());
                                                setCurrentWorker(null);
                                            } catch (ExecutionException e1) {
                                                outputConsole.append(System.lineSeparator());
                                                outputConsole.append("The query produced no results");
                                                outputConsole.append(System.lineSeparator());
                                                setCurrentWorker(null);
                                            } catch (Exception e1) {
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
                        startWorker(queryWorker);
                    } catch (Exception e1) {
                        outputConsole.append(e1.getMessage());
                    }
                }
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

        /**
         * report about the highest number of airports (with count) and
         countries with lowest number of airports.
         */
        airportsWithCountryPerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int numberOfResults = Integer.parseInt(spinner1.getValue().toString());
                final ListOfOrderedAirportsPerCountryWithCountWorker reportWorker = new ListOfOrderedAirportsPerCountryWithCountWorker(numberOfResults);

                reportWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(final PropertyChangeEvent event) {
                        switch (event.getPropertyName()) {
                            case "state":
                                switch ((StateValue) event.getNewValue()) {
                                    case DONE:
                                        try {
                                            printWorkerResults(String.format("Report for airports per country with an output of %d results:", numberOfResults),
                                                    reportWorker.get());
                                            setCurrentWorker(null);
                                        } catch (CancellationException e1) {
                                            outputConsole.append(System.lineSeparator());
                                            outputConsole.append("The report of airports per country was cancelled.");
                                            outputConsole.append(System.lineSeparator());
                                            setCurrentWorker(null);
                                        } catch (ExecutionException e1) {
                                            outputConsole.append(System.lineSeparator());
                                            outputConsole.append("The report produced no results");
                                            outputConsole.append(System.lineSeparator());
                                            setCurrentWorker(null);
                                        } catch (Exception e1) {
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
                    startWorker(reportWorker);
                } catch (Exception e1) {
                    outputConsole.append(e1.getMessage());
                }
            }
        });


        /**
         * Report about the top most common runway latitude
         */
        runwayLatitudeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int numberOfResults = Integer.parseInt(spinner2.getValue().toString());
                final ListOfOrderedRunwaysLatitudeWorker reportWorker = new ListOfOrderedRunwaysLatitudeWorker(numberOfResults);

                reportWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(final PropertyChangeEvent event) {
                        switch (event.getPropertyName()) {
                            case "state":
                                switch ((StateValue) event.getNewValue()) {
                                    case DONE:
                                        try {
                                            printWorkerResults(String.format("Report for ordered most common airport latitudes with an output of %d results (from most common to less common) :", numberOfResults),
                                                    reportWorker.get());
                                            setCurrentWorker(null);
                                        } catch (CancellationException e1) {
                                            outputConsole.append(System.lineSeparator());
                                            outputConsole.append("The report for ordered most common airport latitudes was cancelled.");
                                            outputConsole.append(System.lineSeparator());
                                            setCurrentWorker(null);
                                        } catch (ExecutionException e1) {
                                            outputConsole.append(System.lineSeparator());
                                            outputConsole.append("The report produced no results");
                                            outputConsole.append(System.lineSeparator());
                                            setCurrentWorker(null);
                                        } catch (Exception e1) {
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
                    startWorker(reportWorker);
                } catch (Exception e1) {
                    outputConsole.append(e1.getMessage());
                }
            }
        });

        /**
         * Report for the type of runways per country
         */
        typeRunwaysCountryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final TypeOfRunwayPerCountryWorker reportWorker = new TypeOfRunwayPerCountryWorker();

                reportWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(final PropertyChangeEvent event) {
                        switch (event.getPropertyName()) {
                            case "state":
                                switch ((StateValue) event.getNewValue()) {
                                    case DONE:
                                        try {
                                            printWorkerResults("Report of the type of runways per country:",
                                                    reportWorker.get());
                                            setCurrentWorker(null);
                                        } catch (CancellationException e1) {
                                            outputConsole.append(System.lineSeparator());
                                            outputConsole.append("The report of the type of runways per country was cancelled.");
                                            outputConsole.append(System.lineSeparator());
                                            setCurrentWorker(null);
                                        } catch (ExecutionException e1) {
                                            outputConsole.append(System.lineSeparator());
                                            outputConsole.append("The report produced no results");
                                            outputConsole.append(System.lineSeparator());
                                            setCurrentWorker(null);
                                        } catch (Exception e1) {
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
                    startWorker(reportWorker);
                } catch (Exception e1) {
                    outputConsole.append(e1.getMessage());
                }
            }
        });
        setVisible(true);
    }

    private void startWorker(SwingWorker<String, String> workerToStart)
    {
        if(canQueryBeStarted()) {
            outputConsole.append("Your query is being performed... please wait...");
            outputConsole.append(System.lineSeparator());
            setCurrentWorker(workerToStart);
            workerToStart.execute();
        }
        else{
            outputConsole.append("Please wait until current query / report ends or cancel current query / report");
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
        outputConsole.append("Query completed!");
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
            outputConsole.append(String.format("Please insert a country name (minimum of %d characters).\n", minimumCountryNameCharacters));
            return false;
        }

        return true;
    }

    private boolean isInputForQueryByCountryCodeValid()
    {
        if(!isCountryCodeFilledIn() || !hasCountryCodeExactNumOfCharacters())
        {
            outputConsole.append(String.format("Please insert a country code (with exactly %d characters).\n", minimumCountryCodeCharacters));
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
}

