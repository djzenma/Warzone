import Controller.GameEngineController;
import Controller.MapController;
import Model.GameEngineModel;
import Model.MapModel;
import Utils.CommandsParser;
import View.GameEngineView;
import View.MapView;

public class Main {
    public static void main(String[] args) {
        CommandsParser.parseJson();

        MapModel l_mapModel = new MapModel();
        MapView l_mapView = new MapView();
        MapController l_mapController = new MapController(l_mapModel, l_mapView);

        l_mapController.run();

        GameEngineController l_gameEngineController = new GameEngineController(new GameEngineModel(), new GameEngineView());
        l_gameEngineController.run();
    }
}
