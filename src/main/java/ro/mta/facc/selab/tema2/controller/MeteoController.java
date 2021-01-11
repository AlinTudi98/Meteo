package ro.mta.facc.selab.tema2.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import ro.mta.facc.selab.tema2.model.MeteoModel;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;


public class MeteoController {
    private MeteoModel meteoData;
    private List<Location> list;
    private Map<String,String> countryMap;

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
    private Label tempLabel;
    @FXML
    private Label humidLabel;
    @FXML
    private Label windLabel;
    @FXML
    private Label pressureLabel;

    @FXML
    private Button refreshButton;

    @FXML
    private ImageView imgView;

    private static Map<String,Object> jsonToMap(String str){
        Map<String,Object> map = new Gson().fromJson(str,new TypeToken<HashMap<String,Object>>() {}.getType());
        return map;
    }

    public <K, V> Stream<K> keys(Map<K, V> map, V value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
    }

    public MeteoController(List<Location> list) {
        this.list = list;
        this.countryMap = new HashMap<String,String>();
    }

    public MeteoController(){
        this.list = new ArrayList<Location>();
        this.countryMap = new HashMap<String,String>();
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


        for(String option:options){
            Locale l = new Locale("en",option);
            countryMap.put(option,l.getDisplayCountry());
        }

        options.clear();

        for(Location loc: list){
            if(!options.contains(countryMap.get(loc.country)))
            options.add(countryMap.get(loc.country));
        }

        countryBox.getItems().addAll(options);

        this.meteoData = new MeteoModel(list.get(0).country,list.get(0).city);
        showData();

    }

    @FXML
    private void loadCities(ActionEvent event){
        ObservableList<String> options = FXCollections.observableArrayList();
        if(this.countryBox.getValue()!=null){
            for(Location loc:list){
                if(keys(countryMap,this.countryBox.getValue().toString()).findFirst().get().equals(loc.country)){
                    options.add(loc.city);
                }
            }

            cityBox.getItems().clear();
            cityBox.getItems().addAll(options);
        }
    }

    @FXML
    public void loadData(ActionEvent actionEvent) throws InterruptedException {
        if(this.countryBox.getValue()==null || this.cityBox.getValue()==null){
            return;
        }

        this.meteoData = new MeteoModel((String)keys(countryMap,this.countryBox.getValue().toString()).findFirst().get(),(String)this.cityBox.getValue());
        Thread.sleep(100);
        this.showData();
    }

    private void showData() {
        this.pressureLabel.setText(meteoData.getPressureString());
        this.tempLabel.setText(meteoData.getTempString());
        this.humidLabel.setText(meteoData.getHumidityString());
        this.locLabel.setText(meteoData.getLocationString());
        this.timeLabel.setText(meteoData.getTimeString());
        this.windLabel.setText(meteoData.getWindString());
        this.weatherLabel.setText(meteoData.getWeatherString());
        this.imgView.setImage(meteoData.getWeatherImg());
    }

    public void refreshInfo(ActionEvent actionEvent) {
        if(this.countryBox.getValue() == null || this.cityBox.getValue() == null)
            return;
        this.meteoData = new MeteoModel((String)keys(countryMap,this.countryBox.getValue().toString()).findFirst().get(),(String)this.cityBox.getValue());
        this.showData();
    }
}

