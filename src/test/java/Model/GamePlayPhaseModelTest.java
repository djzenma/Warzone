package Model;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for the GamePlayModel
 */
public class GamePlayPhaseModelTest {

    private static GamePlayModel d_GameEngine;

    /**
     * Initializes the GamePlayModel and a context scenario
     */
    @BeforeClass
    public static void init() {

        HashMap<String, CountryModel> l_countries = new HashMap<>();
        ArrayList<ContinentModel> l_continents = new ArrayList<>();

        CountryModel l_a = new CountryModel(1, "India");
        CountryModel l_b = new CountryModel(2, "Egypt");
        CountryModel l_c = new CountryModel(3, "Canada");
        CountryModel l_d = new CountryModel(4, "Canada1");
        CountryModel l_e = new CountryModel(5, "Canada2");
        CountryModel l_f = new CountryModel(6, "Canada3");
        CountryModel l_g = new CountryModel(7, "Canada4");
        CountryModel l_h = new CountryModel(8, "Canada5");
        CountryModel l_i = new CountryModel(9, "Canada6");
        CountryModel l_j = new CountryModel(10, "Canada7");
        CountryModel l_k = new CountryModel(11, "Canada8");
        CountryModel l_l = new CountryModel(12, "Canada9");

        l_countries.put(l_a.getName(), l_a);
        l_countries.put(l_b.getName(), l_b);
        l_countries.put(l_c.getName(), l_c);
        l_countries.put(l_d.getName(), l_d);
        l_countries.put(l_e.getName(), l_e);
        l_countries.put(l_f.getName(), l_f);
        l_countries.put(l_g.getName(), l_g);
        l_countries.put(l_h.getName(), l_h);
        l_countries.put(l_i.getName(), l_i);
        l_countries.put(l_j.getName(), l_j);
        l_countries.put(l_k.getName(), l_k);
        l_countries.put(l_l.getName(), l_l);

        ContinentModel l_c1 = new ContinentModel("Asia", 7);
        ContinentModel l_c2 = new ContinentModel("Australia", 8);

        l_c1.addCountry(l_a);
        l_c1.addCountry(l_b);
        l_c1.addCountry(l_c);
        l_c1.addCountry(l_d);
        l_c1.addCountry(l_e);
        l_c1.addCountry(l_f);
        l_c1.addCountry(l_g);
        l_c1.addCountry(l_h);
        l_c2.addCountry(l_i);
        l_c2.addCountry(l_j);
        l_c2.addCountry(l_k);
        l_c1.addCountry(l_l);

        l_continents.add(l_c1);
        l_continents.add(l_c2);

        d_GameEngine = new GamePlayModel();
        d_GameEngine.setCountries(l_countries);
        d_GameEngine.setContinents(l_continents);
    }

    /**
     * Adds the players before every test method
      */
    @Before
    public void addPlayers() {
        d_GameEngine.addPlayer("Mazen");
        d_GameEngine.addPlayer("Aman");
        d_GameEngine.addPlayer("Akshat");
    }

    /**
     * Tests the whether the players have been added
     */
    @Test
    public void addPlayer() {
        assertEquals(3, d_GameEngine.getPlayers().size());
    }

    /**
     * Tests if a player has been removed
     */
    @Test
    public void removePlayer() {
        try {
            d_GameEngine.removePlayer("Mazen");
            assertEquals(2, d_GameEngine.getPlayers().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests whether every player has been assigned at least one country
     */
    @Test
    public void assignCountries() {
        d_GameEngine.assignCountries();
        for (PlayerModel l_player : d_GameEngine.getPlayers().values()) {
            assertNotNull(l_player.getCountries());
        }
    }

    /**
     * Tests the number of assigned reinforcement armies for every player
     */
    @Test
    public void assignReinforcements() {
        d_GameEngine.assignCountries();
        d_GameEngine.assignReinforcements();
        assertEquals(11, d_GameEngine.getPlayers().get("Mazen").getReinforcements());
        assertEquals(3, d_GameEngine.getPlayers().get("Aman").getReinforcements());
        assertEquals(3, d_GameEngine.getPlayers().get("Akshat").getReinforcements());
    }
}
