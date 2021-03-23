package Model.Orders;

import Controller.GameEngineController;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeployModelTest {
    private static GameEngineController d_gameEngineController;
    private static DeployModel d_deployModel;

    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        d_gameEngineController = new GameEngineController();
        d_gameEngineController.setPhase(new Startup(d_gameEngineController));
        d_gameEngineController.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_gameEngineController.d_gamePlayModel.addPlayer("A");
        d_gameEngineController.d_gamePlayModel.addPlayer("B");
        d_gameEngineController.d_currentPhase.assignCountries();
        d_gameEngineController.d_gamePlayModel.assignReinforcements();
    }

    @Test
    public void succeedingScenario() {
        // test deploying a number of armies that is more than what the player has
        d_deployModel = new DeployModel(
                new String[]{"deploy", "Venus-North", "50"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(d_deployModel.execute(d_gameEngineController.d_mapModel.getCountries()));

        // test deploying in a country that is not owned by the issuer
        d_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "10"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(d_deployModel.execute(d_gameEngineController.d_mapModel.getCountries()));
    }

    @Test
    public void failingScenario() {
        d_deployModel = new DeployModel(
                new String[]{"deploy", "Venus-North", "50"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("A").getView());

        assertFalse(d_deployModel.execute(d_gameEngineController.d_mapModel.getCountries()));

        d_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("B"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("B").getView());

        assertTrue(d_deployModel.execute(d_gameEngineController.d_mapModel.getCountries()));
        assertEquals(30, d_gameEngineController.d_gamePlayModel.getCountries().get("Saturn-South").getArmies());
    }
}