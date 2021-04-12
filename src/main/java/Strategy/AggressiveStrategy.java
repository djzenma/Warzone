package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;
import Utils.SortCountries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AggressiveStrategy extends Strategy {

    private int d_counter;

    public AggressiveStrategy(Player p_player, HashMap<String, CountryModel> p_countries, HashMap<String, Player> p_players) {
        super(p_player, p_countries, p_players);

        d_counter = 0;
    }

    @Override
    protected CountryModel attackFrom() {
        return defend();
    }

    @Override
    protected CountryModel attackTo() {
        int l_count = 0;
        CountryModel l_ownedCountry = attackFrom();
        while (l_count < l_ownedCountry.getNeighbors().size()) {
            Object[] l_neighbors = l_ownedCountry.getNeighbors().values().toArray();
            CountryModel l_randomNeighbor = (CountryModel) l_neighbors[l_count];
            // attack this neighbor if it is owned by an enemy
            if (!l_randomNeighbor.getOwnerName().equals(this.d_player.getName()))
                return l_randomNeighbor;
            l_count++;
        }
        return null;
    }

    /**
     * Returns the strongest neighbor of the strongest country
     *
     * @return CountryModel of the strongest neighbor of the strongest country
     */
    @Override
    protected CountryModel moveFrom() {
        Object[] l_countries = defend().getNeighbors().values().toArray();
        CountryModel[] l_countriesList = Arrays.copyOf(l_countries,
                l_countries.length,
                CountryModel[].class);

        List<CountryModel> l_list = new ArrayList<CountryModel>(Arrays.asList(l_countriesList));
        l_list.sort(new SortCountries.SortCountriesDescending());

        int l_i = 0;
        while (l_i < l_list.size()) {
            if (l_list.get(l_i).getOwnerName().equals(this.d_player.getName()))
                return l_list.get(l_i);
            l_i++;
        }
        return null;
    }

    @Override
    protected CountryModel defend() {
        ArrayList<CountryModel> l_countries = this.d_player.getCountries();
        l_countries.sort(new SortCountries.SortCountriesDescending());
        return l_countries.get(0);
    }

    @Override
    public OrderModel createOrder() {
        String[] cmd = new String[0];

        CountryModel l_moveFromCountry;
        CountryModel l_attackFromCountry;
        CountryModel l_attackToCountry;

        switch (d_counter) {
            case 0:
                l_attackToCountry = attackTo();
                if (this.d_player.getCards().get("bomb") != 0 && l_attackToCountry != null) {
                    cmd = new String[]{"bomb", l_attackToCountry.getName()};
                    d_counter = (d_counter + 1) % 5;
                    break;
                } else
                    d_counter = (d_counter + 1) % 5;
            case 1:
                cmd = new String[]{"deploy",
                        defend().getName(),
                        String.valueOf(this.d_player.getReinforcements())};
                d_counter = (d_counter + 1) % 5;
                break;
            case 2:
                l_attackFromCountry = attackFrom();
                l_attackToCountry = attackTo();
                if (l_attackToCountry != null) {
                    cmd = new String[]{
                            "advance",
                            l_attackFromCountry.getName(),
                            l_attackToCountry.getName(),
                            String.valueOf(l_attackFromCountry.getArmies())};
                    d_counter = (d_counter + 1) % 5;
                    break;
                } else
                    d_counter = (d_counter + 1) % 5;
            case 3:
                l_moveFromCountry = moveFrom();
                if (l_moveFromCountry != null) {
                    cmd = new String[]{
                            "advance",
                            l_moveFromCountry.getName(),
                            defend().getName(),
                            String.valueOf(l_moveFromCountry.getArmies())};
                    d_counter = (d_counter + 1) % 5;
                    break;
                } else
                    d_counter = (d_counter + 1) % 5;
            case 4:
                cmd = new String[]{"pass"};
                d_counter = (d_counter + 1) % 5;
                break;

            default:
                System.out.println("Invalid counter! (this will never happen)");
        }
        return convertCmdToOrder(cmd);
    }
}

