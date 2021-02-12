package Controller;

import Interfaces.Deploy;
import Model.CountryModel;
import Model.OrderModel;

import java.util.HashMap;

/**
 *
 */
public class OrderController implements Deploy {

    private int d_countryId;
    private int d_numReinforcements;

    public OrderController() {
    }

    /**
     * Checks whether the order issued by the player is valid or not
     * @param orderName order issued by the player
     * @return false if the player issues an invalid order, otherwise true
     */
    public static boolean isValidOrder(String orderName) {
        OrderModel.CMDS[] l_ordersEnums = OrderModel.CMDS.values();

        for (OrderModel.CMDS l_cmd : l_ordersEnums) {
            if(orderName.equals(l_cmd.toString()))
                return true;
        }
        return false;
    }



    /**
     * Mutator for the country
     * @param p_countryId Id of the country
     */
    public void setCountry(int p_countryId) {
        this.d_countryId = p_countryId;
    }

    /**
     * Mutator for the reinforcements
     * @param p_numReinforcements number of reinforcements
     */
    public void setReinforcements(int p_numReinforcements) {
        this.d_numReinforcements = p_numReinforcements;
    }

    public int getReinforcements() {
        return d_numReinforcements;
    }

    @Override
    public void execute(HashMap<Integer, CountryModel> p_countries) {
        CountryModel l_country = p_countries.get(this.d_countryId);
        l_country.setArmies(l_country.getArmies() + this.d_numReinforcements);
        p_countries.put(this.d_countryId, l_country);
    }
}
