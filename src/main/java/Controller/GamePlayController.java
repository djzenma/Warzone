package Controller;

import States.GamePlayPhase;
import Utils.CommandsParser;

/**
 * Controls the game play phase
 * The table has the following information-
 * <ul> TODO: Is any info required?
 * <li>  </li>
 * </ul>
 */
public class GamePlayController {

    GameEngineController d_gameEngineController;

    /**
     * Initialises GamePlayModel, GamePlayView and MapModel
     */
    public GamePlayController(GameEngineController p_gameEngineController) {
        super();
        this.d_gameEngineController = p_gameEngineController;
        d_gameEngineController.setPhase(new GamePlayPhase(p_gameEngineController));
    }


    /**
     * Startup Phase of the game.
     * User stays in this phase until it is ready to assign countries to the player.
     */
    public void startup() {
        d_gameEngineController.d_currentPhase.startup();

        boolean l_end = false;
        boolean l_isMapLoaded = false;
        String[] l_args;

        // stay in the STARTUP phase unless the user assigns countries which moves the game to the next phase
        while (!l_end) {

            // get a valid command from the user
            l_args = this.d_gameEngineController.d_gamePlayView.takeCommand();

            // if map is not yet loaded keep asking to load map first
            if (!l_isMapLoaded && !CommandsParser.isLoadMap(l_args)) {
                this.d_gameEngineController.d_gamePlayView.mapNotLoaded();
                continue;
            }

            try {
                // if the command entered is loadmap
                if (CommandsParser.isLoadMap(l_args)) {
                    d_gameEngineController.d_currentPhase.loadMap(l_args);
                    l_isMapLoaded = true;
                }

                // if the command entered is showmap
                else if (CommandsParser.isShowMap(l_args)) {
                    d_gameEngineController.d_currentPhase.showMap();
                }

                // if the command entered is gameplayer
                else if (CommandsParser.isGameplayer(l_args)) {
                    d_gameEngineController.d_currentPhase.gameplayer(l_args);
                }

                // if the command entered is assigncountries
                else if (CommandsParser.isAssignCountries(l_args)) {
                    l_end = d_gameEngineController.d_currentPhase.assignCountries();
                    if (l_end)
                        d_gameEngineController.d_currentPhase.next();
                } else {
                    this.d_gameEngineController.d_gamePlayView.isMapEditorCommand();
                }
            } catch (Exception l_e) {
                this.d_gameEngineController.d_gamePlayView.exception(l_e.getMessage());
            }
        }
    }

    /**
     * Loads the Startup Phase and loops over Gameplay phase
     */
    public void run() {
        // Startup Phase
        this.d_gameEngineController.d_gamePlayView.startupPhase();
        this.startup();

        int l_turnNumber = 1;

        // Gameplay Loop
        this.d_gameEngineController.d_gamePlayView.gameplayPhase();
        while (true) {
            this.d_gameEngineController.d_gamePlayView.gameplayTurnNumber(l_turnNumber);

            d_gameEngineController.d_currentPhase.assignReinforcements();

            while (d_gameEngineController.d_currentPhase.issueOrders()) ;
            d_gameEngineController.d_currentPhase.next();

            while (d_gameEngineController.d_currentPhase.executeOrders()) ;
            d_gameEngineController.d_currentPhase.next();

            l_turnNumber++;
        }
    }
}
