package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;

import java.util.HashMap;
import java.util.Random;

public class CheaterStrategy extends Strategy {
    public CheaterStrategy(Player p_player, HashMap<String, CountryModel> p_countries, HashMap<String, Player> p_players) {
        super(p_player, p_countries, p_players);
    }

    @Override
    public OrderModel createOrder() {

        for (CountryModel l_country : this.d_player.getCountries()) {
            for (CountryModel l_neighbor : l_country.getNeighbors().values()) {
                if (!l_neighbor.getOwnerName().equals(this.d_player.getName())) {
                    this.d_player.addCountry(l_neighbor);
                    l_neighbor.getOwner().removeCountry(l_neighbor);
                    l_neighbor.setOwner(this.d_player);
                }
            }
        }

        for (CountryModel l_country : this.d_player.getCountries()) {
            for (CountryModel l_neighbor : l_country.getNeighbors().values()) {
                if (!l_neighbor.getOwnerName().equals(this.d_player.getName())) {
                    this.d_player.addCountry(l_neighbor);
                    l_neighbor.getOwner().removeCountry(l_neighbor);
                    l_neighbor.setOwner(this.d_player);
                }
            }
        }

        int l_reinforcements = this.d_player.getReinforcements();
        if (l_reinforcements > 0) {
            return convertCmdToOrder(new String[]{"deploy",
                    defend().getName(),
                    String.valueOf(l_reinforcements)});
        } else
            return convertCmdToOrder(new String[]{"pass"});
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
        return this.d_player.getCountries().get(new Random().nextInt(this.d_player.getCountries().size()));
    }
}
