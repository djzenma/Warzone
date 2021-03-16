package States;

import Controller.GameEngineController;

public class AssignReinforcements extends GamePlayPhase {

    public AssignReinforcements(GameEngineController p_gameEngineController) {
        super(p_gameEngineController);
    }

    @Override
    public void assignReinforcements() {
        this.d_gameEngineController.d_gamePlayModel.assignReinforcements();

        d_gameEngineController.setPhase(new IssueOrder(d_gameEngineController));
    }
}
