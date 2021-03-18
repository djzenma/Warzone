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

    private PlayerModel d_currentPlayer;

    public PlayerModel getCurrentPlayer() {
        return this.d_currentPlayer;
    }

    public void setCurrentPlayer(PlayerModel p_currentPlayer) {
        this.d_currentPlayer = p_currentPlayer;
    }

    /**
     * Constructor of the OrderModel
     *
     * @param p_cmdName name of the command that a player issues
     */
    public OrderModel(String p_cmdName) {
        this.d_cmdName = p_cmdName;
    }

    /**
     * Accessor for the country id
     *
     * @return country name issued in this order
     */
    public String getCountryName() {
        return d_countryName;
    }

    /**
     * Mutator for the country name
     *
     * @param p_countryName name of the country
     */
    public void setCountryName(String p_countryName) {
        this.d_countryName = p_countryName;
    }

    /**
     * Accessor for the number of Reinforcements the player has issued
     *
     * @return Number of Reinforcements the player has issued
     */
    public int getReinforcements() {
        return d_numReinforcements;
    }

    /**
     * Mutator for the reinforcements
     *
     * @param p_numReinforcements number of reinforcements
     */
    public void setReinforcements(int p_numReinforcements) {
        this.d_numReinforcements = p_numReinforcements;
    }

    /**
     * Abstract method to be implemented by every order type
     *
     * @param p_countries HashMap of the countries
     */
    public abstract void execute(HashMap<String, CountryModel> p_countries);



}
