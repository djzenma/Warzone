package Model.Orders;

import Controller.GameEngineController;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NegotiateModelTest {
    private static GameEngineController d_gameEngineController;
    private static NegotiateModel d_negotiateModel;

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
        d_negotiateModel = new NegotiateModel(
                d_gameEngineController.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("B"),
                new String[]{"negotiate", "B"});

        assertTrue(d_negotiateModel.execute(d_gameEngineController.d_mapModel.getCountries()));
    }

    @Test
    public void failingScenario() {
        d_negotiateModel = new NegotiateModel(
                d_gameEngineController.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("B"),
                new String[]{"negotiate", "B"});

        assertTrue(d_negotiateModel.execute(d_gameEngineController.d_mapModel.getCountries()));

        // try attacking while negotiated
        AdvanceModel d_advanceModel = new AdvanceModel(
                d_gameEngineController.d_mapModel.getCountries().get("Venus-North"),
                d_gameEngineController.d_mapModel.getCountries().get("Saturn-South"),
                46,
                d_gameEngineController.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("A").getView(),
                new String[]{"advance", "Venus-North", "Saturn-South", "46"});

        assertFalse(d_advanceModel.execute(d_gameEngineController.d_mapModel.getCountries()));
    }
}