package Controller;

import Model.ContinentModel;
import Model.CountryModel;
import Model.GameEngineModel;
import Model.MapModel;
import Utils.CommandsParser;
import View.GameEngineView;
import View.MapView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

/**
 * Map TODO::
 */
public class MapController {


    private MapModel d_mapModel;
    private MapView d_mapView;
    private GameEngineModel l_gameEngineModel;
    private GameEngineController l_gameEngineController;
    private LinkedHashMap<String, ContinentModel> d_continents;
    private LinkedHashMap<String, CountryModel> d_countries;

    public MapController(MapModel p_mapModel, MapView p_mapView) {
        this.d_continents = new LinkedHashMap<String, ContinentModel>();
        this.d_countries = new LinkedHashMap<String, CountryModel>();
        this.d_mapModel = p_mapModel;
        this.d_mapView = p_mapView;
    }

    public void run() {
        d_mapView.mapEditorPhase();

        String[] l_args;

        // stay in the MapEditor unless the user exits which moves the game to the GameEngine
        while (true) {
            // get a valid command from the user
            do {
                l_args = d_mapView.listenForCommands();

                if (!CommandsParser.isValidCommand(l_args))
                    d_mapView.commandNotValid();
            } while (!CommandsParser.isValidCommand(l_args));

            try {
                switch (l_args[0]) {
                    case "editcontinent":
                        if(!d_mapModel.isMapFileLoaded()){
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        d_mapModel.editContinent(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "editcountry":
                        if(!d_mapModel.isMapFileLoaded()){
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        d_mapModel.editCountry(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "editneighbor":
                        if(!d_mapModel.isMapFileLoaded()){
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        d_mapModel.editNeighbor(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "savemap":
                        if(!d_mapModel.isMapFileLoaded()){
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        d_mapModel.saveMap(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "editmap":
                        boolean l_isNewFile;
                        l_isNewFile = d_mapModel.editMap(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));

                        if(!l_isNewFile) {
                            d_mapModel.validateMap();
                            d_mapView.validMap(d_mapModel.isMapValid());
                        }
                        break;
                    case "validatemap":
                        if(!d_mapModel.isMapFileLoaded()){
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        d_mapModel.validateMap();
                        d_mapView.validMap(d_mapModel.isMapValid());
                        break;
                    case "showmap":
                        if(!d_mapModel.isMapFileLoaded()){
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        showMap();
                        break;
                    case "showcontinents":
                        if(!d_mapModel.isMapFileLoaded()){
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        showContinents();
                        break;
                    case "showcountries":
                        if(!d_mapModel.isMapFileLoaded()){
                            d_mapView.mapNotLoaded();
                            continue;
                        }
                        showCountries();
                        break;
                    case "exit":
                        l_gameEngineModel= new GameEngineModel(d_mapModel.getCountries(),
                                new ArrayList(Arrays.asList(d_mapModel.getContinents().values())));
                        l_gameEngineController = new GameEngineController(l_gameEngineModel, new GameEngineView());
                        l_gameEngineController.run();
                        break;
                    default:
                        d_mapView.commandNotValid();
                }
            } catch (Exception l_e) {
                d_mapView.exception(l_e.toString());
            }
        }
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

    public void showContinents(){
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

    public void showCountries(){
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

            if(countryModel.getNeighbors().keySet().isEmpty())
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
}

