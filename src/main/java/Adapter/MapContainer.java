package Adapter;

import Model.ContinentModel;
import Model.CountryModel;
import java.util.LinkedHashMap;

/**
 * Contains the map
 */
public class MapContainer {
    /**
     * LinkedHashMap of the continents
     */
    private LinkedHashMap<String, ContinentModel> d_continents;
    /**
     * LinkedHashMap of the countries
     */
    private LinkedHashMap<String, CountryModel> d_countries;

    /**
     * Initialises the continents and countries
     *
     * @param d_continents hashmap of the continents
     * @param d_countries hashmap of the countries
     */
    public MapContainer(LinkedHashMap<String, ContinentModel> d_continents, LinkedHashMap<String, CountryModel> d_countries) {
        this.d_continents = d_continents;
        this.d_countries = d_countries;
    }

    /**
     * Accessor for the continents
     *
     * @return hashmap of the continents
     */
    public LinkedHashMap<String, ContinentModel> getContinents() {
        return d_continents;
    }

    /**
     * Accessor for the countries
     *
     * @return hashmap of the countries
     */
    public LinkedHashMap<String, CountryModel> getCountries() {
        return d_countries;
    }
}
