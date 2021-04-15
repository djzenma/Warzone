package Strategy;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;

import java.util.HashMap;
import java.util.Random;

/**
 * Random player strategy
 */
public class RandomStrategy extends Strategy {
    /**
     * Random number
     */
    private final Random d_rand;

    /**
     * Initialises player, hashmap of countries, hashmap of players, random number
     *
     * @param p_player player object
     * @param p_countries hashmap of countries
     * @param p_players hashmap of players
     */
    public RandomStrategy(Player p_player,
                          HashMap<String, CountryModel> p_countries,
                          HashMap<String, Player> p_players) {
        super(p_player, p_countries, p_players);
        this.d_rand = new Random();
    }

    /**
     * Attacking country
     *
     * @return CountryModel
     */
    @Override
    protected CountryModel attackFrom() {
        return moveFrom();
    }

    /**
     * Target country
     *
     * @return CountryModel of the target country
     */
    @Override
    protected CountryModel attackTo() {
        int l_limit = 0;
        while (l_limit < 100) {
            CountryModel l_randOwnedCountry = moveFrom();
            if (l_randOwnedCountry != null) {
                Object[] l_neighbors = l_randOwnedCountry.getNeighbors().values().toArray();
                CountryModel l_randomNeighbor = (CountryModel) l_neighbors[d_rand.nextInt(l_neighbors.length)];
                // attack this neighbor if it is owned by an enemy
                if (!l_randomNeighbor.getOwnerName().equals(this.d_player.getName()))
                    return l_randomNeighbor;
            }
            l_limit++;
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
        if (this.d_player.getCountries().size() != 0)
            return this.d_player.getCountries().get(d_rand.nextInt(this.d_player.getCountries().size()));
        else
            return null;
    }

    /**
     * The country to defend
     *
     * @return the defending country
     */
    @Override
    protected CountryModel defend() {
        return moveFrom();
    }

    /**
     * Creates the orders
     *
     * @return orders
     */
    @Override
    public OrderModel createOrder() {
        String[] cmd = new String[0];
        int l_randCmdNumber = d_rand.nextInt(8);

        CountryModel l_moveFromCountry;
        CountryModel l_attackFromCountry;
        CountryModel l_attackToCountry;
        CountryModel l_defendCountry;

        switch (l_randCmdNumber) {
            case 0:
                l_moveFromCountry = moveFrom();
                l_defendCountry = defend();
                if (l_moveFromCountry != null && l_defendCountry != null) {
                    cmd = new String[]{"advance",
                            l_moveFromCountry.getName(),
                            l_defendCountry.getName(),
                            String.valueOf(d_rand.nextInt(l_moveFromCountry.getArmies() + 1))};
                }
                else
                    cmd = new String[]{"pass"};
                break;
            case 1:
                l_attackFromCountry = attackFrom();
                l_attackToCountry = attackTo();
                if (l_attackToCountry != null && l_attackFromCountry != null)
                    cmd = new String[]{"advance",
                            l_attackFromCountry.getName(),
                            l_attackToCountry.getName(),
                            String.valueOf(d_rand.nextInt(l_attackFromCountry.getArmies() + 1))};
                else
                    cmd = new String[]{"pass"};
                break;
            case 2:
                l_attackToCountry = attackTo();
                if (l_attackToCountry != null)
                    cmd = new String[]{"bomb", l_attackToCountry.getName()};
                else
                    cmd = new String[]{"pass"};
                break;
            case 3:
                Object[] l_players = this.d_players.values().toArray();
                if(l_players.length != 0) {
                    // choose a random player that is not itself
                    int l_limit = 0;
                    Player l_randomPlayer;
                    do {
                        l_randomPlayer = (Player) l_players[d_rand.nextInt(l_players.length)];
                        l_limit++;
                    } while (l_limit < 20 && !l_randomPlayer.getName().equals(this.d_player.getName()));

                    cmd = new String[]{"negotiate", l_randomPlayer.getName()};
                }
                else
                    cmd = new String[]{"pass"};
                break;
            case 4:
                l_defendCountry = defend();
                if (l_defendCountry != null) {
                    cmd = new String[]{"deploy",
                            l_defendCountry.getName(),
                            String.valueOf(d_rand.nextInt(this.d_player.getReinforcements() + 1))};
                }
                else
                    cmd = new String[]{"pass"};
                break;
            case 5:
                l_moveFromCountry = moveFrom();
                if (l_moveFromCountry != null) {
                    cmd = new String[]{"blockade", l_moveFromCountry.getName()};
                }
                else
                    cmd = new String[]{"pass"};
                break;
            case 6:
                l_moveFromCountry = moveFrom();
                l_defendCountry = defend();
                if (l_moveFromCountry != null && l_defendCountry != null) {
                    cmd = new String[]{"airlift",
                            l_moveFromCountry.getName(),
                            l_defendCountry.getName(),
                            String.valueOf(d_rand.nextInt(l_moveFromCountry.getArmies() + 1))};
                }
                else
                    cmd = new String[]{"pass"};
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
