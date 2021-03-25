package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for Deploy Model
 */
public class DeployModelTest {
    /**
     * Object of the gameengine
     */
    private static GameEngine d_GameEngine;
    /**
     * Object of the Deploy model
     */
    private static DeployModel d_DeployModel;

    /**
     * Set up a scenario
     *
     * @throws Exception throws some kind of exception
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        d_GameEngine = new GameEngine();
        d_GameEngine.setPhase(new Startup(d_GameEngine));
        d_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_GameEngine.d_gamePlayModel.addPlayer("A");
        d_GameEngine.d_gamePlayModel.addPlayer("B");
        d_GameEngine.d_currentPhase.assignCountries();
        d_GameEngine.d_gamePlayModel.assignReinforcements();
    }

    /**
     * Tests the succeeding scenario
     */
    @Test
    public void succeedingScenario() {
        d_DeployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("B"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("B").getView());

        assertTrue(d_DeployModel.execute(d_GameEngine.d_mapModel.getCountries()));
        assertEquals(30, d_GameEngine.d_gamePlayModel.getCountries().get("Saturn-South").getArmies());
    }

    /**
     * Tests the failing scenario
     */
    @Test
    public void failingScenario() {
        // test deploying a number of armies that is more than what the player has
        d_DeployModel = new DeployModel(
                new String[]{"deploy", "Venus-North", "50"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(d_DeployModel.execute(d_GameEngine.d_mapModel.getCountries()));

        // test deploying in a country that is not owned by the issuer
        d_DeployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "10"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(d_DeployModel.execute(d_GameEngine.d_mapModel.getCountries()));

        d_DeployModel = new DeployModel(
                new String[]{"deploy", "Venus-North", "50"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(d_DeployModel.execute(d_GameEngine.d_mapModel.getCountries()));
    }
}