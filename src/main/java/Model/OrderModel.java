package Model;

public class OrderModel {
    private String d_cmdName;

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
