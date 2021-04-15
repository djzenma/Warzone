package Controller;

import Model.GamePlayModel;
import Model.MapModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class TournamentController implements Serializable {
    /**
     * Object of the gameEngine
     */
    private final GameEngine d_gameEngine;
    private final HashMap<String, List<String>> d_args;

    /**
     * Initialises gameEngine
     *
     * @param p_gameEngine Object of the game engine
     * @param p_args Command arguments
     */
    public TournamentController(GameEngine p_gameEngine, HashMap<String, List<String>> p_args) {
        this.d_gameEngine = p_gameEngine;
        this.d_args = p_args;
    }

    /**
     * Loads the tournament mode and loops over the gameplay phase
     *
     * @throws Exception If some sort of exception occurred
     */
    public void run() throws Exception {

        this.d_gameEngine.d_tournamentModel.prepareTournament(this.d_args);

        // Play separate games for each map
        for (String l_map : this.d_gameEngine.d_tournamentModel.getMaps()) {

            // play num of games for selected map
            for (int l_gameNum = 0; l_gameNum < this.d_gameEngine.d_tournamentModel.getNumGames(); l_gameNum++) {

                this.d_gameEngine.d_gamePlayModel.l_turnNumber = 1;

                // Startup Phase
                d_gameEngine.d_gamePlayView.startupPhase();
                this.startup(l_map);

                // Gameplay Loop
                this.d_gameEngine.d_gamePlayView.gameplayPhase();

                while (this.d_gameEngine.d_gamePlayModel.l_turnNumber <= this.d_gameEngine.d_tournamentModel.getMaxTurns()) {
                    this.d_gameEngine.d_gamePlayView.gameplayTurnNumber(d_gameEngine.d_gamePlayModel.l_turnNumber);

                    d_gameEngine.d_currentPhase.assignReinforcements();

                    while (d_gameEngine.d_currentPhase.issueOrders()) ;
                    d_gameEngine.d_currentPhase.next();

                    while (d_gameEngine.d_currentPhase.executeOrders()) ;

                    d_gameEngine.d_currentPhase.next();
                    d_gameEngine.d_currentPhase.issueCards();

                    if (d_gameEngine.d_gamePlayModel.isEndGame()) {
                        d_gameEngine.d_gamePlayView.winnerWinnerChickenDinner(
                                d_gameEngine.d_gamePlayModel.getWinner().getName()
                        );
                        d_gameEngine.d_currentPhase.endGame();
                        break;
                    }

                    d_gameEngine.d_currentPhase.next();

                    d_gameEngine.d_gamePlayModel.l_turnNumber++;
                }

                // Record winner at the end of game or after maximum num of turns
                this.d_gameEngine.d_tournamentModel.recordWinner(l_map, l_gameNum,
                        ((d_gameEngine.d_gamePlayModel.getWinner() != null) ?
                                d_gameEngine.d_gamePlayModel.getWinner().getName() :
                                "Draw"));

                this.d_gameEngine.d_gamePlayModel = new GamePlayModel();
                this.d_gameEngine.d_mapModel = new MapModel();
            }
        }

        this.d_gameEngine.d_gamePlayView.showTournamentResults(
                this.d_gameEngine.d_tournamentModel.getMaps(),
                this.d_gameEngine.d_tournamentModel.getNumGames(),
                this.d_gameEngine.d_tournamentModel.getWinners());
    }

    /**
     * Startup Phase of the tournament mode
     *
     * @param p_mapName name of the map file
     * @throws Exception If some sort of exception occurred
     */
    private void startup(String p_mapName) throws Exception {

        d_gameEngine.d_currentPhase.startup();
        // load map
        d_gameEngine.d_currentPhase.loadMap(new String[]{"loadmap", p_mapName});

        // Add Players to the current Game
        for (String l_strategyName : this.d_gameEngine.d_tournamentModel.getPlayerStrategies()) {
            if (!this.d_gameEngine.d_currentPhase.gameplayer(new String[]{"gameplayer", "add", l_strategyName, l_strategyName}))
                throw new Exception("Invalid Strategy!");
        }

        // Assign countries
        if (d_gameEngine.d_currentPhase.assignCountries())
            d_gameEngine.d_currentPhase.next();
    }
}
