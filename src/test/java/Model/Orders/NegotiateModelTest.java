package Model.Orders;

import Controller.GameEngine;
import Model.Player;
import States.Startup;
import Strategy.HumanStrategy;
import Utils.CommandsParser;
import View.PlayerView;
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

        Player l_player1 = new Player("A", new PlayerView());
        l_player1.setStrategy(new HumanStrategy(l_player1,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        Player l_player2 = new Player("B", new PlayerView());
        l_player2.setStrategy(new HumanStrategy(l_player2,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        d_GameEngine.d_gamePlayModel.addPlayer(l_player1);
        d_GameEngine.d_gamePlayModel.addPlayer(l_player2);
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