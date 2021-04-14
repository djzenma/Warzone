package Adapter;

import Model.ContinentModel;
import Model.CountryModel;
import Utils.MapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

/**
 * Read and Write the Conquest Map
 */
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

                    String l_continentName = l_tempData[0].trim().replace(" ", "-");
                    d_continents.put(l_continentName, new ContinentModel(d_continents.size() + 1,
                            l_continentName, Integer.parseInt(l_tempData[1].trim()), "yellow"));
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

                    String l_countryName = l_tempData[0].trim().replace(" ", "-");
                    String l_continentName = l_tempData[3].trim().replace(" ", "-");
                    d_countries.put(l_countryName, new CountryModel(
                            d_countries.size() + 1,
                            l_countryName,
                            l_continentName,
                            l_tempData[1],
                            l_tempData[2]));

                    this.d_continents.get(l_continentName).addCountry(this.d_countries.get(l_countryName));
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

                    String l_mainCountryName = l_tempData[0].trim().replace(" ", "-");
                    String l_neighborCountry;
                    for(int l_i = 0; l_i < l_tempData.length - 4; l_i++) {
                        l_neighborCountry = l_tempData[l_i+4].trim().replace(" ", "-");
                        this.d_countries.get(l_mainCountryName).addNeighbor(
                                this.d_countries.get(l_neighborCountry)
                        );
                    }
                    l_tempLine = l_iterator.next().trim();
                }
            }
        }
        return new MapContainer(this.d_continents, this.d_countries);
    }

    /**
     * Saves the conquest map to the memory
     *
     * @param p_file conquest map file
     * @param p_continents continents map
     * @param p_countries countries map
     * @throws IOException throws exception if unable to write to the file
     */
    public void saveConquestMap(File p_file,
                                LinkedHashMap<String, ContinentModel> p_continents,
                                LinkedHashMap<String, CountryModel> p_countries) throws IOException {
        this.d_continents = p_continents;
        this.d_countries = p_countries;
        String l_tempLine = null;
        FileOutputStream l_fos;

        // writes [files] section
        p_file.createNewFile();
        l_fos = new FileOutputStream(p_file);

        l_tempLine = "[Map]\n" +
                "pic " + p_file.getName().split("\\.")[0] + "_pic.jpg\n" +
                "map " + p_file.getName().split("\\.")[0] + "_map.gif\n" +
                "crd " + p_file.getName().split("\\.")[0] + ".cards\n\n";
        l_fos.write(l_tempLine.getBytes(StandardCharsets.UTF_8));

        // writes [continents] section
        l_fos.write("[Continents]\n".getBytes(StandardCharsets.UTF_8));

        // sort continents based on id
        // write each continent to map file in given format
        d_continents.values().stream().sorted(Comparator.comparingInt(ContinentModel::getId)).forEach(p_continentModel -> {
            try {
                l_fos.write((p_continentModel.getName() + "=" +
                        p_continentModel.getControlValue() + "\n"
                ).getBytes(StandardCharsets.UTF_8));
            } catch (IOException l_e) {
                l_e.printStackTrace();
            }
        });

        // writes [countries] section
        l_fos.write("\n[Territories]\n".getBytes(StandardCharsets.UTF_8));

        // sort countries based on id
        // write each country to map file in given format
        d_countries.values().stream().sorted(Comparator.comparingInt(CountryModel::getId)).forEach(new Consumer<CountryModel>() {
            @Override
            public void accept(CountryModel p_countryModel) {
                try {
                    l_fos.write((p_countryModel.getName() + "," +
                            p_countryModel.getXCoordinate() + "," +
                            p_countryModel.getYCoordinate() + "," +
                            p_countryModel.getContinentId() + ","
                    ).getBytes(StandardCharsets.UTF_8));

                    for(CountryModel l_neighbor: p_countryModel.getNeighbors().values()) {
                        l_fos.write((l_neighbor.getName() + ",").getBytes(StandardCharsets.UTF_8));
                    }
                    l_fos.write(("\n").getBytes(StandardCharsets.UTF_8));
                } catch (IOException l_e) {
                    l_e.printStackTrace();
                }
            }
        });
        l_fos.flush();
        l_fos.close();
    }
}
