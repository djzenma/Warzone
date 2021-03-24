package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BombModelTest {
    private static GameEngine d_gameEngine;
    private static BombModel d_bombModel;

    /**
     * Set up a scenario
     *
     * @throws Exception
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();

        d_gameEngine = new GameEngine();
        d_gameEngine.setPhase(new Startup(d_gameEngine));

        d_gameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_gameEngine.d_gamePlayModel.addPlayer("Aman");
        d_gameEngine.d_gamePlayModel.addPlayer("Mazen");
        d_gameEngine.d_currentPhase.assignCountries();
        d_gameEngine.d_gamePlayModel.assignReinforcements();

        DeployModel d_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Aman").getView());

        DeployModel d_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        d_deployModel_1.execute(d_gameEngine.d_mapModel.getCountries());
        d_deployModel_2.execute(d_gameEngine.d_mapModel.getCountries());

        d_gameEngine.d_gamePlayModel.getPlayers().get("Aman").assignSpecificCard("bomb");
    }

    /**
     * Test the bomb on the true scenario set up
     */
    @Test
    public void succeedingScenario() {
        d_bombModel = new BombModel(
                d_gameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngine.d_mapModel.getCountries().get("Venus-North"),
                new String[]{"bomb", "Venus-North"});
        assertTrue(d_bombModel.execute(d_gameEngine.d_mapModel.getCountries()));
        assertEquals(23, d_gameEngine.d_mapModel.getCountries().get("Venus-North").getArmies());
    }

    /**
     * Test the bomb on the false scenario set up
     */
    @Test
    public void failingScenario() {
        d_bombModel = new BombModel(
                d_gameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngine.d_mapModel.getCountries().get("Saturn-South"),
                new String[]{"bomb", "Saturn-South"});

        assertFalse(d_bombModel.execute(d_gameEngine.d_mapModel.getCountries()));
        assertEquals(30, d_gameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
    }
}