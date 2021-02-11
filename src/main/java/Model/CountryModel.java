package Model;

import java.util.HashMap;

public class CountryModel {
    private int d_id;
    private String d_name;
    private int d_continentId;
    private HashMap<CountryModel, HashMap<String, CountryModel>> d_neighbors;

    public HashMap<String, CountryModel> getNeighbors() {
        return d_neighbors.get(this);
    }

    /**
     * Constructor of CountryModel
     * @param p_id id of the country
     * @param p_name name of the country
     */
    public CountryModel(int p_id, String p_name) {
        this.d_id = p_id;
        this.d_name = p_name;
    }

    public CountryModel(int p_id, String p_name, int p_continentId) {
        this.d_id = p_id;
        this.d_name = p_name;
        this.d_continentId = p_continentId;
        this.d_neighbors = new HashMap<CountryModel, HashMap<String, CountryModel>>();
    }

    /**
     * A method that adds a neighbor
     *
     * @param p_countryModel model object of the neighbor country
     */
    public void addNeighbor(CountryModel p_countryModel) {
        if (!(this.d_neighbors.containsKey(this))) {
            this.d_neighbors.put(this, new HashMap<String, CountryModel>());
        }
        this.d_neighbors.get(this).put(p_countryModel.d_name, p_countryModel);
    }

    /**
     * A method that removes a neighbor
     *
     * @param p_countryModel model object of the neighbor country
     * @throws Exception if user tries to remove a neighbor when country does not have any neighbors
     * @throws Exception if user tries to remove a neighbor when neighboring relationship does not exist
     */
    public void removeNeighbor(CountryModel p_countryModel) throws Exception {
        if (!(this.d_neighbors.containsKey(this))) {
            throw new Exception(this.d_name + " does not have any neighbors");
        } else {
            if (!(this.d_neighbors.get(this).containsKey(p_countryModel.d_name))) {
                throw new Exception(p_countryModel.d_name + " is not a neighbor of " + this.d_name);
            } else {
                this.d_neighbors.get(this).remove(p_countryModel.d_name);
            }
        }
    }

    /**
     * Accessor for the country Id
     * @return Id of the country
     */
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
