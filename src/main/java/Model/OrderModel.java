package Model;



public class OrderModel {
    private String d_cmdName;

    /**
     * enum for the commands
     */
    public enum CMDS {
        DEPLOY("deploy"), PASS("pass");

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
}
