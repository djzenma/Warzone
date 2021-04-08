package Adapter;

import Model.ContinentModel;
import Model.CountryModel;
import Utils.MapUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class DominationMapIO {

    /**
     * LinkedHashMap of the continents
     */
    private LinkedHashMap<String, ContinentModel> d_continents;
    /**
     * LinkedHashMap of the countries
     */
    private LinkedHashMap<String, CountryModel> d_countries;
    /**
     * Object of the MapUtils
     */
    private MapUtils d_mapUtils;

    /**
     * Loads the domination map, specified by p_file, to memory
     *
     * @param p_file .map file object from which the map file is being loaded
     * @throws IOException If I/O exception of some sort has occurred
     *
     * @return DominationMap Object
     */
    public MapContainer loadMap(File p_file) throws IOException {
        this.d_continents = new LinkedHashMap<>();
        this.d_countries = new LinkedHashMap<>();
        d_mapUtils = new MapUtils();
        Iterator<String> l_iterator;
        Iterator<String> l_borderIterator;
        String l_fileContent;
        String[] l_fileLines;
        String l_tempLine;
        String[] l_tempData;

        // fetches the map file content
        l_fileContent = d_mapUtils.readMapFile(p_file);
        l_fileLines = l_fileContent.split("\n");
        l_iterator = Arrays.stream(l_fileLines).iterator();

        // iterate through all lines of the map file
        while (l_iterator.hasNext()) {
            l_tempLine = l_iterator.next().trim();

            // read and load continents
            if (l_tempLine.equals("[continents]")) {
                l_tempLine = l_iterator.next().trim();

                while (!(l_tempLine.startsWith("["))) {
                    if (l_tempLine.isEmpty())
                        break;
                    l_tempData = l_tempLine.split(" ");
                    d_continents.put(l_tempData[0].trim(), new ContinentModel(d_continents.size() + 1,
                            l_tempData[0].trim(), Integer.parseInt(l_tempData[1].trim()), l_tempData[2].trim()));
                    l_tempLine = l_iterator.next().trim();
                }
            }

            // read and load countries
            if (l_tempLine.equals("[countries]")) {
                String[] l_continentName = new String[1];
                l_tempLine = l_iterator.next().trim();

                while (!(l_tempLine.startsWith("[")) && !(l_tempLine.isEmpty())) {
                    l_tempData = l_tempLine.split(" ");

                    int l_tempContinentId = Integer.parseInt(l_tempData[2].trim());
                    String[] l_tempContinentName = new String[1];

                    // fetches the continent name based on the continent id
                    this.d_continents.values().stream().parallel().forEach(p_continentModel -> {
                        if (p_continentModel.getId() == l_tempContinentId) {
                            l_tempContinentName[0] = p_continentModel.getName();
                        }
                    });

                    d_countries.put(l_tempData[1].trim(), new CountryModel(Integer.parseInt(l_tempData[0].trim()),
                            l_tempData[1].trim(), l_tempContinentName[0], l_tempData[3], l_tempData[4]));

                    int l_continentId = Integer.parseInt(l_tempData[2].trim());

                    // fetches the continent name based on the continent id
                    this.d_continents.values().stream().parallel().forEach(p_continentModel -> {
                        if (p_continentModel.getId() == l_continentId)
                            l_continentName[0] = p_continentModel.getName();
                    });

                    this.d_continents.get(l_continentName[0]).addCountry(this.d_countries.get(l_tempData[1].trim()));
                    l_tempLine = l_iterator.next().trim();
                }
            }

            // read and load borders
            if (l_tempLine.equals("[borders]")) {
                l_tempLine = l_iterator.next().trim();
                while (!(l_tempLine.startsWith("[")) && !(l_tempLine.isEmpty()) && l_iterator.hasNext()) {
                    l_tempData = l_tempLine.split(" ");
                    l_borderIterator = Arrays.stream(l_tempData).iterator();

                    String[] l_tempCountryPair = {null, null};

                    int l_firstCountryId = Integer.parseInt(l_borderIterator.next().trim());

                    // fetches the name of first country based on country id
                    this.d_countries.values().forEach(p_countryModel -> {
                        if (p_countryModel.getId() == l_firstCountryId)
                            l_tempCountryPair[0] = p_countryModel.getName();
                    });

                    // fetches the second country's name based on id and add them as neighbors
                    while (l_borderIterator.hasNext()) {
                        int l_secondCountryId = Integer.parseInt(l_borderIterator.next().trim());
                        this.d_countries.values().forEach(p_countryModel -> {
                            if (p_countryModel.getId() == l_secondCountryId)
                                l_tempCountryPair[1] = p_countryModel.getName();
                        });

                        // if only both countries exist, add them as neighbors
                        if (l_tempCountryPair[0] != null && l_tempCountryPair[1] != null)
                            this.d_countries.get(l_tempCountryPair[0]).addNeighbor(this.d_countries.get(l_tempCountryPair[1]));
                    }
                    l_tempLine = l_iterator.next().trim();
                }
            }
        }
        return new MapContainer(this.d_continents, this.d_countries);
    }
    public void saveMap(File p_file) {

    }
}
