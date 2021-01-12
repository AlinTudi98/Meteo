package ro.mta.facc.selab.tema2.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

import static org.junit.Assert.*;

public class MeteoModelTest {

    private MeteoModel model;
    String tempString;
    String country;
    String city;
    String locationString;
    String timeString;
    String pressureString;
    String weatherString;
    String humidityString;
    String windString;

    private static Map<String,Object> jsonToMap(String str){
        Map<String,Object> map = new Gson().fromJson(str,new TypeToken<HashMap<String,Object>>() {}.getType());
        return map;
    }

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

    @Before
    public void setUp(){
        model = new MeteoModel("RO","Slatina");

        country = "RO";
        city = "Slatina";
        locationString = "Slatina, RO";

        String APIKey = "815485b1ad9fa554e90bf9cadcc08404";
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + this.city +
                "," + this.country + "&appid=" + APIKey;
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

            Map<String, Object> respMap = jsonToMap(content.toString());
            Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());
            Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());

            List<Object> weatherJSON = (List<Object>) respMap.get("weather");
            Map<String, Object> weatherMap = (Map<String, Object>) weatherJSON.get(0);

            humidityString = "Humidity: " + mainMap.get("humidity") + "%";

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

            windString = "Wind: " + windMap.get("speed") + " m/s, " + dir;

            double tempK = Double.parseDouble(mainMap.get("temp").toString());
            tempK -= 273.15;
            double trunc = BigDecimal.valueOf(tempK).setScale(2, RoundingMode.HALF_UP).doubleValue();
            tempString = "" + trunc + "°C";

            weatherString = "Weather: " + weatherMap.get("main") + ", " + capitalize(weatherMap.get("description").toString());

            pressureString = "Pressure: " + (long)Double.parseDouble(mainMap.get("pressure").toString()) + " hPa";

            double timestamp = Double.parseDouble(respMap.get("dt").toString());
            LocalDateTime ldt = Instant.ofEpochSecond((long)timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM yyyy, HH:mm:ss");
            String formatDateTime = ldt.format(formatter);

            timeString = "Time: "+ formatDateTime;


        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void getPressureString() {
        assertEquals(model.getPressureString(),this.pressureString);
    }

    @Test
    public void getTempString() {
        assertEquals(model.getTempString(),this.tempString);
    }

    @Test
    public void getCountry() {
        assertEquals(model.getCountry(),this.country);
    }

    @Test
    public void getCity() {
        assertEquals(model.getCity(),this.city);
    }

    @Test
    public void getLocationString() {
        assertEquals(model.getLocationString(),this.locationString);
    }

    @Test
    public void getWeatherString() {
        assertEquals(model.getWeatherString(),this.weatherString);
    }

    @Test
    public void getHumidityString() {
        assertEquals(model.getHumidityString(),this.humidityString);
    }

    @Test
    public void getWindString() {
        assertEquals(model.getWindString(),this.windString);
    }
}