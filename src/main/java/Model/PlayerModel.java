package Model;

import Model.Orders.DeployModel;
import Utils.CommandsParser;
import View.PlayerView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Issues the order and returns the next order
 * Maintains the HashMap of the countries, the reinforcements it owns for the current turn
 * and the armies that this player owns in each country
 */
public class PlayerModel {
    private final PlayerView d_view;

    private String d_name;
    private int d_reinforcements;

    private final HashMap<String, CountryModel> d_countries;
    private final HashMap<Integer, Integer> d_armies;
    private final Queue<OrderModel> d_orderList;

    /**
     * Initialises the name of the player, reinforcements, orders, countries and PlayerView
     *
     * @param p_name Name of the player
     * @param p_view Object of the PlayerView
     */
    public PlayerModel(String p_name, PlayerView p_view) {
        this.setName(p_name);
        d_reinforcements = 0;
        d_orderList = new ArrayDeque<>();
        d_armies = new HashMap<>();
        d_countries = new HashMap<>();
        this.d_view = p_view;
    }

    /**
     * Adds the country to the list of countries
     *
     * @param p_countryModel Object of the CountryModel
     */
    public void addCountry(CountryModel p_countryModel) {
        this.d_countries.put(p_countryModel.getName(),p_countryModel);
    }

    /**
     * Accessor for the name of the player
     *
     * @return Name of the player
     */
    public String getName() {
        return d_name;
    }

    /**
     * Mutator for the name of the player
     *
     * @param p_name Name of the player
     */
    public void setName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Accessor for the Id of the country
     *
     * @param p_countryId Id of the country
     * @return Country object
     */
    public CountryModel getCountryById(int p_countryId) {
        return d_countries.get(p_countryId);
    }

    /**
     * Gets the list of the countries
     *
     * @return The list of the countries
     */
    public ArrayList<CountryModel> getCountries(){
        return new ArrayList<CountryModel>(this.d_countries.values());
    }

    /**
     * Checks if the player owns a particular country or not
     *
     * @param p_countryName Id of the country
     * @return True if the country belongs the player; otherwise false
     */
    public boolean containsCountry(String p_countryName){
        return d_countries.containsKey(p_countryName);
    }

    /**
     * Adds an order to the list of the orders
     *
     * @param p_order Object of the OrderController
     */
    public void addOrder(OrderModel p_order) {
        this.d_orderList.add(p_order);
    }

    /**
     * Accessor for the number of armies in the specified country
     *
     * @param p_countryId Id of the country
     * @return Number of armies in the specified country
     */
    public int getArmies(int p_countryId) {
        return d_armies.get(p_countryId);
    }

    /**
     * Mutator for the number of armies in the specified country
     *
     * @param p_countryId Id of the country
     * @param p_armies    Number of the armies in the specified country
     */
    public void setArmies(int p_countryId, int p_armies) {
        this.d_armies.put(p_countryId, p_armies);
    }

    /**
     * Accessor for the reinforcements
     *
     * @return Number of reinforcements
     */
    public int getReinforcements() {
        return d_reinforcements;
    }

    /**
     * Mutator for the reinforcements
     *
     * @param p_reinforcements Number of reinforcements
     */
    public void setReinforcements(int p_reinforcements){
        this.d_reinforcements = p_reinforcements;
    }

    /**
     * Player issues an order. In this various conditions are checked and the method is recursively called until user issues a valid order
     *
     * @param p_args Arguments of the command
     * @return False if the order is invalid; Otherwise true
     */
    public boolean issueOrder(String[] p_args) {   //TODO:: 12Refactor: Replace enum
        OrderModel l_order;

        int l_nReinforcements = this.getReinforcements();

        // checks if the player is trying to pass/skip the turn
        if (CommandsParser.isPass(p_args)) {
            if (l_nReinforcements != 0) {
                this.d_view.ReinforcementsRemain(l_nReinforcements);
                return false; // impossible command
            }
            return true;
        }

        // checks if the player issued the deploy order
        if (CommandsParser.isDeploy(p_args)) {
            String l_countryName = p_args[1];

            // validate that the number of reinforcements is a valid number
            if (!p_args[2].matches("[-+]?[0-9]*\\.?[0-9]+")) {
                this.d_view.InvalidNumber();
                return false;
            }
            int l_requestedReinforcements = (int) (Float.parseFloat(p_args[2]));

            // handle if the player deploys in a country that it does not owns
            if (!this.containsCountry(l_countryName)) {
                this.d_view.InvalidCountry();
                return false; // impossible command
            }

            // handle if the player has enough reinforcements to deploy
            else if (l_nReinforcements < l_requestedReinforcements) {
                this.d_view.NotEnoughReinforcements(l_nReinforcements);
                return false; // impossible command
            } else {
                l_order = new DeployModel();
                l_order.setCountryName(p_args[1]);
                l_order.setReinforcements(l_requestedReinforcements);
                this.addOrder(l_order);
                this.setReinforcements(this.getReinforcements() - l_requestedReinforcements);
                return true;
            }
        }
        return false;
    }

    /**
     * Takes the next order from the player's list of orders
     *
     * @return next order from the list of the orders
     */
    public OrderModel nextOrder() {
        OrderModel l_order = this.d_orderList.peek();
        this.d_orderList.poll();
        return l_order;
    }

}
