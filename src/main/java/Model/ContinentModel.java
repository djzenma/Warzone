package Model;

import java.util.LinkedHashMap;

public class ContinentModel {

    private final LinkedHashMap<String, CountryModel> d_countries;
    private final String d_name;
    private final int d_controlValue;
    private int d_id;
    private String d_color;

    /**
     * Constructor for the ContinentModel
     *
     * @param p_name         name of the continent
     * @param p_controlValue number of bonus reinforcements every player will get if it owns all the countries of a continent
     */
    public ContinentModel(String p_name, int p_controlValue) {
        this.d_name = p_name;
        this.d_controlValue = p_controlValue;
        d_countries = new LinkedHashMap<String, CountryModel>();
    }

    /**
     * Constructor for the ContinentModel
     *
     * @param p_id           id of the continent
     * @param p_name         name of the continent
     * @param p_controlValue number of bonus reinforcements every player will get if it owns all the countries of a continent
     * @param p_color        color of the continent
     */
    public ContinentModel(int p_id, String p_name, int p_controlValue, String p_color) {
        this.d_id = p_id;
        this.d_name = p_name;
        this.d_color = p_color;
        this.d_controlValue = p_controlValue;
        d_countries = new LinkedHashMap<String, CountryModel>();
    }

    /**
     * Gets the list of all the countries in a continent
     *
     * @return HashMap of all the country objects in a continent
     */
    public LinkedHashMap<String, CountryModel> getCountries() {
        return d_countries;
    }

    /**
     * Gets the number of bonus reinforcements every player will get if it owns all the countries of a continent
     * @return number of bonus reinforcements every player will get if it owns all the countries of a continent
     */
    public int getControlValue() {
        return this.d_controlValue;
    }

    /**
     * Adds the country to the hashmap of the existing countries of a continent
     *
     * @param p_country object of the Country
     */
    public void addCountry(CountryModel p_country) {
        d_countries.put(p_country.getName(), p_country);
    }

    public int getId() {
        return d_id;
    }

    public String getName() {
        return d_name;
    }

    public String getColor() {
        return d_color;
    }

    public void setId(int p_id) {
        this.d_id= p_id;
    }
}
