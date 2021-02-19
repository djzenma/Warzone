package Model;

import java.util.LinkedHashMap;

public class CountryModel {
    private final int d_id;
    private final String d_name;
    private String d_continentId;
    private int d_armies;
    private String d_xCoordinate;
    private String d_yCoordinate;
    private LinkedHashMap<CountryModel, LinkedHashMap<String, CountryModel>> d_neighbors;

    /**
     * Constructor of CountryModel
     *
     * @param p_id   id of the country
     * @param p_name name of the country
     */
    public CountryModel(int p_id, String p_name) {
        this.d_id = p_id;
        this.d_name = p_name.toLowerCase();
        this.d_armies = 0;
    }

    /**
     * Constructor of CountryModel
     *
     * @param p_id          id of the country
     * @param p_name        name of the country
     * @param p_continentId name of the continent from which the country belongs
     * @param p_xCoordinate x_coordinate of the country
     * @param p_yCoordinate y_coordinate of the country
     */
    public CountryModel(int p_id, String p_name, String p_continentId, String p_xCoordinate, String p_yCoordinate) {
        this.d_id = p_id;
        this.d_name = p_name;
        this.d_continentId = p_continentId;
        this.d_xCoordinate = p_xCoordinate;
        this.d_yCoordinate = p_yCoordinate;
        this.d_armies = 0;
        this.d_neighbors = new LinkedHashMap<CountryModel, LinkedHashMap<String, CountryModel>>();
        this.d_neighbors.put(this, new LinkedHashMap<String, CountryModel>());
    }

    /**
     * A method that adds a neighbor
     *
     * @param p_countryModel model object of the neighbor country
     */
    public void addNeighbor(CountryModel p_countryModel) {
        if (!(this.d_neighbors.containsKey(this))) {
            this.d_neighbors.put(this, new LinkedHashMap<String, CountryModel>());
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

    public String getName() {
        return d_name;
    }

    public int getArmies() {
        return d_armies;
    }

    public void setArmies(int p_armies) {
        this.d_armies = p_armies;
    }

    public String getContinentId() {
        return d_continentId;
    }

    public String getXCoordinate() {
        return d_xCoordinate;
    }

    public String getYCoordinate() {
        return d_yCoordinate;
    }

    public LinkedHashMap<String, CountryModel> getNeighbors() {
        return d_neighbors.get(this);
    }

}
