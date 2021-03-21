package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;

import java.util.HashMap;

/**
 * Model for Negotiate Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
public class NegotiateModel extends OrderModel {

    private final PlayerModel d_targetPlayerModel;

    /**
     * Constructor for the NegotiateModel
     *
     * @param p_targetPlayerModel Target player on whom negotiate command would run
     * @param p_playerModel       Current player who is ordering the negotiate command
     */
    public NegotiateModel(PlayerModel p_playerModel, PlayerModel p_targetPlayerModel) {
        super("negotiate", p_playerModel);
        this.d_targetPlayerModel = p_targetPlayerModel;
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
        return true;
    }
}
