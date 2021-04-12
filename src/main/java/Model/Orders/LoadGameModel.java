package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;

import java.util.HashMap;

public class LoadGameModel extends OrderModel {
    /**
     * Constructor of the OrderModel
     *
     * @param p_player  to initialise current player
     * @param p_args    array of the command arguments
     */
    public LoadGameModel(Player p_player, String[] p_args) {
        super("loadgame", p_player, p_args);
    }

    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        return true;
    }
}

