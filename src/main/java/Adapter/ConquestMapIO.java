package Adapter;

import Model.ContinentModel;
import Model.CountryModel;
import Utils.MapUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ConquestMapIO {

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
    public MapContainer loadConquestMap(File p_file) throws IOException {
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
            if (l_tempLine.equals("[Continents]")) {
                l_tempLine = l_iterator.next().trim();

                while (!(l_tempLine.startsWith("["))) {
                    if (l_tempLine.isEmpty())
                        break;
                    l_tempData = l_tempLine.split("=");
                    d_continents.put(l_tempData[0].trim(), new ContinentModel(d_continents.size() + 1,
                            l_tempData[0].trim(), Integer.parseInt(l_tempData[1].trim()), "yellow"));
                    l_tempLine = l_iterator.next().trim();
                }
            }

            // read and load countries
            if (l_tempLine.equals("[Territories]")) {
                l_tempLine = l_iterator.next().trim();

                while (!(l_tempLine.startsWith("[")) && l_iterator.hasNext()) {
                    if(l_tempLine.isEmpty()) {
                        l_tempLine = l_iterator.next().trim();
                        continue;
                    }
                    l_tempData = l_tempLine.split(",");

                    d_countries.put(l_tempData[0].trim(), new CountryModel(
                            d_countries.size() + 1,
                            l_tempData[0].trim(),
                            l_tempData[3].trim(),
                            l_tempData[1],
                            l_tempData[2]));

                    this.d_continents.get(l_tempData[3]).addCountry(this.d_countries.get(l_tempData[0].trim()));
                    l_tempLine = l_iterator.next().trim();
                }
            }
        }

        // read and load borders
        l_iterator = Arrays.stream(l_fileLines).iterator();
        // iterate through all lines of the map file
        while (l_iterator.hasNext()) {
            l_tempLine = l_iterator.next().trim();

            if (l_tempLine.equals("[Territories]")) {
                l_tempLine = l_iterator.next().trim();

                while (!(l_tempLine.startsWith("[")) && l_iterator.hasNext()) {
                    if(l_tempLine.isEmpty()) {
                        l_tempLine = l_iterator.next().trim();
                        continue;
                    }
                    l_tempData = l_tempLine.split(",");


                    for(int l_i = 0; l_i < l_tempData.length - 4; l_i++) {
                        this.d_countries.get(l_tempData[0].trim()).addNeighbor(
                                this.d_countries.get(l_tempData[l_i+4])
                        );
                    }
                    l_tempLine = l_iterator.next().trim();
                }
            }
        }
        return new MapContainer(this.d_continents, this.d_countries);
    }
    public void saveConquestMap(File p_file) {

    }
}
