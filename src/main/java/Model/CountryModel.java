package Model;

import java.util.HashMap;

public class CountryModel {
    private int d_id;
    private String d_name;
    private String d_continentId;
    private HashMap<CountryModel, HashMap<String, CountryModel>> d_neighbors;

    public HashMap<String, CountryModel> getNeighbors() {
        return d_neighbors.get(this);
    }

    public CountryModel(int p_id, String p_name) {
        this.d_id = p_id;
        this.d_name = p_name;
    }

    public int getId() {
        return d_id;
    }

    public void setId(int p_id) {
        this.d_id = p_id;
    }

    public String getName() {
        return d_name;
    }

    public void setName(String p_name) {
        this.d_name = p_name;
    }
}
