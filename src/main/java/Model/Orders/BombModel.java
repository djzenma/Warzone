package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BombModel extends OrderModel {
    PlayerModel d_playerModel;
    CountryModel d_targetCountry;
    HashMap<String, List<String>> d_args;
    ArrayList<CountryModel> d_sourceCountries;

    /**
     * Constructor of the OrderModel
     *
     * @param p_playerModel
     * @param p_targetCountry
     */
    public BombModel(PlayerModel p_playerModel, CountryModel p_targetCountry, HashMap<String, List<String>> p_args) {
        super("bomb", p_playerModel);
        this.d_playerModel = p_playerModel;
        this.d_targetCountry = p_targetCountry;
        this.d_args = p_args;
        this.d_sourceCountries = new ArrayList<>();
    }

    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        this.d_sourceCountries = this.d_playerModel.getCountries();

        for (int l_i = 0; l_i < d_sourceCountries.size(); l_i++) {
            System.out.println(this.d_sourceCountries.get(l_i).getName() + "--" + this.d_targetCountry.getName());
            if (this.d_sourceCountries.get(l_i).getName().equals(this.d_targetCountry.getName())) {
                return false;
            }
        }

        for (int l_j = 0; l_j < d_sourceCountries.size(); l_j++) {
            System.out.println(this.d_sourceCountries.get(l_j).getName() + "--" + this.d_targetCountry.getName());
            if (this.d_sourceCountries.get(l_j).getNeighbors().containsKey(this.d_targetCountry.getName())) {
                this.d_targetCountry.setArmies((int) Math.floor(this.d_targetCountry.getArmies() / 2));
                return true;
            }
        }
        return false;
    }
}
