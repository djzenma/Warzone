package States;

import Controller.GameEngine;

import java.io.IOException;
import java.util.HashMap;

/**
 * Game Play Phase
 * It extends the main phase
 */
public class GamePlayPhase extends Phase {
    /**
     * Constructor
     *
     * @param p_gameEngine Object of the Gameengine
     */
    public GamePlayPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * Moves to the startup phase
     */
    @Override
    public void startup() {
        d_gameEngine.setPhase(new Startup(d_gameEngine));
    }

    /**
     * Assigns the reinforcements
     */
    @Override
    public void assignReinforcements() {
        d_gameEngine.setPhase(new AssignReinforcements(d_gameEngine));
    }

    /**
     * Issues the orders added by the players
     *
     * @return true if there are orders to be issued; otherwise false
     */
    @Override
    public boolean issueOrders() {
        d_gameEngine.setPhase(new IssueOrder(d_gameEngine));
        return false;
    }

    /**
     * Execute the orders issued by the players
     *
     * @return true if there are orders to execute; otherwise false
     */
    @Override
    public boolean executeOrders() {
        d_gameEngine.setPhase(new ExecuteOrders(d_gameEngine));
        return false;
    }

    /**
     * Shows the map in the gameplay phase
     */
    @Override
    public void showMap() {
        this.d_gameEngine.d_gamePlayView.showMap(this.d_gameEngine.d_mapModel.getContinents(), this.d_gameEngine.d_mapModel.getCountries());
    }

    /**
     * Shows the cards in the gameplay phase
     */
    @Override
    public void showCards(HashMap<String, Integer> p_cards) {
        this.d_gameEngine.d_gamePlayView.showCards(p_cards);
    }

    /**
     * Saves the game
     *
     * @param p_args command arguments
     */
    @Override
    public void saveGame(String[] p_args) {
        try {
            serialize(p_args);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
