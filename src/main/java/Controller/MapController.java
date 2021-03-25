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
    /**
     * Object of the mapmodel
     */
    private final MapModel d_mapModel;
    /**
     * Object of the mapview
     */
    private final MapView d_mapView;
    /**
     * Object of the gameengine
     */
    private final GameEngine d_gameEngine;

    /**
     * Initializes MapModel, MapView and MapUtils objects
     */
    public MapController(GameEngine p_gameEngine) {
        super();
        this.d_mapModel = p_gameEngine.d_mapModel;
        this.d_mapView = p_gameEngine.d_mapView;
        this.d_gameEngine = p_gameEngine;
        d_gameEngine.setPhase(new MapEditor(p_gameEngine));
    }

    /**
     * Map-Editor phase of the game. <br>
     * User stays in this phase until it is ready to move to Gameplay start-up phase.
     */
    public void run() {
        d_mapView.showGameTitle();
        d_mapView.mapEditorPhase();

        String[] l_args;
        d_gameEngine.setPhase(new PreLoad(d_gameEngine));

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
                        d_gameEngine.d_currentPhase.exit();
                        return;
                    case "listmaps":
                        d_gameEngine.d_currentPhase.listMaps();
                        break;
                    case "showcommands":
                        d_gameEngine.d_currentPhase.showCommands();
                        break;
                    case "editmap":
                        d_gameEngine.d_currentPhase.editMap(l_args);
                        break;
                    case "editcontinent":
                        d_gameEngine.d_currentPhase.editContinent(l_args);
                        break;
                    case "editcountry":
                        d_gameEngine.d_currentPhase.editCountry(l_args);
                        break;
                    case "editneighbor":
                        d_gameEngine.d_currentPhase.editNeighbor(l_args);
                        break;
                    case "savemap":
                        d_gameEngine.d_currentPhase.saveMap(l_args);
                        break;
                    case "validatemap":
                        d_gameEngine.d_currentPhase.validateMap(l_args);
                        break;
                    case "showmap":
                        d_gameEngine.d_currentPhase.showMap();
                        break;
                    case "showcontinents":
                        d_gameEngine.d_currentPhase.showContinents();
                        break;
                    case "showcountries":
                        d_gameEngine.d_currentPhase.showCountries();
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

