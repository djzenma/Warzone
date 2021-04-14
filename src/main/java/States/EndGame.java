package States;

import Controller.GameEngine;

/**
 * Depicts the end of the game
 * It extends the gameplay phase
 */
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
