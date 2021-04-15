package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;
import Utils.SortCountries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Aggressive player strategy
 */
public class AggressiveStrategy extends Strategy {
    /**
     * Counter for the orders
     */
    private int d_counter;

    /**
     * Initialises player, hashmap of countries, hashmap of players, counter
     *
     * @param p_player player object
     * @param p_countries hashmap of countries
     * @param p_players hashmap of players
     */
    public AggressiveStrategy(Player p_player, HashMap<String, CountryModel> p_countries, HashMap<String, Player> p_players) {
        super(p_player, p_countries, p_players);

        d_counter = 0;
    }

    /**
     * Attacking country
     *
     * @return CountryModel
     */
    @Override
    protected CountryModel attackFrom() {
        return defend();
    }

    /**
     * Target country
     *
     * @return CountryModel of the target country
     */
    @Override
    protected CountryModel attackTo() {
        int l_count = 0;
        CountryModel l_ownedCountry = attackFrom();
        if (l_ownedCountry != null) {
            while (l_count < l_ownedCountry.getNeighbors().size()) {
                Object[] l_neighbors = l_ownedCountry.getNeighbors().values().toArray();
                CountryModel l_randomNeighbor = (CountryModel) l_neighbors[l_count];
                // attack this neighbor if it is owned by an enemy
                if (!l_randomNeighbor.getOwnerName().equals(this.d_player.getName()))
                    return l_randomNeighbor;
                l_count++;
            }
            return null;
        } else
            return null;
    }

    /**
     * Returns the strongest neighbor of the strongest country
     *
     * @return CountryModel of the strongest neighbor of the strongest country
     */
    @Override
    protected CountryModel moveFrom() {
        CountryModel l_defendCountry = defend();
        if (l_defendCountry != null) {
            Object[] l_countries = l_defendCountry.getNeighbors().values().toArray();
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
        } else
            return null;
    }

    /**
     * The country to defend
     *
     * @return the defending country
     */
    @Override
    protected CountryModel defend() {
        ArrayList<CountryModel> l_countries = this.d_player.getCountries();
        if (l_countries.size() != 0) {
            l_countries.sort(new SortCountries.SortCountriesDescending());
            return l_countries.get(0);
        } else
            return null;
    }

    /**
     * Creates the orders
     *
     * @return orders
     */
    @Override
    public OrderModel createOrder() {
        String[] cmd = new String[0];

        CountryModel l_moveFromCountry;
        CountryModel l_attackFromCountry;
        CountryModel l_attackToCountry;
        CountryModel l_defendCountry;

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
                l_defendCountry = defend();
                if (l_defendCountry != null) {
                    cmd = new String[]{"deploy",
                            l_defendCountry.getName(),
                            String.valueOf(this.d_player.getReinforcements())};
                    d_counter = (d_counter + 1) % 5;
                    break;
                } else
                    d_counter = (d_counter + 1) % 5;
            case 2:
                l_attackFromCountry = attackFrom();
                l_attackToCountry = attackTo();
                if (l_attackToCountry != null && l_attackFromCountry != null) {
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
                l_defendCountry = defend();
                if (l_moveFromCountry != null && l_defendCountry != null) {
                    cmd = new String[]{
                            "advance",
                            l_moveFromCountry.getName(),
                            l_defendCountry.getName(),
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

