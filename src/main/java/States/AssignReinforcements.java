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
        if (this.d_gameEngine.d_gamePlayModel.isLoadedGame) {
            this.d_gameEngine.d_gamePlayModel.isLoadedGame = false;
            d_gameEngine.setPhase(new IssueOrder(d_gameEngine));
            return;
        }

        this.d_gameEngine.d_gamePlayModel.assignReinforcements();
        d_gameEngine.setPhase(new IssueOrder(d_gameEngine));
    }
}
