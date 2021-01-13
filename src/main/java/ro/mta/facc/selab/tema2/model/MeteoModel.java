package ro.mta.facc.selab.tema2.model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the model of the MVC Arhitecture for this App.
 * This class makes GET requests to the OpenWeather.org API to
 * get all of the displayed information on the GUI.
 *
 * @author Alin Tudose
 */

public class MeteoModel {

    /**
     * Members of this class are the necessary information strings that will be
     * displayed in the GUI, and the identification information for the city that
     * we want to display.
     * The weatherImg is loaded locally from the resources/Img folder, based on the
     * icon value in the returned JSON from the GET request.
     */
    StringProperty country, city, locationString;
    StringProperty timeString;
    StringProperty weatherString;
    StringProperty humidityString, windString;
    StringProperty tempString;
    StringProperty pressureString;
    Image weatherImg;

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
     * Function to capitalize all the words in a string.
     *
     * @param str The string to be capitalized.
     * @return The capitalized string.
     */
    private String capitalize(String str) {
        String[] strings = str.split(" ");
        String toRet = "";
        for(String string:strings){
            toRet += string.substring(0,1).toUpperCase();
            toRet += string.substring(1);
            toRet += " ";
        }
        return toRet.substring(0,toRet.length()-1);
    }

    /**
     * The class constructor. Here is where the GET request is made, and
     * all the information is parsed and loaded into the class.
     *
     * @param country The country ISO code or name, for identification of the city.
     * @param city The city name to get the information for.
     */

    public MeteoModel(String country, String city) {
        this.country = new SimpleStringProperty(country);
        this.city = new SimpleStringProperty(city);

        String loc = city + ", " + country.toUpperCase();
        this.locationString = new SimpleStringProperty(loc);

        String APIKey = "815485b1ad9fa554e90bf9cadcc08404";
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + this.city.get() +
                "," + this.country.get() + "&appid=" + APIKey;

        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");



            int status = con.getResponseCode();
            if (status >= 299)
                throw new Exception("Bad Request");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();

            Map<String,Object> respMap = jsonToMap(content.toString());
            Map<String,Object> mainMap = jsonToMap(respMap.get("main").toString());
            Map<String, Object > windMap = jsonToMap (respMap.get("wind").toString());

            List<Object> weatherJSON = (List<Object>)respMap.get("weather");
            Map<String,Object> weatherMap = (Map<String,Object>)weatherJSON.get(0);

            humidityString = new SimpleStringProperty( "Humidity: " + mainMap.get("humidity") + "%");



            double deg = Double.parseDouble(windMap.get("deg").toString());
            String dir = "N";

            if( deg >=348.75 || deg <= 11.25)
                dir = "N";
            if(deg > 11.25 && deg <= 33.75)
                dir = "NNE";
            if(deg > 33.75 && deg <= 56.25)
                dir = "NE";
            if(deg > 56.25 && deg <= 78.75)
                dir = "ENE";
            if(deg > 78.75 && deg <= 101.25)
                dir = "E";
            if(deg > 101.25 && deg <= 123.75)
                dir = "ESE";
            if(deg > 123.75 && deg <= 146.25)
                dir = "SE";
            if(deg > 146.25 && deg <= 168.75)
                dir = "SSE";
            if(deg > 168.75 && deg <= 191.25)
                dir = "S";
            if(deg > 191.25 && deg <= 213.75)
                dir = "SSW";
            if(deg > 213.75 && deg <= 236.25)
                dir = "SW";
            if(deg > 236.25 && deg <= 258.75)
                dir = "WSW";
            if(deg > 258.75 && deg <= 281.25)
                dir = "W";
            if(deg > 281.25 && deg <= 303.75)
                dir = "WNW";
            if(deg > 303.75 && deg <= 326.25)
                dir = "NW";
            if(deg > 326.25 && deg < 348.75)
                dir = "NNW";

            windString = new SimpleStringProperty("Wind: " + windMap.get("speed") + " m/s, " + dir);


            double tempK = Double.parseDouble(mainMap.get("temp").toString());
            tempK -= 273.15;
            double trunc = BigDecimal.valueOf(tempK).setScale(2, RoundingMode.HALF_UP).doubleValue();
            tempString = new SimpleStringProperty("" + trunc + "°C");
            weatherString = new SimpleStringProperty("Weather: " + weatherMap.get("main") + ", " +
                    capitalize(weatherMap.get("description").toString()));

            pressureString = new SimpleStringProperty("Pressure: " + (long)Double.parseDouble(mainMap.get("pressure").toString()) + " hPa");

            weatherImg = new Image(new FileInputStream("src/main/resources/Img/" + weatherMap.get("icon") + "@2x.png"));

            double timestamp = Double.parseDouble(respMap.get("dt").toString());
            LocalDateTime ldt = Instant.ofEpochSecond((long)timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM yyyy, HH:mm:ss");
            String formatDateTime = ldt.format(formatter);

            timeString = new SimpleStringProperty("Time: "+ formatDateTime);

        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getPressureString() {
        return pressureString.get();
    }

    public String getTempString() {
        return tempString.get();
    }

    public String getCountry() {
        return country.get();
    }

    public String getCity() {
        return city.get();
    }

    public String getLocationString() {
        return locationString.get();
    }

    public String getTimeString() {
        return timeString.get();
    }

    public String getWeatherString() {
        return weatherString.get();
    }

    public String getHumidityString() {
        return humidityString.get();
    }

    public String getWindString() {
        return windString.get();
    }

    public Image getWeatherImg() {
        return weatherImg;
    }

}
