package Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class PlayerModelTest {
    private static PlayerModel d_PlayerModel;
    private static GameEngineModel d_gameEngine;

    /**
     * Initializes the GameEngineModel
     */
    @Before
    public void beforeEach() {

        // set up the continents and countries and game engine
        HashMap<String, CountryModel> l_countries = new HashMap<>();
        ArrayList<ContinentModel> l_continents = new ArrayList<>();

        CountryModel a = new CountryModel(1, "India");
        CountryModel b = new CountryModel(2, "Egypt");
        CountryModel c = new CountryModel(3, "Canada");
        CountryModel a1 = new CountryModel(4, "Canada1");
        CountryModel a2 = new CountryModel(5, "Canada2");
        CountryModel a3 = new CountryModel(6, "Canada3");
        CountryModel a4 = new CountryModel(7, "Canada4");
        CountryModel a5 = new CountryModel(8, "Canada5");
        CountryModel a6 = new CountryModel(9, "Canada6");
        CountryModel a7 = new CountryModel(10, "Canada7");
        CountryModel a8 = new CountryModel(11, "Canada8");
        CountryModel a9 = new CountryModel(12, "Canada9");

        l_countries.put(a.getName(), a);
        l_countries.put(b.getName(), b);
        l_countries.put(c.getName(), c);
        l_countries.put(a1.getName(), a1);
        l_countries.put(a2.getName(), a2);
        l_countries.put(a3.getName(), a3);
        l_countries.put(a4.getName(), a4);
        l_countries.put(a5.getName(), a5);
        l_countries.put(a6.getName(), a6);
        l_countries.put(a7.getName(), a7);
        l_countries.put(a8.getName(), a8);
        l_countries.put(a9.getName(), a9);

        ContinentModel c1 = new ContinentModel("Asia", 7);
        ContinentModel c2 = new ContinentModel("Australia", 8);

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

        l_continents.add(c1);
        l_continents.add(c2);

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