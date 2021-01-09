package ro.mta.facc.selab.tema2.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeteoModel {
    StringProperty country, city, locationString;
    StringProperty timeString;
    StringProperty weatherString;
    StringProperty humidityString, descrString, windString;


    private static Map<String,Object> jsonToMap(String str){
        Map<String,Object> map = new Gson().fromJson(str,new TypeToken<HashMap<String,Object>>() {}.getType());
        return map;
    }

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
                dir = new String("N");
            if(deg > 11.25 && deg <= 33.75)
                dir = new String("NNE");
            if(deg > 33.75 && deg <= 56.25)
                dir = new String("NE");
            if(deg > 56.25 && deg <= 78.75)
                dir = new String("ENE");
            if(deg > 78.75 && deg <= 101.25)
                dir = new String("E");
            if(deg > 101.25 && deg <= 123.75)
                dir = new String("ESE");
            if(deg > 123.75 && deg <= 146.25)
                dir = new String("SE");
            if(deg > 146.25 && deg <= 168.75)
                dir = new String("SSE");
            if(deg > 168.75 && deg <= 191.25)
                dir = new String("S");
            if(deg > 191.25 && deg <= 213.75)
                dir = new String("SSW");
            if(deg > 213.75 && deg <= 236.25)
                dir = new String("SW");
            if(deg > 236.25 && deg <= 258.75)
                dir = new String("WSW");
            if(deg > 258.75 && deg <= 281.25)
                dir = new String("W");
            if(deg > 281.25 && deg <= 303.75)
                dir = new String("WNW");
            if(deg > 303.75 && deg <= 326.25)
                dir = new String("NW");
            if(deg > 326.25 && deg < 348.75)
                dir = new String("NNW");

            windString = new SimpleStringProperty("Wind: " + windMap.get("speed") + "m/s, " + dir);

            descrString = new SimpleStringProperty("Details: " + weatherMap.get("description"));
            double tempK = Double.parseDouble(mainMap.get("temp").toString());
            tempK -= 272.15;
            double trunc = BigDecimal.valueOf(tempK).setScale(2, RoundingMode.HALF_UP).doubleValue();
            weatherString = new SimpleStringProperty("Weather: " + weatherMap.get("main") + ", " +
                    (trunc) + "°C");

            double timestamp = Double.parseDouble(respMap.get("dt").toString());
            LocalDateTime ldt = Instant.ofEpochSecond((long)timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();

            timeString = new SimpleStringProperty("Time: "+ ldt.toString());



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

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getLocationString() {
        return locationString.get();
    }

    public StringProperty locationStringProperty() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString.set(locationString);
    }

    public String getTimeString() {
        return timeString.get();
    }

    public StringProperty timeStringProperty() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString.set(timeString);
    }

    public String getWeatherString() {
        return weatherString.get();
    }

    public StringProperty weatherStringProperty() {
        return weatherString;
    }

    public void setWeatherString(String weatherString) {
        this.weatherString.set(weatherString);
    }

    public String getHumidityString() {
        return humidityString.get();
    }

    public StringProperty humidityStringProperty() {
        return humidityString;
    }

    public void setHumidityString(String humidityString) {
        this.humidityString.set(humidityString);
    }

    public String getDescrString() {
        return descrString.get();
    }

    public StringProperty descrStringProperty() {
        return descrString;
    }

    public void setDescrString(String descrString) {
        this.descrString.set(descrString);
    }

    public String getWindString() {
        return windString.get();
    }

    public StringProperty windStringProperty() {
        return windString;
    }

    public void setWindString(String windString) {
        this.windString.set(windString);
    }

}
