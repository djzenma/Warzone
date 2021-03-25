package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for Airlift Model
 */
public class AirliftModelTest {
    /**
     * Object of the Gameengine
     */
    private static GameEngine D_GameEngine;
    /**
     * Object of the Airlift Model
     */
    private static AirliftModel D_AirliftModel;

    /**
     * Initialise the context of the test
     *
     * @throws Exception throws some kind of the exception
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        D_GameEngine = new GameEngine();
        D_GameEngine.setPhase(new Startup(D_GameEngine));
        D_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        D_GameEngine.d_gamePlayModel.addPlayer("Mazen");
        D_GameEngine.d_gamePlayModel.addPlayer("Aman");
        D_GameEngine.d_currentPhase.assignCountries();
        D_GameEngine.d_gamePlayModel.assignReinforcements();

        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_1.execute(D_GameEngine.d_mapModel.getCountries());

        DeployModel l_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Aman").getView());

        l_deployModel_2.execute(D_GameEngine.d_mapModel.getCountries());

        D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").assignSpecificCard("airlift");
    }

    /**
     * Succeeding scenario for the airlift
     */
    @Test
    public void succeedingTrue() {
        D_AirliftModel = new AirliftModel(
                D_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                D_GameEngine.d_mapModel.getCountries().get("Comet-Head"),
                20,
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"airlift", "Venus-North", "Comet-Head", "20"});

        assertTrue(D_AirliftModel.execute(D_GameEngine.d_mapModel.getCountries()));
        assertEquals(20, D_GameEngine.d_mapModel.getCountries().get("Comet-Head").getArmies());
        assertEquals(26, D_GameEngine.d_mapModel.getCountries().get("Venus-North").getArmies());
    }

    /**
     * Failing scenario for the airlift
     */
    @Test
    public void failingScenario() {
        // The source country is not owned by the player
        D_AirliftModel = new AirliftModel(
                D_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                D_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                20,
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"airlift", "Venus-North", "Saturn-South", "20"});


        assertFalse(D_AirliftModel.execute(D_GameEngine.d_mapModel.getCountries()));
        assertEquals(30, D_GameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
        assertEquals(46, D_GameEngine.d_mapModel.getCountries().get("Venus-North").getArmies());

        // The target country is not owned by the player
        D_AirliftModel = new AirliftModel(
                D_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                D_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                20,
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"airlift", "Saturn-South", "Venus-North", "20"});


        assertFalse(D_AirliftModel.execute(D_GameEngine.d_mapModel.getCountries()));
    }
}