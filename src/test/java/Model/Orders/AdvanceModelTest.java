package Model.Orders;

import Controller.GameEngine;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdvanceModelTest {
    private static GameEngine d_gameEngine;
    private static AdvanceModel d_advanceModel;

    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        d_gameEngine = new GameEngine();
        d_gameEngine.setPhase(new Startup(d_gameEngine));
        d_gameEngine.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_gameEngine.d_gamePlayModel.addPlayer("Adeetya");
        d_gameEngine.d_gamePlayModel.addPlayer("Mazen");
        d_gameEngine.d_currentPhase.assignCountries();
        d_gameEngine.d_gamePlayModel.assignReinforcements();
    }

    @Test
    public void attack() {
        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("Adeetya"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getView());

        l_deployModel_1.execute(d_gameEngine.d_mapModel.getCountries());

        DeployModel l_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "10"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_2.execute(d_gameEngine.d_mapModel.getCountries());

        // attack and conquer
        d_advanceModel = new AdvanceModel(
                d_gameEngine.d_mapModel.getCountries().get("Venus-North"),
                d_gameEngine.d_mapModel.getCountries().get("Saturn-South"),
                46,
                d_gameEngine.d_gamePlayModel.getPlayers().get("Adeetya"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Adeetya").getView(),
                new String[]{"advance", "Venus-North", "Saturn-South", "46"});

        assertTrue(d_advanceModel.execute(d_gameEngine.d_mapModel.getCountries()));
        assertEquals("Adeetya", d_gameEngine.d_mapModel.getCountries().get("Venus-North").getOwnerName());
        assertEquals("Adeetya", d_gameEngine.d_mapModel.getCountries().get("Saturn-South").getOwnerName());
        assertEquals(39, d_gameEngine.d_mapModel.getCountries().get("Saturn-South").getArmies());
    }

    /**
     * Normal move from one of the player's countries to one of its adjacent countries
     * that is also owned by the player
     */
    @Test
    public void normalMove() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(d_gameEngine.d_mapModel.getCountries());

        d_advanceModel = new AdvanceModel(
                d_gameEngine.d_mapModel.getCountries().get("Saturn-South"),
                d_gameEngine.d_mapModel.getCountries().get("Saturn-Southeast"),
                20,
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Saturn-South", "Saturn-Southeast", "20"});

        assertTrue(d_advanceModel.execute(d_gameEngine.d_mapModel.getCountries()));
    }


    /**
     * 1. The target country is not adjacent to the source country
     * 2. The source country is not owned by the player
     */
    @Test
    public void failingNormalMove() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(d_gameEngine.d_mapModel.getCountries());

        // The target country is not adjacent to the source country
        d_advanceModel = new AdvanceModel(
                d_gameEngine.d_mapModel.getCountries().get("Saturn-South"),
                d_gameEngine.d_mapModel.getCountries().get("Pluto-West"),
                20,
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Saturn-South", "Pluto-West", "20"});

        assertFalse(d_advanceModel.execute(d_gameEngine.d_mapModel.getCountries()));

        // The source country is not owned by the player
        d_advanceModel = new AdvanceModel(
                d_gameEngine.d_mapModel.getCountries().get("Comet-Head"),
                d_gameEngine.d_mapModel.getCountries().get("Comet-Tail"),
                20,
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Comet-Head", "Comet-Tail", "20"});

        assertFalse(d_advanceModel.execute(d_gameEngine.d_mapModel.getCountries()));
    }

    /**
     * Not enough armies to advance
     */
    @Test
    public void failingNormalMove2() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(d_gameEngine.d_mapModel.getCountries());

        d_advanceModel = new AdvanceModel(
                d_gameEngine.d_mapModel.getCountries().get("Pluto-West"),
                d_gameEngine.d_mapModel.getCountries().get("Saturn-South"),
                20,
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngine.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Pluto-West", "Saturn-South", "20"});

        assertFalse(d_advanceModel.execute(d_gameEngine.d_mapModel.getCountries()));
    }
}