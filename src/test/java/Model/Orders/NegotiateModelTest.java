package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for Negotiate Model
 */
public class NegotiateModelTest {
    /**
     * Object of the gameengine
     */
    private static GameEngine d_GameEngine;
    /**
     * Object of the Negotiate model
     */
    private static NegotiateModel d_NegotiateModel;

    /**
     * Set up a scenario
     *
     * @throws Exception throws some kind of exception
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        d_GameEngine = new GameEngine();
        d_GameEngine.setPhase(new Startup(d_GameEngine));
        d_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_GameEngine.d_gamePlayModel.addPlayer("A");
        d_GameEngine.d_gamePlayModel.addPlayer("B");
        d_GameEngine.d_currentPhase.assignCountries();
        d_GameEngine.d_gamePlayModel.assignReinforcements();
    }

    /**
     * Tests the succeeding scenario
     */
    @Test
    public void succeedingScenario() {
        d_NegotiateModel = new NegotiateModel(
                d_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("B"),
                new String[]{"negotiate", "B"});

        assertTrue(d_NegotiateModel.execute(d_GameEngine.d_mapModel.getCountries()));
    }

    /**
     * Tests the failing scenario
     */
    @Test
    public void failingScenario() {
        d_NegotiateModel = new NegotiateModel(
                d_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("B"),
                new String[]{"negotiate", "B"});

        assertTrue(d_NegotiateModel.execute(d_GameEngine.d_mapModel.getCountries()));

        // try attacking while negotiated
        AdvanceModel d_advanceModel = new AdvanceModel(
                d_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                d_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                46,
                d_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("A").getView(),
                new String[]{"advance", "Venus-North", "Saturn-South", "46"});

        assertFalse(d_advanceModel.execute(d_GameEngine.d_mapModel.getCountries()));
    }
}