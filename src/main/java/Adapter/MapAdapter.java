package Adapter;

import Model.ContinentModel;
import Model.CountryModel;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class MapAdapter extends DominationMapIO implements Serializable {
    private static final long serialversionUID = 129348938L;

    // TODO :: Add tests

    @Override
    public MapContainer loadMap(File p_file) throws IOException {
        ConquestMapIO l_conquestMapIO = new ConquestMapIO();
        return l_conquestMapIO.loadConquestMap(p_file);
    }

    @Override
    public void saveMap(File p_file,
                        LinkedHashMap<String, ContinentModel> p_continents,
                        LinkedHashMap<String, CountryModel> p_countries) throws IOException {
        ConquestMapIO l_conquestMapIO = new ConquestMapIO();
        l_conquestMapIO.saveConquestMap(p_file, p_continents, p_countries);
    }
}
