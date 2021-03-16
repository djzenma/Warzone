import Controller.GamePlayController;
import Controller.MapController;
import Model.MapModel;
import Utils.CommandsParser;
import View.MapView;

/**
 * Driver class for the Warzone
 */
public class Main {
    /**
     * Entry function
     *
     * @param args Not used
     */
    public static void main(String[] args) {
        CommandsParser.parseJson();

        MapModel l_mapModel = new MapModel();
        MapView l_mapView = new MapView();
        MapController l_mapController = new MapController(l_mapModel, l_mapView);
        l_mapController.run();

        GamePlayController l_gamePlayController = new GamePlayController();
        l_gamePlayController.run();
    }
}
