package Controller;

import Model.ContinentModel;
import Model.CountryModel;
import Model.PlayerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.lang.Math.floor;

/**
 * Game Engine TODO::
 */
public class GameEngineController {

    private HashMap<String, PlayerModel> d_players;
    private HashMap<Integer, CountryModel> d_countries;
    private ArrayList<ContinentModel> d_continents;

    public GameEngineController() {
        d_players = new HashMap<String, PlayerModel>();
        d_countries = new HashMap<Integer, CountryModel>();
        d_continents = new ArrayList<ContinentModel>();

        CountryModel a = new CountryModel(1,"India");
        CountryModel b = new CountryModel(2,"Egypt");
        CountryModel c = new CountryModel(3,"Canada");
        CountryModel a1 = new CountryModel(4,"Canada1");
        CountryModel a2 = new CountryModel(5,"Canada2");
        CountryModel a3 = new CountryModel(6,"Canada2");
        CountryModel a4 = new CountryModel(7,"Canada2");
        CountryModel a5 = new CountryModel(8,"Canada2");
        CountryModel a6 = new CountryModel(9,"Canada2");
        CountryModel a7 = new CountryModel(10,"Canada2");
        CountryModel a8 = new CountryModel(11,"Canada2");
        CountryModel a9 = new CountryModel(12,"Canada2");

        d_countries.put(1, a);
        d_countries.put(2, b);
        d_countries.put(3, c);
        d_countries.put(4, a1);
        d_countries.put(5, a2);
        d_countries.put(6, a3);
        d_countries.put(7, a4);
        d_countries.put(8, a5);
        d_countries.put(9, a6);
        d_countries.put(10, a7);
        d_countries.put(11, a8);
        d_countries.put(12, a9);

        ContinentModel c1 = new ContinentModel("Asia",7);
        ContinentModel c2 = new ContinentModel("Australia",8);

        c1.addCountry(a);
        c1.addCountry(b);
        c1.addCountry(c);
        c1.addCountry(a1);
        c1.addCountry(a2);
        c1.addCountry(a3);
        c1.addCountry(a4);
        c1.addCountry(a5);
        c2.addCountry(a6);
        c2.addCountry(a7);
        c2.addCountry(a8);
        c1.addCountry(a9);

        d_continents.add(c1);
        d_continents.add(c2);
    }

    public HashMap<String, PlayerModel> getPlayers() {
        return d_players;
    }

    public HashMap<Integer, CountryModel> getCountries() {
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
        Set l_countryIDs = this.d_countries.keySet();
        List<Integer> l_countryIDsList = new ArrayList<Integer>(l_countryIDs);

        // get the player IDs as a List
        Set l_playerIDs = this.d_players.keySet();
        List<String> l_playerIDsList = new ArrayList<String>(l_playerIDs);

        // assign countries evenly between players
        int l_countriesCounter = 0;
        for(PlayerModel l_player : this.d_players.values()) {
            for(int i=0; i<l_countriesPerPlayer; i++){
                int l_countryID = l_countryIDsList.get(l_countriesCounter);
                l_player.addCountry(this.d_countries.get(l_countryID));
                l_countriesCounter++;
            }
        }

        // assign remaining countries to random players
        while(l_countriesCounter <= this.d_countries.size()-1) {
            int l_rand = (int) (Math.random() * (this.d_players.size()));

            this.d_players.get(l_playerIDsList.get(l_rand)).addCountry(this.d_countries.get(l_countriesCounter));
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
            l_numberOfArmies = (int) Math.max(3, floor(d_countries.size() / 3.0));
            l_hasContinent = true;

            // iterate over all the continents
            for (ContinentModel l_continent : this.d_continents) {

                // iterate over all the countries
                for (CountryModel l_country : l_continent.getCountries()) {

                    // checks if the player owns the particular country
                    if(!l_player.containsCountry(l_country.getId())){
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


    public void issueOrders(){

    }

    public void executeOrders(){

    }

    public void run(){
        assignReinforcements();
        issueOrders();
        executeOrders();
    }
}
