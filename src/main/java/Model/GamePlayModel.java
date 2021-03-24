package Model;


import View.PlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.lang.Math.floor;

/**
 * Implements the Gameplay Phase
 * <ul>
 *     <li> Add/remove players </li>
 *     <li> Assign the countries to the players </li>
 *     <li> Assign the reinforcements to the players </li>
 *     <li> Executes the Orders issued by the players </li>
 * </ul>
 */
public class GamePlayModel {
    private final HashMap<String, Player> d_players;
    private HashMap<String, CountryModel> d_countries;
    private ArrayList<ContinentModel> d_continents;
    private boolean d_endGame;
    private Player d_winner;

    /**
     * Initialises the HashMap of PlayerModel, CountryModel and Arraylist of ContinentModel
     */
    public GamePlayModel() {
        this.d_players = new HashMap<>();
        this.d_continents = new ArrayList<>();
        this.d_countries = new HashMap<>();
        this.d_endGame = false;
        this.d_winner = null;
    }

    /**
     * Mutator for the continents
     *
     * @param p_continents HashMap of the continents
     */
    public void setContinents(ArrayList<ContinentModel> p_continents) {
        this.d_continents = p_continents;
    }

    /**
     * Accessor for the players
     *
     * @return HashMap of the players
     */
    public HashMap<String, Player> getPlayers() {
        return d_players;
    }

    /**
     * Accessor for the countries
     *
     * @return HashMap of the countries
     */
    public HashMap<String, CountryModel> getCountries() {
        return d_countries;
    }

    /**
     * Mutator for the countries
     *
     * @param p_countries HashMap of the countries
     */
    public void setCountries(HashMap<String, CountryModel> p_countries) {
        this.d_countries = p_countries;
    }

    /**
     * Accessor for the End game flag
     *
     * @return End game flag
     */
    public boolean isEndGame() {
        return this.d_endGame;
    }

    /**
     * Accessor for the Winner of the game
     *
     * @return Winner
     */
    public Player getWinner() {
        return this.d_winner;
    }

    /**
     * Adds a game player
     *
     * @param p_playerName Name of the player
     */
    public void addPlayer(String p_playerName) {
        Player l_player = new Player(p_playerName, new PlayerView(), this.d_countries, this.d_players);
        this.d_players.put(p_playerName, l_player);
    }

    /**
     * Removes a game player
     *
     * @param p_playerName Name of the player
     * @throws Exception When the player being removed does not exists
     */
    public void removePlayer(String p_playerName) throws Exception {
        //handle playerName
        if (!(this.d_players.containsKey(p_playerName))) {
            throw new Exception("Player does not exists");
        }
        this.d_players.remove(p_playerName);
    }

    /**
     * Assign countries evenly between the list of gameplayers. If a tie happens, the remaining countries are assigned
     * to random players
     */
    public void assignCountries() {
        // get the number of countries to be assigned per player
        int l_countriesPerPlayer = this.d_countries.size() / this.d_players.size();

        // get the countries IDs as a List
        Set<String> l_countryNames = this.d_countries.keySet();
        List<String> l_countryNamesList = new ArrayList<String>(l_countryNames);

        // get the player IDs as a List
        Set<String> l_playerIDs = this.d_players.keySet();
        List<String> l_playerIDsList = new ArrayList<String>(l_playerIDs);

        // assign countries evenly between players
        int l_countriesCounter = 0;
        for (Player l_player : this.d_players.values()) {
            for (int l_i = 0; l_i < l_countriesPerPlayer; l_i++) {
                String l_countryName = l_countryNamesList.get(l_countriesCounter);
                // get the country
                CountryModel l_country = this.d_countries.get(l_countryName);
                // update its owner
                l_country.setOwner(l_player);
                // give it to the player
                l_player.addCountry(l_country);
                l_countriesCounter++;
            }
        }

        // assign remaining countries to random players
        while (l_countriesCounter <= this.d_countries.size() - 1) {
            // get the random player
            int l_rand = (int) (Math.random() * (this.d_players.size()));
            Player l_player = this.d_players.get(l_playerIDsList.get(l_rand));

            // get the name of this country
            String l_countryName = l_countryNamesList.get(l_countriesCounter);
            CountryModel l_country = this.d_countries.get(l_countryName);
            // update its owner
            l_country.setOwner(l_player);

            // assign to him the country
            l_player.addCountry(l_country);
            l_countriesCounter++;
        }
    }

    /**
     * Validates the assignment of countries
     *
     * @return True if the there are no players added in the game; Otherwise false
     */
    public boolean isInValidCommand() {
        return this.d_players.size() == 0;
    }

    /**
     * Assigns the reinforcements to every player
     */
    public void assignReinforcements() {
        int l_numberOfArmies;
        boolean l_hasContinent;

        // iterate over all the players
        for (Player l_player : this.d_players.values()) {
            if (l_player.getName().equals("Neutral"))
                continue;
            l_numberOfArmies = (int) Math.max(3, floor(l_player.getCountries().size() / 3.0));

            // iterate over all the continents
            for (ContinentModel l_continent : this.d_continents) {
                l_hasContinent = true;

                // iterate over all the countries
                for (CountryModel l_country : l_continent.getCountries().values()) {

                    // checks if the player owns the particular country
                    if (!l_player.containsCountry(l_country.getName())) {
                        l_hasContinent = false;
                        break;
                    }
                }

                // checks if the player has all the countries of a continent
                if (l_hasContinent) {
                    l_numberOfArmies += l_continent.getControlValue();
                }
            }
            l_player.setReinforcements(l_numberOfArmies);
        }
    }

    /**
     * Loops over each player to get the orders from them and execute them
     *
     * @return False if the player does not have any other orders to be executed; Otherwise true
     */
    public boolean executeOrders() {
        boolean l_end = true;
        ArrayList<Player> l_playersToBeRemoved = new ArrayList<>();

        for (Player l_player : this.d_players.values()) {
            if (l_player.getName().equals("Neutral"))
                continue;
            OrderModel l_order = l_player.nextOrder();
            if (l_order != null) {
                l_order.execute(d_countries);
                l_end = false;
            }
        }
        for (Player l_player : this.getPlayers().values()) {
            try {
                if (l_player.getCountries().size() == 0 && !(l_player.getName().equals("Neutral"))) {
                    l_playersToBeRemoved.add(l_player);
                }
            } catch (Exception l_e) {
                System.out.println(l_e.getMessage());
            }
        }

        for (Player l_player : l_playersToBeRemoved) {
            try {
                this.removePlayer(l_player.getName());
            } catch (Exception l_e) {
                System.out.println(l_e.getMessage());
            }
        }

        if (this.getPlayers().size() == 2) {
            for (Player l_player : this.getPlayers().values()) {
                if (!(l_player.getName().equals("Neutral"))) {
                    this.d_winner = l_player;
                }
            }
            l_end = true;
            this.d_endGame = true;
        }
        return !l_end;
    }
}
