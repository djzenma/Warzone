package Model;

import java.util.ArrayList;

public class ContinentModel {

    private final ArrayList<CountryModel> d_countries;
    private String d_name;
    private int d_controlValue;
    private int d_id;
    private String d_color;

    /**
     * Constructor for the ContinentModel
     * @param p_name name of the continent
     * @param p_controlValue number of bonus reinforcements every player will get if it owns all the countries of a continent
     */
    public ContinentModel(String p_name, int p_controlValue) {
        this.d_name = p_name;
        this.d_controlValue = p_controlValue;
        d_countries = new ArrayList<CountryModel>();
    }

    /**
     * Constructor for the ContinentModel
     * @param p_id id of the continent
     * @param p_name name of the continent
     * @param p_controlValue number of bonus reinforcements every player will get if it owns all the countries of a continent
     */
    public ContinentModel(int p_id, String p_name, int p_controlValue) {
        this.d_id = p_id;
        this.d_name = p_name;
        this.d_controlValue = p_controlValue;
        d_countries = new ArrayList<CountryModel>();
    }

    /**
     * Gets the list of all the countries in a continent
     * @return list of all the countries in a continent
     */
    public ArrayList<CountryModel> getCountries() {
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
     * Adds the country to the list of the existing countries of a continent
     * @param p_country object of the Country
     */
    public void addCountry(CountryModel p_country){
        d_countries.add(p_country);
    }
}
