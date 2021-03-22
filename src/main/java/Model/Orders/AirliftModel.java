package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;
import View.PlayerView;

import java.util.HashMap;
import java.util.List;

/**
 * Model for AirLift Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
public class AirliftModel extends OrderModel {
    private final CountryModel d_sourceCountry;
    private final CountryModel d_targetCountry;
    private final PlayerView d_playerView;
    private final int d_numArmies;
    HashMap<String, List<String>> d_args;

    /**
     * Constructor of the AirLift
     *
     * @param p_sourceCountry source country from which the army has to be advanced
     * @param p_targetCountry target country on which the army has to be advanced
     * @param p_numArmies     the number of armies that have to be advanced
     * @param p_currentPlayer player that advances the armies
     * @param p_playerView    to check the validity of the command
     * @param p_args          Gets the user command
     */
    public AirliftModel(CountryModel p_sourceCountry, CountryModel p_targetCountry, int p_numArmies, PlayerModel p_currentPlayer, PlayerView p_playerView, HashMap<String, List<String>> p_args) {
        super("airlift", p_currentPlayer);
        this.d_sourceCountry = p_sourceCountry;
        this.d_targetCountry = p_targetCountry;
        this.d_numArmies = p_numArmies;
        this.d_playerView = p_playerView;
        this.d_args = p_args;
        setCurrentPlayer(p_currentPlayer);
    }

    /**
     * Executes the AirLift command by placing the armies in the specified country
     *
     * @param p_countries HashMap of the countries
     * @return true if order is valid; otherwise false
     */
    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        // if the source does not have enough armies
        if (this.d_numArmies > this.d_sourceCountry.getArmies()) {
            this.d_playerView.insufficientArmies(this.d_args, this.d_sourceCountry.getArmies());
            return false;
        }

        // if the source country doesn't belong to the player
        if (!this.getCurrentPlayer().getCountries().contains(this.d_sourceCountry)) {
            this.d_playerView.InvalidCountry(this.d_args);
            return false;
        }

        // normal move without attack
        if (this.getCurrentPlayer().getCountries().contains(this.d_targetCountry)) {
            this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - this.d_numArmies);
            this.d_targetCountry.setArmies(this.d_targetCountry.getArmies() + this.d_numArmies);
            p_countries.put(this.d_targetCountry.getName(), this.d_targetCountry);
            return true;
        } else
            return false;
    }
}
