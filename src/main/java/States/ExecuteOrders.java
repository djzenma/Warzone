package States;

import Controller.GameEngine;

/**
 * Executes the orders issued by the players
 * It extends the gameplay phase
 */
public class ExecuteOrders extends GamePlayPhase {
    /**
     * Constructor
     *
     * @param p_gameEngine Object for game engine controller
     */
    public ExecuteOrders(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * Execute the orders issued by the players
     *
     * @return true if there are orders to execute; otherwise false
     */
    @Override
    public boolean executeOrders() {
        return this.d_gameEngine.d_gamePlayModel.executeOrders();
    }

    /**
     * Moves to the next phase
     */
    @Override
    public void next() {
        d_gameEngine.setPhase(new IssueCards(d_gameEngine));
    }

    /**
     * Depicts the end of the game
     */
    @Override
    public void endGame() {
        d_gameEngine.setPhase(new EndGame(d_gameEngine));
    }

}
