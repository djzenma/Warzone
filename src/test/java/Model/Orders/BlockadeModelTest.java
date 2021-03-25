package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockadeModelTest {
    private static GameEngine d_gameEngine;
    private static BlockadeModel d_blockadeModel;

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
        d_gameEngine = new GameEngine();
        d_gameEngine.setPhase(new Startup(d_gameEngine));

        d_gameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_gameEngine.d_gamePlayModel.addPlayer("Aman");
        d_gameEngine.d_gamePlayModel.addPlayer("Mazen");
        d_gameEngine.d_currentPhase.assignCountries();
        d_gameEngine.d_gamePlayModel.assignReinforcements();


        DeployModel d_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Aman").getView());

        DeployModel d_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());


        d_deployModel_1.execute(d_gameEngine.d_mapModel.getCountries());
        d_deployModel_2.execute(d_gameEngine.d_mapModel.getCountries());

        d_gameEngine.d_gamePlayModel.getPlayers().get("Aman").assignSpecificCard("blockade");
    }

    /**
     * Test the blockade on the scenario set up
     */
    @Test
    public void succeedingScenario() {
        d_blockadeModel = new BlockadeModel(
                d_gameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Neutral"),
                d_gameEngine.d_mapModel.getCountries().get("Saturn-South"), new String[]{"blockade", "Saturn-South"});

        assertTrue(d_blockadeModel.execute(d_gameEngine.d_mapModel.getCountries()));
        assertEquals(90, d_gameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
        assertEquals("Neutral", d_gameEngine.d_mapModel.getCountries().get("Saturn-South").getOwnerName());
    }

    /**
     *
     */
    @Test
    public void failingScenario() {
        d_blockadeModel = new BlockadeModel(
                d_gameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Neutral"),
                d_gameEngine.d_mapModel.getCountries().get("Venus-North"),
                new String[]{"blockade", "Venus-North"});

        assertFalse(d_blockadeModel.execute(d_gameEngine.d_mapModel.getCountries()));
    }
}