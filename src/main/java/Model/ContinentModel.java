package Model;

import java.util.LinkedHashMap;

/**
 * Maintains the state of a continent
 */
public class ContinentModel {
    /**
     * LinkedHashMap of the countries
     */
    private final LinkedHashMap<String, CountryModel> d_countries;
    /**
     * Name of the continent
     */
    private final String d_name;
    /**
     * Control value of the continent
     */
    private final int d_controlValue;
    /**
     * Id of the continent
     */
    private int d_id;
    /**
     * Color of the continent
     */
    private String d_color;

    /**
     * Initializes continent parameters such as name, control value and list of countries
     *
     * @param p_name         Name of the continent
     * @param p_controlValue Number of bonus reinforcements every player will get if it owns all the countries of a continent
     */
    public ContinentModel(String p_name, int p_controlValue) {
        this.d_name = p_name;
        this.d_controlValue = p_controlValue;
        d_countries = new LinkedHashMap<>();
    }

    /**
     * Initializes continent parameters such as id, name, control value, color and list of countries
     *
     * @param p_id           Id of the continent
     * @param p_name         Name of the continent
     * @param p_controlValue Number of bonus reinforcements every player will get if it owns all the countries of a continent
     * @param p_color        Color of the continent
     */
    public ContinentModel(int p_id, String p_name, int p_controlValue, String p_color) {
        this.d_id = p_id;
        this.d_name = p_name;
        this.d_color = p_color;
        this.d_controlValue = p_controlValue;
        d_countries = new LinkedHashMap<>();
    }

    /**
     * Accessor for the countries of the continent
     *
     * @return Countries of the continent
     */
    public LinkedHashMap<String, CountryModel> getCountries() {
        return d_countries;
    }

    /**
     * Accessor for the control value of the continent
     *
     * @return Control value of the continent
     */
    public int getControlValue() {
        return this.d_controlValue;
    }

    /**
     * Accessor for the continent id
     *
     * @return Id of the continent
     */
    public int getId() {
        return d_id;
    }

    /**
     * Mutator fot the continent id
     *
     * @param p_id Id of the continent
     */
    public void setId(int p_id) {
        this.d_id = p_id;
    }

    /**
     * Accessor for the continent name
     *
     * @return Name of the continent
     */
    public String getName() {
        return d_name;
    }

    /**
     * Accessor for the continent color
     *
     * @return Color of the continent
     */
    public String getColor() {
        return d_color;
    }

    /**
     * Adds a country to this continent
     *
     * @param p_country Country to be added
     */
    public void addCountry(CountryModel p_country) {
        d_countries.put(p_country.getName(), p_country);
    }

    /**
     * Removes the country from list of countries of a continent
     *
     * @param p_countryName
     */
    public void removeCountry(String p_countryName) {
        this.d_countries.remove(p_countryName);
    }
}
