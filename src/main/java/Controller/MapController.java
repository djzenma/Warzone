package Controller;

import java.util.HashMap;
import java.util.Scanner;
import Model.ContinentModel;
import Model.CountryModel;

/**
 * Map TODO::
 */
public class MapController {
    private HashMap<String, ContinentModel> d_continents;
    private HashMap<String, CountryModel> d_countries;

    public MapController() {
        this.d_continents = new HashMap<String, ContinentModel>();
        this.d_countries = new HashMap<String, CountryModel>();
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
        ContinentModel l_continentModel;

        while (l_sc.hasNext()) {
            String l_option = l_sc.next();
            if (l_option.equals("-add")){
                l_tempContinentName = l_sc.next();
                l_tempControlValue = Integer.parseInt(l_sc.next());
                l_continentModel = new ContinentModel(l_tempContinentName, l_tempControlValue);
                this.d_continents.put(l_tempContinentName, l_continentModel);
            }
            else if(l_option.equals("-remove")){
                l_tempContinentName = l_sc.next();
                if(!(this.d_continents.containsKey(l_tempContinentName))){
                    throw new Exception(l_tempContinentName + " continent does not exists");
                }
                this.d_continents.remove(l_tempContinentName);
            }
            else{
                throw new Exception(l_option + " is not supported");
            }
        }
        l_sc.close();
    }
}
