package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
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
    private static GameEngine D_GameEngine;
    /**
     * Object of the AdvanceModel
     */
    private static AdvanceModel D_AdvanceModel;

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
        D_GameEngine.d_gamePlayModel.addPlayer("Adeetya");
        D_GameEngine.d_gamePlayModel.addPlayer("Mazen");
        D_GameEngine.d_currentPhase.assignCountries();
        D_GameEngine.d_gamePlayModel.assignReinforcements();
    }

    /**
     * Tests for the attack scenario
     */
    @Test
    public void attack() {
        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getView());

        l_deployModel_1.execute(D_GameEngine.d_mapModel.getCountries());

        DeployModel l_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "10"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_2.execute(D_GameEngine.d_mapModel.getCountries());

        // attack and conquer
        D_AdvanceModel = new AdvanceModel(
                D_GameEngine.d_mapModel.getCountries().get("Venus-North"),
                D_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                46,
                D_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getView(),
                new String[]{"advance", "Venus-North", "Saturn-South", "46"});

        assertTrue(D_AdvanceModel.execute(D_GameEngine.d_mapModel.getCountries()));
        assertEquals("Adeetya", D_GameEngine.d_mapModel.getCountries().get("Venus-North").getOwnerName());
        assertEquals("Adeetya", D_GameEngine.d_mapModel.getCountries().get("Saturn-South").getOwnerName());
        assertEquals(39, D_GameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
    }

    /**
     * Normal move from one of the player's countries to one of its adjacent countries
     * that is also owned by the player
     */
    @Test
    public void normalMove() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(D_GameEngine.d_mapModel.getCountries());

        D_AdvanceModel = new AdvanceModel(
                D_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                D_GameEngine.d_mapModel.getCountries().get("Saturn-Southeast"),
                20,
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Saturn-South", "Saturn-Southeast", "20"});

        assertTrue(D_AdvanceModel.execute(D_GameEngine.d_mapModel.getCountries()));
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
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(D_GameEngine.d_mapModel.getCountries());

        // The target country is not adjacent to the source country
        D_AdvanceModel = new AdvanceModel(
                D_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                D_GameEngine.d_mapModel.getCountries().get("Pluto-West"),
                20,
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Saturn-South", "Pluto-West", "20"});

        assertFalse(D_AdvanceModel.execute(D_GameEngine.d_mapModel.getCountries()));

        // The source country is not owned by the player
        D_AdvanceModel = new AdvanceModel(
                D_GameEngine.d_mapModel.getCountries().get("Comet-Head"),
                D_GameEngine.d_mapModel.getCountries().get("Comet-Tail"),
                20,
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Comet-Head", "Comet-Tail", "20"});

        assertFalse(D_AdvanceModel.execute(D_GameEngine.d_mapModel.getCountries()));
    }

    /**
     * Not enough armies to advance
     */
    @Test
    public void failingNormalMove2() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(D_GameEngine.d_mapModel.getCountries());

        D_AdvanceModel = new AdvanceModel(
                D_GameEngine.d_mapModel.getCountries().get("Pluto-West"),
                D_GameEngine.d_mapModel.getCountries().get("Saturn-South"),
                20,
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                D_GameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Pluto-West", "Saturn-South", "20"});

        assertFalse(D_AdvanceModel.execute(D_GameEngine.d_mapModel.getCountries()));
    }
}