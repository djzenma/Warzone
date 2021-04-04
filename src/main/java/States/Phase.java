package States;

import Controller.GameEngine;
import EventListener.EventListener;
import EventListener.LogEntryBuffer;
import EventListener.Observable;
import Model.OrderModel;
import Model.Player;

import java.io.IOException;
import java.util.HashMap;

/**
 * Main Phase
 * It extend the observable
 */
public abstract class Phase extends Observable {
    /**
     * Object of the gamengine
     */
    protected GameEngine d_gameEngine;

    /**
     * Constructor
     *
     * @param p_gameEngine Object of the game engine
     */
    public Phase(GameEngine p_gameEngine) {
        this.d_gameEngine = p_gameEngine;
        attach(new EventListener());
    }

    /**
     * Shows the edited map
     */
    // Map editor
    public void showMap() {
        printInvalidCommandMessage();
    }

    /**
     * Shows the available cards
     */
    public void showCards(HashMap<String, Integer> p_cards) {
        printInvalidCommandMessage();
    }

    /**
     * Shows the edited map countries
     */
    public void showCountries() {
        printInvalidCommandMessage();
    }

    /**
     * Shows the edited map continents
     */
    public void showContinents() {
        printInvalidCommandMessage();
    }

    /**
     * Shows the edited map commands
     */
    public void showCommands() {
        printInvalidCommandMessage();
    }

    /**
     * Edits Continent
     *
     * @param l_args Array of the command arguments
     * @throws Exception throws some kind of exception
     */
    public void editContinent(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    /**
     * Edits Country
     *
     * @param l_args Array of the command arguments
     * @throws Exception throws some kind of exception
     */
    public void editCountry(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    /**
     * Edits Neighbor
     *
     * @param l_args Array of the command arguments
     * @throws Exception throws some kind of exception
     */
    public void editNeighbor(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    /**
     * Edits the map
     *
     * @param l_args Array of the command arguments
     * @throws Exception throws some kind of exception
     */
    public void editMap(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    /**
     * Validate Map
     *
     * @param l_args Array of the command arguments
     */
    public void validateMap(String[] l_args) {
        printInvalidCommandMessage();
    }

    /**
     * List all the maps
     *
     * @throws IOException throws some kind of exception
     */
    public void listMaps() throws IOException {
        printInvalidCommandMessage();
    }

    /**
     * Saves the maps
     *
     * @param l_args Array of the command arguments
     * @return true if map is saved; otherwise false
     * @throws Exception throws some kind of exception
     */
    public boolean saveMap(String[] l_args) throws Exception {
        printInvalidCommandMessage();
        return false;
    }

    /**
     * Exits from the MapEditor phase
     */
    public void exit() {
        printInvalidCommandMessage();
    }

    /**
     * Startup Phase
     */
    // Gameplay
    public void startup() {
        printInvalidCommandMessage();
    }

    /**
     * Loops over the issued orders
     *
     * @return true if all the orders are issued; otherwise false
     */
    public boolean issueOrders() {
        printInvalidCommandMessage();
        return false;
    }

    /**
     * Loops over the execute orders
     *
     * @return true if all the orders are executed; otherwise false
     */
    public boolean executeOrders() {
        printInvalidCommandMessage();
        return false;
    }

    /**
     * Loads the map
     *
     * @param l_args Array of the command arguments
     * @throws Exception throws some kind of exception
     */
    public void loadMap(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    /**
     * Adds the game player
     *
     * @param l_args Array of the command arguments
     * @throws Exception throws some kind of exception
     */
    public void gameplayer(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    /**
     * Assigns the countries
     *
     * @return true if all the countries are assigned; otherwise false
     */
    public boolean assignCountries() {
        printInvalidCommandMessage();
        return false;
    }

    /**
     * Assigns the reinforcements
     */
    public void assignReinforcements() {
        printInvalidCommandMessage();
    }

    /**
     * Deploy command
     *
     * @param p_args   Array of the command arguments
     * @param p_player Object of the player
     * @return true if the command is valid; otherwise false
     */
    public OrderModel deploy(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return null;
    }

    /**
     * Advance command
     *
     * @param p_args   Array of the command arguments
     * @param p_player Object of the player
     * @return true if the command is valid; otherwise false
     */
    public OrderModel advance(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return null;
    }

    /**
     * Bomb command
     *
     * @param p_args   Array of the command arguments
     * @param p_player Object of the player
     * @return true if the command is valid; otherwise false
     */
    public OrderModel bomb(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return null;
    }

    /**
     * Blockade command
     *
     * @param p_args   Array of the command arguments
     * @param p_player Object of the player
     * @return true if the command is valid; otherwise false
     */
    public OrderModel blockade(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return null;
    }

    /**
     * Airlift command
     *
     * @param p_args   Array of the command arguments
     * @param p_player Object of the player
     * @return true if the command is valid; otherwise false
     */
    public OrderModel airlift(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return null;
    }

    /**
     * Negotiate command
     *
     * @param p_args   Array of the command arguments
     * @param p_player Object of the player
     * @return true if the command is valid; otherwise false
     */
    public OrderModel negotiate(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return null;
    }

    /**
     * Pass command
     *
     * @param p_player Object of the player
     * @return true if the command is valid; otherwise false
     */
    public OrderModel pass(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return null;
    }

    /**
     * Ends the game
     */
    public void endGame() {
        printInvalidCommandMessage();
    }

    /**
     * To go to the next phase
     */
    public void next() {
        printInvalidCommandMessage();
    }

    /**
     * Notifies if the command is invalid
     */
    public void printInvalidCommandMessage() {
        System.out.println("Invalid command in state "
                + this.getClass().getSimpleName());
    }

    /**
     * Loads the log entry buffer with the current order object
     * Notifies about the state change
     *
     * @param p_command Array of commands
     * @param p_phase   Name of phase
     */
    public void triggerEvent(String[] p_command, String p_phase) {
        LogEntryBuffer l_entryBuffer = new LogEntryBuffer(p_command, p_phase);
        notifyObservers(l_entryBuffer);
    }
}
