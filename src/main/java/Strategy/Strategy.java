package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Aggressive player strategy
 */
public abstract class Strategy implements Serializable {
    /**
     * Player
     */
    Player d_player;
    /**
     * Hashmap of the countries
     */
    HashMap<String, CountryModel> d_countries;
    /**
     * Hashmap of the players
     */
    HashMap<String, Player> d_players;

    /**
     * Initialises player, hashmap of countries, hashmap of players
     *
     * @param p_player player object
     * @param p_countries hashmap of countries
     * @param p_players hashmap of players
     */
    public Strategy(Player p_player, HashMap<String, CountryModel> p_countries, HashMap<String, Player> p_players) {
        this.d_player = p_player;
        this.d_countries = p_countries;
        this.d_players = p_players;
    }

    /**
     * Creates the orders
     *
     * @return order model
     */
    public abstract OrderModel createOrder();

    /**
     * Attacking country
     *
     * @return CountryModel
     */
    protected abstract CountryModel attackFrom();

    /**
     * Target country
     *
     * @return country model
     */
    protected abstract CountryModel attackTo();

    /**
     * Returns the strongest neighbor of the strongest country
     *
     * @return country model
     */
    protected abstract CountryModel moveFrom();

    /**
     * The country to defend
     *
     * @return country model
     */
    protected abstract CountryModel defend();

    /**
     * Converts command to the order
     *
     * @param p_args command arguments
     * @return order model
     */
    public OrderModel convertCmdToOrder(String[] p_args) {
        OrderModel l_order = null;

        switch (p_args[0]) {
            case "pass":
                l_order = this.d_player.getCurrentPhase().pass(p_args, this.d_player);
                break;

            case "advance":
                l_order = this.d_player.getCurrentPhase().advance(p_args, this.d_player);
                break;

            case "bomb":
                l_order = this.d_player.getCurrentPhase().bomb(p_args, this.d_player);
                break;

            case "negotiate":
                l_order = this.d_player.getCurrentPhase().negotiate(p_args, this.d_player);
                break;

            case "deploy":
                l_order = this.d_player.getCurrentPhase().deploy(p_args, this.d_player);
                break;

            case "blockade":
                l_order = this.d_player.getCurrentPhase().blockade(p_args, this.d_player);
                break;

            case "airlift":
                l_order = this.d_player.getCurrentPhase().airlift(p_args, this.d_player);
                break;

            case "savegame":
                this.d_player.getCurrentPhase().saveGame(p_args);
                break;

            case "loadgame":
                this.d_player.getCurrentPhase().loadGame(p_args);
                break;

            case "showmap":
            case "showcards":
                l_order = null;
                break;

            default:
                this.d_player.getView().invalidOrder();
        }
        return l_order;
    }
}
