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
    private static GameEngine D_GameEngine;

    /**
     * Initialise the context of the test
     *
     * @throws Exception throws some kind of the exception
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();

        D_GameEngine = new GameEngine();
        D_GameEngine.setPhase(new Startup(D_GameEngine));

        D_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "asia.map"});
        D_GameEngine.d_gamePlayModel.addPlayer("a");
        D_GameEngine.d_gamePlayModel.addPlayer("b");
        D_GameEngine.d_currentPhase.assignCountries();
        D_GameEngine.d_gamePlayModel.assignReinforcements();

        D_GameEngine.d_gamePlayModel.addPlayer("Neutral");
    }

    /**
     * Test for game termination
     */
    @Test
    public void gameTermination() {
        D_GameEngine.d_currentPhase.issueOrders();
        D_GameEngine.d_currentPhase.deploy(
                new String[]{"deploy", "India", "5"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("a"));

        D_GameEngine.d_currentPhase.deploy(
                new String[]{"deploy", "China", "6"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("a"));

        D_GameEngine.d_currentPhase.deploy(
                new String[]{"deploy", "Egypt", "1"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("b"));

        assertTrue(D_GameEngine.d_currentPhase.advance(
                new String[]{"advance", "India", "Egypt", "5"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("a")));

        assertTrue(D_GameEngine.d_currentPhase.advance(
                new String[]{"advance", "China", "Canada", "6"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("a")));

        D_GameEngine.d_currentPhase.next();
        while (D_GameEngine.d_currentPhase.executeOrders()) ;

        assertTrue(D_GameEngine.d_gamePlayModel.isEndGame());
    }
}