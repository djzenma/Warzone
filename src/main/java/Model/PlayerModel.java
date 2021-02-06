package Model;

public class PlayerModel {
    private String d_name;
    private CountryModel d_country;

    public PlayerModel(String p_name) {
        this.setName(p_name);
    }

    public void setName(String p_name) {
        this.d_name = p_name;
    }

    public void setCountry(CountryModel countryModel) {
        this.d_country = countryModel;
    }
}
