package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;

import java.util.HashMap;

/**
 * Model for Deploy Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
public class DeployModel extends OrderModel {

    /**
     * Constructor for the DeployModel
     */
    public DeployModel() {
        super("deploy");
    }

    /**
     * Executes the deploy command by placing the armies in the specified country
     *
     * @param p_countries HashMap of the countries
     */
    @Override
    public void execute(HashMap<String, CountryModel> p_countries) {
        CountryModel l_country = p_countries.get(this.getCountryName());
        l_country.setArmies(l_country.getArmies() + this.getReinforcements());
        p_countries.put(this.getCountryName(), l_country);
    }
}
