package ro.mta.facc.selab.tema2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import ro.mta.facc.selab.tema2.model.MeteoModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MeteoController {
    private MeteoModel meteoData;
    private List<Location> list;

    @FXML
    private ComboBox countryBox;
    @FXML
    private ComboBox cityBox;

    @FXML
    private Label locLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label weatherLabel;

    @FXML
    private Label precipLabel;
    @FXML
    private Label humidLabel;
    @FXML
    private Label windLabel;

    public MeteoController(List<Location> list) {
        this.list = list;
    }

    public MeteoController(){
        this.list = new ArrayList<Location>();
    }

    @FXML
    public void initialize() throws FileNotFoundException {
        File file = new File("src/main/resources/inputFile.txt");
        Scanner scanner = new Scanner(file);
        scanner.nextLine();
        while(scanner.hasNextLine()){
            String data = scanner.nextLine();

            String[] locInfo = data.split("\t");
            Location loc = new Location();
            loc.ID = Long.parseLong(locInfo[0]);
            loc.city = locInfo[1];
            loc.lat = Double.parseDouble(locInfo[2]);
            loc.lon = Double.parseDouble(locInfo[3]);
            loc.country = locInfo[4];

            list.add(loc);
        }

        ObservableList<String> options = FXCollections.observableArrayList();

        for(Location loc: list){
            if(!options.contains(loc.country)){
                options.add(loc.country);
            }
        }

        countryBox.getItems().addAll(options);

    }

    @FXML
    private void loadCities(ActionEvent event){
        ObservableList<String> options = FXCollections.observableArrayList();
        if(this.countryBox.getValue()!=null){
            for(Location loc:list){
                if(this.countryBox.getValue().equals(loc.country)){
                    options.add(loc.city);
                }
            }

            cityBox.getItems().addAll(options);
        }
    }
}

