package Model;


import View.PlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.lang.Math.floor;

/**
 * Game Engine TODO::
 */
public class GameEngineModel {
    private final HashMap<String, PlayerModel> d_players;
    private  HashMap<String, CountryModel> d_countries;
    private  ArrayList<ContinentModel> d_continents;

    /**
     * Constructor of the GameEngineModel
     *
     */
    public GameEngineModel() {
        this.d_players = new HashMap<>();
        this.d_continents = new ArrayList<>();
        this.d_countries = new HashMap<>();
    }

    public void setCountries(HashMap<String, CountryModel> d_countries) {
        this.d_countries = d_countries;
    }

    public void setContinents(ArrayList<ContinentModel> d_continents) {
        this.d_continents = d_continents;
    }

    /**
     * Accessor for the players
     *
     * @return hashmap of the players
     */
    public HashMap<String, PlayerModel> getPlayers() {
        return d_players;
    }

    /**
     * Accessor for the countries
     *
     * @return hashmap of the countries
     */
    public HashMap<String, CountryModel> getCountries() {
        return d_countries;
    }


    /**
     * Adds a game player
     *
     * @param p_playerName name of the player
     */
    public void addPlayer(String p_playerName) {
        PlayerModel l_playerModel = new PlayerModel(p_playerName, new PlayerView());

        this.d_players.put(p_playerName, l_playerModel);
    }


    /**
     * Removes a game player
     * @param p_playerName name of the player
     */
    public void removePlayer(String p_playerName) throws Exception {
        //handle playerName
        if(!(this.d_players.containsKey(p_playerName))){
            throw new Exception("Player does not exists");
        }
        this.d_players.remove(p_playerName);
    }

    /**
     * Assign countries evenly between the list of gameplayers. If a tie happens, we assign the remaining countries
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
        for(PlayerModel l_player : this.d_players.values()) {
            for(int i=0; i<l_countriesPerPlayer; i++){
                String l_countryName = l_countryNamesList.get(l_countriesCounter);
                // get the country
                CountryModel l_country = this.d_countries.get(l_countryName);
                // update its owner
                l_country.setOwnerName(l_player.getName());
                // give it to the player
                l_player.addCountry(l_country);
                l_countriesCounter++;
            }
        }

        // assign remaining countries to random players
        while (l_countriesCounter <= this.d_countries.size() - 1) {
            // get the random player
            int l_rand = (int) (Math.random() * (this.d_players.size()));
            PlayerModel l_player = this.d_players.get(l_playerIDsList.get(l_rand));

            // get the name of this country
            String l_countryName = l_countryNamesList.get(l_countriesCounter);
            CountryModel l_country = this.d_countries.get(l_countryName);
            // update its owner
            l_country.setOwnerName(l_player.getName());

            // assign to him the country
            l_player.addCountry(l_country);
            l_countriesCounter++;
        }
    }

    /**
     * Assigns the reinforcements to every player
     */
    public void assignReinforcements(){
        int l_numberOfArmies;
        boolean l_hasContinent;

        // iterate over all the players
        for (PlayerModel l_player : this.d_players.values()) {
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
     * @return false if the player does not have any other orders to be executed; otherwise true
     */
    public boolean executeOrders() {
        boolean l_end = true;

        for (PlayerModel l_player : this.d_players.values()) {
            OrderModel l_order = l_player.nextOrder();
            if (l_order != null) {
                l_order.execute(d_countries);
                l_end = false;
            }
        }
        return !l_end;
    }
}
