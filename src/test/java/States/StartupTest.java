package States;

import Controller.GameEngine;
import Controller.GamePlayController;
import Model.Player;
import Strategy.HumanStrategy;
import Utils.CommandsParser;
import View.PlayerView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the startup phase
 */
public class StartupTest {
    /**
     * Object of the gameengine
     */
    GameEngine d_gameEngine;
    /**
     * Object of the gameplay controller
     */
    GamePlayController d_gamePlayController;

    /**
     * Initialise the context of the test
     *
     * @throws Exception throws some kind of the exception
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        d_gameEngine = new GameEngine();
        d_gameEngine.setPhase(new Startup(d_gameEngine));
        d_gameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});

        Player l_player1 = new Player("Adeetya", new PlayerView());
        l_player1.setStrategy(new HumanStrategy(l_player1,
                d_gameEngine.d_gamePlayModel.getCountries(),
                d_gameEngine.d_gamePlayModel.getPlayers()));

        Player l_player2 = new Player("Mazen", new PlayerView());
        l_player2.setStrategy(new HumanStrategy(l_player2,
                d_gameEngine.d_gamePlayModel.getCountries(),
                d_gameEngine.d_gamePlayModel.getPlayers()));

        d_gameEngine.d_gamePlayModel.addPlayer(l_player1);
        d_gameEngine.d_gamePlayModel.addPlayer(l_player2);

        d_gameEngine.d_currentPhase.assignCountries();
        d_gameEngine.d_gamePlayModel.assignReinforcements();
    }

    /**
     * Tests the startup phase
     */
    @Test
    public void isStartup() {
        d_gameEngine = new GameEngine();
        d_gamePlayController = new GamePlayController(d_gameEngine);
        d_gameEngine.d_currentPhase.startup();
        assertEquals("Startup", d_gameEngine.d_currentPhase.getClass().getSimpleName());
    }

    /**
     * Tests the added players
     */
    @Test
    public void checkPlayers() {
        assertTrue(d_gameEngine.d_gamePlayModel.getPlayers().containsKey("Mazen"));
        assertTrue(d_gameEngine.d_gamePlayModel.getPlayers().containsKey("Adeetya"));
    }

    /**
     * Tests the number of countries
     */
    @Test
    public void checkCountries() {
        assertEquals(52, d_gameEngine.d_gamePlayModel.getCountries().size());
    }

    /**
     * Tests the number of reinforcements
     */
    @Test
    public void checkReinforcements() {
        assertEquals(46, d_gameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getReinforcements());
        assertEquals(30, d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getReinforcements());
    }

    /**
     * Tests the turn number
     */
    @Test
    public void checkTurnNumber() {
        assertEquals(1, d_gameEngine.d_gamePlayModel.l_turnNumber);
    }
}