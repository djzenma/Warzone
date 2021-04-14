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
public class BenevolentStrategyTest {

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
        l_player2.setStrategy(new HumanStrategy(l_player2,
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
    public void testAttackFrom() {
        assertNull(d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getStrategy().attackFrom());
    }

    /**
     * Tests the attack to
     */
    @Test
    public void testAttackTo() {
        assertNull(d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getStrategy().attackTo());
    }

    /**
     * Tests the move from
     */
    @Test
    public void testMoveFrom() {
        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Mars-Central", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getView());

        l_deployModel_1.execute(d_GameEngine.d_mapModel.getCountries());
        assertEquals("Mars-Central", d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getStrategy().moveFrom().getName());
    }

    /**
     * Tests the defend
     */
    @Test
    public void testDefend() {
        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Mars-Central", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getView());

        l_deployModel_1.execute(d_GameEngine.d_mapModel.getCountries());
        assertEquals("Earth-NorthAmerica", d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getStrategy().defend().getName());
    }
}