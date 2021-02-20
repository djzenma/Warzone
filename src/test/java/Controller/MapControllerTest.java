package Controller;

import Utils.MapUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * TODO::
 * Add more tests for each method
 */
public class MapControllerTest {

    private static final String l_testMapFileName = "solar.map";
    private static MapController d_map;
    private static MapUtils d_mapUtils;

    @BeforeClass
    public static void init() throws Exception {
        d_map = new MapController();
        d_mapUtils = new MapUtils();
    }

    @Before
    public void setUp() throws IOException {
        d_map.editMap(l_testMapFileName);
    }

    @Test
    public void editContinent() throws Exception {
        d_map.editContinent("-add Asia 5");
        assertNotNull(d_map.getContinents().get("Asia"));
        assertEquals(5, d_map.getContinents().get("Asia").getControlValue());

        d_map.editContinent("-remove Asia");
        assertFalse(d_map.getContinents().containsKey("Asia"));
    }

    @Test
    public void editCountry() throws Exception {
        d_map.editCountry("-add Australia Earth");
        assertNotNull(d_map.getCountries().get("Australia"));
        assertEquals("Earth", d_map.getCountries().get("Australia").getContinentId());

        d_map.editCountry("-remove Australia");
        assertFalse(d_map.getCountries().containsKey("Australia"));
    }

    @Test
    public void editNeighbor() throws Exception {
        d_map.editNeighbor("-add Mars-Northwest Mercury-South");
        assertTrue(d_map.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));

        d_map.editNeighbor("-remove Mars-Northwest Mercury-South");
        assertFalse(d_map.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));

        d_map.editNeighbor("-add Mars-Northwest Mercury-South -remove Mars-Northwest Mars-Central");
        assertTrue(d_map.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mercury-South"));
        assertFalse(d_map.getCountries().get("Mars-Northwest").getNeighbors().containsKey("Mars-Central"));
        d_map.editNeighbor("-remove Mars-Northwest Mercury-South -add Mars-Northwest Mars-Central");
    }

    @Test
    public void editMapContinentTests() throws Exception {

        String[] l_continentList = {"Mercury", "Venus", "Earth", "Mars", "Comet", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};
        int[] l_continentControlValues = {6, 8, 10, 10, 4, 12, 8, 6, 6, 2};

        //compare continent names
        assertArrayEquals(l_continentList, d_map.getContinents().keySet().toArray());

        //compare continent control values
        for (int l_i = 0; l_i < l_continentList.length; l_i++) {
            assertEquals(l_continentControlValues[l_i], d_map.getContinents().get(l_continentList[l_i]).getControlValue());
        }
    }

    @Test
    public void showMap() {
    }

    @Test
    public void validateMap() throws Exception {
        d_map.editNeighbor("-remove Pluto-West Pluto-East");
        assertFalse(d_map.validateMap());

        d_map.editNeighbor("-add Pluto-West Pluto-East");
        assertTrue(d_map.validateMap());
    }
}
