package Model;

import java.util.ArrayList;

public class ContinentModel {

    private final ArrayList<CountryModel> d_countries;
    private String d_name;
    private int d_controlValue;
    private int d_id;
    private String d_color;

    public ContinentModel(String p_name, int p_controlValue) {
        this.d_name = p_name;
        this.d_controlValue = p_controlValue;
        d_countries = new ArrayList<CountryModel>();
    }

    public ContinentModel(int p_id, String p_name, int p_controlValue) {
        this.d_id = p_id;
        this.d_name = p_name;
        this.d_controlValue = p_controlValue;
        d_countries = new ArrayList<CountryModel>();
    }

    public ContinentModel() {
        d_countries = new ArrayList<CountryModel>();
    }

    public String getName() {
        return d_name;
    }

    public void setName(String p_name) {
        this.d_name = p_name;
    }

    public int getId() {
        return d_id;
    }

    public ArrayList<CountryModel> getCountries() {
        return d_countries;
    }

    public int getControlValue() {
        return this.d_controlValue;
    }

    public void setControlValue(int p_controlValue) {
        this.d_controlValue = p_controlValue;
    }

    public void addCountry(CountryModel p_country){
        d_countries.add(p_country);
    }
}
