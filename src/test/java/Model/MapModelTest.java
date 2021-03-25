package Model;

import Controller.GameEngine;
import Controller.MapController;
import Utils.MapUtils;
import View.MapView;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for the MapModel
 */
public class MapModelTest {
    /**
     * Map file
     */
    private static final String D_TestMapFileName = "solar.map";
    /**
     * Object of the mapmodel
     */
    private static MapModel D_MapModel;
    /**
     * Object of the mapview
     */
    private static MapView D_MapView;
    /**
     * Object of the mapcontroller
     */
    private static MapController D_MapController;
    /**
     * Object of the maputils
     */
    private static MapUtils D_MapUtils;

    /**
     * Initializes the MapModel and MapUtils
     */
    @BeforeClass
    public static void init() {
        D_MapModel = new MapModel();
        D_MapView = new MapView();
        D_MapController = new MapController(new GameEngine());
        D_MapUtils = new MapUtils();
    }

    /**
     * Sets the context before every test method
     *
     * @throws IOException If I/O exception of some sort has occurred
     */
    @Before
    public void setUp() throws IOException {
        File l_file = (File) MapUtils.getMapFile(D_TestMapFileName, false).get(0);
        D_MapModel.editMap(l_file);
    }

    /**
     * Tests editcontinent command
     *
     * @throws Exception If user tries to remove non-existing continent
     */
    @Test
    public void editContinent() throws Exception {
        D_MapModel.editContinent("add Asia 5");
        assertNotNull(D_MapModel.getContinents().get("Asia"));
        assertEquals(5, D_MapModel.getContinents().get("Asia").getControlValue());

        D_MapModel.editContinent("remove Asia");
        assertFalse(D_MapModel.getContinents().containsKey("Asia"));
    }

    /**
     * Tests editcountry command
     *
     * @throws Exception If user tries to add non-existing continent
     */
    @Test
    public void editCountry() throws Exception {
        D_MapModel.editCountry("add Australia Earth");
        assertNotNull(D_MapModel.getCountries().get("Australia"));
        assertEquals("Earth", D_MapModel.getCountries().get("Australia").getContinentId());

        D_MapModel.editCountry("remove Australia");
        assertFalse(D_MapModel.getCountries().containsKey("Australia"));
    }

    /**
     * Tests editneighbor command
     *
     * @throws Exception If user tries to add borders for non-existing countries
     */
    @Test
    public void editNeighbor() throws Exception {
        D_MapModel.editNeighbor("add Mars-Northwest Mercury-South");
        assertTrue(D_MapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));

        D_MapModel.editNeighbor("remove Mars-Northwest Mercury-South");
        assertFalse(D_MapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));

        D_MapModel.editNeighbor("add Mars-Northwest Mercury-South remove Mars-Northwest Mars-Central");
        assertTrue(D_MapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));
        assertFalse(D_MapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mars-Central"));
        D_MapModel.editNeighbor("remove Mars-Northwest Mercury-South add Mars-Northwest Mars-Central");
    }

    /**
     * Tests for editmap on continents only
     */
    @Test
    public void editMapContinentTests() {
        String[] l_continentList = {"Mercury", "Venus", "Earth", "Mars", "Comet", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};
        int[] l_continentControlValues = {6, 8, 10, 10, 4, 12, 8, 6, 6, 2};

        //compare continent names
        assertArrayEquals(l_continentList, D_MapModel.getContinents().keySet().toArray());

        //compare continent control values
        for (int l_i = 0; l_i < l_continentList.length; l_i++) {
            assertEquals(l_continentControlValues[l_i], D_MapModel.getContinents().get(l_continentList[l_i]).getControlValue());
        }
    }

    /**
     * Tests for entire map validation
     *
     * @throws Exception If user tries to add borders for non-existing countries
     */
    @Test
    public void validateMap() throws Exception {
        D_MapModel.editNeighbor("remove Pluto-West Pluto-East");
        D_MapModel.validateMap();
        assertFalse(D_MapModel.isMapValid());

        D_MapModel.editNeighbor("add Pluto-West Pluto-East");
        D_MapModel.validateMap();
        assertTrue(D_MapModel.isMapValid());
    }

    /**
     * Tests for continent validation
     *
     * @throws Exception If user tries to add borders for non-existing countries
     */
    @Test
    public void validateContinentConnectivity() throws Exception {
        D_MapModel.editNeighbor("remove Neptune-North Neptune-East");
        D_MapModel.editNeighbor("remove Neptune-North Neptune-West");

        // validate the entire map
        D_MapModel.validateMap();
        assertFalse(D_MapModel.isMapValid());

        // validate only continent
        assertFalse(D_MapModel.validateContinentConnectivity());
    }

    /**
     * Tests savemap command
     *
     * @throws IOException If I/O exception of some sort has occurred
     */
    @Test
    public void saveMap() throws IOException {
        D_MapModel.editMap(new File(MapUtils.getMapsPath() + "solar.map"));
        D_MapModel.saveMap(new File(MapUtils.getMapsPath() + "savemaptest.map"));
        assertTrue(D_MapUtils.areMapFilesEqual("solar.map", "savemaptest.map"));
    }

    /**
     * Tests whether the name is a string or not
     */
    @Test
    public void isNameNumber() {
        assertTrue(D_MapModel.isNameNumber("5"));
        assertFalse(D_MapModel.isNameNumber("Asia"));
        assertFalse(D_MapModel.isNameNumber("Asia1"));
        assertFalse(D_MapModel.isNameNumber("1Asia"));
    }

    /**
     * Tests load only valid map
     *
     * @throws Exception If exception of some sort has occurred
     */
    @Test
    public void loadOnlyValidMap() throws Exception {
        File l_file = new File("maps/artic.map");
        try {
            D_MapModel.loadOnlyValidMap(l_file);
        } catch (Exception l_e) {
            assertFalse(D_MapModel.isMapValid());
        }
        l_file = new File("maps/us.map");
        D_MapModel.loadOnlyValidMap(l_file);
        assertTrue(D_MapModel.isMapValid());
    }
}
