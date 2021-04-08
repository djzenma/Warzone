package Adapter;

import java.io.File;
import java.io.IOException;

public class MapAdapter extends DominationMapIO{

    @Override
    public MapContainer loadMap(File p_file) throws IOException {
        ConquestMapIO l_conquestMapIO = new ConquestMapIO();
        return l_conquestMapIO.loadConquestMap(p_file);
    }
    @Override
    public void saveMap(File p_file) {
        ConquestMapIO l_conquestMapIO = new ConquestMapIO();
        l_conquestMapIO.saveConquestMap(p_file);
    }
}
