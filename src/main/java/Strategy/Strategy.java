package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;

import java.io.Serializable;
import java.util.HashMap;

public abstract class Strategy implements Serializable {

    Player d_player;
    HashMap<String, CountryModel> d_countries;
    HashMap<String, Player> d_players;

    public Strategy(Player p_player, HashMap<String, CountryModel> p_countries, HashMap<String, Player> p_players) {
        this.d_player = p_player;
        this.d_countries = p_countries;
        this.d_players = p_players;
    }

    public abstract OrderModel createOrder();

    protected abstract CountryModel attackFrom();

    protected abstract CountryModel attackTo();

    protected abstract CountryModel moveFrom();

    protected abstract CountryModel defend();


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
