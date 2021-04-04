package Model.Orders;

import Controller.GameEngine;
import Model.Player;
import States.Startup;
import Strategy.HumanStrategy;
import Utils.CommandsParser;
import View.PlayerView;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for Blockade Model
 */
public class BlockadeModelTest {
    /**
     * Object of the gameengine
     */
    private static GameEngine d_GameEngine;
    /**
     * Object of the Blockade Model
     */
    private static BlockadeModel d_BlockadeModel;

    /**
     * Initialise the context of the test
     */
    @BeforeClass
    public static void init() {
        CommandsParser.parseJson();
    }

    /**
     * Set up a scenario
     *
     * @throws Exception throws some kind of exception
     */
    @Before
    public void beforeEach() throws Exception {
        d_GameEngine = new GameEngine();
        d_GameEngine.setPhase(new Startup(d_GameEngine));

        d_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});

        Player l_player1 = new Player("Aman", new PlayerView());
        l_player1.setStrategy(new HumanStrategy(l_player1,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        Player l_player2 = new Player("Mazen", new PlayerView());
        l_player2.setStrategy(new HumanStrategy(l_player2,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        d_GameEngine.d_gamePlayModel.addPlayer(l_player1);
        d_GameEngine.d_gamePlayModel.addPlayer(l_player2);

        d_GameEngine.d_currentPhase.assignCountries();
        d_GameEngine.d_gamePlayModel.assignReinforcements();


        DeployModel d_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Aman").getView());

        DeployModel d_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());


        d_deployModel_1.execute(d_GameEngine.d_mapModel.getCountries());
        d_deployModel_2.execute(d_GameEngine.d_mapModel.getCountries());

        d_GameEngine.d_gamePlayModel.getPlayers().get("Aman").assignSpecificCard("blockade");
    }

    /**
     * Test the blockade on the scenario set up
     */
    @Test
    public void succeedingScenario() {
        d_BlockadeModel = new BlockadeModel(
                d_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Neutral"),
                d_GameEngine.d_mapModel.getCountries().get("Saturn-South"), new String[]{"blockade", "Saturn-South"});

        assertTrue(d_BlockadeModel.execute(d_GameEngine.d_mapModel.getCountries()));
        assertEquals(90, d_GameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
        assertEquals("Neutral", d_GameEngine.d_mapModel.getCountries().get("Saturn-South").getOwnerName());
    }

    /**
     * Tests the failing scenario
     */
    @Test
    public void failingScenario() {
        d_BlockadeModel = new BlockadeModel(
                d_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Neutral"),
                d_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                new String[]{"blockade", "Venus-North"});

        assertFalse(d_BlockadeModel.execute(d_GameEngine.d_mapModel.getCountries()));
    }
}