package Model.Orders;

import Controller.GameEngineController;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AirliftModelTest {
    private static GameEngineController d_gameEngineController;
    private static AirliftModel d_airliftModel;

    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        d_gameEngineController = new GameEngineController();
        d_gameEngineController.setPhase(new Startup(d_gameEngineController));
        d_gameEngineController.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_gameEngineController.d_gamePlayModel.addPlayer("Mazen");
        d_gameEngineController.d_gamePlayModel.addPlayer("Aman");
        d_gameEngineController.d_currentPhase.assignCountries();

        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_1.execute(d_gameEngineController.d_mapModel.getCountries());

        DeployModel l_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman").getView());

        l_deployModel_2.execute(d_gameEngineController.d_mapModel.getCountries());

        d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").assignSpecificCard("airlift");
    }

    @Test
    public void executeTrue() {
        d_airliftModel = new AirliftModel(
                d_gameEngineController.d_mapModel.getCountries().get("Venus-North"),
                d_gameEngineController.d_mapModel.getCountries().get("Comet-Head"),
                20,
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"airlift", "Venus-North", "Comet-Head", "20"});

        assertTrue(d_airliftModel.execute(d_gameEngineController.d_mapModel.getCountries()));
        assertEquals(20, d_gameEngineController.d_mapModel.getCountries().get("Comet-Head").getArmies());
        assertEquals(26, d_gameEngineController.d_mapModel.getCountries().get("Venus-North").getArmies());
    }

    @Test
    public void executeFalse() {
        d_airliftModel = new AirliftModel(
                d_gameEngineController.d_mapModel.getCountries().get("Venus-North"),
                d_gameEngineController.d_mapModel.getCountries().get("Saturn-South"),
                20,
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"airlift", "Venus-North", "Saturn-South", "20"});


        assertFalse(d_airliftModel.execute(d_gameEngineController.d_mapModel.getCountries()));
        assertEquals(30, d_gameEngineController.d_mapModel.getCountries().get("Saturn-South").getArmies());
        assertEquals(46, d_gameEngineController.d_mapModel.getCountries().get("Venus-North").getArmies());
    }
}