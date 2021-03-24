package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;
import Utils.CommandsParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Model for Bomb Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
public class BombModel extends OrderModel {
    PlayerModel d_playerModel;
    CountryModel d_targetCountry;
    HashMap<String, List<String>> d_args;
    ArrayList<CountryModel> d_sourceCountries;

    /**
     * Constructor for the BombModel
     *
     * @param p_args          TODO: remove it as it is not used anywhere or should we write that we are using it to get the user command
     * @param p_targetCountry Target country on which the armies have to be decreased
     * @param p_playerModel   To initialise current player
     */
    public BombModel(PlayerModel p_playerModel, CountryModel p_targetCountry, String[] p_args) {
        super("bomb", p_playerModel, p_args);
        this.d_playerModel = p_playerModel;
        this.d_targetCountry = p_targetCountry;
        this.d_args = CommandsParser.getArguments(p_args);
        this.d_sourceCountries = new ArrayList<>();
    }

    /**
     * Executes the bomb command by reducing the enemy army to half
     *
     * @param p_countries HashMap of the countries
     * @return True, if the bomb command runs successfully ; False otherwise
     */
    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {

        //Checks if the target country is a negotiator of the current player
        if (this.d_currentPlayer.getActiveNegotiators().containsKey(this.d_targetCountry.getOwnerName())) {
            this.d_playerModel.getView().invalidBombOrder(this.d_targetCountry.getOwnerName());
            return false;
        }

        this.d_sourceCountries = this.d_playerModel.getCountries();

        //Checks if source and target countries are same ot not
        for (CountryModel d_sourceCountry : d_sourceCountries) {
            if (d_sourceCountry.getName().equals(this.d_targetCountry.getName())) {
                return false;
            }
        }

        //Checks if the target country is a neighbor and reduces its army to half
        for (CountryModel d_sourceCountry : d_sourceCountries) {
            if (d_sourceCountry.getNeighbors().containsKey(this.d_targetCountry.getName())) {
                this.d_targetCountry.setArmies((int) Math.floor(this.d_targetCountry.getArmies() / 2));
                triggerEvent(true);
                return true;
            }
        }
        return false;
    }
}
