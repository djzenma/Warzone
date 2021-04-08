package Adapter;

import Model.ContinentModel;
import Model.CountryModel;
import java.util.LinkedHashMap;

public class MapContainer {
    /**
     * LinkedHashMap of the continents
     */
    private LinkedHashMap<String, ContinentModel> d_continents;
    /**
     * LinkedHashMap of the countries
     */
    private LinkedHashMap<String, CountryModel> d_countries;

    public MapContainer(LinkedHashMap<String, ContinentModel> d_continents, LinkedHashMap<String, CountryModel> d_countries) {
        this.d_continents = d_continents;
        this.d_countries = d_countries;
    }

    public LinkedHashMap<String, ContinentModel> getContinents() {
        return d_continents;
    }

    public LinkedHashMap<String, CountryModel> getCountries() {
        return d_countries;
    }
}
