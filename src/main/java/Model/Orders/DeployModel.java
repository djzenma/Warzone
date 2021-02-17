package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;

import java.util.HashMap;

public class DeployModel extends OrderModel {

    /**
     * Constructor of the OrderModel
     *
     */
    public DeployModel() {
        super("deploy");
    }

    @Override
    public void execute(HashMap<String, CountryModel> p_countries) {
        CountryModel l_country = p_countries.get(this.getCountryName());
        l_country.setArmies(l_country.getArmies() + this.getReinforcements());
        p_countries.put(this.getCountryName(), l_country);
    }
}
