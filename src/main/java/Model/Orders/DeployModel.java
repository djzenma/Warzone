package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;
import Utils.CommandsParser;
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
     *
     * @param p_player     initialise the current player
     * @param p_args       initialise the user command
     * @param p_playerView to check the validity of the command
     */
    public DeployModel(String[] p_args, Player p_player, PlayerView p_playerView) {
        super("deploy", p_player, p_args);

        this.d_playerView = p_playerView;
        this.d_args = CommandsParser.getArguments(p_args);
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
     * @return true if order is valid; otherwise false
     */
    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        String l_countryName = d_args.get("country_name").get(0);

        // handle if the player deploys in a country that it does not owns
        if (!this.d_currentPlayer.containsCountry(l_countryName)) {
            this.d_playerView.invalidCountry(d_currentPlayer.getName(), l_countryName);
            return false; // impossible command
        }

        // handle if the player has enough reinforcements to deploy
        int l_requestedReinforcements = Integer.parseInt(d_args.get("reinforcements_num").get(0));

        if (l_requestedReinforcements > this.d_reinforcementsBeforeExec) {
            d_currentPlayer.getView().notEnoughReinforcements(d_args, d_currentPlayer.getReinforcements());
            return false; // impossible command
        }

        triggerEvent(true);

        //deploys the army and add them.
        CountryModel l_country = p_countries.get(this.getCountryName());
        l_country.setArmies(l_country.getArmies() + this.getReinforcements());
        p_countries.put(this.getCountryName(), l_country);
        return true;
    }
}
