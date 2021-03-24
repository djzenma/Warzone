package Controller;

import Model.GamePlayModel;
import Model.MapModel;
import States.Phase;
import View.GamePlayView;
import View.MapView;

import java.io.File;
import java.io.IOException;

/**
 * Controller for the game engine
 */
public class GameEngine {
    /**
     * Object of the gameplay view
     */
    public final GamePlayView d_gamePlayView;
    /**
     * Object of the gameplay model
     */
    public final GamePlayModel d_gamePlayModel;
    /**
     * Object of the map model
     */
    public final MapModel d_mapModel;
    /**
     * Object of the map view
     */
    public final MapView d_mapView;

    public Phase d_currentPhase;

    /**
     * Constructor to initialise data members
     */
    public GameEngine() {
        this.d_gamePlayView = new GamePlayView();
        this.d_gamePlayModel = new GamePlayModel();
        this.d_mapModel = new MapModel();
        this.d_mapView = new MapView();
    }

    /**
     * Mutator for current phase
     *
     * @param p_currentPhase Current phase
     */
    public void setPhase(Phase p_currentPhase) {
        this.d_currentPhase = p_currentPhase;
    }

    /**
     * Initial step to run the whole program
     */
    public void run() {
        resetLog();

        MapController l_mapController = new MapController(this);
        l_mapController.run();

        GamePlayController l_gamePlayController = new GamePlayController(this);
        l_gamePlayController.run();
    }

    private void resetLog() {
        try {
            File l_file = new File("log/log.txt");
            l_file.delete();
            l_file.createNewFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
