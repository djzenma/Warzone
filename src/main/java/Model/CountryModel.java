package Model;

import java.util.LinkedHashMap;

/**
 * Maintains the state of a country
 */
public class CountryModel {
    private int d_id;
    private final String d_name;
    private String d_continentId;
    private int d_armies;
    private String d_xCoordinate;
    private String d_yCoordinate;
    private PlayerModel d_owner;
    private LinkedHashMap<CountryModel, LinkedHashMap<String, CountryModel>> d_neighbors;

    /**
     * Initializes country parameters such as id and name
     *
     * @param p_id   Id of the country
     * @param p_name Name of the country
     */
    public CountryModel(int p_id, String p_name) {
        this.d_id = p_id;
        this.d_name = p_name.toLowerCase();
        this.d_armies = 0;
    }

    /**
     * Accessor for the owner name of the country
     *
     * @return Owner name of the country
     */
    public String getOwnerName() {
        return this.d_owner.getName();
    }

    /**
     * Accessor for the owner of the country
     *
     * @return Owner of the country
     */
    public PlayerModel getOwner() {
        return this.d_owner;
    }

    /**
     * Mutator for the owner of the country
     *
     * @param p_owner Owner of the country
     */
    public void setOwner(PlayerModel p_owner) {
        this.d_owner = p_owner;
    }

    /**
     * Initializes country parameters such as id, name, continent id, x-coordinate, y-coordinate
     *
     * @param p_id          Id of the country
     * @param p_name        Name of the country
     * @param p_continentId Name of the continent from which the country belongs
     * @param p_xCoordinate X_coordinate of the country
     * @param p_yCoordinate Y_coordinate of the country
     */
    public CountryModel(int p_id, String p_name, String p_continentId, String p_xCoordinate, String p_yCoordinate) {
        this.d_id = p_id;
        this.d_name = p_name;
        this.d_continentId = p_continentId;
        this.d_xCoordinate = p_xCoordinate;
        this.d_yCoordinate = p_yCoordinate;
        this.d_armies = 0;
        this.d_neighbors = new LinkedHashMap<>();
        this.d_neighbors.put(this, new LinkedHashMap<>());
    }

    /**
     * Adds a neighbor country
     *
     * @param p_countryModel Neighbor country
     */
    public void addNeighbor(CountryModel p_countryModel) {
        if (!(this.d_neighbors.containsKey(this))) {
            this.d_neighbors.put(this, new LinkedHashMap<>());
        }
        this.d_neighbors.get(this).put(p_countryModel.d_name, p_countryModel);
    }

    /**
     * Removes a neighbor country
     *
     * @param p_countryModel Neighbor country
     * @throws Exception If user tries to remove a neighbor when country does not have any neighbors
     * @throws Exception If user tries to remove a neighbor when neighboring relationship does not exist
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
     * Accessor for the country id
     *
     * @return Id of the country
     */
    public int getId() {
        return d_id;
    }

    /**
     * Mutator for the country id
     *
     * @param p_id  Id of the country
     */
    public void setId(int p_id) {
        this.d_id = p_id;
    }

    /**
     * Accessor for the country name
     *
     * @return Name of the country
     */
    public String getName() {
        return d_name;
    }

    /**
     * Accessor for the armies on the country
     *
     * @return Armies on the country
     */
    public int getArmies() {
        return d_armies;
    }

    /**
     * Mutator for the armies on the country
     *
     * @param p_armies Armies on the country
     */
    public void setArmies(int p_armies) {
        this.d_armies = p_armies;
    }

    /**
     * Accessor for the continent id
     *
     * @return Id of the continent
     */
    public String getContinentId() {
        return d_continentId;
    }

    /**
     * Accessor for the x-coordinate
     *
     * @return X-coordinate of the country
     */
    public String getXCoordinate() {
        return d_xCoordinate;
    }

    /**
     * Accessor for the y-coordinate
     *
     * @return Y-coordinate of the country
     */
    public String getYCoordinate() {
        return d_yCoordinate;
    }

    /**
     * Accessor for the neighbors of the country
     *
     * @return Neighbors of the country
     */
    public LinkedHashMap<String, CountryModel> getNeighbors() {
        return this.d_neighbors.get(this);
    }
}
