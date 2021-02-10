package Controller;

import Model.OrderModel;

import java.util.ArrayList;

public class OrderController {

    private String d_countryId;
    private int d_numReinforcements;

    public static boolean isValidOrder(String orderName) {
        OrderModel.CMDS[] l_ordersEnums = OrderModel.CMDS.values();

        for (OrderModel.CMDS l_cmd : l_ordersEnums) {
            if(orderName.equals(l_cmd.toString()))
                return true;
        }
        return false;
    }

    public void run(){

    }

    public void setCountry(String p_countryId) {
        this.d_countryId = p_countryId;
    }

    public void setReinforcements(int p_numReinforcements) {
        this.d_numReinforcements = p_numReinforcements;
    }
}
