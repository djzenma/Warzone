package Model;


import java.util.HashMap;

public abstract class OrderModel {
    private String d_cmdName;
    private String d_countryName;
    private int d_numReinforcements;

    /**
     * enum for the commands
     */
    public enum CMDS {
        DEPLOY("deploy"),
        PASS("pass"),
        GAME_PLAYER("gameplayer"),
        ASSIGN_COUNTRIES("assigncountries");

        private String d_cmdName;
        private CMDS(String p_cmdName) {
            this.d_cmdName = p_cmdName;
        }

        @Override
        public String toString(){
            return d_cmdName;
        }
    }

    /**
     * Constructor of the OrderModel
     * @param d_cmdName name of the command that a player issues
     */
    public OrderModel(String d_cmdName) {
        this.d_cmdName = d_cmdName;
    }

    public String getCmdName() {
        return d_cmdName;
    }

    public void setCmdName(String d_cmdName) {
        this.d_cmdName = d_cmdName;
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
     * Mutator for the country name
     * @param p_countryName name of the country
     */
    public void setCountryName(String p_countryName) {
        this.d_countryName = p_countryName;
    }

    /**
     * Accessor for the country id
     * @return country name issued in this order
     */
    public String getCountryName() {
        return d_countryName;
    }


    /**
     * Mutator for the reinforcements
     * @param p_numReinforcements number of reinforcements
     */
    public void setReinforcements(int p_numReinforcements) {
        this.d_numReinforcements = p_numReinforcements;
    }

    /**
     * Getter for the number of Reinforcements this player has issued
     * */
    public int getReinforcements() {
        return d_numReinforcements;
    }

    /**
     * Abstract method to be implemented by every order type
     * */
    public abstract void execute(HashMap<String, CountryModel> p_countries);
}
