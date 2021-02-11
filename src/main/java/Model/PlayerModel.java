package Model;

import Controller.OrderController;
import Controller.Orders.DeployController;
import View.PlayerView;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerModel {
    private String d_name;
    private int d_reinforcements;

    private HashMap<Integer, CountryModel> d_countryList;
    private HashMap<Integer, Integer> d_armies;
    private ArrayList<OrderController> d_orderList;

    /**
     * Constructor of thr PlayerModel
     * @param p_name name of the player
     */
    public PlayerModel(String p_name) {
        this.setName(p_name);
        d_reinforcements = 0;
        d_orderList = new ArrayList<OrderController>();
        d_armies = new HashMap<Integer, Integer>();
        d_countryList = new HashMap<Integer, CountryModel>();
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
        this.d_countryList.put(p_countryModel.getId(),p_countryModel);
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
     * @param p_countryId
     * @return
     */
    public CountryModel getCountry(int p_countryId) {
        return d_countryList.get(p_countryId);
    }

    /**
     * Gets the list of the countries
     * @return the list of the countries
     */
    public ArrayList<CountryModel> getCountries(){
        return new ArrayList<CountryModel>(this.d_countryList.values());
    }

    /**
     * Checks if the player owns a particular country or not
     * @param p_countryId Id of the country
     * @return true if the country belongs the player, otherwise false
     */
    public boolean containsCountry(int p_countryId){
        return d_countryList.containsKey(p_countryId);
    }

    /**
     * Adds an order to the list of the orders
     * @param p_order object of the OrderController
     */
    public void addOrder(OrderController p_order){
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
        } while(!OrderController.isValidOrder(l_args[0]));
        return l_args;
    }

    /**
     * Player issues an order. In this various conditions are checked and the method is recursively called until user issues a valid order
     * @return false if the order is invalid, otherwise true
     */
    public boolean issueOrder(){
        OrderController l_order;
        String[] l_args;

        l_args = takeOrder();

        // checks if the player is trying to pass/skip the turn
        if(l_args[0].equals(OrderModel.CMDS.PASS.toString().toLowerCase())) {
            if(this.getReinforcements() != 0) {
                PlayerView.reinforcementsRemain(this.getReinforcements());
                issueOrder();
            }
            return false;
        }

        // checks if the player issued the deploy order
        if(l_args[0].equals(OrderModel.CMDS.DEPLOY.toString().toLowerCase())) {
            int l_countryId = Integer.parseInt(l_args[1]);
            int l_requestedReinforcements = Integer.parseInt(l_args[2]);

            int l_nReinforcements = this.getReinforcements();
            for (OrderController order : this.d_orderList) {
                l_nReinforcements -= order.getReinforcements();
            }

            // handle if the player deploys in a country that it does not owns
            if(!this.containsCountry(l_countryId)) {
                PlayerView.invalidCountry();
                issueOrder(); // retake the order from the beginning
            }


            // handle if the player has enough reinforcements to deploy
            else if(l_nReinforcements < l_requestedReinforcements) {
                PlayerView.notEnoughReinforcements(l_nReinforcements);
                issueOrder(); // retake the order from the beginning
            }

            else {
                l_order = new DeployController();
                l_order.setCountry(Integer.parseInt(l_args[1]));
                l_order.setReinforcements(l_requestedReinforcements);
                this.addOrder(l_order);
                return true;
            }
        }

        return false;
    }
}
