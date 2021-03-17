package States;

import Controller.GameEngineController;
import Utils.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PostLoad extends MapEditor {

    public PostLoad(GameEngineController p_gameEngineController) {
        super(p_gameEngineController);
    }

    @Override
    public void editContinent(String[] l_args) throws Exception {
        // TODO: Refactor to have command parser method
        String l_commandArgs = String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length));
        d_gameEngineController.d_mapModel.editContinent(l_commandArgs);
    }

    @Override
    public void editCountry(String[] l_args) throws Exception {
        // TODO: Refactor to have command parser method
        String l_commandArgs = String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length));
        d_gameEngineController.d_mapModel.editCountry(l_commandArgs);
    }

    @Override
    public void editNeighbor(String[] l_args) throws Exception {
        // TODO: Refactor to have command parser method
        String l_commandArgs = String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length));
        d_gameEngineController.d_mapModel.editNeighbor(l_commandArgs);
    }

    @Override
    public void showMap() {
        d_gameEngineController.d_mapView.showMap(d_gameEngineController.d_mapModel.getContinents(),
                d_gameEngineController.d_mapModel.getCountries());
    }

    @Override
    public void showCountries() {

        d_gameEngineController.d_mapView.showCountries(d_gameEngineController.d_mapModel.getContinents(),
                d_gameEngineController.d_mapModel.getCountries());
    }

    @Override
    public void showContinents() {
        d_gameEngineController.d_mapView.showContinents(d_gameEngineController.d_mapModel.getContinents());
    }

    @Override
    public void validateMap(String[] l_args) {
        d_gameEngineController.d_mapModel.validateMap();
        d_gameEngineController.d_mapView.validMap(d_gameEngineController.d_mapModel.isMapValid());
    }

    @Override
    public boolean saveMap(String[] l_args) throws Exception {
        d_gameEngineController.d_mapModel.validateMap();

        // if the currently loaded map is invalid, ask for user input
        if (!d_gameEngineController.d_mapModel.isMapValid()) {
            String l_input = d_gameEngineController.d_mapView.askForUserInput("The map being edited is invalid, " +
                    "do you still want to save it (Y/N)? ");
            if (l_input.equals("n"))
                return true;
        }
        String l_fileName = MapUtils.getValidFileName(l_args);
        ArrayList l_fileData = MapUtils.getMapFile(l_fileName, false);
        this.saveMapWarning(l_fileName, l_fileData);
        d_gameEngineController.d_mapModel.saveMap((File) l_fileData.get(0));
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
        if (!(d_gameEngineController.d_mapModel.getCurrentFileName().equals(p_fileName)) && ((File) p_fileData.get(0)).exists()) {
            d_gameEngineController.d_mapView.showMsg("\nWARNING: The " + p_fileName + " file is not loaded currently.");
        }
    }
}
