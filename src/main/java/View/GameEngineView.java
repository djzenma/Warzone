package View;

import Model.ContinentModel;
import Model.CountryModel;
import Model.PlayerModel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Handles the user inputs and prints the user game experience
 */
public class GameEngineView {
    /**
     * Listens for the startup commands from the user
     *
     * @return Array of arguments of the commands
     */
    public String[] listenForStartupCommand() {
        System.out.print("\n>> ");

        // take the command
        Scanner l_scanner = new Scanner(System.in);
        String l_command = l_scanner.nextLine();

        String[] l_commandArgs = l_command.split("\\s+");

        // remove any '-' before any named argument
        for (int l_i = 0; l_i < l_commandArgs.length; l_i++) {
            if (l_commandArgs[l_i].startsWith("-"))
                l_commandArgs[l_i] = l_commandArgs[l_i].replace("-", "");
        }
        return l_commandArgs;
    }

    /**
     * Prints the exception
     *
     * @param p_message Exception message
     */
    public void exception(String p_message) {
        System.out.println(p_message);
    }

    /**
     * Prints the information of the current player
     * <ul>
     *     <li> Name of the Player </li>
     *     <li> Number of total Reinforcements a Player owns</li>
     *     <li> Name of the Country </li>
     *     <li> Number of Reinforcements a Player owns as per the country </li>
     * </ul>
     *
     * @param p_player Object of the PlayerModel
     */
    public void currentPlayer(PlayerModel p_player) {
        System.out.println("\n" + p_player.getName() + "'s Turn");
        System.out.println("You have " + p_player.getReinforcements() + " reinforcements");
        System.out.println("Country Name " + "\t" + "Number of Reinforcements");
        for (CountryModel l_countryModel : p_player.getCountries()) {
            System.out.println(l_countryModel.getName() + "\t" + l_countryModel.getArmies());
        }
    }

    /**
     * Prints a starting Startup phase line
     */
    public void startupPhase() {
        System.out.println("\n********** STARTUP PHASE **********\n");
    }

    /**
     * Prints a starting Gameplay phase line
     */
    public void gameplayPhase() {
        System.out.println("\n********** GAMEPLAY PHASE **********\n");
    }

    /**
     * Displays the turn number of every player
     *
     * @param p_turnNumber Turn number
     */
    public void gameplayTurnNumber(int p_turnNumber) {
        System.out.println("\nTurn #" + p_turnNumber);
    }

    /**
     * Prints a statement for the invalid command
     */
    public void commandNotValid() {
        System.out.println("Please Enter a Valid Command!");
    }

    /**
     * Prints a statement for the invalid map editor command in the gameplay phase
     */
    public void isMapEditorCommand() {
        System.out.println("Please enter a valid Game Play Startup Command");
    }

    /**
     * Prints a statement if the map is not loaded
     */
    public void mapNotLoaded() {
        System.out.println("You have not loaded any map yet. Please load a map first!");
    }

    /**
     * Prints a statement if there is invalid assignment of the countries
     */
    public void isInvalidAssignment() {
        System.out.println("Invalid assignment of countries. No players in the game.");
    }

    /**
     * Prints a table on the showmap command entered by the user.
     * The table has the following information-
     * <ul>
     *     <li> Name of the Country </li>
     *     <li> Name of the Continent the country belongs to along with the control value of the continent</li>
     *     <li> Number of Reinforcements deployed on the country </li>
     *     <li> Number of Player that owns the country </li>
     *     <li> Name of the Neighbors of the country </li>
     * </ul>
     *
     * @param p_continents HashMap of the continents
     * @param p_countries HashMap of the countries
     */
    public void showMap(HashMap<String, ContinentModel> p_continents, HashMap<String, CountryModel> p_countries) {
        // Number of Entries  = Number of Neighbors of every country or 1 if it has none
        int l_rowsNum = p_countries
                .values()
                .stream()
                .mapToInt(countryModel -> (countryModel.getNeighbors().size() == 0) ?
                        1 :
                        countryModel.getNeighbors().size())
                .sum();

        int[] l_longestNameOfTheContinent = {0};
        int[] l_longestNameOfTheCountry = {0};

        p_continents.values().forEach(new Consumer<ContinentModel>() {
            @Override
            public void accept(ContinentModel p_continentModel) {
                if (p_continentModel.getName().length() > l_longestNameOfTheContinent[0])
                    l_longestNameOfTheContinent[0] = p_continentModel.getName().length();
            }
        });

        l_longestNameOfTheContinent[0] += 4;

        p_countries.values().forEach(new Consumer<CountryModel>() {
            @Override
            public void accept(CountryModel p_countryModel) {
                if (p_countryModel.getName().length() > l_longestNameOfTheCountry[0])
                    l_longestNameOfTheCountry[0] = p_countryModel.getName().length();
            }
        });

        l_longestNameOfTheContinent[0] = Math.max(l_longestNameOfTheContinent[0] + 3, "Continent Name (CV)".length());
        l_longestNameOfTheCountry[0] = Math.max(l_longestNameOfTheCountry[0] + 3, "Neighbor Countries".length());

        String l_continentBorder = new String(new char[l_longestNameOfTheContinent[0]]).replace("\0", "-");
        String l_countryBorder = new String(new char[l_longestNameOfTheCountry[0]]).replace("\0", "-");

        String[] l_separator = new String[]{l_countryBorder, l_continentBorder, l_countryBorder, l_countryBorder, l_countryBorder};

        String l_format = "|%" + l_countryBorder.length() + "s|%" +
                l_continentBorder.length() + "s|%" + l_countryBorder.length() + "s|%" + l_countryBorder.length() + "s|%" +
                l_countryBorder.length() + "s|\n";

        // the table
        final Object[][][] l_row = {new String[l_rowsNum][]};

        // The Column Names
        l_row[0][0] = new String[]{StringUtils.center("Country Name".toUpperCase(), l_countryBorder.length()),
                StringUtils.center("Continent Name (CV)".toUpperCase(), l_continentBorder.length()),
                StringUtils.center("Number Of Armies".toUpperCase(), l_countryBorder.length()),
                StringUtils.center("Owner Name".toUpperCase(), l_countryBorder.length()),
                StringUtils.center("Neighbor Countries".toUpperCase(), l_countryBorder.length())};
        System.out.println();
        System.out.format(l_format, l_separator);
        System.out.format(l_format, l_row[0][0]);
        System.out.format(l_format, l_separator);

        // iterate over all the countries
        final int[] l_i = {0};
        p_countries.values().forEach(p_countryModel -> {

            // get its next neighbor
            Iterator<String> l_it = p_countryModel.getNeighbors().keySet().iterator();

            // if no neighbors
            if (p_countryModel.getNeighbors().keySet().isEmpty()) {

                // Display all country's information
                l_row[0][l_i[0]] = new String[]{
                        StringUtils.center(p_countryModel.getName(), l_countryBorder.length()),
                        StringUtils.center(p_countryModel.getContinentId(), l_continentBorder.length()),
                        StringUtils.center(String.valueOf(p_countryModel.getArmies()), l_countryBorder.length()),
                        StringUtils.center((p_countryModel.getOwnerName() == null ? "Not Assigned" : p_countryModel.getOwnerName()), l_countryBorder.length()),
                        StringUtils.center("[No Neighbors]", l_countryBorder.length())
                };
                System.out.format(l_format, l_row[0][l_i[0]]);
                l_i[0]++;
            }

            // If the country has neighbors Display all country's information
            else {

                // display the neighbors line by line
                for (int l_j = 0; l_j < p_countryModel.getNeighbors().keySet().size(); l_j++) {
                    if (l_j != 0)
                        l_row[0][l_i[0]] = new String[]{
                                " ",
                                " ",
                                " ",
                                " ",
                                StringUtils.center((l_j + 1) + ". " + l_it.next(), l_countryBorder.length())
                        };
                    else
                        l_row[0][l_i[0]] = new String[]{
                                StringUtils.center(p_countryModel.getName(), l_countryBorder.length()),
                                StringUtils.center(p_countryModel.getContinentId() + "(" + p_continents.get(p_countryModel.getContinentId()).getControlValue() + ")", l_continentBorder.length()),
                                StringUtils.center(String.valueOf(p_countryModel.getArmies()), l_countryBorder.length()),
                                StringUtils.center((p_countryModel.getOwnerName() == null ? "Not Assigned" : p_countryModel.getOwnerName()), l_countryBorder.length()),
                                StringUtils.center((l_j + 1) + ". " + (p_countryModel.getNeighbors().keySet().isEmpty() ?
                                        "[No Neighbors]" : l_it.next()), l_countryBorder.length())
                        };
                    System.out.format(l_format, l_row[0][l_i[0]]);
                    l_i[0]++;
                }
            }
            System.out.format(l_format, l_separator);
        });
        System.out.println();
    }
}
