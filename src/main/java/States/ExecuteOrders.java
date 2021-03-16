package States;

import Controller.GameEngineController;

public class ExecuteOrders extends GamePlayPhase {

    public ExecuteOrders(GameEngineController p_gameEngineController) {
        super(p_gameEngineController);
    }

    @Override
    public boolean executeOrders() {
        return this.d_gameEngineController.d_gamePlayModel.executeOrders();
    }

    @Override
    public void next() {
        d_gameEngineController.setPhase(new AssignReinforcements(d_gameEngineController));
    }
}
