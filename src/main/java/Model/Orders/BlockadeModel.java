package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;
import Utils.CommandsParser;

import java.util.HashMap;
import java.util.List;

/**
 * Model for Blockade Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
public class BlockadeModel extends OrderModel {
    CountryModel d_country;
    HashMap<String, List<String>> d_args;
    PlayerModel d_neutralPlayer;

    /**
     * Constructor for the BlockadeModel
     *
     * @param p_targetCountry      target country on which the armies have to be increased and neutralised
     * @param p_currentPlayerModel to initialise current player
     */
    public BlockadeModel(PlayerModel p_currentPlayerModel, PlayerModel p_neutralPlayer, CountryModel p_targetCountry, String[] p_args) {
        super("blockade", p_currentPlayerModel, p_args);
        this.d_neutralPlayer = p_neutralPlayer;
        this.d_country = p_targetCountry;
        this.d_args = CommandsParser.getArguments(p_args);
    }

    /**
     * Executes the blockade command by tripling the army and neutralising the country
     *
     * @param p_countries HashMap of the countries
     * @return true if order is valid; otherwise false
     */
    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {

        // check if the player owns the country to play blockade on
        if (!this.d_currentPlayer.getCountries().contains(this.d_country))
            return false;

        triggerEvent(true);

        // triple the number of armies
        this.d_country.setArmies(this.d_country.getArmies() * 3);

        // set the owner to be neutral
        this.d_country.setOwner(this.d_neutralPlayer);

        d_neutralPlayer.addCountry(this.d_country);
        this.d_currentPlayer.removeCountry(this.d_country);
        return true;
    }
}
