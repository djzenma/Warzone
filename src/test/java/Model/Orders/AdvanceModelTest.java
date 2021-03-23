package Model.Orders;

import Controller.GameEngineController;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdvanceModelTest {
    private static GameEngineController d_gameEngineController;
    private static AdvanceModel d_advanceModel;

    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        d_gameEngineController = new GameEngineController();
        d_gameEngineController.setPhase(new Startup(d_gameEngineController));
        d_gameEngineController.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_gameEngineController.d_gamePlayModel.addPlayer("Adeetya");
        d_gameEngineController.d_gamePlayModel.addPlayer("Mazen");
        d_gameEngineController.d_currentPhase.assignCountries();
        d_gameEngineController.d_gamePlayModel.assignReinforcements();
    }

    @Test
    public void attack() {
        DeployModel l_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Adeetya"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Adeetya").getView());

        l_deployModel_1.execute(d_gameEngineController.d_mapModel.getCountries());

        DeployModel l_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel_2.execute(d_gameEngineController.d_mapModel.getCountries());

        d_advanceModel = new AdvanceModel(
                d_gameEngineController.d_mapModel.getCountries().get("Venus-North"),
                d_gameEngineController.d_mapModel.getCountries().get("Saturn-South"),
                46,
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Adeetya"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Adeetya").getView(),
                new String[]{"advance", "Venus-North", "Saturn-South", "46"});

        assertTrue(d_advanceModel.execute(d_gameEngineController.d_mapModel.getCountries()));
    }

    /**
     * Normal move from one of the player's countries to one of its adjacent countries
     * that is also owned by the player
     */
    @Test
    public void normalMove() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(d_gameEngineController.d_mapModel.getCountries());

        d_advanceModel = new AdvanceModel(
                d_gameEngineController.d_mapModel.getCountries().get("Saturn-South"),
                d_gameEngineController.d_mapModel.getCountries().get("Saturn-Southeast"),
                20,
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Saturn-South", "Saturn-Southeast", "20"});

        assertTrue(d_advanceModel.execute(d_gameEngineController.d_mapModel.getCountries()));
    }


    /**
     * The target country is not adjacent to the source country
     */
    @Test
    public void failingNormalMove() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(d_gameEngineController.d_mapModel.getCountries());

        d_advanceModel = new AdvanceModel(
                d_gameEngineController.d_mapModel.getCountries().get("Saturn-South"),
                d_gameEngineController.d_mapModel.getCountries().get("Pluto-West"),
                20,
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Saturn-South", "Pluto-West", "20"});

        assertFalse(d_advanceModel.execute(d_gameEngineController.d_mapModel.getCountries()));
    }

    /**
     * Not enough armies to advance
     */
    @Test
    public void failingNormalMove2() {
        DeployModel l_deployModel = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView());

        l_deployModel.execute(d_gameEngineController.d_mapModel.getCountries());

        d_advanceModel = new AdvanceModel(
                d_gameEngineController.d_mapModel.getCountries().get("Pluto-West"),
                d_gameEngineController.d_mapModel.getCountries().get("Saturn-South"),
                20,
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView(),
                new String[]{"advance", "Pluto-West", "Saturn-South", "20"});

        assertFalse(d_advanceModel.execute(d_gameEngineController.d_mapModel.getCountries()));
    }
}