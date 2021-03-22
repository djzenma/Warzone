package Controller;

import Model.MapModel;
import States.MapEditor;
import States.PreLoad;
import Utils.CommandsParser;
import View.MapView;

/**
 * Takes inputs from the MapView and controls the flow of execution of MapEditor
 */
public class MapController {

    private final MapModel d_mapModel;
    private final MapView d_mapView;
    private final GameEngineController d_gameEngineController;

    /**
     * Initializes MapModel, MapView and MapUtils objects
     */
    public MapController(GameEngineController p_gameEngineController) {
        super();
        this.d_mapModel = p_gameEngineController.d_mapModel;
        this.d_mapView = p_gameEngineController.d_mapView;
        this.d_gameEngineController = p_gameEngineController;
        d_gameEngineController.setPhase(new MapEditor(p_gameEngineController));
    }

    /**
     * Map-Editor phase of the game. <br>
     * User stays in this phase until it is ready to move to Gameplay start-up phase.
     */
    public void run() {
        d_mapView.showGameTitle();
        d_mapView.mapEditorPhase();

        String[] l_args;
        d_gameEngineController.setPhase(new PreLoad(d_gameEngineController));

        //stay in the MapEditor unless the user exits which moves the game to the GameEngine
        while (true) {
            try {
                //get a valid command from the user
                do {
                    l_args = d_mapView.listenForCommands();

                    if (!CommandsParser.isValidCommand(l_args))
                        d_mapView.commandNotValid();
                } while (!CommandsParser.isValidCommand(l_args));

                switch (l_args[0]) {
                    case "exit":
                        d_gameEngineController.d_currentPhase.exit();
                        return;
                    case "listmaps":
                        d_gameEngineController.d_currentPhase.listMaps();
                        break;
                    case "showcommands":
                        d_gameEngineController.d_currentPhase.showCommands();
                        break;
                    case "editmap":
                        d_gameEngineController.d_currentPhase.editMap(l_args);
                        break;
                    case "editcontinent":
                        d_gameEngineController.d_currentPhase.editContinent(l_args);
                        break;
                    case "editcountry":
                        d_gameEngineController.d_currentPhase.editCountry(l_args);
                        break;
                    case "editneighbor":
                        d_gameEngineController.d_currentPhase.editNeighbor(l_args);
                        break;
                    case "savemap":
                        d_gameEngineController.d_currentPhase.saveMap(l_args);
                        break;
                    case "validatemap":
                        d_gameEngineController.d_currentPhase.validateMap(l_args);
                        break;
                    case "showmap":
                        d_gameEngineController.d_currentPhase.showMap();
                        break;
                    case "showcontinents":
                        d_gameEngineController.d_currentPhase.showContinents();
                        break;
                    case "showcountries":
                        d_gameEngineController.d_currentPhase.showCountries();
                        break;
                    default:
                        d_mapView.notMapEditorCommand();
                }
            } catch (Exception l_e) {
                d_mapView.exception(l_e.getMessage());
            }
        }
    }


}

