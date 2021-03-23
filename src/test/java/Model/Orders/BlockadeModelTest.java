package Model.Orders;

import Controller.GameEngineController;
import States.Startup;
import Utils.CommandsParser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BlockadeModelTest {
    private static GameEngineController d_gameEngineController;
    private static BlockadeModel d_blockadeModel;

    @BeforeClass
    public static void init() {
        CommandsParser.parseJson();
    }

    /**
     * Set up a scenario
     *
     * @throws Exception
     */
    @Before
    public void beforeEach() throws Exception {
        d_gameEngineController = new GameEngineController();
        d_gameEngineController.setPhase(new Startup(d_gameEngineController));

        d_gameEngineController.d_currentPhase.loadMap(new String[]{"loadmap", "solar.map"});
        d_gameEngineController.d_gamePlayModel.addPlayer("Aman");
        d_gameEngineController.d_gamePlayModel.addPlayer("Mazen");
        d_gameEngineController.d_currentPhase.assignCountries();
        d_gameEngineController.d_gamePlayModel.assignReinforcements();


        DeployModel d_deployModel_1 = new DeployModel(
                new String[]{"deploy", "Saturn-South", "30"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman").getView());

        DeployModel d_deployModel_2 = new DeployModel(
                new String[]{"deploy", "Venus-North", "46"},
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Mazen").getView());


        d_deployModel_1.execute(d_gameEngineController.d_mapModel.getCountries());
        d_deployModel_2.execute(d_gameEngineController.d_mapModel.getCountries());

        d_blockadeModel = new BlockadeModel(
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman"),
                d_gameEngineController.d_gamePlayModel.getPlayers().get("Neutral"),
                d_gameEngineController.d_mapModel.getCountries().get("Saturn-South"), new String[]{"blockade", "Saturn-South"});

        d_gameEngineController.d_gamePlayModel.getPlayers().get("Aman").assignSpecificCard("blockade");
    }

    /**
     * Test the blockade on the scenario set up
     */
    @Test
    public void execute() {
        assertTrue(d_blockadeModel.execute(d_gameEngineController.d_mapModel.getCountries()));
        assertEquals(90, d_gameEngineController.d_mapModel.getCountries().get("Saturn-South").getArmies());
        assertEquals("Neutral", d_gameEngineController.d_mapModel.getCountries().get("Saturn-South").getOwnerName());
    }
}