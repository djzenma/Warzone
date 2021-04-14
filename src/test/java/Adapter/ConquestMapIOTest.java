package Adapter;

import Utils.MapUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests the Conquest Map
 */
public class ConquestMapIOTest {
    /**
     * Conquest map object
     */
    private ConquestMapIO d_conquestIO;
    /**
     * Test map file object
     */
    private File d_testMapFile;
    /**
     * Test save map file object
     */
    private File d_testSaveMapFile;
    /**
     * MapContainer object
     */
    private MapContainer d_map;
    /**
     * Test map file
     */
    private static final String d_TestMapFileName = "Africa.map";
    /**
     * Test save map file
     */
    private static final String d_TestSaveMapFileName = "ConquestTestMap.map";

    /**
     * Initialises context
     *
     * @throws IOException If some sort of IOException occurs
     */
    @Before
    public void init() throws IOException {
        d_conquestIO = new ConquestMapIO();
        d_testMapFile = (File) MapUtils.getMapFile(d_TestMapFileName, false).get(0);
        d_testSaveMapFile = (File) MapUtils.getMapFile(d_TestSaveMapFileName, false).get(0);
    }

    /**
     * Test loading of conquest map
     *
     * @throws IOException If some sort of IOException occurs
     */
    @Test
    public void testLoadConquestMap() throws IOException {
        d_map = d_conquestIO.loadConquestMap(d_testMapFile);
        assertEquals(41, d_map.getCountries().size());
        assertEquals(7, d_map.getContinents().size());
    }

    /**
     * Test saving of conquest map
     *
     * @throws IOException If some sort of IOException occurs
     */
    @Test
    public void testSaveConquestMap() throws IOException {
        d_map = d_conquestIO.loadConquestMap(d_testMapFile);
        d_conquestIO.saveConquestMap(d_testSaveMapFile, d_map.getContinents(), d_map.getCountries());

        // check the saved map and verify
        d_map = d_conquestIO.loadConquestMap(d_testSaveMapFile);
        assertEquals(41, d_map.getCountries().size());
        assertEquals(7, d_map.getContinents().size());
    }
}