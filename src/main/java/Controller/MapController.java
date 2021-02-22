package Controller;
import Model.ContinentModel;
import Model.CountryModel;
import Model.GameEngineModel;
import Model.MapModel;
import Utils.CommandsParser;
import Utils.MapUtils;
import View.MapView;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Map TODO::
 */
public class MapController {

    private final MapModel d_mapModel;
    private final MapView d_mapView;
    private final MapUtils d_mapUtils;

    private LinkedHashMap<String, ContinentModel> d_continents;
    private LinkedHashMap<String, CountryModel> d_countries;

    public MapController(MapModel p_mapModel, MapView p_mapView) {
        this.d_continents = new LinkedHashMap<String, ContinentModel>();
        this.d_countries = new LinkedHashMap<String, CountryModel>();
        this.d_mapModel = p_mapModel;
        this.d_mapView = p_mapView;
        this.d_mapUtils = new MapUtils();
    }

    public void run() {
        d_mapView.mapEditorPhase();

        String[] l_args;
        String l_fileName;
        ArrayList l_fileData;

        // stay in the MapEditor unless the user exits which moves the game to the GameEngine
        while (true) {
            // get a valid command from the user
            do {
                l_args = d_mapView.listenForCommands();

                if (!CommandsParser.isValidCommand(l_args))
                    d_mapView.commandNotValid();
            } while (!CommandsParser.isValidCommand(l_args));

            try {
                if (l_args[0].equals("exit"))
                    break;

                switch (l_args[0]) {
                    case "editcontinent":
                        if (d_mapModel.isMapFileLoaded()) {
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        d_mapModel.editContinent(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "editcountry":
                        if (d_mapModel.isMapFileLoaded()) {
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        d_mapModel.editCountry(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "editneighbor":
                        if (d_mapModel.isMapFileLoaded()) {
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        d_mapModel.editNeighbor(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "savemap":
                        l_fileName = getValidFileName(l_args);
                        if (d_mapModel.isMapFileLoaded()) {
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        l_fileData = getMapFile(l_fileName, false);

                        // Give Warning for overwriting already existing file
                        if (!(d_mapModel.getCurrentFileName().equals(l_fileName)) && ((File) l_fileData.get(0)).exists()) {
                            d_mapView.showMsg("\nWARNING: The " + l_fileName + " file is not loaded currently.");
                        }
                        d_mapModel.saveMap((File) l_fileData.get(0));
                        break;
                    case "editmap":
                        l_fileName = getValidFileName(l_args);
                        l_fileData = getMapFile(l_fileName, false);
                        d_mapModel.editMap((File) l_fileData.get(0));

                        if (!(boolean) l_fileData.get(1)) {
                            d_mapModel.validateMap();
                            d_mapView.validMap(d_mapModel.isMapValid());
                        }
                        break;
                    case "validatemap":
                        if (d_mapModel.isMapFileLoaded()) {
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        d_mapModel.validateMap();
                        d_mapView.validMap(d_mapModel.isMapValid());
                        break;
                    case "showmap":
                        if (d_mapModel.isMapFileLoaded()) {
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        showMap();
                        break;
                    case "showcontinents":
                        if (d_mapModel.isMapFileLoaded()) {
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        showContinents();
                        break;
                    case "showcountries":
                        if (d_mapModel.isMapFileLoaded()) {
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        showCountries();
                        break;
                    default:
                        d_mapView.commandNotValid();
                }
            } catch (Exception l_e) {
                d_mapView.exception(l_e.toString());
            }
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

    /**
     * A method that handles the showMap command
     */
    public void showMap() {
        this.d_continents = d_mapModel.getContinents();
        this.d_countries = d_mapModel.getCountries();
        showContinents();
        showCountries();
    }

    public void showContinents() {
        System.out.println("\n<ContinentID> <ContinentName> <ControlValue>\n");

        this.d_continents.values().stream().sorted(new Comparator<ContinentModel>() {
            @Override
            public int compare(ContinentModel o1, ContinentModel o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        }).forEach(new Consumer<ContinentModel>() {
            @Override
            public void accept(ContinentModel continentModel) {
                System.out.println("(" + continentModel.getId() + ") " +
                        continentModel.getName() +
                        " \"" + continentModel.getControlValue() + "\"");
            }
        });
    }

    public void showCountries() {
        System.out.println("\n<CountryID> <CountryName> <ContinentName> <Neighbor Countries>\n");

        this.d_countries.values().stream().sorted(new Comparator<CountryModel>() {
            @Override
            public int compare(CountryModel o1, CountryModel o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        }).forEach(countryModel -> {

            System.out.print("(" + countryModel.getId() + ") ");
            System.out.print(countryModel.getName() + " ");
            System.out.print(countryModel.getContinentId() + " ");

            if (countryModel.getNeighbors().keySet().isEmpty())
                System.out.println("[No Neighbors]");
            else {
                System.out.print(countryModel.getNeighbors().keySet());

                // print neighbor countries' ids
                System.out.print(" [");
                countryModel.getNeighbors().values().forEach(countryModel1 -> {
                    System.out.print(countryModel1.getId() + " ");
                });
                System.out.println("\b]");
            }
            System.out.println();
        });
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

