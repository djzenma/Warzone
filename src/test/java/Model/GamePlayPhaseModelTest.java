package Model;


import Strategy.HumanStrategy;
import View.PlayerView;
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
    /**
     * Object of the gameplay
     */
    private static GamePlayModel d_GamePlay;

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

        d_GamePlay = new GamePlayModel();
        d_GamePlay.setCountries(l_countries);
        d_GamePlay.setContinents(l_continents);
    }

    /**
     * Adds the players before every test method
      */
    @Before
    public void addPlayers() {
        Player l_player1 = new Player("Mazen", new PlayerView());
        l_player1.setStrategy(new HumanStrategy(l_player1,
                d_GamePlay.getCountries(),
                d_GamePlay.getPlayers()));

        Player l_player2 = new Player("Aman", new PlayerView());
        l_player2.setStrategy(new HumanStrategy(l_player2,
                d_GamePlay.getCountries(),
                d_GamePlay.getPlayers()));

        Player l_player3 = new Player("Akshat", new PlayerView());
        l_player3.setStrategy(new HumanStrategy(l_player3,
                d_GamePlay.getCountries(),
                d_GamePlay.getPlayers()));

        d_GamePlay.addPlayer(l_player1);
        d_GamePlay.addPlayer(l_player2);
        d_GamePlay.addPlayer(l_player3);
    }

    /**
     * Tests the whether the players have been added
     */
    @Test
    public void addPlayer() {
        assertEquals(3, d_GamePlay.getPlayers().size());
    }

    /**
     * Tests if a player has been removed
     */
    @Test
    public void removePlayer() {
        try {
            d_GamePlay.removePlayer("Mazen");
            assertEquals(2, d_GamePlay.getPlayers().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests whether every player has been assigned at least one country
     */
    @Test
    public void assignCountries() {
        d_GamePlay.assignCountries();
        for (Player l_player : d_GamePlay.getPlayers().values()) {
            assertNotNull(l_player.getCountries());
        }
    }

    /**
     * Tests the number of assigned reinforcement armies for every player
     */
    @Test
    public void assignReinforcements() {
        d_GamePlay.assignCountries();
        d_GamePlay.assignReinforcements();
        assertEquals(11, d_GamePlay.getPlayers().get("Mazen").getReinforcements());
        assertEquals(3, d_GamePlay.getPlayers().get("Aman").getReinforcements());
        assertEquals(3, d_GamePlay.getPlayers().get("Akshat").getReinforcements());
    }
}
