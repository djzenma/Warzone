package States;

import Controller.GameEngine;
import Model.ContinentModel;
import Model.CountryModel;
import Utils.MapUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class MapEditor extends Phase {
    public MapEditor(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    @Override
    public void showCommands() {
        d_gameEngine.d_mapView.showAvailableCommands(true);
    }

    @Override
    public void listMaps() throws IOException {
        String[] l_allFileNames = MapUtils.getAllAvailableFileNames();
        d_gameEngine.d_mapView.showAvailableFiles(checkAllFilesValidation(l_allFileNames));
    }

    @Override
    public void exit() {
        d_gameEngine.setPhase(new GamePlayPhase(d_gameEngine));
    }

    /**
     * Checks whether all files contain valid, invalid or empty maps
     *
     * @param p_allFileNames Names of files to be checked
     * @return Validations for all files
     * @throws IOException If I/O exception of some sort has occurred
     */
    private LinkedHashMap<String, String> checkAllFilesValidation(String[] p_allFileNames) throws IOException {

        //copy currently being edited map to restore later
        LinkedHashMap<String, ContinentModel> l_currentContinents = d_gameEngine.d_mapModel.getContinents();
        LinkedHashMap<String, CountryModel> l_currentCountries = d_gameEngine.d_mapModel.getCountries();

        LinkedHashMap<String, String> l_allFilesValidation = new LinkedHashMap<>();
        File l_mapFile = null;
        String l_fileExt;

        try {
            // iterate through all the files, check extensions and assign validations
            for (String l_allFileName : p_allFileNames) {
                l_mapFile = new File(MapUtils.getMapsPath() + l_allFileName);
                l_fileExt = l_mapFile.getName().split("\\.")[l_mapFile.getName().split("\\.").length - 1];

                // check extension
                if (!(l_fileExt.trim().equals("map"))) {
                    continue;
                }

                // assign validations
                if (l_mapFile.length() == 0) {
                    l_allFilesValidation.put(l_mapFile.getName(), "Empty");
                    continue;
                }
                d_gameEngine.d_mapModel.loadMap(l_mapFile);
                d_gameEngine.d_mapModel.validateMap();
                l_allFilesValidation.put(l_mapFile.getName(), d_gameEngine.d_mapModel.isMapValid() ? "Valid" : "Invalid");
            }
        } catch (Exception l_e) {
            System.out.println(l_mapFile.getName() + " file follows different format than supported map files.");
            l_mapFile.delete();
            String[] l_allFileNames = MapUtils.getAllAvailableFileNames();
            d_gameEngine.d_mapView.showAvailableFiles(checkAllFilesValidation(l_allFileNames));
            return null;
        }
        //restore the current map to memory
        d_gameEngine.d_mapModel.setContinents(l_currentContinents);
        d_gameEngine.d_mapModel.setCountries(l_currentCountries);

        return l_allFilesValidation;
    }
}
