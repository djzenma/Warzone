package Model;

import java.util.HashMap;

/**
 * Parent OrderModel class for all the orders
 * Every new order will implement the abstract execute method
 */
public abstract class OrderModel {
    private final String d_cmdName;
    private String d_countryName;
    private int d_numReinforcements;

    protected PlayerModel d_currentPlayer;



    /**
     * Constructor of the OrderModel
     *
     * @param p_cmdName     name of the command that a player issues
     * @param p_playerModel to initialise current player
     */
    public OrderModel(String p_cmdName, PlayerModel p_playerModel) {
        this.d_cmdName = p_cmdName;
        this.d_currentPlayer = p_playerModel;
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
    public PlayerModel getCurrentPlayer() {
        return this.d_currentPlayer;
    }

    /**
     * Mutator for the reinforcements
     *
     * @param p_currentPlayer set current player
     */
    public void setCurrentPlayer(PlayerModel p_currentPlayer) {
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

    /**
     * Abstract method to be implemented by every order type
     *
     * @param p_countries HashMap of the countries
     * @return
     */
    public abstract boolean execute(HashMap<String, CountryModel> p_countries);



}
