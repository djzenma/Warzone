package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;
import Utils.CommandsParser;

import java.util.HashMap;

public class HumanStrategy extends Strategy {

    public HumanStrategy(Player p_player,
                         HashMap<String, CountryModel> p_countries,
                         HashMap<String, Player> p_players) {
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
        String[] l_args = this.d_player.getView().takeCommand();

        // if the command is showmap
        if (CommandsParser.isShowMap(l_args)) {
            this.d_player.getCurrentPhase().showMap();
        }

        // if the command is showcards
        else if (CommandsParser.isShowCards(l_args)) {
            this.d_player.getCurrentPhase().showCards(this.d_player.getCards());
        }

        return convertCmdToOrder(l_args);
    }
}
