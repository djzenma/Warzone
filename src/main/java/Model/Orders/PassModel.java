package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;

import java.util.HashMap;

/**
 * Model for Pass Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
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

    /**
     * Executes the advance command by placing the armies in the specified country
     *
     * @param p_countries HashMap of the countries
     * @return true
     */
    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        return true;
    }
}
