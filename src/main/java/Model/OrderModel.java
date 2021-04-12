package Model;

import EventListener.EventListener;
import EventListener.LogEntryBuffer;
import EventListener.Observable;
import Utils.CommandsParser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Parent OrderModel class for all the orders
 * Every new order will implement the abstract execute method
 */
public abstract class OrderModel extends Observable implements Serializable {
    private static final long serialversionUID = 129348938L;
    /**
     * Name of the command
     */
    private final String d_cmdName;
    /**
     * Hashmap of the command arguments
     */
    private final HashMap<String, List<String>> d_args;
    /**
     * String array of teh command arguments
     */
    protected final String[] d_command;
    /**
     * Object of the current player
     */
    protected Player d_currentPlayer;
    /**
     * Name of the country
     */
    private String d_countryName;
    /**
     * Number of reinforcements
     */
    private int d_numReinforcements;

    /**
     * Constructor of the OrderModel
     *
     * @param p_cmdName name of the command that a player issues
     * @param p_player  to initialise current player
     * @param p_args    array of the command arguments
     */
    public OrderModel(String p_cmdName, Player p_player, String[] p_args) {
        this.d_cmdName = p_cmdName;
        this.d_currentPlayer = p_player;
        this.d_args = CommandsParser.getArguments(p_args);
        this.d_command = p_args;
        attach(new EventListener());
        triggerEvent(false);
    }

    /**
     * Mutator for the reinforcements
     *
     * @param p_numReinforcements set number of reinforcements
     */
    public void setReinforcements(int p_numReinforcements) {
        this.d_numReinforcements = p_numReinforcements;
    }

    /**
     * Accessor for current player
     *
     * @return current player
     */
    public Player getCurrentPlayer() {
        return this.d_currentPlayer;
    }

    /**
     * Mutator for the reinforcements
     *
     * @param p_currentPlayer set current player
     */
    public void setCurrentPlayer(Player p_currentPlayer) {
        this.d_currentPlayer = p_currentPlayer;
    }

    /**
     * Accessor for entered command
     *
     * @return the command entered
     */
    public String getCmdName() {
        return this.d_cmdName;
    }


    public String[] getCmd() {
        return this.d_command;
    }

    /**
     * Abstract method to be implemented by every order type
     *
     * @param p_countries HashMap of the countries
     * @return true if it executes; otherwise false
     */
    public abstract boolean execute(HashMap<String, CountryModel> p_countries);

    /**
     * Loads the log entry buffer with the current order object
     * Notifies about the state change
     *
     * @param p_isExec if it is the execute orders phase
     */
    public void triggerEvent(boolean p_isExec) {
        LogEntryBuffer l_entryBuffer = new LogEntryBuffer(p_isExec, d_command, this.d_currentPlayer, "Game Play Phase");
        notifyObservers(l_entryBuffer);
    }
}
