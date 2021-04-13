package States;

import Controller.GameEngine;
import Model.Player;
import Strategy.HumanStrategy;
import Utils.CommandsParser;
import View.PlayerView;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IssueOrderTest {
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
    public void beforeClass() throws Exception {
        d_GameEngine = new GameEngine();
        init();
        d_GameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});

        Player l_player1 = new Player("Adeetya", new PlayerView());
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

        d_GameEngine.d_currentPhase.saveGame(new String[]{"savegame", "checkpointtest"});
    }

    public void init(){
        CommandsParser.parseJson();
        d_GameEngine.setPhase(new Startup(d_GameEngine));
    }

    @Test
    public void saveAndLoadGame() {
        init();
        d_GameEngine.d_currentPhase.loadGame(new String[]{"loadgame", "checkpointtest"});

        assertTrue(d_GameEngine.d_gamePlayModel.getPlayers().containsKey("Mazen"));
        assertTrue(d_GameEngine.d_gamePlayModel.getPlayers().containsKey("Adeetya"));

        assertEquals(52, d_GameEngine.d_gamePlayModel.getCountries().size());
    }
}