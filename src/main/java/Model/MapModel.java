package Model;

import Utils.MapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class MapModel {

    private final String MAPS_PATH;
    private final MapUtils d_mapUtils;

    private boolean d_IsMapValid;
    private boolean d_mapFileLoaded;

    private String d_currentFileName;

    private LinkedHashMap<String, ContinentModel> d_continents;
    private LinkedHashMap<String, CountryModel> d_countries;

    /**
     * Instantiates countries and continents
     */
    public MapModel() {
        this.d_continents = new LinkedHashMap<>();
        this.d_countries = new LinkedHashMap<>();
        this.d_mapUtils = new MapUtils();
        this.MAPS_PATH = d_mapUtils.getMapsPath();
        this.d_mapFileLoaded = false;
        this.d_IsMapValid = false;
    }

    public String getCurrentFileName() {
        return d_currentFileName;
    }

    public boolean isMapValid() {
        return d_IsMapValid;
    }

    public boolean isMapFileLoaded() {
        return d_mapFileLoaded;
    }

    public LinkedHashMap<String, ContinentModel> getContinents() {
        return d_continents;
    }

    public LinkedHashMap<String, CountryModel> getCountries() {
        return d_countries;
    }

    /**
     * handles the editcontinent command; used
     * used to add or remove entire continents from the map
     * when a continent is being removed, all countries belonging to the continent are also removed
     *
     * @param p_command command in the form of "add continentID controlValue remove continentID"
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
            if (l_option.equals("add")) {
                l_tempContinentName = l_sc.next();

                if(isNameNumber(l_tempContinentName)){
                    throw new Exception("Continent name cannot be a number!");
                }

                String l_temp = l_sc.next();

                if(!(isNameNumber(String.valueOf(l_temp)))){
                    throw new Exception("Control Value must be a number!");
                }

                l_tempControlValue = Integer.parseInt(l_temp);

                l_tempContinentId = this.d_continents.size() + 1;

                String DEFAULT_COLOR = "yellow";
                l_continentModel = new ContinentModel(l_tempContinentId, l_tempContinentName, l_tempControlValue, DEFAULT_COLOR);
                this.d_continents.put(l_tempContinentName, l_continentModel);
            }
            else if (l_option.equals("remove")) {
                l_tempContinentName = l_sc.next();

                if(isNameNumber(l_tempContinentName)){
                    throw new Exception("Continent name cannot be a number!");
                }

                if (!(this.d_continents.containsKey(l_tempContinentName))) {
                    throw new Exception(l_tempContinentName + " continent does not exists");
                }

                // ensure that removing continent also removes countries that belonged to this continent
                this.updateCountries(l_tempContinentName);
                this.d_continents.remove(l_tempContinentName);

                // Update all continent ids
                final int[] l_id = {1};

                this.d_continents.replaceAll((p_continentName, p_continentModel) -> {
                    p_continentModel.setId(l_id[0]++);
                    return p_continentModel;
                });
            }
            else {
                throw new Exception(l_option + " is not supported");
            }
        }
        l_sc.close();
    }

    /**
     * updates the list of all countries when triggered
     * triggered on removal of a continent (which in turn removes all countries belonging to the continent)
     *
     * @param p_continentName name of the continent being removed
     */
    public void updateCountries(String p_continentName) {
        ArrayList<CountryModel> l_countryModels = new ArrayList<>();

        this.d_countries.values().forEach(new Consumer<CountryModel>() {
            @Override
            public void accept(CountryModel countryModel) {
                if(countryModel.getContinentId().trim().equals(p_continentName.trim())){
                    l_countryModels.add(countryModel);
                }
            }
        });

        for (CountryModel l_countryModel: l_countryModels) {
            this.d_countries.remove(l_countryModel.getName());
            updateNeighbors(l_countryModel.getName());
        }

        final int[] l_id = {1};

        // Update all countries' ids
        this.d_countries.replaceAll((p_countryName, p_countryModel) -> {
            p_countryModel.setId(l_id[0]++);
            return p_countryModel;
        });
    }

    /**
     * handles the editcountry command
     * used to add or remove countries from the map
     * when a country is being removed, neighbor countries' borders are also updated.
     *
     * @param p_command command in the form of "add countryID continentID remove countryID"
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
            if (l_option.equals("add")) {
                l_tempCountryName = l_sc.next();

                if(isNameNumber(l_tempCountryName)){
                    throw new Exception("Country name cannot be a number!");
                }

                l_tempContinentName = l_sc.next();

                if(isNameNumber(l_tempContinentName)){
                    throw new Exception("Continent name cannot be a number!");
                }

                if (!(this.d_continents.containsKey(l_tempContinentName))) {
                    throw new Exception(l_tempContinentName + " continent does not exists");
                }

                l_tempCountryId = d_countries.size() + 1;
                String DEFAULT_Y_COORDINATE = "000";
                String DEFAULT_X_COORDINATE = "000";
                l_countryModel = new CountryModel(l_tempCountryId, l_tempCountryName, l_tempContinentName, DEFAULT_X_COORDINATE, DEFAULT_Y_COORDINATE);
                this.d_countries.put(l_tempCountryName, l_countryModel);

                this.d_continents.get(l_tempContinentName).addCountry(l_countryModel);

            } else if (l_option.equals("remove")) {
                l_tempCountryName = l_sc.next();

                if(isNameNumber(l_tempCountryName)){
                    throw new Exception("Country name cannot be a number!");
                }

                if (!(this.d_countries.containsKey(l_tempCountryName))) {
                    throw new Exception(l_tempCountryName + " country does not exists");
                }

                this.d_countries.remove(l_tempCountryName);

                // Update all continent ids
                final int[] l_id = {1};

                // Update all countries' ids
                this.d_countries.replaceAll((p_countryName, p_countryModel) -> {
                    p_countryModel.setId(l_id[0]++);
                    return p_countryModel;
                });

                updateNeighbors(l_tempCountryName);

            } else {
                throw new Exception(l_option + " is not supported");
            }
        }
        l_sc.close();
    }

    /**
     * updates applicable countries' neighbors when triggered
     * triggered on removal of a country
     * notifies all the countries to update their border information
     *
     * @param p_countryName name of the country being removed
     */
    public void updateNeighbors(String p_countryName) {

        ArrayList<CountryModel> l_countryModels = new ArrayList<>();

        this.d_countries.values().forEach(countryModel -> {
            if(countryModel.getNeighbors().containsKey(p_countryName)){
                l_countryModels.add(countryModel);
            }
        });

        for (CountryModel l_countryModel: l_countryModels) {
            this.d_countries.get(l_countryModel.getName()).getNeighbors().remove(p_countryName.trim());
        }
    }

    /**
     * handles the editneighbor command
     * used to add or remove neighbors of countries
     *
     * @param p_command command in the form of "add countryID neighborCountryID remove countryID neighborCountryID"
     * @throws Exception if user tries to add or remove neighbors of a non-existing country
     * @throws Exception if user tries to add or remove a non-existing country as a neighbor of an existing country
     * @throws Exception if user tries to use options other than -add or -remove
     */
    public void editNeighbor(String p_command) throws Exception {
        Scanner l_sc = new Scanner(p_command);
        String l_tempCountryName;
        String l_tempNeighborCountryName;

        while (l_sc.hasNext()) {
            String l_option = l_sc.next().trim();
            if (l_option.equals("add")) {
                l_tempCountryName = l_sc.next().trim();
                l_tempNeighborCountryName = l_sc.next().trim();

                if(isNameNumber(l_tempCountryName) || isNameNumber(l_tempNeighborCountryName)){
                    throw new Exception("Country name cannot be a number!");
                }

                if (!(this.d_countries.containsKey(l_tempCountryName))) {
                    throw new Exception(l_tempCountryName + " country does not exists");
                } else if (!(this.d_countries.containsKey(l_tempNeighborCountryName))) {
                    throw new Exception(l_tempNeighborCountryName + " country does not exists");
                }

                if(l_tempCountryName.trim().equals(l_tempNeighborCountryName.trim())){
                    throw new Exception("You cannot add the country as a neighbor of itself!");
                }

                this.d_countries.get(l_tempCountryName).addNeighbor(this.d_countries.get(l_tempNeighborCountryName));
            } else if (l_option.equals("remove")) {
                l_tempCountryName = l_sc.next();
                l_tempNeighborCountryName = l_sc.next();

                if(isNameNumber(l_tempCountryName) || isNameNumber(l_tempNeighborCountryName)){
                    throw new Exception("Country name cannot be a number!");
                }

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
     * checks whether the passed string contains only digits
     *
     * @param p_name string to be checked
     * @return true, if the string passed only contains digits; false otherwise
     */
    public boolean isNameNumber(String p_name){
        try{
            Integer.parseInt(p_name);
        }catch (Exception l_e){
            return false;
        }
        return true;
    }

    /**
     * handles the editmap command and loads the map specified by p_file to memory
     *
     * @param p_file .map file object
     * @throws IOException if I/O exception of some sort has occurred.
     */
    public void editMap(File p_file) throws IOException {
        this.d_mapFileLoaded = true;
        this.d_currentFileName = p_file.getName();
        load(p_file);
    }

    /**
     * loads the map specified by p_file to memory if valid
     *
     * @param p_file .map file object from which the map file is being loaded
     * @throws IOException if I/O exception of some sort has occurred.
     */
    public void loadMap(File p_file) throws Exception {
        load(p_file);
        validateMap();
        if (!isMapValid()) {
            if (!p_file.exists()) {
                p_file.createNewFile();
                editMap(new File(d_mapUtils.getMapsPath() + "empty.map"));
                p_file.delete();
            }
            throw new Exception("This Map is Invalid! Please Load a valid one...");
        }
    }

    /**
     * loads the map specified by p_file to memory
     *
     * @param p_file .map file object from which the map file is being loaded
     * @throws IOException if I/O exception of some sort has occurred.
     */
    public void load(File p_file) throws IOException {
        this.d_continents = new LinkedHashMap<>();
        this.d_countries = new LinkedHashMap<>();
        Iterator<String> d_iterator;
        Iterator<String> d_localIterator;
        String l_fileContent;
        String[] l_fileLines;
        String l_tempLine;
        String[] l_tempData;

        l_fileContent = d_mapUtils.readMapFile(p_file);
        l_fileLines = l_fileContent.split("\n");
        d_iterator = Arrays.stream(l_fileLines).iterator();

        while (d_iterator.hasNext()) {
            l_tempLine = d_iterator.next().trim();

            if (l_tempLine.equals("[continents]")) {
                l_tempLine = d_iterator.next().trim();
                while (!(l_tempLine.startsWith("["))) {
                    if (l_tempLine.isEmpty())
                        break;
                    l_tempData = l_tempLine.split(" ");
                    d_continents.put(l_tempData[0].trim(), new ContinentModel(d_continents.size() + 1, l_tempData[0].trim(), Integer.parseInt(l_tempData[1].trim()), l_tempData[2].trim()));
                    l_tempLine = d_iterator.next().trim();
                }
            }

            if (l_tempLine.equals("[countries]")) {
                final String[] l_continentName = new String[1];
                l_tempLine = d_iterator.next().trim();
                while (!(l_tempLine.startsWith("[")) && !(l_tempLine.isEmpty())) {
                    l_tempData = l_tempLine.split(" ");

                    int l_tempContinentId = Integer.parseInt(l_tempData[2].trim());
                    final String[] l_tempContinentName = new String[1];

                    this.d_continents.values().stream().parallel().forEach(new Consumer<ContinentModel>() {
                        @Override
                        public void accept(ContinentModel continentModel) {
                            if (continentModel.getId() == l_tempContinentId) {
                                l_tempContinentName[0] = continentModel.getName();
                            }
                        }
                    });

                    d_countries.put(l_tempData[1].trim(), new CountryModel(Integer.parseInt(l_tempData[0].trim()), l_tempData[1].trim(), l_tempContinentName[0], l_tempData[3], l_tempData[4]));
                    int l_continentId = Integer.parseInt(l_tempData[2].trim());
                    this.d_continents.values().stream().parallel().forEach(new Consumer<ContinentModel>() {
                        @Override
                        public void accept(ContinentModel continentModel) {
                            if (continentModel.getId() == l_continentId)
                                l_continentName[0] = continentModel.getName();
                        }
                    });
                    this.d_continents.get(l_continentName[0]).addCountry(this.d_countries.get(l_tempData[1].trim()));
                    l_tempLine = d_iterator.next().trim();
                }
            }

            if (l_tempLine.equals("[borders]")) {
                l_tempLine = d_iterator.next().trim();
                while (!(l_tempLine.startsWith("[")) && !(l_tempLine.isEmpty()) && d_iterator.hasNext()) {
                    l_tempData = l_tempLine.split(" ");
                    d_localIterator = Arrays.stream(l_tempData).iterator();

                    final String[] l_tempCountryPair = {null , null};

                    int l_firstCountryId = Integer.parseInt(d_localIterator.next().trim());
                    this.d_countries.values().stream().forEach(new Consumer<CountryModel>() {
                        @Override
                        public void accept(CountryModel countryModel) {
                            if (countryModel.getId() == l_firstCountryId)
                                l_tempCountryPair[0] = countryModel.getName();
                        }
                    });
                    while (d_localIterator.hasNext()) {
                        int l_secondCountryId = Integer.parseInt(d_localIterator.next().trim());
                        this.d_countries.values().stream().forEach(new Consumer<CountryModel>() {
                            @Override
                            public void accept(CountryModel countryModel) {
                                if (countryModel.getId() == l_secondCountryId)
                                    l_tempCountryPair[1] = countryModel.getName();
                            }
                        });
                        if(l_tempCountryPair[0]!= null && l_tempCountryPair[1]!=null)
                            this.d_countries.get(l_tempCountryPair[0]).addNeighbor(this.d_countries.get(l_tempCountryPair[1]));

                        /**
                         * TODO:
                         */

                        /*else
                            throw new Exception("The map is invalid!");*/
                    }
                    l_tempLine = d_iterator.next().trim();
                }
            }
        }
    }

    /**
     * A method that handles the validatemap command
     *
     */
    public void validateMap() {
        d_IsMapValid = validateMapTypeOne(this.d_countries) && validateMapTypeTwo() && validateMapTypeThree() && validateMapTypeFour();
    }

    /**
     * A method to traverse the entire map and visit countries using recursive DFS (Depth first Search) traversal.
     *
     * @param p_countryOne Index of country from where the traversal starts
     * @param p_visited    an array that keeps track of countries visited during traversal
     */
    public void traverseMap(CountryModel p_countryOne, LinkedHashMap<String, Boolean> p_visited,
                            LinkedHashMap<String, CountryModel> p_countries) {
        p_visited.put(p_countryOne.getName(), true);    //mark p_countryOne as visited

        for (CountryModel l_countryTwo: p_countries.values()) {
            if (p_countryOne.getNeighbors().containsKey(l_countryTwo.getName())) {
                if (!p_visited.get(l_countryTwo.getName()))
                    traverseMap(l_countryTwo, p_visited, p_countries);
            }
        }
    }

    /**
     * checks whether the map is fully connected
     * i.e. every country should be reachable from every other country.
     *
     * @return false if the map is invalid
     */
    public boolean validateMapTypeOne(LinkedHashMap<String, CountryModel> p_countries) {
        LinkedHashMap<String, Boolean> l_visited = new LinkedHashMap<>();

        //for all countries l_countryOne as starting point, check whether all countries are reachable or not
        for (CountryModel l_countryOne: p_countries.values()) {
            for (CountryModel l_countryTwo: p_countries.values()) {
                //initially no countries are visited
                l_visited.put(l_countryTwo.getName(), false);
            }

            traverseMap(l_countryOne, l_visited, p_countries);
            for (CountryModel l_countryTwo: p_countries.values()) {
                if (!l_visited.get(l_countryTwo.getName())) {
                    //if there exists a l_countryTwo, not visited by map traversal, map is not connected.
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * checks whether continent exists for all countries
     * i.e. each country should belong to a continent that exists in the map.
     *
     * @return true if all continents are present in the map; false otherwise
     */
    public boolean validateMapTypeTwo() {
        AtomicBoolean l_valid = new AtomicBoolean(true);
        this.d_countries.values().forEach(countryModel -> {
            if(!(this.d_continents.containsKey(countryModel.getContinentId()))){
                l_valid.set(false);
            }
        });
        return l_valid.get();
    }

    /**
     * checks whether any country has neighbors that does not exist in the map
     * i.e. every country in a continent should be reachable from every other country in that continent.
     *
     * @return true if every countries' neighbors are present in a map; false otherwise
     */
    public boolean validateMapTypeThree() {
        AtomicBoolean l_valid = new AtomicBoolean(true);
        this.d_countries.values().forEach(countryModel -> {
            countryModel.getNeighbors().values().forEach(countryModel1 ->{
                if(!(this.d_countries.containsKey(countryModel1.getName()))){
                    l_valid.set(false);
                }
            });
        });
        return l_valid.get();
    }

    /**
     * checks whether all continents are connected subgraphs
     * i.e. every country in each continent should belong to a continent that exists in the map.
     * @return true if every continent is valid; false otherwise.
     */
    public boolean validateMapTypeFour() {
        AtomicBoolean l_valid = new AtomicBoolean(true);

        this.d_continents.values().forEach(new Consumer<ContinentModel>() {
            @Override
            public void accept(ContinentModel continentModel) {
                if(!validateMapTypeOne(continentModel.getCountries()))
                    l_valid.set(false);
            }
        });
        return l_valid.get();
    }

    /**
     * handles savemap command
     * @param p_file file object where the map should be saved
     * @throws Exception if the map being saved is an invalid map
     */
    public void saveMap(File p_file) throws Exception {
        validateMap();
        if(!d_IsMapValid){
            throw new Exception("The map is invalid. Cannot be saved!");
        }

        File l_file;
        String l_tempLine = null;
        FileOutputStream l_fos;

        // Writes [files] section
        p_file.createNewFile();
        l_fos = new FileOutputStream(p_file);

        l_tempLine = "[files]\n" +
                "pic " + p_file.getName().split("\\.")[0] + "_pic.jpg\n" +
                "map " + p_file.getName().split("\\.")[0] + "_map.gif\n" +
                "crd " + p_file.getName().split("\\.")[0] + ".cards\n\n";
        l_fos.write(l_tempLine.getBytes(StandardCharsets.UTF_8));


        // Writes [continents] section
        l_fos.write("[continents]\n".getBytes(StandardCharsets.UTF_8));

        d_continents.values().stream().sorted(new Comparator<ContinentModel>() {
            @Override
            public int compare(ContinentModel o1, ContinentModel o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        }).forEach(new Consumer<ContinentModel>() {
            @Override
            public void accept(ContinentModel continentModel) {
                try {
                    l_fos.write((continentModel.getName() + " " +
                            continentModel.getControlValue() + " " +
                            continentModel.getColor() + "\n"
                    ).getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Writes [countries] section
        l_fos.write("\n[countries]\n".getBytes(StandardCharsets.UTF_8));

        d_countries.values().stream().sorted(new Comparator<CountryModel>() {
            @Override
            public int compare(CountryModel o1, CountryModel o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        }).forEach(new Consumer<CountryModel>() {
            @Override
            public void accept(CountryModel countryModel) {
                try {
                    l_fos.write((countryModel.getId() + " " +
                            countryModel.getName() + " " +
                            this.getContinentId(countryModel.getContinentId()) + " " +
                            countryModel.getXCoordinate() + " " +
                            countryModel.getYCoordinate() + "\n"
                    ).getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public String getContinentId(String p_continentName) {
                final int[] l_continentId = new int[1];
                d_continents.values().forEach(new Consumer<ContinentModel>() {
                    @Override
                    public void accept(ContinentModel continentModel) {
                        if (continentModel.getName().trim().equals(p_continentName.trim())) {
                            l_continentId[0] = continentModel.getId();
                            return;
                        }
                    }
                });
                return String.valueOf(l_continentId[0]);
            }
        });

        //Writes [borders] section
        l_fos.write("\n[borders]\n".getBytes(StandardCharsets.UTF_8));
        d_countries.values().stream().forEach(new Consumer<CountryModel>() {
            String l_tempLine = "";

            /**
             * TODO:
             * @param countryModel
             */
            @Override
            public void accept(CountryModel countryModel) {
                l_tempLine += countryModel.getId() + " ";
                countryModel.getNeighbors().values().forEach(inCountryModel -> l_tempLine += inCountryModel.getId() + " ");
                l_tempLine = l_tempLine.trim();
                l_tempLine += "\n";
                try {
                    l_fos.write(l_tempLine.getBytes(StandardCharsets.UTF_8));
                    l_tempLine = "";
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        l_fos.flush();
        l_fos.close();
    }

}
