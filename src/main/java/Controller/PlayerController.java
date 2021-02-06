package Controller;

import Model.CountryModel;
import Model.PlayerModel;

public class PlayerController {
    private PlayerModel d_playerModel;

    public PlayerController(String p_name) {
        this.d_playerModel = new PlayerModel(p_name);
    }

    public void assignCountry(CountryModel countryModel) {
        this.d_playerModel.setCountry(countryModel);
    }
}
