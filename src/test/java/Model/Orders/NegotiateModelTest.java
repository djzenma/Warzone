package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NegotiateModelTest {
    private static GameEngine d_gameEngine;
    private static NegotiateModel d_negotiateModel;

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
        d_negotiateModel = new NegotiateModel(
                d_gameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("B"),
                new String[]{"negotiate", "B"});

        assertTrue(d_negotiateModel.execute(d_gameEngine.d_mapModel.getCountries()));
    }

    @Test
    public void failingScenario() {
        d_negotiateModel = new NegotiateModel(
                d_gameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("B"),
                new String[]{"negotiate", "B"});

        assertTrue(d_negotiateModel.execute(d_gameEngine.d_mapModel.getCountries()));

        // try attacking while negotiated
        AdvanceModel d_advanceModel = new AdvanceModel(
                d_gameEngine.d_mapModel.getCountries().get("Venus-North"),
                d_gameEngine.d_mapModel.getCountries().get("Saturn-South"),
                46,
                d_gameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("A").getView(),
                new String[]{"advance", "Venus-North", "Saturn-South", "46"});

        assertFalse(d_advanceModel.execute(d_gameEngine.d_mapModel.getCountries()));
    }
}