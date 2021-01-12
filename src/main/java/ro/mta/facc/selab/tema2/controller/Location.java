package ro.mta.facc.selab.tema2.controller;

/**
 * Class used to store identification information from the
 * inputFile.txt file. This class is used to also populate
 * the values in the countries and cities ComboBoxes.
 *
 * @author Alin Tudose
 */
class Location {
    /**
     * ID The city id from the OpenWeather.org API
     * city The name of the city
     * country The ISO Country Code, or the full country name
     * lat The geographic latitude of the city
     * lon The geographic longitute of the city
     */
    public long ID;
    public String city, country;
    public double lat, lon;
}
