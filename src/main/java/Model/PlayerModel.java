package Model;

import Controller.OrderController;
import Model.Orders.DeployModel;
import Utils.CommandsParser;
import View.PlayerView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class PlayerModel {
    private final PlayerView d_view;

    private String d_name;
    private int d_reinforcements;

    private final HashMap<String, CountryModel> d_countries;
    private final HashMap<Integer, Integer> d_armies;
    private final Queue<OrderModel> d_orderList;

    /**
     * Constructor of thr PlayerModel
     *
     * @param p_name name of the player
     * @param p_view
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
     * Mutator for the name of the player
     * @param p_name name of the player
     */
    public void setName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Adds the country to the list of countries
     * @param p_countryModel object of the CountryModel
     */
    public void addCountry(CountryModel p_countryModel) {
        this.d_countries.put(p_countryModel.getName(),p_countryModel);
    }

    /**
     * Accessor for the name of the player
     * @return name of the player
     */
    public String getName() {
        return d_name;
    }

    /**
     *
     * @param p_countryId id of the country
     * @return country object
     */
    public CountryModel getCountryById(int p_countryId) {
        return d_countries.get(p_countryId);
    }

    /**
     * Gets the list of the countries
     * @return the list of the countries
     */
    public ArrayList<CountryModel> getCountries(){
        return new ArrayList<CountryModel>(this.d_countries.values());
    }

    /**
     * Checks if the player owns a particular country or not
     * @param p_countryName Id of the country
     * @return true if the country belongs the player, otherwise false
     */
    public boolean containsCountry(String p_countryName){
        return d_countries.containsKey(p_countryName);
    }

    /**
     * Adds an order to the list of the orders
     * @param p_order object of the OrderController
     */
    public void addOrder(OrderModel p_order){
        this.d_orderList.add(p_order);
    }

    public void removeOrder(OrderController p_order){
        this.d_orderList.remove(p_order);
    }

    public int getArmies(int p_countryId) {
        return d_armies.get(p_countryId);
    }

    public void setArmies(int p_countryId, int p_armies) {
        this.d_armies.put(p_countryId,p_armies);
    }

    /**
     * Accessor for the reinforcements
     * @return number of reinforcements
     */
    public int getReinforcements() {
        return d_reinforcements;
    }

    /**
     * Mutator for the reinforcements
     * @param p_reinforcements number of reinforcements
     */
    public void setReinforcements(int p_reinforcements){
        this.d_reinforcements = p_reinforcements;
    }

    /**
     * Takes an order from the player
     * @return array of the arguments of the order command
     */
    private String[] takeOrder() {
        String[] l_args;
        do {
            l_args = PlayerView.issueOrderView();
        } while (!CommandsParser.isValidCommand(l_args));
        return l_args;
    }

    /**
     * Player issues an order. In this various conditions are checked and the method is recursively called until user issues a valid order
     * @return false if the order is invalid, otherwise true
     */
    public boolean issueOrder() {
        OrderModel l_order;
        String[] l_args;

        l_args = takeOrder();

        int l_nReinforcements = this.getReinforcements();

        // checks if the player is trying to pass/skip the turn
        if(l_args[0].equals(OrderModel.CMDS.PASS.toString().toLowerCase())) {
            if(l_nReinforcements != 0) {
                this.d_view.ReinforcementsRemain(l_nReinforcements);
                return issueOrder();
            }
        }

        // checks if the player issued the deploy order
        if(l_args[0].equals(OrderModel.CMDS.DEPLOY.toString().toLowerCase())) {
            String l_countryName = l_args[1];
            int l_requestedReinforcements = (int) (Float.parseFloat(l_args[2]));

            // handle if the player deploys in a country that it does not owns
            if(!this.containsCountry(l_countryName)) {
                this.d_view.InvalidCountry();
                return issueOrder(); // retake the order from the beginning
            }

            // handle if the player has enough reinforcements to deploy
            else if(l_nReinforcements < l_requestedReinforcements) {
                this.d_view.NotEnoughReinforcements(l_nReinforcements);
                return issueOrder(); // retake the order from the beginning
            } else {
                l_order = new DeployModel();
                l_order.setCountryName(l_args[1]);
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
