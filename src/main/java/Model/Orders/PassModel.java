package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;

import java.util.HashMap;

public class PassModel extends OrderModel {
    /**
     * Constructor of the OrderModel
     *
     * @param p_player to initialise current player
     * @param p_args   array of the command arguments
     */
    public PassModel(Player p_player, String[] p_args) {
        super("pass", p_player, p_args);
    }

    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        return true;
    }
}
