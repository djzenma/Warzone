package Controller;

import Model.Player;
import States.Startup;
import Strategy.HumanStrategy;
import Utils.CommandsParser;
import View.PlayerView;
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

        Player l_player1 = new Player("a", new PlayerView());
        l_player1.setStrategy(new HumanStrategy(l_player1,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        Player l_player2 = new Player("b", new PlayerView());
        l_player2.setStrategy(new HumanStrategy(l_player2,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        d_GameEngine.d_gamePlayModel.addPlayer(l_player1);
        d_GameEngine.d_gamePlayModel.addPlayer(l_player2);
        d_GameEngine.d_currentPhase.assignCountries();
        d_GameEngine.d_gamePlayModel.assignReinforcements();

        Player l_player3 = new Player("Neutral", new PlayerView());
        l_player2.setStrategy(new HumanStrategy(l_player2,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        d_GameEngine.d_gamePlayModel.addPlayer(l_player3);
    }

    /**
     * Test for game termination
     */
    @Test
    public void gameTermination() {
        d_GameEngine.d_currentPhase.issueOrders();

        Player l_playerA = d_GameEngine.d_gamePlayModel.getPlayers().get("a");
        Player l_playerB = d_GameEngine.d_gamePlayModel.getPlayers().get("b");
        l_playerA.setPhase(d_GameEngine.d_currentPhase);
        l_playerB.setPhase(d_GameEngine.d_currentPhase);

        l_playerA.setCommand(new String[]{"deploy", "India", "5"});
        l_playerA.issueOrder();

        l_playerA.setCommand(new String[]{"deploy", "China", "6"});
        l_playerA.issueOrder();

        l_playerB.setCommand(new String[]{"deploy", "Egypt", "1"});
        l_playerB.issueOrder();

        l_playerA.setCommand(new String[]{"advance", "India", "Egypt", "5"});
        assertTrue(l_playerA.issueOrder());

        l_playerA.setCommand(new String[]{"advance", "China", "Canada", "6"});
        assertTrue(l_playerA.issueOrder());


        d_GameEngine.d_currentPhase.next();
        while (d_GameEngine.d_currentPhase.executeOrders()) ;

        assertTrue(d_GameEngine.d_gamePlayModel.isEndGame());
    }
}