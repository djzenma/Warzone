package Model;

import Controller.GameEngine;
import Model.Orders.DeployModel;
import States.ExecuteOrders;
import States.IssueOrder;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the PlayerModel
 */
public class PlayerTest {
    /**
     * Object of the player
     */
    private static Player D_Player;
    /**
     * Object of the gameengine
     */
    private static GameEngine D_GameEngine;

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
        D_GameEngine = new GameEngine();
        D_GameEngine.setPhase(new Startup(D_GameEngine));
        try {
            D_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});

            // add the players
            D_GameEngine.d_gamePlayModel.addPlayer("Mazen");
            D_GameEngine.d_gamePlayModel.addPlayer("Aman");

            // assign countries and reinforcements
            D_GameEngine.d_gamePlayModel.assignCountries();
            D_GameEngine.d_gamePlayModel.assignReinforcements();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests whether the player can deploy more armies than there is in the reinforcements pool
     */
    @Test
    public void issueOrder() {
        D_GameEngine.setPhase(new IssueOrder(D_GameEngine));
        assertEquals(46, D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getReinforcements());
        assertEquals(30, D_GameEngine.d_gamePlayModel.getPlayers().get("Aman").getReinforcements());

        D_Player = D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen");
        D_Player.setPhase(D_GameEngine.d_currentPhase);
        D_Player.setCommand(new String[]{"pass"});
        assertFalse(D_Player.issueOrder());

        D_Player.setCommand(new String[]{"deploy", "Comet-Tail", "44"});
        assertTrue(D_Player.issueOrder());
        assertEquals(2, D_Player.getReinforcements());
    }

    /**
     * Tests whether the player returns the correct next order from its orders queue
     */
    @Test
    public void nextOrder() {
        String l_country = "Comet-Tail";
        String l_orderName = "deploy";
        DeployModel l_nextOrder;

        D_Player = D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen");

        D_GameEngine.setPhase(new IssueOrder(D_GameEngine));
        D_Player.setPhase(D_GameEngine.d_currentPhase);
        D_Player.setCommand(new String[]{l_orderName, l_country, "44"});
        assertTrue(D_Player.issueOrder());

        D_Player.setCommand(new String[]{l_orderName, l_country, "2"});
        assertTrue(D_Player.issueOrder());

        D_GameEngine.setPhase(new ExecuteOrders(D_GameEngine));
        l_nextOrder = (DeployModel) D_Player.nextOrder();
        assertEquals(l_country, l_nextOrder.getCountryName());
        assertEquals(44, l_nextOrder.getReinforcements());

        l_nextOrder = (DeployModel) D_Player.nextOrder();
        assertEquals(l_country, l_nextOrder.getCountryName());
        assertEquals(2, l_nextOrder.getReinforcements());
    }
}