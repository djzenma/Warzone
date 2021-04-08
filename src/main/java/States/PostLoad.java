package States;

import Controller.GameEngine;
import Utils.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Post load phase
 * It extend the preload
 */
public class PostLoad extends PreLoad {
    public PostLoad(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * Edits continents
     *
     * @param l_args Array of the command arguments
     * @throws Exception Throws some kind of an exception
     */
    @Override
    public void editContinent(String[] l_args) throws Exception {
        triggerEvent(l_args, "Map Editor Phase");
        String l_commandArgs = String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length));
        d_gameEngine.d_mapModel.editContinent(l_commandArgs);
    }

    /**
     * Edits country
     *
     * @param l_args Array of the command arguments
     * @throws Exception Throws some kind of an exception
     */
    @Override
    public void editCountry(String[] l_args) throws Exception {
        triggerEvent(l_args, "Map Editor Phase");
        String l_commandArgs = String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length));
        d_gameEngine.d_mapModel.editCountry(l_commandArgs);
    }

    /**
     * Edits neighbor
     *
     * @param l_args Array of the command arguments
     * @throws Exception Throws some kind of an exception
     */
    @Override
    public void editNeighbor(String[] l_args) throws Exception {
        triggerEvent(l_args, "Map Editor Phase");
        // TODO: Refactor to have command parser method
        String l_commandArgs = String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length));
        d_gameEngine.d_mapModel.editNeighbor(l_commandArgs);
    }

    /**
     * Shows map in the map editor phase
     */
    @Override
    public void showMap() {
        triggerEvent(new String[]{"showmap"}, "Map Editor Phase");
        d_gameEngine.d_mapView.showMap(d_gameEngine.d_mapModel.getContinents(),
                d_gameEngine.d_mapModel.getCountries());
    }

    /**
     * Show countries
     */
    @Override
    public void showCountries() {
        d_gameEngine.d_mapView.showCountries(d_gameEngine.d_mapModel.getContinents(),
                d_gameEngine.d_mapModel.getCountries());
    }

    /**
     * Show continents
     */
    @Override
    public void showContinents() {
        d_gameEngine.d_mapView.showContinents(d_gameEngine.d_mapModel.getContinents());
    }

    /**
     * Validates map
     *
     * @param l_args Array of the command arguments
     */
    @Override
    public void validateMap(String[] l_args) {
        triggerEvent(l_args, "Map Editor Phase");
        d_gameEngine.d_mapModel.validateMap();
        d_gameEngine.d_mapView.validMap(d_gameEngine.d_mapModel.isMapValid());
    }

    /**
     * Saves map
     *
     * @param p_args Array of the command arguments
     * @throws Exception Throws some kind of the exception
     */
    @Override
    public boolean saveMap(String[] p_args) throws Exception {
        triggerEvent(p_args, "Map Editor Phase");
        d_gameEngine.d_mapModel.validateMap();

        // if the currently loaded map is invalid, ask for user input
        if (!d_gameEngine.d_mapModel.isMapValid()) {
            String l_input = d_gameEngine.d_mapView.askForUserInput("The map being edited is invalid, " +
                    "do you still want to save it (Y/N)? ");
            if (l_input.equals("n"))
                return true;
        }
        String l_fileName = MapUtils.getValidFileName(p_args);
        ArrayList l_fileData = MapUtils.getMapFile(l_fileName, false);
        this.saveMapWarning(l_fileName, l_fileData);
        d_gameEngine.d_mapModel.saveMap((File) l_fileData.get(0), p_args[2]);
        return false;
    }

    /**
     * Gives the warning to user if the file is being overwritten by savemap command
     *
     * @param p_fileName Name of the file
     * @param p_fileData File object of the file
     */
    private void saveMapWarning(String p_fileName, ArrayList p_fileData) {
        // Give Warning for overwriting already existing file
        if (!(d_gameEngine.d_mapModel.getCurrentFileName().equals(p_fileName)) && ((File) p_fileData.get(0)).exists()) {
            d_gameEngine.d_mapView.showMsg("\nWARNING: The " + p_fileName + " file is not loaded currently.");
        }
    }
}
