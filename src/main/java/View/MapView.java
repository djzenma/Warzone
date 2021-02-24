package View;

import Model.ContinentModel;
import Model.CountryModel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public class MapView {
    Scanner d_scanner = new Scanner(System.in);

    public String[] listenForCommands() {
        System.out.print("\n>> ");

        // take the command
        String l_command = d_scanner.nextLine().trim();

        // clean it
        String[] l_commandArgs = l_command.split("\\s+");

        // remove any '-' before any named argument
        for (int l_i = 0; l_i < l_commandArgs.length; l_i++) {
            if (l_commandArgs[l_i].startsWith("-"))
                l_commandArgs[l_i] = l_commandArgs[l_i].replace("-", "");
        }
        return l_commandArgs;
    }

    public void exception(String p_message) {
        System.out.println(p_message);
    }

    public void showMsg(String p_msg) {
        System.out.println(p_msg);
    }

    public void commandNotValid() {
        System.out.println("Please Enter a Valid Command!");
    }

    public void notMapEditorCommand() {
        System.out.println("Please enter a valid Map Editor Command.");
    }

    public void showGameTitle() {
        System.out.println("\n");
        System.out.println("***************************************** WARZONE *******************************************");
        System.out.println("---------------------------------------------------------------------------------------------");
    }

    public void mapNotLoaded() {
        System.out.println("Domination map file is not loaded currently. \n" +
                "Must load the domination map file using editmap command.");
    }

    public void validMap(boolean p_validateMap) {
        if (p_validateMap) {
            System.out.println("The map is valid!");
        } else {
            System.out.println("The map is invalid!");
        }
    }

    public void mapEditorPhase() {
        System.out.print("\n************************************* MAP-EDITOR PHASE **************************************\n\n");
        this.showAvailableCommands(false);
    }

    public void showAvailableCommands(boolean p_withTitle){

        if(p_withTitle) {
            System.out.println("\n==================================== Map-Editor Commands ====================================".toUpperCase());
            System.out.println("---------------------------------------------------------------------------------------------\n");
        }

        System.out.println("1.  editmap filename.map");
        System.out.println("2.  editcontinent -add continentName controlValue -remove continentName");
        System.out.println("3.  editcountry -add countryName continentName -remove countryName");
        System.out.println("4.  editneighbor -add countryName neighborCountryName -remove countryName neighborCountryName");
        System.out.println("5.  showmap");
        System.out.println("6.  savemap filename.map");
        System.out.println("7.  validatemap");
        System.out.println("8.  showcontinents");
        System.out.println("9.  showcountries");
        System.out.println("10. showcommands");
        System.out.println("11. listmaps (shows all available domination map files)");
        System.out.println("12. exit");
    }

    public void showAvailableFiles(LinkedHashMap<String, String> p_allFilesValidation) {

        if(p_allFilesValidation == null) return;

        System.out.println("\n=========== Available Domination Map Files =========".toUpperCase());
        System.out.print("----------------------------------------------------\n");

        int l_longestNameSize = 0;

        for (String l_fileName:p_allFilesValidation.keySet()) {
            if (l_fileName.length() > l_longestNameSize)
                l_longestNameSize = l_fileName.length();
        }
        String l_format = "|%2s|%" + l_longestNameSize + "s|%9s|\n";

        String[] l_row;
        int l_counter = 1;

        for (String l_fileName : p_allFilesValidation.keySet()) {
            l_row = new String[]{String.valueOf(l_counter),
                                 l_fileName,
                                 p_allFilesValidation.get(l_fileName)};
            System.out.format(l_format, l_row);
            l_counter++;
        }
    }

    public void showMap(HashMap<String, ContinentModel> p_continents, HashMap<String, CountryModel> p_countries) {

        if (p_continents.size() == 0 || p_countries.size() == 0) {
            System.out.println("The Map File is Empty!");
            return;
        }
        this.showContinents(p_continents);
        this.showCountries(p_continents, p_countries);
    }

    public void showContinents(HashMap<String, ContinentModel> p_continents) {

        //TODO: check if this is considered constant or not
        final int[] l_LONGEST_CONTINENT_NAME = {0};


        p_continents.values().forEach(new Consumer<ContinentModel>() {
            @Override
            public void accept(ContinentModel continentModel) {
                if (continentModel.getName().length() > l_LONGEST_CONTINENT_NAME[0])
                    l_LONGEST_CONTINENT_NAME[0] = continentModel.getName().length();
            }
        });

        if ("Continent Name".length() > l_LONGEST_CONTINENT_NAME[0])
            l_LONGEST_CONTINENT_NAME[0] = "Continent Name".length();

        String l_border = new String(new char[l_LONGEST_CONTINENT_NAME[0]]).replace("\0", "-");
        System.out.println(l_LONGEST_CONTINENT_NAME[0]);
        String[] l_separator = new String[]{l_border, l_border, l_border, l_border};

        String l_format = "|%15s|%" + l_LONGEST_CONTINENT_NAME[0] + "s|%15s|%15s|\n";
        final Object[][][] l_row = {new String[p_continents.size()][]};
        l_row[0][0] = new String[]{
                StringUtils.center("Continent ID".toUpperCase(), l_border.length()),
                StringUtils.center("Continent Name".toUpperCase(), l_border.length()),
                StringUtils.center("Control Value".toUpperCase(), l_border.length()),
                StringUtils.center("# of Countries".toUpperCase(), l_border.length())};

        System.out.println();
        System.out.format(l_format, l_separator);
        System.out.format(l_format, l_row[0][0]);
        System.out.format(l_format, l_separator);

        int[] i = {0};
        p_continents.values()
                .forEach(continentModel -> {
                    l_row[0][i[0]] = new String[]{
                            StringUtils.center(String.valueOf(continentModel.getId()), l_border.length()),
                            StringUtils.center(continentModel.getName(), l_border.length()),
                            StringUtils.center(String.valueOf(continentModel.getControlValue()), l_border.length()),
                            StringUtils.center(String.valueOf(continentModel.getCountries().size()), l_border.length())};

                    System.out.format(l_format, l_row[0][i[0]]);
                    i[0]++;
                });
        System.out.format(l_format, l_separator);
        System.out.println();
    }

    public void showCountries(HashMap<String, ContinentModel> p_continents, HashMap<String, CountryModel> p_countries) {
        int l_rowsNum = p_countries
                .values()
                .stream()
                .mapToInt(countryModel -> (countryModel.getNeighbors().size() == 0) ?
                        1 :
                        countryModel.getNeighbors().size())
                .sum();

        final int[] l_longestNameOfContinent = {0};
        final int[] l_longestNameOfCountry = {0};

        p_continents.values().forEach(new Consumer<ContinentModel>() {
            @Override
            public void accept(ContinentModel continentModel) {
                if (continentModel.getName().length() > l_longestNameOfContinent[0])
                    l_longestNameOfContinent[0] = continentModel.getName().length();
            }
        });

        l_longestNameOfContinent[0] += 4;

        p_countries.values().forEach(new Consumer<CountryModel>() {
            @Override
            public void accept(CountryModel countryModel) {
                if (countryModel.getName().length() > l_longestNameOfCountry[0])
                    l_longestNameOfCountry[0] = countryModel.getName().length();
            }
        });

        l_longestNameOfContinent[0] = Math.max(l_longestNameOfContinent[0] + 3, "Continent Name (CV)".length());
        l_longestNameOfCountry[0] = Math.max(l_longestNameOfCountry[0] + 3, "Neighbor Countries".length());


        String l_continentBorder = new String(new char[l_longestNameOfContinent[0]]).replace("\0", "-");
        String l_countryBorder = new String(new char[l_longestNameOfCountry[0]]).replace("\0", "-");

        String[] l_separator = new String[]{l_countryBorder, l_continentBorder, l_countryBorder};

        String l_format = "|%" + l_countryBorder.length() + "s|%" +
                l_continentBorder.length() + "s|%" +
                l_countryBorder.length() + "s|\n";

        final Object[][][] l_row = {new String[l_rowsNum][]};
        l_row[0][0] = new String[]{
                StringUtils.center("Country Name".toUpperCase(), l_countryBorder.length()),
                StringUtils.center("Continent Name (CV)".toUpperCase(), l_continentBorder.length()),
                StringUtils.center("Neighbor Countries".toUpperCase(), l_countryBorder.length())};
        System.out.println();
        System.out.format(l_format, l_separator);
        System.out.format(l_format, l_row[0][0]);
        System.out.format(l_format, l_separator);

        final int[] i = {0};
        p_countries.values().forEach(countryModel -> {
            Iterator<String> l_it = countryModel.getNeighbors().keySet().iterator();
            if (countryModel.getNeighbors().keySet().isEmpty()) {
                l_row[0][i[0]] = new String[]{
                        StringUtils.center(countryModel.getName(), l_countryBorder.length()),
                        StringUtils.center(countryModel.getContinentId() + "(" + p_continents.
                                get(countryModel.getContinentId()).getControlValue() + ")", l_continentBorder.length()),
                        StringUtils.center("[No Neighbors]", l_countryBorder.length())
                };
                System.out.format(l_format, l_row[0][i[0]]);
                i[0]++;
            } else {
                for (int j = 0; j < countryModel.getNeighbors().keySet().size(); j++) {
                    if (j != 0)
                        l_row[0][i[0]] = new String[]{
                                " ",
                                " ",
                                StringUtils.center((j + 1) + ". " + l_it.next(), l_countryBorder.length())
                        };
                    else
                        l_row[0][i[0]] = new String[]{
                                StringUtils.center(countryModel.getName(), l_countryBorder.length()),
                                StringUtils.center(countryModel.getContinentId() + "(" + p_continents.
                                        get(countryModel.getContinentId()).getControlValue() + ")", l_continentBorder.length()),
                                StringUtils.center((j + 1) + ". " + (countryModel.getNeighbors().keySet().isEmpty() ?
                                        "[No Neighbors]" : l_it.next()), l_countryBorder.length())
                        };

                    System.out.format(l_format, l_row[0][i[0]]);
                    i[0]++;
                }
            }
            System.out.format(l_format, l_separator);
        });
        System.out.println();
    }
}
