package Controller;

import Model.ContinentModel;
import Model.CountryModel;
import Utils.MapUtils;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Map TODO::
 */
public class MapController {
    private final boolean d_mapFileLoaded;
    private final String d_mapsPath;
    private final HashMap<String, ContinentModel> d_continents;
    private final HashMap<String, CountryModel> d_countries;
    MapUtils d_mapUtils;

    public MapController() {
        this.d_continents = new HashMap<String, ContinentModel>();
        this.d_countries = new HashMap<String, CountryModel>();
        this.d_mapUtils = new MapUtils();
        this.d_mapsPath = d_mapUtils.getMapsPath();
        this.d_mapFileLoaded = false;
    }

    public HashMap<String, ContinentModel> getContinents() {
        return d_continents;
    }

    public HashMap<String, CountryModel> getCountries() {
        return d_countries;
    }

    /**
     * A method that handles the editcontinent command
     * @param p_command command in the form of "-add continentID continentValue -remove continentID"
     * @throws Exception if the user tries to remove a non-existing continent
     * @throws Exception if the user tries to use options other than -add or -remove
     */
    public void editContinent(String p_command) throws Exception {
        Scanner l_sc = new Scanner(p_command);
        String l_tempContinentName;
        int l_tempControlValue;
        int l_tempContinentId;
        ContinentModel l_continentModel;

        while (l_sc.hasNext()) {
            String l_option = l_sc.next();
            if (l_option.equals("-add")) {
                l_tempContinentName = l_sc.next();
                l_tempControlValue = Integer.parseInt(l_sc.next());
                l_tempContinentId = this.d_continents.size() + 1;
                l_continentModel = new ContinentModel(l_tempContinentId, l_tempContinentName, l_tempControlValue);
                this.d_continents.put(l_tempContinentName, l_continentModel);
            } else if (l_option.equals("-remove")) {
                l_tempContinentName = l_sc.next();
                if (!(this.d_continents.containsKey(l_tempContinentName))) {
                    throw new Exception(l_tempContinentName + " continent does not exists");
                }
                this.d_continents.remove(l_tempContinentName);
            } else {
                throw new Exception(l_option + " is not supported");
            }
        }
        l_sc.close();
    }

    /**
     * A method that handles the editcountry command
     * @param p_command command in the form of "-add countryID continentID -remove countryID"
     * @throws Exception if the user tries to add a country to a non-existing continent
     * @throws Exception if the user tries to remove a non-existing country
     * @throws Exception if the user tries to use options other than -add or -remove
     */
    public void editCountry(String p_command) throws Exception {
        Scanner l_sc = new Scanner(p_command);
        String l_tempCountryName;
        String l_tempContinentName;
        int l_tempCountryId;
        CountryModel l_countryModel;

        while (l_sc.hasNext()) {
            String l_option = l_sc.next();
            if (l_option.equals("-add")){
                l_tempCountryName = l_sc.next();
                l_tempContinentName = l_sc.next();
                if(!(this.d_continents.containsKey(l_tempContinentName))){
                    throw new Exception(l_tempContinentName +  " continent does not exists");
                }

                l_tempCountryId = d_countries.size()+1;
                l_countryModel = new CountryModel(l_tempCountryId, l_tempCountryName, l_tempContinentName);
                this.d_countries.put(l_tempCountryName, l_countryModel);

                this.d_continents.get(l_tempContinentName).addCountry(l_countryModel);

            }
            else if(l_option.equals("-remove")){
                l_tempCountryName = l_sc.next();
                if(!(this.d_countries.containsKey(l_tempCountryName))){
                    throw new Exception(l_tempCountryName + " country does not exists");
                }
                this.d_countries.remove(l_tempCountryName);
            }
            else{
                throw new Exception(l_option + " is not supported");
            }
        }
        l_sc.close();
    }

    /**
     * A method that handles the editneighbor command
     *
     * @param p_command command in the form of "-add countryID neighborCountryID -remove countryID neighborCountryID"
     * @throws Exception if the user tries to add or remove neighbors of a non-existing country
     * @throws Exception if the user tries to add or remove a non-existing country as a neighbor of an existing country
     * @throws Exception if the user tries to use options other than -add or -remove
     */
    public void editNeighbor(String p_command) throws Exception {
        Scanner l_sc = new Scanner(p_command);
        String l_tempCountryName;
        String l_tempNeighborCountryName;

        while (l_sc.hasNext()) {
            String l_option = l_sc.next();
            if (l_option.equals("-add")) {
                l_tempCountryName = l_sc.next();
                l_tempNeighborCountryName = l_sc.next();

                if (!(this.d_countries.containsKey(l_tempCountryName))) {
                    throw new Exception(l_tempCountryName + " country does not exists");
                } else if (!(this.d_countries.containsKey(l_tempNeighborCountryName))) {
                    throw new Exception(l_tempNeighborCountryName + " country does not exists");
                }

                this.d_countries.get(l_tempCountryName).addNeighbor(this.d_countries.get(l_tempNeighborCountryName));
            } else if (l_option.equals("-remove")) {
                l_tempCountryName = l_sc.next();
                l_tempNeighborCountryName = l_sc.next();

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
}
