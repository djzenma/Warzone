package Controller;

import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test for GamePlayController
 */
public class GamePlayControllerTest {
    /**
     * Object of the Gameengine
     */
    private static GameEngine d_GameEngine;

    /**
     * Initialise the context of the test
     *
     * @throws Exception throws some kind of the exception
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();

        d_GameEngine = new GameEngine();
        d_GameEngine.setPhase(new Startup(d_GameEngine));

        d_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "asia.map"});
        d_GameEngine.d_gamePlayModel.addPlayer("a");
        d_GameEngine.d_gamePlayModel.addPlayer("b");
        d_GameEngine.d_currentPhase.assignCountries();
        d_GameEngine.d_gamePlayModel.assignReinforcements();

        d_GameEngine.d_gamePlayModel.addPlayer("Neutral");
    }

    /**
     * Test for game termination
     */
    @Test
    public void gameTermination() {
        d_GameEngine.d_currentPhase.issueOrders();
        d_GameEngine.d_currentPhase.deploy(
                new String[]{"deploy", "India", "5"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("a"));

        d_GameEngine.d_currentPhase.deploy(
                new String[]{"deploy", "China", "6"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("a"));

        d_GameEngine.d_currentPhase.deploy(
                new String[]{"deploy", "Egypt", "1"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("b"));

        assertTrue(d_GameEngine.d_currentPhase.advance(
                new String[]{"advance", "India", "Egypt", "5"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("a")));

        assertTrue(d_GameEngine.d_currentPhase.advance(
                new String[]{"advance", "China", "Canada", "6"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("a")));

        d_GameEngine.d_currentPhase.next();
        while (d_GameEngine.d_currentPhase.executeOrders()) ;

        assertTrue(d_GameEngine.d_gamePlayModel.isEndGame());
    }
}