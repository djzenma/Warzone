package Model;

import java.util.LinkedHashMap;

/**
 * Maintains the state of a continent
 */
public class ContinentModel {

    private final LinkedHashMap<String, CountryModel> COUNTRIES;
    private final String NAME;
    private final int CONTROL_VALUE;
    private int d_id;
    private String d_color;

    /**
     * Initializes continent parameters such as name, control value and list of countries
     *
     * @param p_name         Name of the continent
     * @param p_controlValue Number of bonus reinforcements every player will get if it owns all the countries of a continent
     */
    public ContinentModel(String p_name, int p_controlValue) {
        this.NAME = p_name;
        this.CONTROL_VALUE = p_controlValue;
        COUNTRIES = new LinkedHashMap<>();
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
        this.NAME = p_name;
        this.d_color = p_color;
        this.CONTROL_VALUE = p_controlValue;
        COUNTRIES = new LinkedHashMap<>();
    }

    /**
     * Accessor for the countries of the continent
     *
     * @return Countries of the continent
     */
    public LinkedHashMap<String, CountryModel> getCountries() {
        return COUNTRIES;
    }

    /**
     * Accessor for the control value of the continent
     *
     * @return Control value of the continent
     */
    public int getControlValue() {
        return this.CONTROL_VALUE;
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
        return NAME;
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
        COUNTRIES.put(p_country.getName(), p_country);
    }
}
