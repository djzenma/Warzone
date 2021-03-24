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
    private static Player d_Player;
    private static GameEngine d_gameEngine;

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
        d_gameEngine = new GameEngine();
        d_gameEngine.setPhase(new Startup(d_gameEngine));
        try {
            d_gameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});

            // add the players
            d_gameEngine.d_gamePlayModel.addPlayer("Mazen");
            d_gameEngine.d_gamePlayModel.addPlayer("Aman");

            // assign countries and reinforcements
            d_gameEngine.d_gamePlayModel.assignCountries();
            d_gameEngine.d_gamePlayModel.assignReinforcements();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests whether the player can deploy more armies than there is in the reinforcements pool
     */
    @Test
    public void issueOrder() {
        d_gameEngine.setPhase(new IssueOrder(d_gameEngine));
        assertEquals(46, d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getReinforcements());
        assertEquals(30, d_gameEngine.d_gamePlayModel.getPlayers().get("Aman").getReinforcements());

        d_Player = d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen");
        d_Player.setPhase(d_gameEngine.d_currentPhase);
        d_Player.setCommand(new String[]{"pass"});
        assertFalse(d_Player.issueOrder());

        d_Player.setCommand(new String[]{"deploy", "Comet-Tail", "44"});
        assertTrue(d_Player.issueOrder());
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

        d_Player = d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen");

        d_gameEngine.setPhase(new IssueOrder(d_gameEngine));
        d_Player.setPhase(d_gameEngine.d_currentPhase);
        d_Player.setCommand(new String[]{l_orderName, l_country, "44"});
        assertTrue(d_Player.issueOrder());

        d_Player.setCommand(new String[]{l_orderName, l_country, "2"});
        assertTrue(d_Player.issueOrder());

        d_gameEngine.setPhase(new ExecuteOrders(d_gameEngine));
        l_nextOrder = (DeployModel) d_Player.nextOrder();
        assertEquals(l_country, l_nextOrder.getCountryName());
        assertEquals(44, l_nextOrder.getReinforcements());

        l_nextOrder = (DeployModel) d_Player.nextOrder();
        assertEquals(l_country, l_nextOrder.getCountryName());
        assertEquals(2, l_nextOrder.getReinforcements());
    }
}