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
    private static final String d_TestMapFileName = "solar.map";
    /**
     * Object of the mapmodel
     */
    private static MapModel d_MapModel;
    /**
     * Object of the mapview
     */
    private static MapView d_MapView;
    /**
     * Object of the mapcontroller
     */
    private static MapController d_MapController;
    /**
     * Object of the maputils
     */
    private static MapUtils d_MapUtils;

    /**
     * Initializes the MapModel and MapUtils
     */
    @BeforeClass
    public static void init() {
        d_MapModel = new MapModel();
        d_MapView = new MapView();
        d_MapController = new MapController(new GameEngine());
        d_MapUtils = new MapUtils();
    }

    /**
     * Sets the context before every test method
     *
     * @throws Exception If I/O exception of some sort has occurred
     */
    @Before
    public void setUp() throws Exception {
        File l_file = (File) MapUtils.getMapFile(d_TestMapFileName, false).get(0);
        d_MapModel.editMap(l_file);
    }

    /**
     * Tests editcontinent command
     *
     * @throws Exception If user tries to remove non-existing continent
     */
    @Test
    public void editContinent() throws Exception {
        d_MapModel.editContinent("add Asia 5");
        assertNotNull(d_MapModel.getContinents().get("Asia"));
        assertEquals(5, d_MapModel.getContinents().get("Asia").getControlValue());

        d_MapModel.editContinent("remove Asia");
        assertFalse(d_MapModel.getContinents().containsKey("Asia"));
    }

    /**
     * Tests editcountry command
     *
     * @throws Exception If user tries to add non-existing continent
     */
    @Test
    public void editCountry() throws Exception {
        d_MapModel.editCountry("add Australia Earth");
        assertNotNull(d_MapModel.getCountries().get("Australia"));
        assertEquals("Earth", d_MapModel.getCountries().get("Australia").getContinentId());

        d_MapModel.editCountry("remove Australia");
        assertFalse(d_MapModel.getCountries().containsKey("Australia"));
    }

    /**
     * Tests editneighbor command
     *
     * @throws Exception If user tries to add borders for non-existing countries
     */
    @Test
    public void editNeighbor() throws Exception {
        d_MapModel.editNeighbor("add Mars-Northwest Mercury-South");
        assertTrue(d_MapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));

        d_MapModel.editNeighbor("remove Mars-Northwest Mercury-South");
        assertFalse(d_MapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));

        d_MapModel.editNeighbor("add Mars-Northwest Mercury-South remove Mars-Northwest Mars-Central");
        assertTrue(d_MapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));
        assertFalse(d_MapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mars-Central"));
        d_MapModel.editNeighbor("remove Mars-Northwest Mercury-South add Mars-Northwest Mars-Central");
    }

    /**
     * Tests for editmap on continents only
     */
    @Test
    public void editMapContinentTests() {
        String[] l_continentList = {"Mercury", "Venus", "Earth", "Mars", "Comet", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};
        int[] l_continentControlValues = {6, 8, 10, 10, 4, 12, 8, 6, 6, 2};

        //compare continent names
        assertArrayEquals(l_continentList, d_MapModel.getContinents().keySet().toArray());

        //compare continent control values
        for (int l_i = 0; l_i < l_continentList.length; l_i++) {
            assertEquals(l_continentControlValues[l_i], d_MapModel.getContinents().get(l_continentList[l_i]).getControlValue());
        }
    }

    /**
     * Tests for entire map validation
     *
     * @throws Exception If user tries to add borders for non-existing countries
     */
    @Test
    public void validateMap() throws Exception {
        d_MapModel.editNeighbor("remove Pluto-West Pluto-East");
        d_MapModel.validateMap();
        assertFalse(d_MapModel.isMapValid());

        d_MapModel.editNeighbor("add Pluto-West Pluto-East");
        d_MapModel.validateMap();
        assertTrue(d_MapModel.isMapValid());
    }

    /**
     * Tests for continent validation
     *
     * @throws Exception If user tries to add borders for non-existing countries
     */
    @Test
    public void validateContinentConnectivity() throws Exception {
        d_MapModel.editNeighbor("remove Neptune-North Neptune-East");
        d_MapModel.editNeighbor("remove Neptune-North Neptune-West");

        // validate the entire map
        d_MapModel.validateMap();
        assertFalse(d_MapModel.isMapValid());

        // validate only continent
        assertFalse(d_MapModel.validateContinentConnectivity());
    }

    /**
     * Tests savemap command
     *
     * @throws Exception If I/O exception of some sort has occurred
     */
    @Test
    public void saveMap() throws Exception {
        d_MapModel.editMap(new File(MapUtils.getMapsPath() + "solar.map"));
        d_MapModel.saveMap(new File(MapUtils.getMapsPath() + "savemaptest.map"));
        assertTrue(d_MapUtils.areMapFilesEqual("solar.map", "savemaptest.map"));
    }

    /**
     * Tests whether the name is a string or not
     */
    @Test
    public void isNameNumber() {
        assertTrue(d_MapModel.isNameNumber("5"));
        assertFalse(d_MapModel.isNameNumber("Asia"));
        assertFalse(d_MapModel.isNameNumber("Asia1"));
        assertFalse(d_MapModel.isNameNumber("1Asia"));
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
            d_MapModel.loadOnlyValidMap(l_file);
        } catch (Exception l_e) {
            l_e.printStackTrace();
            assertFalse(d_MapModel.isMapValid());
        }
        l_file = new File("maps/us.map");
        d_MapModel.loadOnlyValidMap(l_file);
        assertTrue(d_MapModel.isMapValid());
    }
}
