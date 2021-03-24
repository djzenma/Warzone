package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;
import Utils.CommandsParser;

import java.util.HashMap;
import java.util.List;

/**
 * Model for Negotiate Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
public class NegotiateModel extends OrderModel {

    private final Player d_targetPlayer;
    HashMap<String, List<String>> d_args;

    /**
     * Constructor for the NegotiateModel
     *
     * @param p_targetPlayer Target player on whom negotiate command would run
     * @param p_player       Current player who is ordering the negotiate command
     */
    public NegotiateModel(Player p_player, Player p_targetPlayer, String[] p_args) {
        super("negotiate", p_player, p_args);
        this.d_targetPlayer = p_targetPlayer;
        this.d_args = CommandsParser.getArguments(p_args);
    }

    /**
     * Executes the negotiate command between the specified countries
     *
     * @param p_countries HashMap of the countries
     * @return true if order is valid; otherwise false
     */
    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        this.d_currentPlayer.addNegotiator(this.d_targetPlayer);
        this.d_targetPlayer.addNegotiator(this.d_currentPlayer);
        triggerEvent(true);
        return true;
    }
}
