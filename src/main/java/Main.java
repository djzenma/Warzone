import Controller.GameEngineController;
import Controller.MapController;
import Model.GameEngineModel;
import Model.MapModel;
import Utils.CommandsParser;
import View.GameEngineView;
import View.MapView;

public class Main {
    public static void main(String[] args){
        CommandsParser.parseJson();

        MapModel d_mapModel = new MapModel();
        MapView d_mapView = new MapView();
        MapController d_mapController = new MapController(d_mapModel, d_mapView);

        d_mapController.run();

        GameEngineController l_gameEngineController = new GameEngineController(new GameEngineModel(), new GameEngineView());
        l_gameEngineController.run();
    }

    public void init() {
        System.out.println("***********************WARZONE***********************" + "\n" + "\n");
        System.out.println("Choose from the menu: ");
        System.out.println("1. Create a new map");
        System.out.println("2. Use an existing map");
        System.out.println("3. Exit");
    }

}
