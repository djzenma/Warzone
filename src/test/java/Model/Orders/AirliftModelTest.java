package Model.Orders;

import Controller.GameEngine;
import Model.Player;
import States.Startup;
import Strategy.HumanStrategy;
import Utils.*;
import View.PlayerView;
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
    private static GameEngine d_GameEngine;
    /**
     * Object of the Airlift Model
     */
    private static AirliftModel d_AirliftModel;

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
        d_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});

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

        d_GameEngine.d_currentPhase.assignCountries();
        d_GameEngine.d_gamePlayModel.assignReinforcements();

        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_1.execute(d_GameEngine.d_mapModel.getCountries());

        DeployModel l_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Aman").getView());

        l_deployModel_2.execute(d_GameEngine.d_mapModel.getCountries());

        d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").assignSpecificCard("airlift");
    }

    /**
     * Succeeding scenario for the airlift
     */
    @Test
    public void succeedingTrue() {
        d_AirliftModel = new AirliftModel(
                d_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                d_GameEngine.d_mapModel.getCountries().get("Comet-Head"),
                20,
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"airlift", "Venus-North", "Comet-Head", "20"});

        assertTrue(d_AirliftModel.execute(d_GameEngine.d_mapModel.getCountries()));
        assertEquals(20, d_GameEngine.d_mapModel.getCountries().get("Comet-Head").getArmies());
        assertEquals(26, d_GameEngine.d_mapModel.getCountries().get("Venus-North").getArmies());
    }

    /**
     * Failing scenario for the airlift
     */
    @Test
    public void failingScenario() {
        // The source country is not owned by the player
        d_AirliftModel = new AirliftModel(
                d_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                d_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                20,
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"airlift", "Venus-North", "Saturn-South", "20"});


        assertFalse(d_AirliftModel.execute(d_GameEngine.d_mapModel.getCountries()));
        assertEquals(30, d_GameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
        assertEquals(46, d_GameEngine.d_mapModel.getCountries().get("Venus-North").getArmies());

        // The target country is not owned by the player
        d_AirliftModel = new AirliftModel(
                d_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                d_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                20,
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"airlift", "Saturn-South", "Venus-North", "20"});

        assertFalse(d_AirliftModel.execute(d_GameEngine.d_mapModel.getCountries()));
    }
}