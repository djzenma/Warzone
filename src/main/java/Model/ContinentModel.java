package Model;

import java.util.ArrayList;

public class ContinentModel {
    private String d_name;
    private int d_controlValue;
    private ArrayList<CountryModel> d_countries;

    public ContinentModel(String p_name, int p_controlValue) {
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

    public ArrayList<CountryModel> getCountries(){
        return d_countries;
    }

    public int getControlValue() {
        return this.d_controlValue;
    }

    public void setControlValue(int d_controlValue) {
        this.d_controlValue = d_controlValue;
    }

    public void addCountry(CountryModel p_country){
        d_countries.add(p_country);
    }
}
