package Controller;

import Model.MapModel;
import States.MapEditor;
import States.PreLoad;
import Utils.CommandsParser;
import View.MapView;

/**
 * Takes inputs from the MapView and controls the flow of execution of MapEditor
 */
public class MapController extends GameEngineController {

    private final MapModel d_mapModel;
    private final MapView d_mapView;

    /**
     * Initializes MapModel, MapView and MapUtils objects
     *
     * @param p_mapModel MapModel object
     * @param p_mapView  MapView object
     */
    public MapController(MapModel p_mapModel, MapView p_mapView) {
        super();
        this.d_mapModel = p_mapModel;
        this.d_mapView = p_mapView;
        setPhase(new MapEditor(this));
    }

    /**
     * Map-Editor phase of the game. <br>
     * User stays in this phase until it is ready to move to Gameplay start-up phase.
     */
    public void run() {
        d_mapView.showGameTitle();
        d_mapView.mapEditorPhase();

        String[] l_args;
        setPhase(new PreLoad(this));

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
                        d_currentPhase.exit();
                        break;
                    case "listmaps":
                        d_currentPhase.listMaps();
                        break;
                    case "showcommands":
                        d_currentPhase.showCommands();
                        break;
                    case "editmap":
                        d_currentPhase.editMap(l_args);
                        break;
                    case "editcontinent":
                        d_currentPhase.editContinent(l_args);
                        break;
                    case "editcountry":
                        d_currentPhase.editCountry(l_args);
                        break;
                    case "editneighbor":
                        d_currentPhase.editNeighbor(l_args);
                        break;
                    case "savemap":
                        d_currentPhase.saveMap(l_args);
                        break;
                    case "validatemap":
                        d_currentPhase.validateMap(l_args);
                        break;
                    case "showmap":
                        d_currentPhase.showMap();
                        break;
                    case "showcontinents":
                        d_currentPhase.showContinents();
                        break;
                    case "showcountries":
                        d_currentPhase.showCountries();
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

