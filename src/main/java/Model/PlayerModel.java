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

    public PlayerModel(String p_name) {
        this.setName(p_name);
        d_reinforcements = 0;
        d_orderList = new ArrayList<OrderController>();
        d_armies = new HashMap<Integer, Integer>();
        d_countryList = new HashMap<Integer, CountryModel>();
    }

    public void setName(String p_name) {
        this.d_name = p_name;
    }

    public void addCountry(CountryModel p_countryModel) {
        this.d_countryList.put(p_countryModel.getId(),p_countryModel);
    }

    public String getName() {
        return d_name;
    }

    public CountryModel getCountry(int p_countryId) {
        return d_countryList.get(p_countryId);
    }

    public ArrayList<CountryModel> getCountries(){
        return new ArrayList<CountryModel>(this.d_countryList.values());
    }

    public boolean containsCountry(int p_countryId){
        return d_countryList.containsKey(p_countryId);
    }

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

    public int getReinforcements() {
        return d_reinforcements;
    }

    public void setReinforcements(int p_reinforcements){
        this.d_reinforcements = p_reinforcements;
    }

    private String[] takeOrder() {
        String[] l_args;
        do {
            l_args = PlayerView.issueOrderView();
        } while(!OrderController.isValidOrder(l_args[0]));
        return l_args;
    }

    public boolean issueOrder(){
        OrderController l_order;
        String[] l_args;

        l_args = takeOrder();

        if(l_args[0].equals(OrderModel.CMDS.PASS.toString().toLowerCase())) {
            if(this.getReinforcements() != 0) {
                PlayerView.reinforcementsRemain(this.getReinforcements());
                issueOrder();
            }
            return false;
        }

        if(l_args[0].equals(OrderModel.CMDS.DEPLOY.toString().toLowerCase())) {
            int l_countryId = Integer.parseInt(l_args[1]);
            int l_requestedReinforcements = Integer.parseInt(l_args[2]);

            // handle if the player deploys in a country that it owns
            if(!this.containsCountry(l_countryId)) {
                PlayerView.invalidCountry();
                issueOrder(); // retake the order from the beginning
            }

            // handle if the player has enough reinforcements to deploy
            if(this.getReinforcements() < l_requestedReinforcements) {
                PlayerView.notEnoughReinforcements(this.getReinforcements());
                issueOrder(); // retake the order from the beginning
            }

            l_order = new DeployController();
            l_order.setCountry(l_args[1]);
            l_order.setReinforcements(l_requestedReinforcements);

            this.addOrder(l_order);

            return true;
        }

        return false;
    }
}
