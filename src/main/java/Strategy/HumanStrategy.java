package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;
import Utils.CommandsParser;

import java.util.HashMap;

/**
 * Human player strategy
 */
public class HumanStrategy extends Strategy {
    /**
     * Initialises player, hashmap of countries, hashmap of players
     *
     * @param p_player player object
     * @param p_countries hashmap of countries
     * @param p_players hashmap of players
     */
    public HumanStrategy(Player p_player,
                         HashMap<String, CountryModel> p_countries,
                         HashMap<String, Player> p_players) {
        super(p_player, p_countries, p_players);
    }

    /**
     * Attacking country
     *
     * @return null
     */
    @Override
    protected CountryModel attackFrom() {
        return null;
    }

    /**
     * Target country
     *
     * @return null
     */
    @Override
    protected CountryModel attackTo() {
        return null;
    }

    /**
     * Returns the strongest neighbor of the strongest country
     *
     * @return null
     */
    @Override
    protected CountryModel moveFrom() {
        return null;
    }

    /**
     * The country to defend
     *
     * @return null
     */
    @Override
    protected CountryModel defend() {
        return null;
    }

    /**
     * Creates the orders
     *
     * @return orders
     */
    @Override
    public OrderModel createOrder() {
        String[] l_args = this.d_player.getView().takeCommand();

        // if the command is showmap
        if (CommandsParser.isShowMap(l_args)) {
            this.d_player.getCurrentPhase().showMap();
        }

        // if the command is showcards
        else if (CommandsParser.isShowCards(l_args)) {
            this.d_player.getCurrentPhase().showCards(this.d_player.getCards());
        }

        return convertCmdToOrder(l_args);
    }
}
