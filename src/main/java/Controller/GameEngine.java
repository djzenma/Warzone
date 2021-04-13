package Controller;

import Model.GamePlayModel;
import Model.MapModel;
import Model.TournamentModel;
import States.Phase;
import Utils.CommandsParser;
import View.GamePlayView;
import View.MapView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Controller for the game engine
 */
public class GameEngine implements Serializable {
    private static final long serialversionUID = 129348938L;

    /**
     * Object of the gameplay view
     */
    public final GamePlayView d_gamePlayView;
    /**
     * Object of the gameplay model
     */
    public GamePlayModel d_gamePlayModel;
    /**
     * Object of the tournament model
     */
    public final TournamentModel d_tournamentModel;
    /**
     * Object of the map model
     */
    public MapModel d_mapModel;
    /**
     * Object of the map view
     */
    public final MapView d_mapView;
    /**
     * Object of the phase
     */
    public Phase d_currentPhase;

    /**
     * Constructor to initialise data members
     */
    public GameEngine() {
        this.d_gamePlayView = new GamePlayView();
        this.d_gamePlayModel = new GamePlayModel();
        this.d_mapModel = new MapModel();
        this.d_mapView = new MapView();
        this.d_tournamentModel = new TournamentModel();
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

        while (true) {
            this.d_gamePlayView.modeSelection();
            // Ask for mode specific commands
            String[] l_args = this.d_gamePlayView.takeCommand();

            try {
                switch (l_args[0]) {
                    case "singlegame":
                        GamePlayController l_gamePlayController = new GamePlayController(this);
                        l_gamePlayController.run();
                        return;
                    case "tournament":
                        TournamentController l_tournamentController = new TournamentController(this, CommandsParser.getArguments(l_args));
                        l_tournamentController.run();
                        return;
                    default:
                        this.d_gamePlayView.invalidMode();
                }
            } catch (Exception l_e) {
                l_e.printStackTrace();
                this.d_gamePlayView.exception(l_e.getMessage());
            }
        }
    }

    /**
     * Resets the log file at the starting
     */
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
