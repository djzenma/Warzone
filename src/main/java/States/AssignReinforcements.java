package States;

import Controller.GameEngineController;

/**
 * Assign reinforcements after every turn
 * It extends the gameplay phase
 */
public class AssignReinforcements extends GamePlayPhase {

    /**
     * Constructor
     *
     * @param p_gameEngineController Object for game engine controller
     */
    public AssignReinforcements(GameEngineController p_gameEngineController) {
        super(p_gameEngineController);
    }

    /**
     * Assigns reinforcements to the player
     */
    @Override
    public void assignReinforcements() {
        this.d_gameEngineController.d_gamePlayModel.assignReinforcements();

        d_gameEngineController.setPhase(new IssueOrder(d_gameEngineController));
    }
}
