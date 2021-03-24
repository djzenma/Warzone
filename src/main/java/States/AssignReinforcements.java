package States;

import Controller.GameEngine;

/**
 * Assign reinforcements after every turn
 * It extends the gameplay phase
 */
public class AssignReinforcements extends GamePlayPhase {

    /**
     * Constructor
     *
     * @param p_gameEngine Object for game engine controller
     */
    public AssignReinforcements(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * Assigns reinforcements to the player
     */
    @Override
    public void assignReinforcements() {
        this.d_gameEngine.d_gamePlayModel.assignReinforcements();

        d_gameEngine.setPhase(new IssueOrder(d_gameEngine));
    }
}
