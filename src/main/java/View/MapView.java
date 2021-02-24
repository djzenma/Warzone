package View;

import Model.ContinentModel;
import Model.CountryModel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Listens for commands from user and displays the output
 */
public class MapView {
    Scanner d_scanner = new Scanner(System.in);

    /**
     * Listens for commands from user
     *
     * @return Command entered
     */
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

    /**
     * Displays the exception
     *
     * @param p_message Exception message
     */
    public void exception(String p_message) {
        System.out.println(p_message);
    }

    /**
     * Displays the message
     *
     * @param p_msg Message to be displayed
     */
    public void showMsg(String p_msg) {
        System.out.println(p_msg);
    }

    /**
     * Displays message that command is not valid
     */
    public void commandNotValid() {
        System.out.println("Please Enter a Valid Command!");
    }

    /**
     * Displays message to ask for valid Map-Editor command
     */
    public void notMapEditorCommand() {
        System.out.println("Please enter a valid Map Editor Command.");
    }

    /**
     * Displays game title
     */
    public void showGameTitle() {
        System.out.println("\n");
        System.out.println("***************************************** WARZONE *******************************************");
        System.out.println("---------------------------------------------------------------------------------------------");
    }

    /**
     * Displays message that the map file is not loaded yet
     */
    public void mapNotLoaded() {
        System.out.println("Domination map file is not loaded currently. \n" +
                "Must load the domination map file using editmap command.");
    }

    /**
     * Displays validity of the map
     *
     * @param p_validateMap Validity of the map
     */
    public void validMap(boolean p_validateMap) {
        if (p_validateMap) {
            System.out.println("The map is valid!");
        } else {
            System.out.println("The map is invalid!");
        }
    }

    /**
     * Displays Map-Editor phase and its commands
     */
    public void mapEditorPhase() {
        System.out.print("\n************************************* MAP-EDITOR PHASE **************************************\n\n");
        this.showAvailableCommands(false);
    }

    /**
     * Asks for user input until the input is valid
     *
     * @param p_msg Message to be displayed
     * @return Valid user input
     */
    public String askForUserInput(String p_msg) {
        System.out.print("\n" + p_msg + ": ");
        String l_input = new Scanner(System.in).nextLine().trim().toLowerCase();
        if (l_input.equals("y") || l_input.equals("n"))
            return l_input;
        else
            l_input = askForUserInput("Invalid input, please enter again (Y/N)?");
        return l_input;
    }

    /**
     * Displays all Map-Editor commands
     *
     * @param p_withTitle If true, commands will be displayed with title
     */
    public void showAvailableCommands(boolean p_withTitle) {

        if (p_withTitle) {
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

    /**
     * Displays all available map files and their validations
     *
     * @param p_allFilesValidation Names and validations of all the files
     */
    public void showAvailableFiles(LinkedHashMap<String, String> p_allFilesValidation) {

        if (p_allFilesValidation == null) return;

        System.out.println("\n=========== Available Domination Map Files =========".toUpperCase());
        System.out.print("----------------------------------------------------\n");

        int l_longestNameSize = 0;

        for (String l_fileName : p_allFilesValidation.keySet()) {
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

    /**
     * Displays two tables on the showmap command entered by the user <br>
     * The first table has the following information- <br>
     * <ul>
     *     <li> ID of the Continent </li>
     *     <li> Name of the Continent </li>
     *     <li> Control Value of the continent </li>
     * <ul/>
     * <p>
     * The second table has the following information- <br>
     * <ul>
     *     <li> Name of the Country </li>
     *     <li> Name of the Continent the country belongs to along with the control value of the continent</li>
     *     <li> Name of the Neighbors of the country </li>
     * </ul>
     *
     * @param p_continents Continents to be displayed
     * @param p_countries  Countries to be displayed
     */
    public void showMap(HashMap<String, ContinentModel> p_continents, HashMap<String, CountryModel> p_countries) {

        if (p_continents.size() == 0 || p_countries.size() == 0) {
            System.out.println("The Map File is Empty!");
            return;
        }
        this.showContinents(p_continents);
        this.showCountries(p_continents, p_countries);
    }

    /**
     * Displays a table on the showcontinents command entered by the user.
     * The table has the following information-
     * <ul>
     *     <li> ID of the Continent </li>
     *     <li> Name of the Continent </li>
     *     <li> Control Value of the continent </li>
     * </ul>
     *
     * @param p_continents Continents to be displayed
     */
    public void showContinents(HashMap<String, ContinentModel> p_continents) {

        int[] l_longestContinentName = {0};

        // fetches the length of the longest name of continent
        p_continents.values().forEach(p_continentModel -> {
            if (p_continentModel.getName().length() > l_longestContinentName[0])
                l_longestContinentName[0] = p_continentModel.getName().length();
        });

        if ("Continent Name".length() > l_longestContinentName[0])
            l_longestContinentName[0] = "Continent Name".length();

        String l_border = new String(new char[l_longestContinentName[0]]).replace("\0", "-");
        String[] l_separator = new String[]{l_border, l_border, l_border, l_border};

        String l_format = "|%15s|%" + l_longestContinentName[0] + "s|%15s|%15s|\n";

        // the table
        final Object[][][] ROW = {new String[p_continents.size()][]};

        // The Column Names
        ROW[0][0] = new String[]{
                StringUtils.center("Continent ID".toUpperCase(), l_border.length()),
                StringUtils.center("Continent Name".toUpperCase(), l_border.length()),
                StringUtils.center("Control Value".toUpperCase(), l_border.length()),
                StringUtils.center("# of Countries".toUpperCase(), l_border.length())};

        System.out.println();
        System.out.format(l_format, l_separator);
        System.out.format(l_format, ROW[0][0]);
        System.out.format(l_format, l_separator);

        // iterate over all the continents
        int[] l_i = {0};
        p_continents.values().forEach(p_continentModel -> {

            // display a row
            ROW[0][l_i[0]] = new String[]{
                    StringUtils.center(String.valueOf(p_continentModel.getId()), l_border.length()),
                    StringUtils.center(p_continentModel.getName(), l_border.length()),
                    StringUtils.center(String.valueOf(p_continentModel.getControlValue()), l_border.length()),
                    StringUtils.center(String.valueOf(p_continentModel.getCountries().size()), l_border.length())};

            System.out.format(l_format, ROW[0][l_i[0]]);
            l_i[0]++;
        });
        System.out.format(l_format, l_separator);
        System.out.println();
    }

    /**
     * Displays a table on the showcountries command entered by the user.
     * The table has the following information-
     * <ul>
     *     <li> Name of the Country </li>
     *     <li> Name of the Continent the country belongs to along with the control value of the continent</li>
     *     <li> Name of the Neighbors of the country </li>
     * </ul>
     *
     * @param p_continents Continents to be displayed
     * @param p_countries  Countries to be displayed
     */
    public void showCountries(HashMap<String, ContinentModel> p_continents, HashMap<String, CountryModel> p_countries) {
        // Number of Entries  = Number of Neighbors of every country or 1 if it has none
        int l_rowsNum = p_countries
                .values()
                .stream()
                .mapToInt(p_countryModel -> (p_countryModel.getNeighbors().size() == 0) ?
                        1 :
                        p_countryModel.getNeighbors().size())
                .sum();

        int[] l_longestNameOfContinent = {0};
        int[] l_longestNameOfCountry = {0};

        // fetches the length of the longest name of continent
        p_continents.values().forEach(p_continentModel -> {
            if (p_continentModel.getName().length() > l_longestNameOfContinent[0])
                l_longestNameOfContinent[0] = p_continentModel.getName().length();
        });

        l_longestNameOfContinent[0] += 4;

        // fetches the length of the longest name of country
        p_countries.values().forEach(p_countryModel -> {
            if (p_countryModel.getName().length() > l_longestNameOfCountry[0])
                l_longestNameOfCountry[0] = p_countryModel.getName().length();
        });

        l_longestNameOfContinent[0] = Math.max(l_longestNameOfContinent[0] + 3, "Continent Name (CV)".length());
        l_longestNameOfCountry[0] = Math.max(l_longestNameOfCountry[0] + 3, "Neighbor Countries".length());


        String l_continentBorder = new String(new char[l_longestNameOfContinent[0]]).replace("\0", "-");
        String l_countryBorder = new String(new char[l_longestNameOfCountry[0]]).replace("\0", "-");

        String[] l_separator = new String[]{l_countryBorder, l_continentBorder, l_countryBorder};

        String l_format = "|%" + l_countryBorder.length() + "s|%" +
                l_continentBorder.length() + "s|%" +
                l_countryBorder.length() + "s|\n";

        // the table
        final Object[][][] ROW = {new String[l_rowsNum][]};

        // The Column Names
        ROW[0][0] = new String[]{
                StringUtils.center("Country Name".toUpperCase(), l_countryBorder.length()),
                StringUtils.center("Continent Name (CV)".toUpperCase(), l_continentBorder.length()),
                StringUtils.center("Neighbor Countries".toUpperCase(), l_countryBorder.length())};
        System.out.println();
        System.out.format(l_format, l_separator);
        System.out.format(l_format, ROW[0][0]);
        System.out.format(l_format, l_separator);

        // iterate over all the countries
        int[] l_i = {0};
        p_countries.values().forEach(p_countryModel -> {

            // get its next neighbor
            Iterator<String> l_it = p_countryModel.getNeighbors().keySet().iterator();

            // if no neighbors
            if (p_countryModel.getNeighbors().keySet().isEmpty()) {

                // display all country's information
                ROW[0][l_i[0]] = new String[]{
                        StringUtils.center(p_countryModel.getName(), l_countryBorder.length()),
                        StringUtils.center(p_countryModel.getContinentId() + "(" + p_continents.
                                get(p_countryModel.getContinentId()).getControlValue() + ")", l_continentBorder.length()),
                        StringUtils.center("[No Neighbors]", l_countryBorder.length())
                };
                System.out.format(l_format, ROW[0][l_i[0]]);
                l_i[0]++;
            }

            // if the country has neighbors Display all country's information
            else {
                // display the neighbors line by line
                for (int l_j = 0; l_j < p_countryModel.getNeighbors().keySet().size(); l_j++) {
                    if (l_j != 0)
                        ROW[0][l_i[0]] = new String[]{
                                " ",
                                " ",
                                StringUtils.center((l_j + 1) + ". " + l_it.next(), l_countryBorder.length())
                        };
                    else
                        ROW[0][l_i[0]] = new String[]{
                                StringUtils.center(p_countryModel.getName(), l_countryBorder.length()),
                                StringUtils.center(p_countryModel.getContinentId() + "(" + p_continents.
                                        get(p_countryModel.getContinentId()).getControlValue() + ")", l_continentBorder.length()),
                                StringUtils.center((l_j + 1) + ". " + (p_countryModel.getNeighbors().keySet().isEmpty() ?
                                        "[No Neighbors]" : l_it.next()), l_countryBorder.length())
                        };

                    System.out.format(l_format, ROW[0][l_i[0]]);
                    l_i[0]++;
                }
            }
            System.out.format(l_format, l_separator);
        });
        System.out.println();
    }
}
