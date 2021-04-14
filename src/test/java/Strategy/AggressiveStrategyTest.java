package Strategy;

import Controller.GameEngine;
import Model.Orders.DeployModel;
import Model.Player;
import States.Startup;
import Utils.CommandsParser;
import View.PlayerView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the aggressive strategy
 */
public class AggressiveStrategyTest {

    /**
     * Object of the Gameengine
     */
    private static GameEngine d_GameEngine;

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

        Player l_player1 = new Player("Adeetya", new PlayerView());
        l_player1.setStrategy(new BenevolentStrategy(l_player1,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        Player l_player2 = new Player("Mazen", new PlayerView());
        l_player2.setStrategy(new AggressiveStrategy(l_player2,
                d_GameEngine.d_gamePlayModel.getCountries(),
                d_GameEngine.d_gamePlayModel.getPlayers()));

        d_GameEngine.d_gamePlayModel.addPlayer(l_player1);
        d_GameEngine.d_gamePlayModel.addPlayer(l_player2);

        d_GameEngine.d_currentPhase.assignCountries();
        d_GameEngine.d_gamePlayModel.assignReinforcements();
    }

    /**
     * Tests the attack from
     */
    @Test
    public void attackFrom() {
        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Pluto-West", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_1.execute(d_GameEngine.d_mapModel.getCountries());
        assertEquals("Pluto-West", d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getStrategy().attackFrom().getName());
    }

    /**
     * Tests the attack to
     */
    @Test
    public void attackTo() {
        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_1.execute(d_GameEngine.d_mapModel.getCountries());
        assertEquals("Venus-North", d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getStrategy().attackTo().getName());
    }

    /**
     * Tests the move from
     */
    @Test
    public void moveFrom() {
        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "15"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_1.execute(d_GameEngine.d_mapModel.getCountries());

        DeployModel l_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Saturn-Southeast", "14"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_2.execute(d_GameEngine.d_mapModel.getCountries());

        assertEquals("Saturn-Southeast", d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getStrategy().moveFrom().getName());
    }

    /**
     * Tests the defend
     */
    @Test
    public void defend() {
        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Pluto-West", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_1.execute(d_GameEngine.d_mapModel.getCountries());
        assertEquals("Pluto-West", d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getStrategy().defend().getName());
    }
}