package Controller;

import Model.Player;
import States.Startup;
import Strategy.HumanStrategy;
import Strategy.NeutralStrategy;
import Utils.CommandsParser;
import View.PlayerView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
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
        l_player1.setStrategy(new NeutralStrategy(l_player1,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        Player l_player2 = new Player("b", new PlayerView());
        l_player2.setStrategy(new NeutralStrategy(l_player2,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        d_GameEngine.d_gamePlayModel.addPlayer(l_player1);
        d_GameEngine.d_gamePlayModel.addPlayer(l_player2);
        d_GameEngine.d_currentPhase.assignCountries();
        d_GameEngine.d_gamePlayModel.assignReinforcements();

        Player l_player3 = new Player("Neutral", new PlayerView());
        l_player2.setStrategy(new NeutralStrategy(l_player2,
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

        l_playerA.addOrder(new HumanStrategy(l_playerA,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers())
                .convertCmdToOrder(new String[]{"deploy", "India", "5"}));

        l_playerA.addOrder(new HumanStrategy(l_playerA,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers())
                .convertCmdToOrder(new String[]{"deploy", "China", "6"}));

        l_playerB.addOrder(new HumanStrategy(l_playerB,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers())
                .convertCmdToOrder(new String[]{"deploy", "Egypt", "1"}));

        l_playerA.addOrder(new HumanStrategy(l_playerA,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers())
                .convertCmdToOrder(new String[]{"advance", "India", "Egypt", "5"}));

        l_playerA.addOrder(new HumanStrategy(l_playerA,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers())
                .convertCmdToOrder(new String[]{"advance", "China", "Canada", "6"}));


        d_GameEngine.d_currentPhase.next();
        while (d_GameEngine.d_currentPhase.executeOrders()) ;

        //TODO::
        assertFalse(d_GameEngine.d_gamePlayModel.isEndGame());
    }
}