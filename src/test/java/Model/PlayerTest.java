package Model;

import Controller.GameEngine;
import Model.Orders.DeployModel;
import States.ExecuteOrders;
import States.IssueOrder;
import States.Startup;
import Strategy.HumanStrategy;
import Utils.*;
import View.PlayerView;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the PlayerModel
 */
public class PlayerTest {
    /**
     * Object of the player
     */
    private static Player d_Player;
    /**
     * Object of the gameengine
     */
    private static GameEngine d_GameEngine;

    /**
     * Initializes the Commands Parser
     */
    @BeforeClass
    public static void init() {
        CommandsParser.parseJson();
    }

    /**
     * Initializes the PlayerModel
     */
    @Before
    public void beforeEach() {
        d_GameEngine = new GameEngine();
        d_GameEngine.setPhase(new Startup(d_GameEngine));
        try {
            d_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});

            // add the players
            Player l_player1 = new Player("Mazen", new PlayerView());
            l_player1.setStrategy(new HumanStrategy(l_player1,
                    d_GameEngine.d_gamePlayModel.getCountries(),
                    d_GameEngine.d_gamePlayModel.getPlayers()));

            Player l_player2 = new Player("Aman", new PlayerView());
            l_player2.setStrategy(new HumanStrategy(l_player2,
                    d_GameEngine.d_gamePlayModel.getCountries(),
                    d_GameEngine.d_gamePlayModel.getPlayers()));

            d_GameEngine.d_gamePlayModel.addPlayer(l_player1);
            d_GameEngine.d_gamePlayModel.addPlayer(l_player2);

            // assign countries and reinforcements
            d_GameEngine.d_gamePlayModel.assignCountries();
            d_GameEngine.d_gamePlayModel.assignReinforcements();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests whether the player can deploy more armies than there is in the reinforcements pool
     */
    @Test
    public void issueOrder() {
        d_GameEngine.setPhase(new IssueOrder(d_GameEngine));
        assertEquals(46, d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getReinforcements());
        assertEquals(30, d_GameEngine.d_gamePlayModel.getPlayers().get("Aman").getReinforcements());

        d_Player = d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen");
        d_Player.setPhase(d_GameEngine.d_currentPhase);

        d_Player.addOrder(new HumanStrategy(d_Player,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers())
                .convertCmdToOrder(new String[]{"deploy", "Comet-Tail", "44"}));

        assertEquals(2, d_Player.getReinforcements());
    }

    /**
     * Tests whether the player returns the correct next order from its orders queue
     */
    @Test
    public void nextOrder() {
        String l_country = "Comet-Tail";
        String l_orderName = "deploy";
        DeployModel l_nextOrder;

        d_Player = d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen");

        d_GameEngine.setPhase(new IssueOrder(d_GameEngine));
        d_Player.setPhase(d_GameEngine.d_currentPhase);

        d_Player.addOrder(new HumanStrategy(d_Player,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers())
                .convertCmdToOrder(new String[]{l_orderName, l_country, "44"}));

        d_Player.addOrder(new HumanStrategy(d_Player,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers())
                .convertCmdToOrder(new String[]{l_orderName, l_country, "2"}));

        d_GameEngine.setPhase(new ExecuteOrders(d_GameEngine));
        l_nextOrder = (DeployModel) d_Player.nextOrder();
        assertEquals(l_country, l_nextOrder.getCountryName());
        assertEquals(44, l_nextOrder.getReinforcements());

        l_nextOrder = (DeployModel) d_Player.nextOrder();
        assertEquals(l_country, l_nextOrder.getCountryName());
        assertEquals(2, l_nextOrder.getReinforcements());
    }
}