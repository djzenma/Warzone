package States;

import Controller.GameEngine;

public class ExecuteOrders extends GamePlayPhase {

    public ExecuteOrders(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    @Override
    public boolean executeOrders() {
        return this.d_gameEngine.d_gamePlayModel.executeOrders();
    }

    @Override
    public void next() {
        d_gameEngine.setPhase(new AssignReinforcements(d_gameEngine));
    }

    @Override
    public void endGame() {
        d_gameEngine.setPhase(new EndGame(d_gameEngine));
    }

}
