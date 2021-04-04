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
 * Test for Advance Model
 */
public class AdvanceModelTest {
    /**
     * Object of the Gameengine
     */
    private static GameEngine d_GameEngine;
    /**
     * Object of the AdvanceModel
     */
    private static AdvanceModel d_AdvanceModel;

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
    }

    /**
     * Tests for the attack scenario
     */
    @Test
    public void attack() {
        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getView());

        l_deployModel_1.execute(d_GameEngine.d_mapModel.getCountries());

        DeployModel l_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "10"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_2.execute(d_GameEngine.d_mapModel.getCountries());

        // attack and conquer
        d_AdvanceModel = new AdvanceModel(
                d_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                d_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                46,
                d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getView(),
                new String[]{"advance", "Venus-North", "Saturn-South", "46"});

        assertTrue(d_AdvanceModel.execute(d_GameEngine.d_mapModel.getCountries()));
        assertEquals("Adeetya", d_GameEngine.d_mapModel.getCountries().get("Venus-North").getOwnerName());
        assertEquals("Adeetya", d_GameEngine.d_mapModel.getCountries().get("Saturn-South").getOwnerName());
        assertEquals(39, d_GameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
    }

    /**
     * Normal move from one of the player's countries to one of its adjacent countries
     * that is also owned by the player
     */
    @Test
    public void normalMove() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(d_GameEngine.d_mapModel.getCountries());

        d_AdvanceModel = new AdvanceModel(
                d_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                d_GameEngine.d_mapModel.getCountries().get("Saturn-Southeast"),
                20,
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Saturn-South", "Saturn-Southeast", "20"});

        assertTrue(d_AdvanceModel.execute(d_GameEngine.d_mapModel.getCountries()));
    }


    /**
     * <ol>
     *     <li> The target country is not adjacent to the source country </li>
     *     <li> The source country is not owned by the player </li>
     * </ol>
     */
    @Test
    public void failingNormalMove() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(d_GameEngine.d_mapModel.getCountries());

        // The target country is not adjacent to the source country
        d_AdvanceModel = new AdvanceModel(
                d_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                d_GameEngine.d_mapModel.getCountries().get("Pluto-West"),
                20,
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Saturn-South", "Pluto-West", "20"});

        assertFalse(d_AdvanceModel.execute(d_GameEngine.d_mapModel.getCountries()));

        // The source country is not owned by the player
        d_AdvanceModel = new AdvanceModel(
                d_GameEngine.d_mapModel.getCountries().get("Comet-Head"),
                d_GameEngine.d_mapModel.getCountries().get("Comet-Tail"),
                20,
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Comet-Head", "Comet-Tail", "20"});

        assertFalse(d_AdvanceModel.execute(d_GameEngine.d_mapModel.getCountries()));
    }

    /**
     * Not enough armies to advance
     */
    @Test
    public void failingNormalMove2() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(d_GameEngine.d_mapModel.getCountries());

        d_AdvanceModel = new AdvanceModel(
                d_GameEngine.d_mapModel.getCountries().get("Pluto-West"),
                d_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                20,
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Pluto-West", "Saturn-South", "20"});

        assertFalse(d_AdvanceModel.execute(d_GameEngine.d_mapModel.getCountries()));
    }
}