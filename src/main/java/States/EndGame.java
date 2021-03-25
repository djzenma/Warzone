package States;

/**
 * Depicts the end of the game
 * It extends the gameplay phase
 */
import Controller.GameEngine;

public class EndGame extends GamePlayPhase {
    /**
     * Constructor
     *
     * @param p_gameEngine Object for game engine controller
     */
    public EndGame(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }
}
