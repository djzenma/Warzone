package Controller;

import Model.Player;
import Strategy.*;
import View.PlayerView;

import java.util.HashMap;
import java.util.List;

public class TournamentController {
    /**
     * Object of the gameEngine
     */
    private final GameEngine d_gameEngine;
    private final HashMap<String, List<String>> d_args;

    /**
     * Initialises gameEngine
     *
     * @param p_gameEngine Object of the game engine
     */
    public TournamentController(GameEngine p_gameEngine, HashMap<String, List<String>> p_args) {
        this.d_gameEngine = p_gameEngine;
        this.d_args = p_args;
    }

    public void run() throws Exception {

        this.d_gameEngine.d_tournamentModel.prepareTournament(this.d_args);

        // Play separate games for each map
        for (String l_map : this.d_gameEngine.d_tournamentModel.getMaps()) {

            // play num of games for selected map
            for (int l_gameNum = 0; l_gameNum < this.d_gameEngine.d_tournamentModel.getNumGames(); l_gameNum++) {

                this.d_gameEngine.d_gamePlayModel.l_turnNumber = 0;

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
            }
        }

        this.d_gameEngine.d_gamePlayView.showTournamentResults(
                this.d_gameEngine.d_tournamentModel.getMaps(),
                this.d_gameEngine.d_tournamentModel.getNumGames(),
                this.d_gameEngine.d_tournamentModel.getWinners());
    }

    private void startup(String p_mapName) throws Exception {
        d_gameEngine.d_currentPhase.startup();
        // load map
        d_gameEngine.d_currentPhase.loadMap(new String[]{"loadmap", p_mapName});

        // Add Players to the current Game
        for (String l_strategyName : this.d_gameEngine.d_tournamentModel.getPlayerStrategies()) {

            Player l_player = new Player(l_strategyName, new PlayerView());
            Strategy l_strategy = getStrategy(l_strategyName, l_player);
            // invalid strategy
            if (l_strategy == null)
                this.d_gameEngine.d_gamePlayView.invalidStrategy(l_strategyName);
            else {
                l_player.setStrategy(l_strategy);
                this.d_gameEngine.d_gamePlayModel.addPlayer(l_player);
            }
        }

        // Assign countries
        if (d_gameEngine.d_currentPhase.assignCountries())
            d_gameEngine.d_currentPhase.next();
    }

    /**
     * Convert entered string strategy to its corresponding strategy object
     *
     * @param p_strategyName the strategy name
     * @param p_player       the player that will be assigned the strategy
     * @return
     */
    private Strategy getStrategy(String p_strategyName, Player p_player) throws Exception {
        switch (p_strategyName.toLowerCase()) {
            case "human":
                return new HumanStrategy(p_player,
                        this.d_gameEngine.d_gamePlayModel.getCountries(),
                        this.d_gameEngine.d_gamePlayModel.getPlayers());
            case "random":
                return new RandomStrategy(p_player,
                        this.d_gameEngine.d_gamePlayModel.getCountries(),
                        this.d_gameEngine.d_gamePlayModel.getPlayers());
            case "benevolent":
                return new BenevolentStrategy(p_player,
                        this.d_gameEngine.d_gamePlayModel.getCountries(),
                        this.d_gameEngine.d_gamePlayModel.getPlayers());
            case "aggressive":
                return new AggressiveStrategy(p_player,
                        this.d_gameEngine.d_gamePlayModel.getCountries(),
                        this.d_gameEngine.d_gamePlayModel.getPlayers());
            default:
                throw new Exception("Invalid Strategy Entered!");
        }
    }
}
