package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;
import View.PlayerView;

import java.util.HashMap;
import java.util.List;

/**
 * Model for Deploy Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
public class DeployModel extends OrderModel {
    private final HashMap<String, List<String>> d_args;
    private final PlayerView d_playerView;
    private final int d_reinforcementsBeforeExec;

    /**
     * Constructor for the DeployModel
     */
    public DeployModel(HashMap<String, List<String>> p_args, PlayerModel p_playerModel, PlayerView p_playerView) {
        super("deploy", p_playerModel);

        this.d_playerView = p_playerView;
        this.d_args = p_args;
        this.d_reinforcementsBeforeExec = this.d_currentPlayer.getReinforcements();
    }

    /**
     * Accessor for the country id
     *
     * @return country name issued in this order
     */
    public String getCountryName() {
        return d_args.get("country_name").get(0);
    }

    /**
     * Accessor for the number of Reinforcements the player has issued
     *
     * @return Number of Reinforcements the player has issued
     */
    public int getReinforcements() {
        return (int) (Float.parseFloat(d_args.get("reinforcements_num").get(0)));
    }


    /**
     * Executes the deploy command by placing the armies in the specified country
     *
     * @param p_countries HashMap of the countries
     * @return
     */
    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        String l_countryName = d_args.get("country_name").get(0);

        // handle if the player deploys in a country that it does not owns
        if (!this.d_currentPlayer.containsCountry(l_countryName)) {
            this.d_playerView.InvalidCountry(this.d_args);
            return false; // impossible command
        }

        CountryModel l_country = p_countries.get(this.getCountryName());
        l_country.setArmies(l_country.getArmies() + this.getReinforcements());
        p_countries.put(this.getCountryName(), l_country);
        return true;
    }
}
