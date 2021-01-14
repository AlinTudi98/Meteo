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

/**
 * This class represents the controller of the MVC Architecture of this App.
 * This class gets the information from the model and manages the correct
 * display of that information in the GUI.
 *
 * @author Alin Tudose
 */
public class MeteoController {

    /**
     * meteoData The model in which we store the information to display at
     * a given time. This variable will be modified every time we load a
     * different city or we hit the Refresh button
     *
     * list The list with the identification information for the cities
     * that we make available to display. It is loaded from the inputFile.txt file.
     *
     * countryMap The map which we use to get the full country name from the
     * ISO Country Code.
     *
     * The @FXML tagged members are instances of the objects displayed in the GUI.
     */
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

    /**
     * Function to parse JSON objects using the Google JSON API.
     *
     * @param str The string containing the JSON object.
     * @return The map built after the structure of the JSON object.
     */
    private static Map<String,Object> jsonToMap(String str){
        Map<String,Object> map = new Gson().fromJson(str,new TypeToken<HashMap<String,Object>>() {}.getType());
        return map;
    }

    /**
     * This function is used to create a backward association betwen
     * the values and the keys of a Map
     * @param map The map from which we want to extract the info
     * @param value The value we want the key of.
     * @param <K> The type of the keys in the map
     * @param <V> The type of the values in the map
     * @return A stream containing all the keys that have the wanted value in the map
     */
    public <K, V> Stream<K> keys(Map<K, V> map, V value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
    }

    /**
     * Constructor with parameters. Used to instantiate the list and
     * countryMap members
     * @param list the value of the preloaded list that will be copied in the list member
     */
    public MeteoController(List<Location> list) {
        this.list = list;
        this.countryMap = new HashMap<String,String>();
    }

    /**
     * Constructor without parameters. Used to instantiate the list and
     * countryMap members as empty ArrayList and HashMap, respectively
     */
    public MeteoController(){
        this.list = new ArrayList<Location>();
        this.countryMap = new HashMap<String,String>();
    }

    /**
     * Function used to read the list of cities from the inputFile.txt file,
     * and load the country options in the countries ComboBox.
     * @throws FileNotFoundException if the file inputFile.txt cannot be found.
     */
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

    /**
     * Function used to load the cities of the selected country in the
     * cities ComboBox.
     * @param event The event that triggered the function call from the GUI.
     *              (Selection changed in the countries ComboBox).
     */
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

    /**
     * Function used to instantiate the meteoData model with the
     * selected information.
     * @param actionEvent The event that triggered the function call from the GUI.
     *                    (Selection changed in the cities ComboBox).
     */
    @FXML
    private void loadData(ActionEvent actionEvent) {
        if(this.countryBox.getValue()==null || this.cityBox.getValue()==null){
            return;
        }

        this.meteoData = new MeteoModel((String)keys(countryMap,this.countryBox.getValue().toString()).findFirst().get(),(String)this.cityBox.getValue());
        this.showData();
    }

    /**
     * Function used to load the data from the meteoData model class
     * into the GUI.
     */
    private void showData() {
        try {
            File log = new File("Log.txt");
            log.createNewFile();
            FileWriter logWriter = new FileWriter(log,true);
            logWriter.write("/******************************************************************/\n");
            logWriter.write("Location: "+ meteoData.getLocationString() + "\n");

            this.pressureLabel.setText(meteoData.getPressureString());
            logWriter.write(meteoData.getPressureString()+ "\n");

            this.tempLabel.setText(meteoData.getTempString());
            logWriter.write("Temperature: "+ meteoData.getTempString()+"\n");

            this.humidLabel.setText(meteoData.getHumidityString());
            logWriter.write(meteoData.getHumidityString()+"\n");

            this.locLabel.setText(meteoData.getLocationString());

            this.timeLabel.setText(meteoData.getTimeString());
            logWriter.write(meteoData.getTimeString()+"\n");

            this.windLabel.setText(meteoData.getWindString());
            logWriter.write(meteoData.getWindString()+"\n");

            this.weatherLabel.setText(meteoData.getWeatherString());
            logWriter.write(meteoData.getWeatherString()+"\n");

            this.imgView.setImage(meteoData.getWeatherImg());

            logWriter.write("/******************************************************************/\n\n");

            logWriter.close();

        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    /**
     * Function used to reinstantiate the meteoData member with the same current
     * city information, for an update of the info displayed in the GUI.
     * @param actionEvent The event that triggered the function call from the GUI.
     *                    (Clicked the Refresh Button).
     */
    @FXML
    private void refreshInfo(ActionEvent actionEvent) {
        if(this.countryBox.getValue() == null || this.cityBox.getValue() == null)
            return;
        this.meteoData = new MeteoModel((String)keys(countryMap,this.countryBox.getValue().toString()).findFirst().get(),(String)this.cityBox.getValue());
        this.showData();
    }
}

