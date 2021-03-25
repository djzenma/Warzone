package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for Bomb Model
 */
public class BombModelTest {
    /**
     * Object of the gameengine
     */
    private static GameEngine D_GameEngine;
    /**
     * Object of the Bomb model
     */
    private static BombModel D_BombModel;

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
        D_GameEngine.d_gamePlayModel.addPlayer("Aman");
        D_GameEngine.d_gamePlayModel.addPlayer("Mazen");
        D_GameEngine.d_currentPhase.assignCountries();
        D_GameEngine.d_gamePlayModel.assignReinforcements();

        DeployModel d_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Aman").getView());

        DeployModel d_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        d_deployModel_1.execute(D_GameEngine.d_mapModel.getCountries());
        d_deployModel_2.execute(D_GameEngine.d_mapModel.getCountries());

        D_GameEngine.d_gamePlayModel.getPlayers().get("Aman").assignSpecificCard("bomb");
    }

    /**
     * Test the bomb on the true scenario set up
     */
    @Test
    public void succeedingScenario() {
        D_BombModel = new BombModel(
                D_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                D_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                new String[]{"bomb", "Venus-North"});
        assertTrue(D_BombModel.execute(D_GameEngine.d_mapModel.getCountries()));
        assertEquals(23, D_GameEngine.d_mapModel.getCountries().get("Venus-North").getArmies());
    }

    /**
     * Test the bomb on the false scenario set up
     */
    @Test
    public void failingScenario() {
        D_BombModel = new BombModel(
                D_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                D_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                new String[]{"bomb", "Saturn-South"});

        assertFalse(D_BombModel.execute(D_GameEngine.d_mapModel.getCountries()));
        assertEquals(30, D_GameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
    }
}