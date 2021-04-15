package Adapter;

import Model.ContinentModel;
import Model.CountryModel;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Map Adapter is a child class of DominationMapIO
 * It overrides the loadmap and savemap
 */
public class MapAdapter extends DominationMapIO implements Serializable {
    /**
     * serial version id
     */
    private static final long SERIAL_VERSION_UID = 129348938L;

    /**
     * Loads the map from the memory
     *
     * @param p_file .map file object from which the map file is being loaded
     * @return MapContainer object
     * @throws IOException If I/O exception of some sort has occurred
     */
    @Override
    public MapContainer loadMap(File p_file) throws IOException {
        ConquestMapIO l_conquestMapIO = new ConquestMapIO();
        return l_conquestMapIO.loadConquestMap(p_file);
    }

    /**
     * Saves the map to the memory
     *
     * @param p_file conquest map file
     * @param p_continents continents map
     * @param p_countries countries map
     * @throws IOException If I/O exception of some sort has occurred
     */
    @Override
    public void saveMap(File p_file,
                        LinkedHashMap<String, ContinentModel> p_continents,
                        LinkedHashMap<String, CountryModel> p_countries) throws IOException {
        ConquestMapIO l_conquestMapIO = new ConquestMapIO();
        l_conquestMapIO.saveConquestMap(p_file, p_continents, p_countries);
    }
}
