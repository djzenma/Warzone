package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;

import java.util.HashMap;

/**
 * Model for Blockade Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
public class BlockadeModel extends OrderModel {
    CountryModel d_country;

    /**
     * Constructor for the BlockadeModel
     *
     * @param p_currentPlayerModel
     */
    public BlockadeModel(PlayerModel p_currentPlayerModel, CountryModel p_targetCountry) {
        super("blockade", p_currentPlayerModel);

        this.d_country = p_targetCountry;
    }

    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        // check if the player owns the country to play blockade on
        if (!this.d_currentPlayer.getCountries().contains(this.d_country))
            return false;

        // create neutral player
        PlayerModel d_neutralPlayer = new PlayerModel("Neutral", this.d_currentPlayer.getView(), p_countries);

        // triple the number of armies
        this.d_country.setArmies(this.d_country.getArmies() * 3);

        // set the owner to be neutral
        this.d_country.setOwner(d_neutralPlayer);

        d_neutralPlayer.addCountry(this.d_country);
        this.d_currentPlayer.removeCountry(this.d_country);
        return true;
    }
}
