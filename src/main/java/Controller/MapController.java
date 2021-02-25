package Controller;

import Model.ContinentModel;
import Model.CountryModel;
import Model.MapModel;
import Utils.CommandsParser;
import Utils.MapUtils;
import View.MapView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Takes inputs from the MapView and controls the flow of execution of MapEditor
 */
public class MapController {

    private final MapModel d_mapModel;
    private final MapView d_mapView;
    private final MapUtils d_mapUtils;

    /**
     * Initializes MapModel, MapView and MapUtils objects
     *
     * @param p_mapModel MapModel object
     * @param p_mapView  MapView object
     */
    public MapController(MapModel p_mapModel, MapView p_mapView) {
        this.d_mapModel = p_mapModel;
        this.d_mapView = p_mapView;
        this.d_mapUtils = new MapUtils();
    }

    /**
     * Map-Editor phase of the game. <br>
     * User stays in this phase until it is ready to move to Gameplay start-up phase.
     */
    public void run() {
        d_mapView.showGameTitle();
        d_mapView.mapEditorPhase();

        String[] l_args;
        String l_fileName;
        ArrayList l_fileData;

        //stay in the MapEditor unless the user exits which moves the game to the GameEngine
        while (true) {
            try {
                //get a valid command from the user
                do {
                    l_args = d_mapView.listenForCommands();

                    if (!CommandsParser.isValidCommand(l_args))
                        d_mapView.commandNotValid();
                } while (!CommandsParser.isValidCommand(l_args));

                if (l_args[0].equals("exit"))
                    break;

                String l_commandArgs = String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length));

                switch (l_args[0]) {
                    case "listmaps":
                        String[] l_allFileNames = getAllAvailableFileNames();
                        d_mapView.showAvailableFiles(checkAllFilesValidation(l_allFileNames));
                        break;
                    case "showcommands":
                        d_mapView.showAvailableCommands(true);
                        break;
                    case "editmap":
                        l_fileName = getValidFileName(l_args);
                        l_fileData = getMapFile(l_fileName, false);
                        d_mapModel.editMap((File) l_fileData.get(0));

                        //if existing map file is loaded, validate the map
                        //i.e don't validate for newly created files
                        if (((File) l_fileData.get(0)).length() != 0) {
                            d_mapModel.validateMap();
                            d_mapView.validMap(d_mapModel.isMapValid());
                        }
                        break;
                    case "editcontinent":
                        if (!this.isMapFileLoaded())
                            continue;
                        d_mapModel.editContinent(l_commandArgs);
                        break;
                    case "editcountry":
                        if (!this.isMapFileLoaded())
                            continue;
                        d_mapModel.editCountry(l_commandArgs);
                        break;
                    case "editneighbor":
                        if (!this.isMapFileLoaded())
                            continue;
                        d_mapModel.editNeighbor(l_commandArgs);
                        break;
                    case "savemap":
                        if (!this.isMapFileLoaded())
                            continue;
                        d_mapModel.validateMap();

                        // if the currently loaded map is invalid, ask for user input
                        if (!d_mapModel.isMapValid()) {
                            String l_input = d_mapView.askForUserInput("The map being edited is invalid, " +
                                    "do you still want to save it (Y/N)? ");
                            if (l_input.equals("n"))
                                continue;
                        }
                        l_fileName = getValidFileName(l_args);
                        l_fileData = getMapFile(l_fileName, false);
                        this.saveMapWarning(l_fileName, l_fileData);
                        d_mapModel.saveMap((File) l_fileData.get(0));
                        break;
                    case "validatemap":
                        if (!this.isMapFileLoaded())
                            continue;
                        d_mapModel.validateMap();
                        d_mapView.validMap(d_mapModel.isMapValid());
                        break;
                    case "showmap":
                        if (!this.isMapFileLoaded())
                            continue;
                        d_mapView.showMap(d_mapModel.getContinents(), d_mapModel.getCountries());
                        break;
                    case "showcontinents":
                        if (!this.isMapFileLoaded())
                            continue;
                        d_mapView.showContinents(d_mapModel.getContinents());
                        break;
                    case "showcountries":
                        if (!this.isMapFileLoaded())
                            continue;
                        d_mapView.showCountries(d_mapModel.getContinents(), d_mapModel.getCountries());
                        break;
                    default:
                        d_mapView.notMapEditorCommand();
                }
            } catch (Exception l_e) {
                d_mapView.exception(l_e.toString());
            }
        }
    }

    /**
     * Checks if any map file is currently loaded or not
     *
     * @return True, if any map is currently loaded; False otherwise
     */
    public boolean isMapFileLoaded() {
        if (!d_mapModel.isMapFileLoaded()) {
            d_mapView.mapNotLoaded();
            return false;
        }
        return true;
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
        LinkedHashMap<String, ContinentModel> l_currentContinents = d_mapModel.getContinents();
        LinkedHashMap<String, CountryModel> l_currentCountries = d_mapModel.getCountries();

        LinkedHashMap<String, String> l_allFilesValidation = new LinkedHashMap<>();
        File l_mapFile = null;
        String l_fileExt;

        try {
            // iterate through all the files, check extensions and assign validations
            for (String l_allFileName : p_allFileNames) {
                l_mapFile = new File(d_mapUtils.getMapsPath() + l_allFileName);
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
                d_mapModel.loadMap(l_mapFile);
                d_mapModel.validateMap();
                l_allFilesValidation.put(l_mapFile.getName(), d_mapModel.isMapValid() ? "Valid" : "Invalid");
            }
        } catch (Exception l_e) {
            System.out.println(l_mapFile.getName() + " file follows different format than supported map files.");
            l_mapFile.delete();
            System.out.println(l_e.toString());
            String[] l_allFileNames = getAllAvailableFileNames();
            d_mapView.showAvailableFiles(checkAllFilesValidation(l_allFileNames));
            return null;
        }

        //restore the current map to memory
        d_mapModel.setContinents(l_currentContinents);
        d_mapModel.setCountries(l_currentCountries);

        return l_allFilesValidation;
    }

    /**
     * Gets names of all the files from the "maps/" directory
     *
     * @return Names of all the files
     */
    private String[] getAllAvailableFileNames() {
        File l_maps = new File(d_mapUtils.getMapsPath());
        return l_maps.list();
    }

    /**
     * Gives the warning to user if the file is being overwritten by savemap command
     *
     * @param p_fileName Name of the file
     * @param p_fileData File object of the file
     */
    private void saveMapWarning(String p_fileName, ArrayList p_fileData) {
        // Give Warning for overwriting already existing file
        if (!(d_mapModel.getCurrentFileName().equals(p_fileName)) && ((File) p_fileData.get(0)).exists()) {
            d_mapView.showMsg("\nWARNING: The " + p_fileName + " file is not loaded currently.");
        }
    }

    /**
     * Ensures that the file extension is .map file
     *
     * @param p_args Name of the file
     * @return Name of .map file
     * @throws Exception If the file is not .map file
     */
    private String getValidFileName(String[] p_args) throws Exception {
        String l_fileName = String.join(" ", Arrays.copyOfRange(p_args, 1, p_args.length));
        String l_fileExt = l_fileName.split("\\.")[l_fileName.split("\\.").length - 1].trim();
        if (!(l_fileExt.equals("map"))) {
            throw new Exception("." + l_fileExt + " files are not acceptable! Please enter .map filename.");
        }
        return l_fileName;
    }

    /**
     * Gets the object of map file
     *
     * @param p_fileName  Name of the map file
     * @param p_isNewFile Specifies whether the map file is newly created or not
     * @return Map file object and information about the file
     * @throws IOException If I/O exception of some sort has occurred
     */
    public ArrayList getMapFile(String p_fileName, boolean p_isNewFile) throws IOException {
        File l_file;
        l_file = new File(d_mapUtils.getMapsPath() + p_fileName);

        if (!(l_file.exists())) {
            l_file.createNewFile();
            p_isNewFile = true;
            d_mapView.showMsg(p_fileName + " file does not exists!\nCreated new " + p_fileName + " file.");
        }
        return new ArrayList(Arrays.asList(l_file, p_isNewFile));
    }
}

