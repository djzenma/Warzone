package Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Tests the PlayerModel
 */
public class PlayerModelTest {
    private static PlayerModel d_PlayerModel;
    private static GameEngineModel d_gameEngine;

    /**
     * Initializes the PlayerModel
     */
    @Before
    public void beforeEach() {

        // set up the continents and countries and game engine
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

        d_gameEngine = new GameEngineModel();
        d_gameEngine.setCountries(l_countries);
        d_gameEngine.setContinents(l_continents);

        // add the players
        d_gameEngine.addPlayer("Mazen");
        d_gameEngine.addPlayer("Aman");
        d_gameEngine.addPlayer("Akshat");

        // assign countries and reinforcements
        d_gameEngine.assignCountries();
        d_gameEngine.assignReinforcements();
        d_PlayerModel = d_gameEngine.getPlayers().get("Mazen");
    }

    /**
     * Tests whether the player can deploy more armies than there is in the reinforcements pool
     */
    @Test
    public void issueOrder() {
        assertEquals(11, d_gameEngine.getPlayers().get("Mazen").getReinforcements());
        assertEquals(3, d_gameEngine.getPlayers().get("Aman").getReinforcements());
        assertEquals(3, d_gameEngine.getPlayers().get("Akshat").getReinforcements());

        assertFalse(d_PlayerModel.issueOrder(new String[]{"deploy", "canada8", "12"}));
        assertTrue(d_PlayerModel.issueOrder(new String[]{"deploy", "canada8", "10"}));
        assertEquals(1, d_PlayerModel.getReinforcements());
    }

    /**
     * Tests whether the player returns the correct next order from its orders queue
     */
    @Test
    public void nextOrder() {
        String l_country = "canada8";
        String l_orderName = "deploy";
        OrderModel l_nextOrder;

        assertTrue(d_PlayerModel.issueOrder(new String[]{l_orderName, l_country, "10"}));
        assertTrue(d_PlayerModel.issueOrder(new String[]{l_orderName, l_country, "1"}));

        l_nextOrder = d_PlayerModel.nextOrder();
        assertEquals(l_country, l_nextOrder.getCountryName());
        assertEquals(10, l_nextOrder.getReinforcements());

        l_nextOrder = d_PlayerModel.nextOrder();
        assertEquals(l_country, l_nextOrder.getCountryName());
        assertEquals(1, l_nextOrder.getReinforcements());
    }
}