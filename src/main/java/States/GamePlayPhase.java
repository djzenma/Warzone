package States;

import Controller.GameEngine;
import Utils.CommandsParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public void saveGame(String[] p_args) {
        try {
            serialize(p_args);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Override
    public GameEngine loadGame(String[] p_args) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);
        try {
            FileInputStream fileIn = new FileInputStream("checkpoint/" + l_args.get("filename").get(0) + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            d_gameEngine = (GameEngine) in.readObject();
            in.close();
            fileIn.close();
            d_gameEngine.d_gamePlayView.loadedCheckpoint();

            return d_gameEngine;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("GameEngine class not found!");
            c.printStackTrace();
            return null;
        }
    }
}
