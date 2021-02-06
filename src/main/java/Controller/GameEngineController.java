package Controller;

import Model.CountryModel;
import Model.PlayerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Game Engine TODO::
 */
public class GameEngineController {

    private HashMap<String, PlayerModel> d_players;
    private HashMap<Integer, CountryModel> d_countries;



    public GameEngineController() {
        d_players = new HashMap<String, PlayerModel>();
        d_countries = new HashMap<Integer, CountryModel>();

        d_countries.put(1, new CountryModel("India"));
        d_countries.put(2, new CountryModel("Egypt"));
        d_countries.put(3, new CountryModel("Canada"));
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
    public void removePlayer(String p_playerName) {
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
        Set countryIDs = this.d_countries.keySet();
        List<Integer> countryIDsList = new ArrayList<Integer>(countryIDs);

        // get the player IDs as a List
        Set playerIDs = this.d_players.keySet();
        List<String> playerIDsList = new ArrayList<String>(playerIDs);

        // assign countries evenly between players
        int l_countriesCounter = 0;
        for(PlayerModel l_player : this.d_players.values()) {
            for(int i=0; i<l_countriesPerPlayer; i++){
                int countryID = countryIDsList.get(l_countriesCounter);
                l_player.setCountry(this.d_countries.get(countryID));
                l_countriesCounter++;
            }
        }

        // assign remaining countries to random players
        while(l_countriesCounter < this.d_countries.size()-1) {
            int l_rand = (int) (Math.random() * (this.d_players.size()));


            this.d_players.get(playerIDsList.get(l_rand)).setCountry(this.d_countries.get(l_countriesCounter));
            l_countriesCounter++;
        }
    }
}
