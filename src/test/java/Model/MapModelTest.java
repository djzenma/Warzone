package Model;

import Controller.MapController;
import Utils.MapUtils;
import View.MapView;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * TODO::
 * Add more tests for each method
 */
public class MapModelTest {

    private static final String l_testMapFileName = "solar.map";
    private static MapModel d_mapModel;
    private static MapView d_mapView;
    private static MapController d_mapController;
    private static MapUtils d_mapUtils;

    @BeforeClass
    public static void init() throws Exception {
        d_mapModel = new MapModel();
        d_mapView = new MapView();
        d_mapController = new MapController(d_mapModel, d_mapView);

        d_mapUtils = new MapUtils();
    }

    @Before
    public void setUp() throws Exception {
        File l_file = (File) d_mapController.getMapFile(l_testMapFileName, false).get(0);
        d_mapModel.editMap(l_file);
    }

    @Test
    public void editContinent() throws Exception {
        d_mapModel.editContinent("add Asia 5");
        assertNotNull(d_mapModel.getContinents().get("Asia"));
        assertEquals(5, d_mapModel.getContinents().get("Asia").getControlValue());

        d_mapModel.editContinent("remove Asia");
        assertFalse(d_mapModel.getContinents().containsKey("Asia"));
    }

    @Test
    public void editCountry() throws Exception {
        d_mapModel.editCountry("add Australia Earth");
        assertNotNull(d_mapModel.getCountries().get("Australia"));
        assertEquals("Earth", d_mapModel.getCountries().get("Australia").getContinentId());

        d_mapModel.editCountry("remove Australia");
        assertFalse(d_mapModel.getCountries().containsKey("Australia"));
    }

    @Test
    public void editNeighbor() throws Exception {
        d_mapModel.editNeighbor("add Mars-Northwest Mercury-South");
        assertTrue(d_mapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));

        d_mapModel.editNeighbor("remove Mars-Northwest Mercury-South");
        assertFalse(d_mapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));

        d_mapModel.editNeighbor("add Mars-Northwest Mercury-South remove Mars-Northwest Mars-Central");
        assertTrue(d_mapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));
        assertFalse(d_mapModel.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mars-Central"));
        d_mapModel.editNeighbor("remove Mars-Northwest Mercury-South add Mars-Northwest Mars-Central");
    }

    @Test
    public void editMapContinentTests() throws Exception {

        String[] l_continentList = {"Mercury", "Venus", "Earth", "Mars", "Comet", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};
        int[] l_continentControlValues = {6, 8, 10, 10, 4, 12, 8, 6, 6, 2};

        //compare continent names
        assertArrayEquals(l_continentList, d_mapModel.getContinents().keySet().toArray());

        //compare continent control values
        for (int l_i = 0; l_i < l_continentList.length; l_i++) {
            assertEquals(l_continentControlValues[l_i], d_mapModel.getContinents().get(l_continentList[l_i]).getControlValue());
        }
    }

    @Test
    public void validateMap() throws Exception {
        d_mapModel.editNeighbor("remove Pluto-West Pluto-East");
        d_mapModel.validateMap();
        assertFalse(d_mapModel.isMapValid());

        d_mapModel.editNeighbor("add Pluto-West Pluto-East");
        d_mapModel.validateMap();
        assertTrue(d_mapModel.isMapValid());
    }
}
