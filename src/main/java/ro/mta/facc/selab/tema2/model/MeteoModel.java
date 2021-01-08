package ro.mta.facc.selab.tema2.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MeteoModel {
    StringProperty country, city, locationString;
    StringProperty timeString;
    StringProperty weatherString;
    StringProperty humidityString, precipitationString, windString;

    IntegerProperty temp, precipitation, wind;

    public MeteoModel(String country, String city) {
        this.country = new SimpleStringProperty(country);
        this.city = new SimpleStringProperty(city);

        String loc = city + ", " + country.toUpperCase();
        this.locationString = new SimpleStringProperty(loc);
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

    public String getPrecipitationString() {
        return precipitationString.get();
    }

    public StringProperty precipitationStringProperty() {
        return precipitationString;
    }

    public void setPrecipitationString(String precipitationString) {
        this.precipitationString.set(precipitationString);
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

    public int getTemp() {
        return temp.get();
    }

    public IntegerProperty tempProperty() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp.set(temp);
    }

    public int getPrecipitation() {
        return precipitation.get();
    }

    public IntegerProperty precipitationProperty() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation.set(precipitation);
    }

    public int getWind() {
        return wind.get();
    }

    public IntegerProperty windProperty() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind.set(wind);
    }
}
