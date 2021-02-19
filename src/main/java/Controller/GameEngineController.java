package Controller;

import Model.ContinentModel;
import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;
import View.PlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.lang.Math.floor;

/**
 * Game Engine TODO::
 */
public class GameEngineController {

    private final HashMap<String, PlayerModel> d_players;
    private final HashMap<String, CountryModel> d_countries;
    private final ArrayList<ContinentModel> d_continents;

    public GameEngineController(HashMap<String, CountryModel> p_countries, ArrayList<ContinentModel> p_continents) {
        d_players = new HashMap<>();
        d_countries = p_countries;
        d_continents = p_continents;
    }

    public HashMap<String, PlayerModel> getPlayers() {
        return d_players;
    }

    public HashMap<String, CountryModel> getCountries() {
        return d_countries;
    }


    /**
     * Adds a game player
     * @param p_playerName name of the player
     */
    public void addPlayer(String p_playerName) {
        PlayerModel l_playerModel = new PlayerModel(p_playerName);

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
                l_player.addCountry(this.d_countries.get(l_countryName));
                l_countriesCounter++;
            }
        }

        // assign remaining countries to random players TODO
        /* while(l_countriesCounter <= this.d_countries.size()-1) {
            int l_rand = (int) (Math.random() * (this.d_players.size()));

            this.d_players.get(l_playerIDsList.get(l_rand)).addCountry(this.d_countries.get(l_countriesCounter));
            l_countriesCounter++;
        }*/
    }

    /**
     * Assigns the reinforcements to every player
     */
    public void assignReinforcements(){
        int l_numberOfArmies;
        boolean l_hasContinent;

        // iterate over all the players
        for (PlayerModel l_player : this.d_players.values()) {
            l_numberOfArmies = (int) Math.max(3, floor(d_countries.size() / 3.0));

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
                if(l_hasContinent){
                    l_numberOfArmies += l_continent.getControlValue();
                }
            }
            l_player.setReinforcements(l_numberOfArmies);
        }
    }

    /**
     * Loops over each player to get the orders from them
     * @return false if the player does not have any other orders to issue, otherwise true
     */
    public boolean issueOrders(){
        boolean end = true;

        for (PlayerModel l_player : this.d_players.values()) {
            PlayerView.CurrentPlayer(l_player);
            boolean issued = l_player.issueOrder();
            // if the player issued an order
            if (issued)
                end = false;
        }
        return !end;
    }

    public boolean executeOrders() {
        boolean end = true;

        for (PlayerModel l_player : this.d_players.values()) {
            OrderModel l_order = l_player.nextOrder();
            if(l_order != null){
                l_order.execute(d_countries);
                end = false;
            }
        }
        return !end;
    }

    public void run(){
        assignReinforcements();

        while(issueOrders());

        while(executeOrders());
    }
}
