package Model.Orders;

import Controller.GameEngineController;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AdvanceModelTest {
    private static GameEngineController d_gameEngineController;
    private static AdvanceModel d_advanceModel;

    @Before
    public void init() throws Exception {
        d_gameEngineController = new GameEngineController();
        d_gameEngineController.setPhase(new Startup(d_gameEngineController));
        d_gameEngineController.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_gameEngineController.d_gamePlayModel.addPlayer("Adeetya");
        d_gameEngineController.d_gamePlayModel.addPlayer("Mazen");
        d_gameEngineController.d_currentPhase.assignCountries();

        DeployModel d_deployModel_1 = new DeployModel(
                CommandsParser.getArguments(new String[]{"deploy", "Venus-North", "46"}),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Adeetya"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Adeetya").getView());

        d_deployModel_1.execute(d_gameEngineController.d_mapModel.getCountries());

        DeployModel d_deployModel_2 = new DeployModel(
                CommandsParser.getArguments(new String[]{"deploy", "Saturn-South", "30"}),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView());

        d_deployModel_2.execute(d_gameEngineController.d_mapModel.getCountries());

        d_advanceModel = new AdvanceModel(
                d_gameEngineController.d_mapModel.getCountries().get("Venus-North"),
                d_gameEngineController.d_mapModel.getCountries().get("Saturn-South"),
                46,
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Adeetya"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Adeetya").getView(),
                CommandsParser.getArguments(new String[]{"advance", "Venus-North", "Saturn-South", "46"}));
    }

    @Test
    public void execute() {
        assertTrue(d_advanceModel.execute(d_gameEngineController.d_mapModel.getCountries()));
    }
}