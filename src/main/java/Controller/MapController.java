package Controller;

import Model.GameEngineModel;
import Model.MapModel;
import Utils.CommandsParser;
import View.GameEngineView;
import View.MapView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Map TODO::
 */
public class MapController {

    private final MapModel d_mapModel;
    private final MapView d_mapView;

    public MapController(MapModel p_mapModel, MapView p_mapView) {
        this.d_mapModel = p_mapModel;
        this.d_mapView = p_mapView;
    }

    public void run() {
        d_mapView.mapEditorPhase();

        boolean l_end = false;
        String[] l_args;

        // stay in the MapEditor unless the user exit which moves the game to the GameEngine
        while (!l_end) {
            // get a valid command from the user
            do {
                l_args = d_mapView.listenForCommands();

                System.out.println(String.join(" ", l_args));

                if (!CommandsParser.isValidCommand(l_args))
                    d_mapView.commandNotValid();
            } while (!CommandsParser.isValidCommand(l_args));

            try {
                switch (l_args[0]) {
                    case "editcontinent":
                        d_mapModel.editContinent(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "editcountry":
                        d_mapModel.editCountry(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "editneighbor":
                        d_mapModel.editNeighbor(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "savemap":
                        d_mapModel.saveMap(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "editmap":
                        d_mapModel.editMap(String.join(" ", Arrays.copyOfRange(l_args, 1, l_args.length)));
                        break;
                    case "validatemap":
                        d_mapModel.validateMap();
                        break;
                    case "showmap":
                        d_mapModel.showMap();
                        break;
                    case "exit":
                        GameEngineModel l_gameEngineModel = new GameEngineModel(d_mapModel.getCountries(),
                                new ArrayList(Arrays.asList(d_mapModel.getContinents().values())));
                        GameEngineController l_gameEngineController = new GameEngineController(l_gameEngineModel, new GameEngineView());
                        l_gameEngineController.run();
                        break;
                    default:
                        d_mapView.commandNotValid();
                }
            } catch (Exception l_e) {
                d_mapView.exception("Exception: " + l_e.getMessage());
            }

        }
    }
}

