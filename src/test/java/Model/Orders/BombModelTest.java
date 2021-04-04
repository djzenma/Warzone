package Model.Orders;

import Controller.GameEngine;
import Model.Player;
import States.Startup;
import Strategy.HumanStrategy;
import Utils.CommandsParser;
import View.PlayerView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for Bomb Model
 */
public class BombModelTest {
    /**
     * Object of the gameengine
     */
    private static GameEngine d_GameEngine;
    /**
     * Object of the Bomb model
     */
    private static BombModel d_BombModel;

    /**
     * Set up a scenario
     *
     * @throws Exception throws some kind of exception
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();

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

        d_GameEngine.d_gamePlayModel.getPlayers().get("Aman").assignSpecificCard("bomb");
    }

    /**
     * Test the bomb on the true scenario set up
     */
    @Test
    public void succeedingScenario() {
        d_BombModel = new BombModel(
                d_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                new String[]{"bomb", "Venus-North"});
        assertTrue(d_BombModel.execute(d_GameEngine.d_mapModel.getCountries()));
        assertEquals(23, d_GameEngine.d_mapModel.getCountries().get("Venus-North").getArmies());
    }

    /**
     * Test the bomb on the false scenario set up
     */
    @Test
    public void failingScenario() {
        d_BombModel = new BombModel(
                d_GameEngine.d_gamePlayModel.getPlayers().get("Aman"),
                d_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                new String[]{"bomb", "Saturn-South"});

        assertFalse(d_BombModel.execute(d_GameEngine.d_mapModel.getCountries()));
        assertEquals(30, d_GameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
    }
}