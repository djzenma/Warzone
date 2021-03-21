package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;

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
     * @param p_args TODO: remove it as it is not used anywhere
     */
    public BombModel(PlayerModel p_playerModel, CountryModel p_targetCountry, HashMap<String, List<String>> p_args) {
        super("bomb", p_playerModel);
        this.d_playerModel = p_playerModel;
        this.d_targetCountry = p_targetCountry;
        this.d_args = p_args;
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
        for (int l_i = 0; l_i < d_sourceCountries.size(); l_i++) {
            if (this.d_sourceCountries.get(l_i).getName().equals(this.d_targetCountry.getName())) {
                return false;
            }
        }

        //Checks if the target country is a neighbor and reduces its army to half
        for (int l_j = 0; l_j < d_sourceCountries.size(); l_j++) {
            if (this.d_sourceCountries.get(l_j).getNeighbors().containsKey(this.d_targetCountry.getName())) {
                this.d_targetCountry.setArmies((int) Math.floor(this.d_targetCountry.getArmies() / 2));
                return true;
            }
        }
        return false;
    }
}
