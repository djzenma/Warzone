package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;
import Utils.SortCountries;

import java.util.ArrayList;
import java.util.HashMap;

public class BenevolentStrategy extends Strategy {
    private int d_counter;

    public BenevolentStrategy(Player p_player, HashMap<String, CountryModel> p_countries, HashMap<String, Player> p_players) {
        super(p_player, p_countries, p_players);
        d_counter = 0;
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
        ArrayList<CountryModel> l_countries = this.d_player.getCountries();
        if (l_countries.size() > 0) {
            l_countries.sort(new SortCountries.SortCountriesDescending());
            return l_countries.get(0);
        } else
            return null;
    }

    @Override
    protected CountryModel defend() {
        ArrayList<CountryModel> l_countries = this.d_player.getCountries();
        if (l_countries.size() > 0) {
            l_countries.sort(new SortCountries.SortCountriesAscending());
            return l_countries.get(0);
        } else
            return null;
    }

    private CountryModel moveTo() {
        ArrayList<CountryModel> l_countries = this.d_player.getCountries();
        if (l_countries.size() > 1) {
            l_countries.sort(new SortCountries.SortCountriesAscending());
            return l_countries.get(1);
        } else
            return null;
    }

    @Override
    public OrderModel createOrder() {
        String[] cmd = new String[0];

        CountryModel l_defendCountry;
        CountryModel l_moveFromCountry;
        CountryModel l_moveToCountry;
        switch (d_counter) {
            case 0:
                l_defendCountry = defend();
                if (l_defendCountry != null) {
                    cmd = new String[]{"deploy",
                            l_defendCountry.getName(),
                            String.valueOf(this.d_player.getReinforcements())};
                    d_counter = (d_counter + 1) % 3;
                    break;
                } else
                    d_counter = (d_counter + 1) % 3;
            case 1:
                l_moveFromCountry = moveFrom();
                l_moveToCountry = moveTo();
                if (l_moveFromCountry != null && l_moveToCountry != null) {
                    cmd = new String[]{
                            "advance",
                            l_moveFromCountry.getName(),
                            l_moveToCountry.getName(),
                            String.valueOf((l_moveFromCountry.getArmies() - l_moveToCountry.getArmies()) / 2)};
                    d_counter = (d_counter + 1) % 3;
                    break;
                } else
                    d_counter = (d_counter + 1) % 3;
            case 2:
                cmd = new String[]{"pass"};
                d_counter = (d_counter + 1) % 3;
                break;
            default:
                System.out.println("Invalid counter! (this will never happen)");
        }
        return convertCmdToOrder(cmd);
    }
}


