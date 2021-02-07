package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerModel {
    private String d_name;
    private int d_reinforcements;
    private HashMap<Integer, CountryModel> d_countryList;
    private HashMap<Integer, Integer> d_armies;
    private ArrayList<OrderModel> d_orderList;

    public PlayerModel(String p_name) {
        this.setName(p_name);
        d_reinforcements = 0;
        d_orderList = new ArrayList<OrderModel>();
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

    public void addOrder(OrderModel p_order){
        this.d_orderList.add(p_order);
    }

    public void removeOrder(OrderModel p_order){
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
}
