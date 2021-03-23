package Model;

import Controller.GameEngineController;
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
public class PlayerModelTest {
    private static PlayerModel d_PlayerModel;
    private static GameEngineController d_gameEngineController;

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
        d_gameEngineController = new GameEngineController();
        d_gameEngineController.setPhase(new Startup(d_gameEngineController));
        try {
            d_gameEngineController.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});

            // add the players
            d_gameEngineController.d_gamePlayModel.addPlayer("Mazen");
            d_gameEngineController.d_gamePlayModel.addPlayer("Aman");

            // assign countries and reinforcements
            d_gameEngineController.d_gamePlayModel.assignCountries();
            d_gameEngineController.d_gamePlayModel.assignReinforcements();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests whether the player can deploy more armies than there is in the reinforcements pool
     */
    @Test
    public void issueOrder() {
        d_gameEngineController.setPhase(new IssueOrder(d_gameEngineController));
        assertEquals(46, d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getReinforcements());
        assertEquals(30, d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman").getReinforcements());

        d_PlayerModel = d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen");
        d_PlayerModel.setPhase(d_gameEngineController.d_currentPhase);

        assertFalse(d_PlayerModel.issueOrder(new String[]{"pass"}));
        assertTrue(d_PlayerModel.issueOrder(new String[]{"deploy", "Comet-Tail", "44"}));
        assertEquals(2, d_PlayerModel.getReinforcements());
    }

    /**
     * Tests whether the player returns the correct next order from its orders queue
     */
    @Test
    public void nextOrder() {
        String l_country = "Comet-Tail";
        String l_orderName = "deploy";
        DeployModel l_nextOrder;

        d_PlayerModel = d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen");

        d_gameEngineController.setPhase(new IssueOrder(d_gameEngineController));
        d_PlayerModel.setPhase(d_gameEngineController.d_currentPhase);
        assertTrue(d_PlayerModel.issueOrder(new String[]{l_orderName, l_country, "44"}));
        assertTrue(d_PlayerModel.issueOrder(new String[]{l_orderName, l_country, "2"}));

        d_gameEngineController.setPhase(new ExecuteOrders(d_gameEngineController));
        l_nextOrder = (DeployModel) d_PlayerModel.nextOrder();
        assertEquals(l_country, l_nextOrder.getCountryName());
        assertEquals(44, l_nextOrder.getReinforcements());

        l_nextOrder = (DeployModel) d_PlayerModel.nextOrder();
        assertEquals(l_country, l_nextOrder.getCountryName());
        assertEquals(2, l_nextOrder.getReinforcements());
    }
}