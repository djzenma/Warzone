package Controller;

import Model.PlayerModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This is a test class for the GameEngineController
 */
public class GameEngineControllerTest {

    static GameEngineController d_gameEngine;

    /**
     * Initializes the GameEngineController
     */
    @BeforeClass
    public static void init() {
        d_gameEngine = new GameEngineController();
    }

    /**
     * Adds the players before every test method
      */
    @Before
    public void addPlayers() {
        d_gameEngine.addPlayer("Mazen");
        d_gameEngine.addPlayer("Aman");
        d_gameEngine.addPlayer("Akshat");
    }

    /**
     * Tests the whether the players have been added
     */
    @Test
    public void addPlayer() {
        assertEquals(3, d_gameEngine.getPlayers().size());
    }

    /**
     * Tests if a player has been removed
     */
    @Test
    public void removePlayer() throws Exception {
        d_gameEngine.removePlayer("Mazen");

        assertEquals(2, d_gameEngine.getPlayers().size());
    }

    /**
     * Tests whether every player has been assigned a country
     * */
    @Test
    public void assignCountries() {
        d_gameEngine.assignCountries();
        for(PlayerModel l_player: d_gameEngine.getPlayers().values()){
            assertNotNull(l_player.getCountries());
        }
    }

    @Test
    public void assignReinforcements() {
        d_gameEngine.assignCountries();
        d_gameEngine.assignReinforcements();
        assertEquals(4, d_gameEngine.getPlayers().get("Mazen").getReinforcements());
        assertEquals(4, d_gameEngine.getPlayers().get("Aman").getReinforcements());
        assertEquals(4, d_gameEngine.getPlayers().get("Akshat").getReinforcements());
    }
}