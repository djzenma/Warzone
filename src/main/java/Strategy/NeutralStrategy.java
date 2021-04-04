package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;

import java.util.HashMap;

public class NeutralStrategy extends Strategy {
    public NeutralStrategy(Player p_player, HashMap<String, CountryModel> p_countries, HashMap<String, Player> p_players) {
        super(p_player, p_countries, p_players);
    }

    @Override
    protected CountryModel attackFrom() {
        return null;
    }

    @Override
    protected CountryModel attackTo() {
        return null;
    }

    @Override
    protected CountryModel moveFrom() {
        return null;
    }

    @Override
    protected CountryModel defend() {
        return null;
    }

    @Override
    public OrderModel createOrder() {
        return null;
    }
}
