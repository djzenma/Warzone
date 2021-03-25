package Controller;

import States.GamePlayPhase;
import Utils.CommandsParser;

/**
 * Controls the game play phase
 * The table has the following information-
 * <ul>
 * <li> Constructor of the Gameplay </li>
 * <li> Startup phase of the game </li>
 * <li> Loads the Startup Phase and loops over Gameplay phase </li>
 * </ul>
 */
public class GamePlayController {

    /**
     * Object of the gameengine
     */
    private final GameEngine d_gameEngine;

    /**
     * Initialises GamePlayModel, GamePlayView and MapModel
     *
     * @param p_gameEngine Object of the game engine
     */
    public GamePlayController(GameEngine p_gameEngine) {
        super();
        this.d_gameEngine = p_gameEngine;
        d_gameEngine.setPhase(new GamePlayPhase(p_gameEngine));
    }

    /**
     * Startup Phase of the game.
     * User stays in this phase until it is ready to assign countries to the player.
     */
    public void startup() {
        d_gameEngine.d_currentPhase.startup();

        boolean l_end = false;
        boolean l_isMapLoaded = false;
        String[] l_args;

        // stay in the STARTUP phase unless the user assigns countries which moves the game to the next phase
        while (!l_end) {

            // get a valid command from the user
            l_args = this.d_gameEngine.d_gamePlayView.takeCommand();

            // if map is not yet loaded keep asking to load map first
            if (!l_isMapLoaded && !CommandsParser.isLoadMap(l_args)) {
                this.d_gameEngine.d_gamePlayView.mapNotLoaded();
                continue;
            }

            try {
                // if the command entered is loadmap
                if (CommandsParser.isLoadMap(l_args)) {
                    d_gameEngine.d_currentPhase.loadMap(l_args);
                    l_isMapLoaded = true;
                }

                // if the command entered is showmap
                else if (CommandsParser.isShowMap(l_args)) {
                    d_gameEngine.d_currentPhase.showMap();
                }

                // if the command entered is gameplayer
                else if (CommandsParser.isGameplayer(l_args)) {
                    d_gameEngine.d_currentPhase.gameplayer(l_args);
                }

                // if the command entered is assigncountries
                else if (CommandsParser.isAssignCountries(l_args)) {
                    l_end = d_gameEngine.d_currentPhase.assignCountries();
                    if (l_end)
                        d_gameEngine.d_currentPhase.next();
                } else {
                    this.d_gameEngine.d_gamePlayView.isMapEditorCommand();
                }
            } catch (Exception l_e) {
                this.d_gameEngine.d_gamePlayView.exception(l_e.getMessage());
            }
        }
    }

    /**
     * Loads the Startup Phase and loops over Gameplay phase
     */
    public void run() {
        // Startup Phase
        this.d_gameEngine.d_gamePlayView.startupPhase();
        this.startup();

        int l_turnNumber = 1;

        // Gameplay Loop
        this.d_gameEngine.d_gamePlayView.gameplayPhase();
        while (true) {
            this.d_gameEngine.d_gamePlayView.gameplayTurnNumber(l_turnNumber);

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

            l_turnNumber++;
        }
    }
}
