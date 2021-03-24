package Controller;

import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GamePlayControllerTest {
    private static GameEngine d_gameEngine;

    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();

        d_gameEngine = new GameEngine();
        d_gameEngine.setPhase(new Startup(d_gameEngine));

        d_gameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "asia.map"});
        d_gameEngine.d_gamePlayModel.addPlayer("a");
        d_gameEngine.d_gamePlayModel.addPlayer("b");
        d_gameEngine.d_currentPhase.assignCountries();
        d_gameEngine.d_gamePlayModel.assignReinforcements();

        d_gameEngine.d_gamePlayModel.addPlayer("Neutral");
    }

    @Test
    public void gameTermination() {
        d_gameEngine.d_currentPhase.issueOrders();
        d_gameEngine.d_currentPhase.deploy(
                new String[]{"deploy", "India", "5"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("a"));

        d_gameEngine.d_currentPhase.deploy(
                new String[]{"deploy", "China", "6"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("a"));

        d_gameEngine.d_currentPhase.deploy(
                new String[]{"deploy", "Egypt", "1"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("b"));

        assertTrue(d_gameEngine.d_currentPhase.advance(
                new String[]{"advance", "India", "Egypt", "5"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("a")));

        assertTrue(d_gameEngine.d_currentPhase.advance(
                new String[]{"advance", "China", "Canada", "6"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("a")));

        d_gameEngine.d_currentPhase.next();
        while (d_gameEngine.d_currentPhase.executeOrders()) ;

        assertTrue(d_gameEngine.d_gamePlayModel.isEndGame());
    }
}