package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;

import java.util.HashMap;

public class NegotiateModel extends OrderModel {

    private final PlayerModel d_targetPlayerModel;

    /**
     * Constructor of the OrderModel
     *
     * @param p_playerModel Current player
     */
    public NegotiateModel(PlayerModel p_playerModel, PlayerModel p_targetPlayerModel) {
        super("negotiate", p_playerModel);
        this.d_targetPlayerModel = p_targetPlayerModel;
    }

    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        this.d_currentPlayer.addNegotiator(this.d_targetPlayerModel);
        this.d_targetPlayerModel.addNegotiator(this.d_currentPlayer);
        return true;
    }
}
