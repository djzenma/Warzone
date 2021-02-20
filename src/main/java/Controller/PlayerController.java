package Controller;

import Model.CountryModel;
import Model.PlayerModel;
import View.PlayerView;

/**
 *
 */
public class PlayerController {
    private PlayerModel d_playerModel;

    /**
     * Constructor for the PlayerController
     * @param p_name name of the player
     */
    public PlayerController(String p_name) {
        this.d_playerModel = new PlayerModel(p_name, new PlayerView());
    }

    /**
     * Assigns the country to the player
     * @param countryModel object of the CountryModel
     */
    public void assignCountry(CountryModel countryModel) {
        this.d_playerModel.addCountry(countryModel);
    }

    /*public boolean issueOrder(){
        OrderController l_order = new DeployController();

    }

    public OrderModel nextOrder(){

    }*/
}
