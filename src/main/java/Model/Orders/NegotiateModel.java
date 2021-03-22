package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;
import Utils.CommandsParser;

import java.util.HashMap;
import java.util.List;

/**
 * Model for Negotiate Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
public class NegotiateModel extends OrderModel {

    private final PlayerModel d_targetPlayerModel;
    HashMap<String, List<String>> d_args;

    /**
     * Constructor for the NegotiateModel
     *
     * @param p_targetPlayerModel Target player on whom negotiate command would run
     * @param p_playerModel       Current player who is ordering the negotiate command
     */
    public NegotiateModel(PlayerModel p_playerModel, PlayerModel p_targetPlayerModel, String[] p_args) {
        super("negotiate", p_playerModel, p_args);
        this.d_targetPlayerModel = p_targetPlayerModel;
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
        this.d_currentPlayer.addNegotiator(this.d_targetPlayerModel);
        this.d_targetPlayerModel.addNegotiator(this.d_currentPlayer);
        triggerEvent(true);
        return true;
    }
}
