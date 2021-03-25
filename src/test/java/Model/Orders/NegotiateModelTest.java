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
    private static GameEngine D_GameEngine;
    /**
     * Object of the Negotiate model
     */
    private static NegotiateModel D_NegotiateModel;

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
        D_GameEngine.d_gamePlayModel.addPlayer("A");
        D_GameEngine.d_gamePlayModel.addPlayer("B");
        D_GameEngine.d_currentPhase.assignCountries();
        D_GameEngine.d_gamePlayModel.assignReinforcements();
    }

    /**
     * Tests the succeeding scenario
     */
    @Test
    public void succeedingScenario() {
        D_NegotiateModel = new NegotiateModel(
                D_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("B"),
                new String[]{"negotiate", "B"});

        assertTrue(D_NegotiateModel.execute(D_GameEngine.d_mapModel.getCountries()));
    }

    /**
     * Tests the failing scenario
     */
    @Test
    public void failingScenario() {
        D_NegotiateModel = new NegotiateModel(
                D_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("B"),
                new String[]{"negotiate", "B"});

        assertTrue(D_NegotiateModel.execute(D_GameEngine.d_mapModel.getCountries()));

        // try attacking while negotiated
        AdvanceModel d_advanceModel = new AdvanceModel(
                D_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                D_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                46,
                D_GameEngine.d_gamePlayModel.getPlayers().get("A"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("A").getView(),
                new String[]{"advance", "Venus-North", "Saturn-South", "46"});

        assertFalse(d_advanceModel.execute(D_GameEngine.d_mapModel.getCountries()));
    }
}