package Model;

public class CountryModel {
    private int d_id;
    private String d_name;
    private int d_continentId;

    public CountryModel(int p_id, String p_name) {
        this.d_id = p_id;
        this.d_name = p_name;
    }



    public int getId() {
        return d_id;
    }

    public void setId(int d_id) {
        this.d_id = d_id;
    }

    public String getName() {
        return d_name;
    }

    public void setName(String d_name) {
        this.d_name = d_name;
    }
}
