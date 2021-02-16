package Controller;

import Model.ContinentModel;
import Model.CountryModel;
import Utils.MapUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Map TODO::
 */
public class MapController {
    private final boolean d_mapFileLoaded;
    private final String d_mapsPath;
    private final HashMap<String, ContinentModel> d_continents;
    private final HashMap<String, CountryModel> d_countries;
    int[][] d_MapConnectivityGrid;
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
     *
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
     *
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
            if (l_option.equals("-add")) {
                l_tempCountryName = l_sc.next();
                l_tempContinentName = l_sc.next();
                if (!(this.d_continents.containsKey(l_tempContinentName))) {
                    throw new Exception(l_tempContinentName + " continent does not exists");
                }

                l_tempCountryId = d_countries.size() + 1;
                l_countryModel = new CountryModel(l_tempCountryId, l_tempCountryName, l_tempContinentName);
                this.d_countries.put(l_tempCountryName, l_countryModel);

                this.d_continents.get(l_tempContinentName).addCountry(l_countryModel);

            } else if (l_option.equals("-remove")) {
                l_tempCountryName = l_sc.next();
                if (!(this.d_countries.containsKey(l_tempCountryName))) {
                    throw new Exception(l_tempCountryName + " country does not exists");
                }
                this.d_countries.remove(l_tempCountryName);
            } else {
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

    /**
     * A method that handles the editmap command
     *
     * @param p_filename .map file's name
     * @throws IOException If an I/O error occurred
     */
    public void editMap(String p_filename) throws IOException {
        Iterator<String> d_iterator;
        Iterator<String> d_localIterator;
        File l_file;
        String l_fileContent;
        String[] l_fileLines;
        String l_tempLine;
        String[] l_tempData;

        l_file = new File(d_mapsPath + p_filename);

        if (!(l_file.exists())) {
            l_file.createNewFile();
            System.out.println(p_filename + " file does not exists!\nCreated new " + p_filename + " file.");
            return;
        }

        l_fileContent = d_mapUtils.readMapFile(l_file);
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
                    d_continents.put(l_tempData[0].trim(), new ContinentModel(d_continents.size() + 1, l_tempData[0].trim(), Integer.parseInt(l_tempData[1].trim())));
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

                    d_countries.put(l_tempData[1].trim(), new CountryModel(Integer.parseInt(l_tempData[0].trim()), l_tempData[1].trim(), l_tempContinentName[0]));
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

                    final String[] l_tempCountryPair = new String[2];
                    int l_firstCountryId = Integer.parseInt(d_localIterator.next().trim());
                    this.d_countries.values().stream().parallel().forEach(new Consumer<CountryModel>() {
                        @Override
                        public void accept(CountryModel countryModel) {
                            if (countryModel.getId() == l_firstCountryId)
                                l_tempCountryPair[0] = countryModel.getName();
                        }
                    });
                    while (d_localIterator.hasNext()) {
                        int l_secondCountryId = Integer.parseInt(d_localIterator.next().trim());
                        this.d_countries.values().stream().parallel().forEach(new Consumer<CountryModel>() {
                            @Override
                            public void accept(CountryModel countryModel) {
                                if (countryModel.getId() == l_secondCountryId)
                                    l_tempCountryPair[1] = countryModel.getName();
                            }
                        });
                        this.d_countries.get(l_tempCountryPair[0]).addNeighbor(this.d_countries.get(l_tempCountryPair[1]));
                    }
                    l_tempLine = d_iterator.next().trim();
                }
            }
        }
        validateMap();
    }

    /**
     * A method that handles the showMap command
     */
    public void showMap() {
        d_MapConnectivityGrid = new int[this.d_countries.size()][this.d_countries.size()];

        System.out.println("\n<ContinentID> <ContinentName> <ControlValue>\n");

        d_continents.values().stream().sorted(new Comparator<ContinentModel>() {
            @Override
            public int compare(ContinentModel o1, ContinentModel o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        }).forEach(new Consumer<ContinentModel>() {
            @Override
            public void accept(ContinentModel continentModel) {
                System.out.println(continentModel.getId() + " " + continentModel.getName() + " " + continentModel.getControlValue());
            }
        });

        System.out.println("\n<CountryID> <CountryName> <ContinentName> \n<Neighbor CountryNames List> <Neighbor CountryIDs List>\n");

        this.d_countries.values().stream().sorted(new Comparator<CountryModel>() {
            @Override
            public int compare(CountryModel o1, CountryModel o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        }).forEach(new Consumer<CountryModel>() {
            @Override
            public void accept(CountryModel countryModel) {
                System.out.print(countryModel.getId() + " ");

                System.out.print(countryModel.getName() + " ");
                System.out.println(countryModel.getContinentId());
                System.out.print(countryModel.getNeighbors().keySet() + " [");

                countryModel.getNeighbors().values().forEach(new Consumer<CountryModel>() {
                    @Override
                    public void accept(CountryModel countryModel) {
                        System.out.print(countryModel.getId() + " ");
                    }
                });
                System.out.println("\b]");
                System.out.println();

                countryModel.getNeighbors().values().forEach(new Consumer<CountryModel>() {
                    @Override
                    public void accept(CountryModel countryModel2) {
                        if (countryModel.getNeighbors().containsKey(countryModel2.getName())) {
                            d_MapConnectivityGrid[countryModel.getId() - 1][countryModel2.getId() - 1] = 1;
                        } else {
                            d_MapConnectivityGrid[countryModel.getId() - 1][countryModel2.getId() - 1] = 0;
                        }
                    }
                });
            }
        });

        System.out.println("\n[borders]\n");
        int l_counter = 1;
        for (int[] l_row : d_MapConnectivityGrid) {
            System.out.print(l_counter++ + ": ");
            System.out.println(Arrays.toString(l_row));
        }
    }

    /**
     * A method that handles the validatemap command
     *
     * @return false if the map is invalid
     */
    public boolean validateMap() {
        d_MapConnectivityGrid = new int[this.d_countries.size()][this.d_countries.size()];
        this.d_countries.values().stream().parallel().forEach(new Consumer<CountryModel>() {
            @Override
            public void accept(CountryModel countryModel) {
                countryModel.getNeighbors().values().forEach(countryModel2 -> {
                    if (countryModel.getNeighbors().containsKey(countryModel2.getName())) {
                        d_MapConnectivityGrid[countryModel.getId() - 1][countryModel2.getId() - 1] = 1;
                    } else {
                        d_MapConnectivityGrid[countryModel.getId() - 1][countryModel2.getId() - 1] = 0;
                    }
                });
            }
        });

        return (validateMapTypeOne() && validateMapTypeTwo() && validateMapTypeThree());
    }

    /**
     * A method to traverse the entire map and visit countries using recursive DFS (Depth first Search) traversal.
     *
     * @param p_countryOne Index of country from where the traversal starts
     * @param p_visited    an array that keeps track of countries visited during traversal
     */
    public void traverseMap(int p_countryOne, boolean[] p_visited) {
        p_visited[p_countryOne] = true;    //mark p_countryOne as visited

        for (int l_countryTwo = 0; l_countryTwo < this.d_countries.size(); l_countryTwo++) {
            if (d_MapConnectivityGrid[p_countryOne][l_countryTwo] == 1) {
                if (!p_visited[l_countryTwo])
                    traverseMap(l_countryTwo, p_visited);
            }
        }
    }

    /**
     * A method that checks the first type of incorrect map, which is if the map is not connected
     * i.e. every country in the map should be reachable from every other country.
     *
     * @return false if the map is invalid
     */
    public boolean validateMapTypeOne() {
        boolean[] l_visited = new boolean[this.d_countries.size()];

        //for all countries l_countryOne as starting point, check whether all countries are reachable or not
        for (int l_countryOne = 0; l_countryOne < this.d_countries.size(); l_countryOne++) {
            for (int l_countryTwo = 0; l_countryTwo < this.d_countries.size(); l_countryTwo++) {
                //initially no countries are visited
                l_visited[l_countryTwo] = false;
            }

            traverseMap(l_countryOne, l_visited);
            for (int l_countryTwo = 0; l_countryTwo < this.d_countries.size(); l_countryTwo++) {
                if (!l_visited[l_countryTwo]) {
                    //if there exists a l_countryTwo, not visited by map traversal, map is not connected.
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateMapTypeTwo() {
        return true;
    }

    public boolean validateMapTypeThree() {
        return true;
    }

}

