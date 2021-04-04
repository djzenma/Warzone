package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;

import java.util.HashMap;
import java.util.Random;

public class RandomStrategy extends Strategy {
    private Random d_rand;

    public RandomStrategy(Player p_player,
                          HashMap<String, CountryModel> p_countries,
                          HashMap<String, Player> p_players) {
        super(p_player, p_countries, p_players);
        this.d_rand = new Random();
    }

    @Override
    protected CountryModel attackFrom() {
        return moveFrom();
    }

    @Override
    protected CountryModel attackTo() {
        while (true) {
            CountryModel l_randOwnedCountry = moveFrom();
            Object[] l_neighbors = l_randOwnedCountry.getNeighbors().values().toArray();
            CountryModel l_randomNeighbor = (CountryModel) l_neighbors[d_rand.nextInt(l_neighbors.length)];
            // attack this neighbor if it is owned by an enemy
            if (!l_randomNeighbor.getOwnerName().equals(this.d_player.getName()))
                return l_randomNeighbor;
        }
    }

    @Override
    protected CountryModel moveFrom() {
        return this.d_player.getCountries().get(d_rand.nextInt(this.d_player.getCountries().size()));
    }

    @Override
    protected CountryModel defend() {
        return moveFrom();
    }

    @Override
    public OrderModel createOrder() {
        String[] cmd = new String[0];
        int l_randCmdNumber = d_rand.nextInt(8);

        switch (l_randCmdNumber) {
            case 0:
                cmd = new String[]{"advance",
                        moveFrom().getName(),
                        defend().getName(),
                        String.valueOf(d_rand.nextInt(moveFrom().getArmies() + 1))};
                break;
            case 1:
                cmd = new String[]{"advance",
                        attackFrom().getName(),
                        attackTo().getName(),
                        String.valueOf(d_rand.nextInt(attackFrom().getArmies() + 1))};
                break;
            case 2:
                cmd = new String[]{"bomb", attackTo().getName()};
                break;
            case 3:
                Object[] l_players = this.d_players.values().toArray();
                Player l_randomPlayer = (Player) l_players[d_rand.nextInt(l_players.length)];
                // TODO:: error? negotiate with himself
                cmd = new String[]{"negotiate", l_randomPlayer.getName()};
                break;
            case 4:
                cmd = new String[]{"deploy",
                        defend().getName(),
                        String.valueOf(d_rand.nextInt(this.d_player.getReinforcements() + 1))};
                break;
            case 5:
                cmd = new String[]{"blockade", moveFrom().getName()};
                break;
            case 6:
                cmd = new String[]{"airlift",
                        moveFrom().getName(),
                        defend().getName(),
                        String.valueOf(d_rand.nextInt(moveFrom().getArmies() + 1))};
                break;

            case 7:
                cmd = new String[]{"pass"};
                break;

            default:
                System.out.println("Invalid generated random number! (this will never happen)");
        }

        return convertCmdToOrder(cmd);
    }
}
