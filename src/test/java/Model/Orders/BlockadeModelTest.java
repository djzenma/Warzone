package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
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
    private static GameEngine D_GameEngine;
    /**
     * Object of the Blockade Model
     */
    private static BlockadeModel D_BlockadeModel;

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
        D_GameEngine = new GameEngine();
        D_GameEngine.setPhase(new Startup(D_GameEngine));

        D_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        D_GameEngine.d_gamePlayModel.addPlayer("Aman");
        D_GameEngine.d_gamePlayModel.addPlayer("Mazen");
        D_GameEngine.d_currentPhase.assignCountries();
        D_GameEngine.d_gamePlayModel.assignReinforcements();


        DeployModel d_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Aman").getView());

        DeployModel d_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());


        d_deployModel_1.execute(D_GameEngine.d_mapModel.getCountries());
        d_deployModel_2.execute(D_GameEngine.d_mapModel.getCountries());

        D_GameEngine.d_gamePlayModel.getPlayers().get("Aman").assignSpecificCard("blockade");
    }

    /**
     * Test the blockade on the scenario set up
     */
    @Test
    public void succeedingScenario() {
        D_BlockadeModel = new BlockadeModel(
                D_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Neutral"),
                D_GameEngine.d_mapModel.getCountries().get("Saturn-South"), new String[]{"blockade", "Saturn-South"});

        assertTrue(D_BlockadeModel.execute(D_GameEngine.d_mapModel.getCountries()));
        assertEquals(90, D_GameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
        assertEquals("Neutral", D_GameEngine.d_mapModel.getCountries().get("Saturn-South").getOwnerName());
    }

    /**
     * Tests the failing scenario
     */
    @Test
    public void failingScenario() {
        D_BlockadeModel = new BlockadeModel(
                D_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Neutral"),
                D_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                new String[]{"blockade", "Venus-North"});

        assertFalse(D_BlockadeModel.execute(D_GameEngine.d_mapModel.getCountries()));
    }
}