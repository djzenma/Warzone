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
    private static GameEngine D_GameEngine;
    /**
     * Object of the Deploy model
     */
    private static DeployModel D_DeployModel;

    /**
     * Set up a scenario
     *
     * @throws Exception throws some kind of exception
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        D_GameEngine = new GameEngine();
        D_GameEngine.setPhase(new Startup(D_GameEngine));
        D_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        D_GameEngine.d_gamePlayModel.addPlayer("A");
        D_GameEngine.d_gamePlayModel.addPlayer("B");
        D_GameEngine.d_currentPhase.assignCountries();
        D_GameEngine.d_gamePlayModel.assignReinforcements();
    }

    /**
     * Tests the succeeding scenario
     */
    @Test
    public void succeedingScenario() {
        D_DeployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("B"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("B").getView());

        assertTrue(D_DeployModel.execute(D_GameEngine.d_mapModel.getCountries()));
        assertEquals(30, D_GameEngine.d_gamePlayModel.getCountries().get("Saturn-South").getArmies());
    }

    /**
     * Tests the failing scenario
     */
    @Test
    public void failingScenario() {
        // test deploying a number of armies that is more than what the player has
        D_DeployModel = new DeployModel(
                new String[]{"deploy", "Venus-North", "50"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(D_DeployModel.execute(D_GameEngine.d_mapModel.getCountries()));

        // test deploying in a country that is not owned by the issuer
        D_DeployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "10"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(D_DeployModel.execute(D_GameEngine.d_mapModel.getCountries()));

        D_DeployModel = new DeployModel(
                new String[]{"deploy", "Venus-North", "50"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(D_DeployModel.execute(D_GameEngine.d_mapModel.getCountries()));
    }
}