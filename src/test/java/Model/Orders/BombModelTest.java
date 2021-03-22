package Model.Orders;

import Controller.GameEngineController;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BombModelTest {
    private static GameEngineController d_gameEngineController;
    private static BombModel d_bombModel;

    /**
     * Set up a scenario
     *
     * @throws Exception
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();

        d_gameEngineController = new GameEngineController();
        d_gameEngineController.setPhase(new Startup(d_gameEngineController));

        d_gameEngineController.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_gameEngineController.d_gamePlayModel.addPlayer("Aman");
        d_gameEngineController.d_gamePlayModel.addPlayer("Mazen");
        d_gameEngineController.d_currentPhase.assignCountries();

        DeployModel d_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman").getView());

        DeployModel d_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView());

        d_deployModel_1.execute(d_gameEngineController.d_mapModel.getCountries());
        d_deployModel_2.execute(d_gameEngineController.d_mapModel.getCountries());
    }

    /**
     * Test the bomb on the true scenario set up
     */
    @Test
    public void executeTrue() {
        d_bombModel = new BombModel(
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngineController.d_mapModel.getCountries().get("Venus-North"),
                new String[]{"bomb", "Venus-North"});

        assertTrue(d_bombModel.execute(d_gameEngineController.d_mapModel.getCountries()));
        assertEquals(23, d_gameEngineController.d_mapModel.getCountries().get("Venus-North").getArmies());
    }

    /**
     * Test the bomb on the false scenario set up
     */
    @Test
    public void executeFalse() {
        d_bombModel = new BombModel(
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngineController.d_mapModel.getCountries().get("Saturn-South"),
                new String[]{"bomb", "Saturn-South"});

        assertFalse(d_bombModel.execute(d_gameEngineController.d_mapModel.getCountries()));
        assertEquals(30, d_gameEngineController.d_mapModel.getCountries().get("Saturn-South").getArmies());
    }
}