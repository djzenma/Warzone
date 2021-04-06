package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;

import java.util.ArrayList;
import java.util.Comparator;
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
        l_countries.sort(new SortCountriesDescending());
        return l_countries.get(0);
    }

    @Override
    protected CountryModel defend() {
        ArrayList<CountryModel> l_countries = this.d_player.getCountries();
        l_countries.sort(new SortCountriesAscending());
        return l_countries.get(0);
    }

    private CountryModel moveTo() {
        ArrayList<CountryModel> l_countries = this.d_player.getCountries();
        l_countries.sort(new SortCountriesAscending());
        return l_countries.get(1);
    }

    @Override
    public OrderModel createOrder() {
        String[] cmd = new String[0];
        switch (d_counter) {
            case 0:
                cmd = new String[]{"deploy",
                        defend().getName(),
                        String.valueOf(this.d_player.getReinforcements())};
                d_counter = (d_counter + 1) % 3;
                break;
            case 1:
                cmd = new String[]{
                        "advance",
                        moveFrom().getName(),
                        moveTo().getName(),
                        String.valueOf((moveFrom().getArmies() - moveTo().getArmies()) / 2)};
                d_counter = (d_counter + 1) % 3;
                break;
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

class SortCountriesDescending implements Comparator<CountryModel> {
    @Override
    public int compare(CountryModel o1, CountryModel o2) {
        return o2.getArmies() - o1.getArmies();
    }
}

class SortCountriesAscending implements Comparator<CountryModel> {
    @Override
    public int compare(CountryModel o1, CountryModel o2) {
        return o1.getArmies() - o2.getArmies();
    }
}

