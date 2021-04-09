package Model;

import Adapter.DominationMapIO;
import Adapter.MapAdapter;
import Adapter.MapContainer;
import Utils.MapUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Maintains the state of the map in MapEditor <br>
 * Implements methods to handle MapEditor commands
 */
public class MapModel implements Serializable {
    private static final long serialversionUID = 129348938L;

    /**
     * Object of the MapUtils
     */
    private final MapUtils d_mapUtils;
    /**
     * Boolean for the validity of the map
     */
    private boolean d_isMapValid;
    /**
     * Boolean for the map file loaded
     */
    private boolean d_mapFileLoaded;
    /**
     * Current file name
     */
    private String d_currentFileName;
    /**
     * LinkedHashMap of the continents
     */
    private LinkedHashMap<String, ContinentModel> d_continents;
    /**
     * LinkedHashMap of the countries
     */
    private LinkedHashMap<String, CountryModel> d_countries;

    /**
     * Object of the MapAdapter
     */
    private final MapAdapter d_mapAdapter;
    /**
     * Initializes countries and continents
     */
    public MapModel() {
        this.d_continents = new LinkedHashMap<>();
        this.d_countries = new LinkedHashMap<>();
        d_mapUtils = new MapUtils();
        this.d_mapFileLoaded = false;
        this.d_isMapValid = false;
        this.d_mapAdapter = new MapAdapter();
    }

    /**
     * Accessor of d_currentFileName
     *
     * @return Name of currently loaded map file
     */
    public String getCurrentFileName() {
        return d_currentFileName;
    }

    /**
     * Accessor for the isMapValid
     *
     * @return True, if currently loaded map is valid; False otherwise
     */
    public boolean isMapValid() {
        return d_isMapValid;
    }

    /**
     * Accessor for the isMapFileLoaded
     *
     * @return True, if the map is loaded; False otherwise
     */
    public boolean isMapFileLoaded() {
        return d_mapFileLoaded;
    }

    /**
     * Accessor for the continents
     *
     * @return Continents of the currently loaded map
     */
    public LinkedHashMap<String, ContinentModel> getContinents() {
        return d_continents;
    }

    /**
     * Mutator for the continents
     *
     * @param p_continents Continents to be loaded in the map
     */
    public void setContinents(LinkedHashMap<String, ContinentModel> p_continents) {
        this.d_continents = p_continents;
    }

    /**
     * Accessor for the countries
     *
     * @return Countries of the currently loaded map
     */
    public LinkedHashMap<String, CountryModel> getCountries() {
        return d_countries;
    }

    /**
     * Mutator for the countries
     *
     * @param p_countries Countries to be loaded in the map
     */
    public void setCountries(LinkedHashMap<String, CountryModel> p_countries) {
        this.d_countries = p_countries;
    }

    /**
     * Handles the editcontinent command <br>
     * Used to add or remove entire continents from the map <br>
     * When a continent is being removed, all countries belonging to the continent are also removed
     *
     * @param p_command Command in the form of "add continentID controlValue remove continentID"
     * @throws Exception If the user tries to remove a non-existing continent
     * @throws Exception If the user tries to use options other than -add or -remove
     */
    public void editContinent(String p_command) throws Exception {

        Scanner l_sc = new Scanner(p_command);

        String l_tempContinentName;
        int l_tempControlValue;
        int l_tempContinentId;

        ContinentModel l_continentModel;

        while (l_sc.hasNext()) {
            String l_option = l_sc.next();

            // handles add option
            if (l_option.equals("add")) {
                l_tempContinentName = l_sc.next();

                // ensures that entered continent name is a string
                if (this.isNameNumber(l_tempContinentName)) {
                    throw new Exception("Continent name cannot be a number!");
                }

                String l_temp = l_sc.next();

                // ensures that entered control value is a number
                if (!(this.isNameNumber(String.valueOf(l_temp)))) {
                    throw new Exception("Control Value must be a number!");
                }

                l_tempControlValue = Integer.parseInt(l_temp);

                l_tempContinentId = this.d_continents.size() + 1;

                String l_defaultColor = "yellow";
                l_continentModel = new ContinentModel(l_tempContinentId, l_tempContinentName, l_tempControlValue, l_defaultColor);
                this.d_continents.put(l_tempContinentName, l_continentModel);
            }

            // handles remove option
            else if (l_option.equals("remove")) {
                l_tempContinentName = l_sc.next();

                // ensures that entered continent name is a string
                if (this.isNameNumber(l_tempContinentName)) {
                    throw new Exception("Continent name cannot be a number!");
                }

                // ensures that entered continent exists
                if (!(this.d_continents.containsKey(l_tempContinentName))) {
                    throw new Exception(l_tempContinentName + " continent does not exists");
                }

                // ensures that removing continent also removes countries that belong to this continent
                this.updateCountries(l_tempContinentName);
                this.d_continents.remove(l_tempContinentName);

                // update all continents' ids
                int[] l_id = {1};

                this.d_continents.replaceAll((p_continentName, p_continentModel) -> {
                    p_continentModel.setId(l_id[0]++);
                    return p_continentModel;
                });
            } else {
                throw new Exception(l_option + " is not supported");
            }
        }
        l_sc.close();
    }

    /**
     * Updates the list of all countries on removal of a continent (which in turn removes all countries belonging to the continent)
     *
     * @param p_continentName Name of the continent being removed
     */
    public void updateCountries(String p_continentName) {
        ArrayList<CountryModel> l_countryModels = new ArrayList<>();

        // get countries to be removed from the continent
        this.d_countries.values().forEach(p_countryModel -> {
            if (p_countryModel.getContinentId().trim().equals(p_continentName.trim())) {
                l_countryModels.add(p_countryModel);
            }
        });

        // update neighbors of countries being removed
        for (CountryModel l_countryModel : l_countryModels) {
            this.d_countries.remove(l_countryModel.getName());
            updateNeighbors(l_countryModel.getName());
        }

        int[] l_id = {1};

        // update all countries' ids
        this.d_countries.replaceAll((p_countryName, p_countryModel) -> {
            p_countryModel.setId(l_id[0]++);
            return p_countryModel;
        });
    }

    /**
     * Handles the editcountry command <br>
     * Used to add or remove countries from the map
     *
     * @param p_command Command in the format "add countryID continentID remove countryID"
     * @throws Exception If the user tries to add a country to a non-existing continent
     * @throws Exception If the user tries to remove a non-existing country
     * @throws Exception If the user tries to use options other than add or remove
     */
    public void editCountry(String p_command) throws Exception {
        Scanner l_sc = new Scanner(p_command);
        String l_tempCountryName;
        String l_tempContinentName;
        int l_tempCountryId;
        CountryModel l_countryModel;

        while (l_sc.hasNext()) {
            String l_option = l_sc.next();

            // handles add option
            if (l_option.equals("add")) {
                l_tempCountryName = l_sc.next();

                // ensures that entered country name is a string
                if (this.isNameNumber(l_tempCountryName)) {
                    throw new Exception("Country name cannot be a number!");
                }

                l_tempContinentName = l_sc.next();

                // ensures that entered continent name is a string
                if (this.isNameNumber(l_tempContinentName)) {
                    throw new Exception("Continent name cannot be a number!");
                }

                // ensures that entered continent name exists
                if (!(this.d_continents.containsKey(l_tempContinentName))) {
                    throw new Exception(l_tempContinentName + " continent does not exists");
                }

                l_tempCountryId = d_countries.size() + 1;
                String l_defaultXCoordinate = "000";
                String l_defaultYCoordinate = "000";

                l_countryModel = new CountryModel(l_tempCountryId, l_tempCountryName, l_tempContinentName, l_defaultXCoordinate, l_defaultYCoordinate);
                this.d_countries.put(l_tempCountryName, l_countryModel);

                // add newly added country to continent's countries list
                this.d_continents.get(l_tempContinentName).addCountry(l_countryModel);
            }

            // handles remove option
            else if (l_option.equals("remove")) {
                l_tempCountryName = l_sc.next();

                // ensures that entered country name is a string
                if (this.isNameNumber(l_tempCountryName)) {
                    throw new Exception("Country name cannot be a number!");
                }

                // ensures that entered country exists
                if (!(this.d_countries.containsKey(l_tempCountryName))) {
                    throw new Exception(l_tempCountryName + " country does not exists");
                }


                // remove country from continent's list as well
                this.d_continents.get(this.d_countries.get(l_tempCountryName).getContinentId()).removeCountry(l_tempCountryName);

                // remove country from map
                this.d_countries.remove(l_tempCountryName);

                // update all continents' ids
                int[] l_id = {1};

                // update all countries' ids
                this.d_countries.replaceAll((p_countryName, p_countryModel) -> {
                    p_countryModel.setId(l_id[0]++);
                    return p_countryModel;
                });

                // ensures that neighbors of applicable countries are updated
                updateNeighbors(l_tempCountryName);
            } else {
                throw new Exception(l_option + " is not supported");
            }
        }
        l_sc.close();
    }

    /**
     * Notifies all the countries to update their border information on removal of a country
     *
     * @param p_countryName Name of the country being removed
     */
    public void updateNeighbors(String p_countryName) {
        ArrayList<CountryModel> l_countryModels = new ArrayList<>();

        // get the countries of which the country being removed is a neighbor
        this.d_countries.values().forEach(p_countryModel -> {
            if (p_countryModel.getNeighbors().containsKey(p_countryName)) {
                l_countryModels.add(p_countryModel);
            }
        });

        for (CountryModel l_countryModel : l_countryModels) {
            this.d_countries.get(l_countryModel.getName()).getNeighbors().remove(p_countryName.trim());
        }
    }

    /**
     * Handles the editneighbor command <br>
     * Used to add or remove neighbors of countries
     *
     * @param p_command Command in the format "add countryID neighborCountryID remove countryID neighborCountryID"
     * @throws Exception If user tries to add or remove neighbors of a non-existing country
     * @throws Exception If user tries to add or remove a non-existing country as a neighbor of an existing country
     * @throws Exception If user tries to use options other than add or remove
     */
    public void editNeighbor(String p_command) throws Exception {
        Scanner l_sc = new Scanner(p_command);
        String l_tempCountryName;
        String l_tempNeighborCountryName;

        while (l_sc.hasNext()) {
            String l_option = l_sc.next().trim();

            // handles add option
            if (l_option.equals("add")) {
                l_tempCountryName = l_sc.next().trim();
                l_tempNeighborCountryName = l_sc.next().trim();

                // ensures that entered country names are strings
                if (this.isNameNumber(l_tempCountryName) || this.isNameNumber(l_tempNeighborCountryName)) {
                    throw new Exception("Country name cannot be a number!");
                }

                // ensure that both entered countries exist
                if (!(this.d_countries.containsKey(l_tempCountryName))) {
                    throw new Exception(l_tempCountryName + " country does not exists");
                } else if (!(this.d_countries.containsKey(l_tempNeighborCountryName))) {
                    throw new Exception(l_tempNeighborCountryName + " country does not exists");
                }

                if (l_tempCountryName.trim().equals(l_tempNeighborCountryName.trim())) {
                    throw new Exception("You cannot add the country as a neighbor of itself!");
                }

                this.d_countries.get(l_tempCountryName).addNeighbor(this.d_countries.get(l_tempNeighborCountryName));
            }
            // handles the remove option
            else if (l_option.equals("remove")) {
                l_tempCountryName = l_sc.next();
                l_tempNeighborCountryName = l_sc.next();

                // ensures that entered country names are strings
                if (this.isNameNumber(l_tempCountryName) || this.isNameNumber(l_tempNeighborCountryName)) {
                    throw new Exception("Country name cannot be a number!");
                }

                // ensure that both entered countries exist
                if (!(this.d_countries.containsKey(l_tempCountryName))) {
                    throw new Exception(l_tempCountryName + " country does not exists");
                } else if (!(this.d_countries.containsKey(l_tempNeighborCountryName))) {
                    throw new Exception(l_tempNeighborCountryName + " country does not exists");
                }
                this.d_countries.get(l_tempCountryName).removeNeighbor(this.d_countries.get(l_tempNeighborCountryName));
            } else {
                throw new Exception(l_option + " is not supported");
            }
        }
        l_sc.close();
    }

    /**
     * Checks whether the string contains only digits
     *
     * @param p_name String to be checked
     * @return True, if the string passed only contains digits; False otherwise
     */
    public boolean isNameNumber(String p_name) {
        return p_name.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    /**
     * Handles the editmap command and loads the map, specified by p_file, to memory
     *
     * @param p_file .map file object from which the map file is being loaded
     * @throws Exception If I/O exception of some sort has occurred
     */
    public void editMap(File p_file) throws Exception {
        this.d_mapFileLoaded = true;
        this.d_currentFileName = p_file.getName();
        loadMap(p_file);
    }

    /**
     * Loads the map, specified by p_file, to memory, if map is valid
     *
     * @param p_file .map file object from which the map file is being loaded
     * @throws IOException If I/O exception of some sort has occurred
     */
    public void loadOnlyValidMap(File p_file) throws Exception {
        this.loadMap(p_file);
        this.validateMap();
        if (!this.isMapValid()) {
            if (!p_file.exists()) {
                p_file.createNewFile();
                this.editMap(new File(MapUtils.getMapsPath() + "empty.map"));
                p_file.delete();
            }
            throw new Exception("This Map is Invalid! Please Load a valid one...");
        }
    }

    /**
     * Loads the map, specified by p_file, to memory
     *
     * @param p_file .map file object from which the map file is being loaded
     * @throws Exception Invalid map type
     */
    public void loadMap(File p_file) throws Exception {
        MapContainer l_newMap;
        String l_key = d_mapUtils.getMapType(p_file);
        switch(l_key) {
            case "Domination":
                DominationMapIO l_dominationMapIO = new DominationMapIO();
                l_newMap = l_dominationMapIO.loadMap(p_file);
                this.d_countries = l_newMap.getCountries();
                this.d_continents = l_newMap.getContinents();
                break;
            case "Conquest":
                l_newMap = d_mapAdapter.loadMap(p_file);
                this.d_countries = l_newMap.getCountries();
                this.d_continents = l_newMap.getContinents();
                break;
            default:
                throw new Exception("Invalid map type");
        }
    }

    /**
     * Handles the validatemap command
     */
    public void validateMap() {
        this.d_isMapValid = validateFullConnectivity(this.d_countries) && validateContinentExistence() && validateNeighborExistence() && validateContinentConnectivity();
    }

    /**
     * Traverse the entire map and visit countries using recursive DFS (Depth first Search) traversal
     *
     * @param p_countryOne Index of country from where the traversal starts
     * @param p_visited    Data structure that keeps track of countries visited during traversal
     * @param p_countries  All countries in the map
     */
    public void traverseMap(CountryModel p_countryOne, LinkedHashMap<String, Boolean> p_visited,
                            LinkedHashMap<String, CountryModel> p_countries) {

        //mark p_countryOne as visited
        p_visited.put(p_countryOne.getName(), true);

        // recursively traverse the map by setting second country as a starting node
        for (CountryModel l_countryTwo : p_countries.values()) {
            if (p_countryOne.getNeighbors().containsKey(l_countryTwo.getName())) {
                if (!p_visited.get(l_countryTwo.getName()))
                    this.traverseMap(l_countryTwo, p_visited, p_countries);
            }
        }
    }

    /**
     * Checks whether the map is fully connected graph or not <br>
     * i.e. every country should be reachable from every other country
     *
     * @param p_countries  All countries in the map
     * @return True, if the map is connected; False otherwise
     */
    public boolean validateFullConnectivity(LinkedHashMap<String, CountryModel> p_countries) {
        LinkedHashMap<String, Boolean> l_visited = new LinkedHashMap<>();

        //for all countries l_countryOne as starting point, check whether all countries are reachable or not
        for (CountryModel l_countryOne : p_countries.values()) {
            for (CountryModel l_countryTwo : p_countries.values()) {

                //initially no countries are visited
                l_visited.put(l_countryTwo.getName(), false);
            }

            traverseMap(l_countryOne, l_visited, p_countries);
            for (CountryModel l_countryTwo : p_countries.values()) {

                //if there exists a l_countryTwo, not visited by map traversal, map is not connected.
                if (!l_visited.get(l_countryTwo.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks whether continent exists for all countries <br>
     * i.e. each country should belong to a continent that exists in the map
     *
     * @return True, if all continents are present in the map; False otherwise
     */
    public boolean validateContinentExistence() {
        AtomicBoolean l_valid = new AtomicBoolean(true);
        this.d_countries.values().forEach(p_countryModel -> {
            if (!(this.d_continents.containsKey(p_countryModel.getContinentId()))) {
                l_valid.set(false);
            }
        });
        return l_valid.get();
    }

    /**
     * Checks whether any country has neighbors that does not exist in the map
     *
     * @return True, if every country's neighbors are present in the map; False otherwise
     */
    public boolean validateNeighborExistence() {
        AtomicBoolean l_valid = new AtomicBoolean(true);
        this.d_countries.values().forEach(p_countryModel -> {
            p_countryModel.getNeighbors().values().forEach(p_countryModel1 -> {
                if (!(this.d_countries.containsKey(p_countryModel1.getName()))) {
                    l_valid.set(false);
                }
            });
        });
        return l_valid.get();
    }

    /**
     * Checks whether all continents are fully connected subgraphs <br>
     * i.e. every country in a continent should be reachable from every other country in that continent
     *
     * @return True if every continent is fully connected; False otherwise
     */
    public boolean validateContinentConnectivity() {
        AtomicBoolean l_valid = new AtomicBoolean(true);
        this.d_continents.values().forEach(p_continentModel -> {
            if (!validateFullConnectivity(p_continentModel.getCountries()))
                l_valid.set(false);
        });
        return l_valid.get();
    }

    /**
     * Handles savemap command
     *
     * @param p_file File object where the current map should be saved
     * @throws IOException If I/O exception of some sort has occurred
     */
    public void saveMap(File p_file, String p_mapType) throws Exception {
        switch(p_mapType.trim()) {
            case "Domination":
                DominationMapIO l_dominationMapIO = new DominationMapIO();
                l_dominationMapIO.saveMap(p_file, this.d_continents, this.d_countries);
                break;
            case "Conquest":
                d_mapAdapter.saveMap(p_file, this.d_continents, this.d_countries);
                break;
            default:
                throw new Exception("Invalid map type");
        }
    }
}
