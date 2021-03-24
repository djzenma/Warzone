package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeployModelTest {
    private static GameEngine d_gameEngine;
    private static DeployModel d_deployModel;

    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        d_gameEngine = new GameEngine();
        d_gameEngine.setPhase(new Startup(d_gameEngine));
        d_gameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_gameEngine.d_gamePlayModel.addPlayer("A");
        d_gameEngine.d_gamePlayModel.addPlayer("B");
        d_gameEngine.d_currentPhase.assignCountries();
        d_gameEngine.d_gamePlayModel.assignReinforcements();
    }

    @Test
    public void succeedingScenario() {
        d_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("B"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("B").getView());

        assertTrue(d_deployModel.execute(d_gameEngine.d_mapModel.getCountries()));
        assertEquals(30, d_gameEngine.d_gamePlayModel.getCountries().get("Saturn-South").getArmies());
    }

    @Test
    public void failingScenario() {
        // test deploying a number of armies that is more than what the player has
        d_deployModel = new DeployModel(
                new String[]{"deploy", "Venus-North", "50"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(d_deployModel.execute(d_gameEngine.d_mapModel.getCountries()));

        // test deploying in a country that is not owned by the issuer
        d_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "10"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(d_deployModel.execute(d_gameEngine.d_mapModel.getCountries()));

        d_deployModel = new DeployModel(
                new String[]{"deploy", "Venus-North", "50"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(d_deployModel.execute(d_gameEngine.d_mapModel.getCountries()));
    }
}