package Controller;

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
 * Map TODO::
 */
public class MapController {

    private final MapModel d_mapModel;
    private final MapView d_mapView;
    private final MapUtils d_mapUtils;


    public MapController(MapModel p_mapModel, MapView p_mapView) {
        this.d_mapModel = p_mapModel;
        this.d_mapView = p_mapView;
        this.d_mapUtils = new MapUtils();
    }

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
                        if (!this.isMapFileLoaded(l_args[0]))
                            continue;
                        d_mapModel.editContinent(l_commandArgs);
                        break;
                    case "editcountry":
                        if (!this.isMapFileLoaded(l_args[0]))
                            continue;
                        d_mapModel.editCountry(l_commandArgs);
                        break;
                    case "editneighbor":
                        if (!this.isMapFileLoaded(l_args[0]))
                            continue;
                        d_mapModel.editNeighbor(l_commandArgs);
                        break;
                    case "savemap":
                        if (!this.isMapFileLoaded(l_args[0]))
                            continue;
                        l_fileName = getValidFileName(l_args);
                        l_fileData = getMapFile(l_fileName, false);
                        this.savemapWarning(l_fileName, l_fileData);
                        d_mapModel.saveMap((File) l_fileData.get(0));
                        break;
                    case "validatemap":
                        if (!this.isMapFileLoaded(l_args[0]))
                            continue;
                        d_mapModel.validateMap();
                        d_mapView.validMap(d_mapModel.isMapValid());
                        break;
                    case "showmap":
                        if (!this.isMapFileLoaded(l_args[0]))
                            continue;
                        d_mapView.showMap(d_mapModel.getContinents(), d_mapModel.getCountries());
                        break;
                    case "showcontinents":
                        if (!this.isMapFileLoaded(l_args[0]))
                            continue;
                        d_mapView.showContinents(d_mapModel.getContinents());
                        break;
                    case "showcountries":
                        if (!this.isMapFileLoaded(l_args[0]))
                            continue;
                        d_mapView.showCountries(d_mapModel.getContinents(), d_mapModel.getCountries());
                        break;
                    default:
                        d_mapView.notMapEditorCommand();
                }
            } catch (Exception l_e){
                d_mapView.exception(l_e.toString());
            }
        }
    }

    public boolean isMapFileLoaded(String p_command) {
        if (!d_mapModel.isMapFileLoaded()) {
            d_mapView.mapNotLoaded();
            return false;
        }
        return true;
    }

    private LinkedHashMap<String, String> checkAllFilesValidation(String[] p_allFileNames) throws IOException {
        LinkedHashMap<String, String> l_allFilesValidation = new LinkedHashMap<String, String>();
        File l_mapFile = null;
        String l_fileExt;

        try{
            for (int l_counter = 0; l_counter < p_allFileNames.length; l_counter++) {
                l_mapFile = new File(d_mapUtils.getMapsPath() + p_allFileNames[l_counter]);
                l_fileExt = l_mapFile.getName().split("\\.")[l_mapFile.getName().split("\\.").length - 1];

                if(!(l_fileExt.trim().equals("map"))){
                    continue;
                }

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
        return l_allFilesValidation;
    }

    private String[] getAllAvailableFileNames() {
        File l_maps = new File(d_mapUtils.getMapsPath());
        return l_maps.list();
    }

    private void savemapWarning(String p_fileName, ArrayList p_fileData) {

        // Give Warning for overwriting already existing file
        if (!(d_mapModel.getCurrentFileName().equals(p_fileName)) && ((File) p_fileData.get(0)).exists()) {
            d_mapView.showMsg("\nWARNING: The " + p_fileName + " file is not loaded currently.");
        }
    }

    private String getValidFileName(String[] l_args) throws Exception {
        String l_fileName = String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length));
        String l_fileExt = l_fileName.split("\\.")[l_fileName.split("\\.").length - 1].trim();
        if (!(l_fileExt.equals("map"))) {
            throw new Exception("." + l_fileExt + " files are not acceptable! Please enter .map filename.");
        }

        return l_fileName;
    }

    public ArrayList getMapFile(String p_fileName, boolean p_isNewFile) throws IOException {
        File l_file;
        l_file = new File(d_mapUtils.getMapsPath() + p_fileName);

        if (!(l_file.exists())) {
            l_file.createNewFile();
            p_isNewFile = true;
            d_mapView.showMsg(p_fileName + " file does not exists!\nCreated new " + p_fileName + " file.");
        }

        return new ArrayList (Arrays.asList(l_file, p_isNewFile));
    }
}

