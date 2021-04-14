package View;

import Model.ContinentModel;
import Model.CountryModel;
import Model.Player;
import Utils.CommandsParser;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * View of the Game Play
 */
public class GamePlayView implements Serializable {
    private static final long serialversionUID = 129348938L;

    /**
     * Prints the exception
     *
     * @param p_message Exception message
     */
    public void exception(String p_message) {
        System.out.println();
        System.out.println(p_message);
        System.out.println();
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
    public void currentPlayer(Player p_player) {
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
     * @param p_continents Hashmap of the continents
     * @param p_countries Hashmap of the countries
     */
    public void showMap(HashMap<String, ContinentModel> p_continents, HashMap<String, CountryModel> p_countries) { //TODO:: 13Refactor: Lambda expressions
        // Number of Entries  = Number of Neighbors of every country or 1 if it has none
        int l_rowsNum = p_countries
                .values()
                .stream()
                .mapToInt(countryModel -> (countryModel.getNeighbors().size() == 0) ?
                        1 :
                        countryModel.getNeighbors().size())
                .sum();

        final int[] LONGEST_NAME_OF_THE_CONTINENT = {0};
        final int[] LONGEST_NAME_OF_THE_COUNTRY = {0};

        p_continents.values().forEach(new Consumer<ContinentModel>() {
            @Override
            public void accept(ContinentModel p_continentModel) {
                if (p_continentModel.getName().length() > LONGEST_NAME_OF_THE_CONTINENT[0])
                    LONGEST_NAME_OF_THE_CONTINENT[0] = p_continentModel.getName().length();
            }
        });

        LONGEST_NAME_OF_THE_CONTINENT[0] += 4;

        p_countries.values().forEach(new Consumer<CountryModel>() {
            @Override
            public void accept(CountryModel p_countryModel) {
                if (p_countryModel.getName().length() > LONGEST_NAME_OF_THE_COUNTRY[0])
                    LONGEST_NAME_OF_THE_COUNTRY[0] = p_countryModel.getName().length();
            }
        });

        LONGEST_NAME_OF_THE_CONTINENT[0] = Math.max(LONGEST_NAME_OF_THE_CONTINENT[0] + 3, "Continent Name (CV)".length());
        LONGEST_NAME_OF_THE_COUNTRY[0] = Math.max(LONGEST_NAME_OF_THE_COUNTRY[0] + 3, "Neighbor Countries".length());

        String l_continentBorder = new String(new char[LONGEST_NAME_OF_THE_CONTINENT[0]]).replace("\0", "-");
        String l_countryBorder = new String(new char[LONGEST_NAME_OF_THE_COUNTRY[0]]).replace("\0", "-");

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
                for (int j = 0; j < p_countryModel.getNeighbors().keySet().size(); j++) {
                    if (j != 0)
                        l_row[0][l_i[0]] = new String[]{
                                " ",
                                " ",
                                " ",
                                " ",
                                StringUtils.center((j + 1) + ". " + l_it.next(), l_countryBorder.length())
                        };
                    else
                        l_row[0][l_i[0]] = new String[]{
                                StringUtils.center(p_countryModel.getName(), l_countryBorder.length()),
                                StringUtils.center(p_countryModel.getContinentId() + "(" + p_continents.get(p_countryModel.getContinentId()).getControlValue() + ")", l_continentBorder.length()),
                                StringUtils.center(String.valueOf(p_countryModel.getArmies()), l_countryBorder.length()),
                                StringUtils.center((p_countryModel.getOwnerName() == null ? "Not Assigned" : p_countryModel.getOwnerName()), l_countryBorder.length()),
                                StringUtils.center((j + 1) + ". " + (p_countryModel.getNeighbors().keySet().isEmpty() ?
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

    /**
     * Prints all the cards player owns
     *
     * @param p_cards Hasmap of the cards
     */
    public void showCards(HashMap<String, Integer> p_cards) {
        System.out.println("You have following Cards: \n");
        for (String l_cardType : p_cards.keySet()) {
            System.out.println(l_cardType + ": " + p_cards.get(l_cardType));
        }
    }


    /**
     * If the player of the name is invalid
     */
    public void invalidPlayerName() {
        System.out.println("You can't add/remove neutral player.");
    }

    /**
     * Checks the duplicate players
     *
     * @param p_player name of the player
     */
    public void duplicatePlayer(String p_player) {
        System.out.println(p_player + " is already in the game, can't add again.");
    }

    /**
     * Checks if no player is found
     *
     * @param p_player name of the player
     */
    public void noPlayerFound(String p_player) {
        System.out.println(p_player + " is not in the game, hence can't be removed");
    }

    /**
     * Prints if the player won
     *
     * @param p_player name of the winning player
     */
    public void winnerWinnerChickenDinner(String p_player) {
        System.out.println("WINNER WINNER CHICKEN DINNER \n" +
                p_player + ", You rock man!!");
    }

    /**
     * Prints that the command is invalid
     */
    public void invalidGameplayerCmd() {
        System.out.println("Please enter a valid gameplayer command!");
    }

    /**
     * Prints that the strategy entered is invalid
     *
     * @param p_strategyName the invalid strategy that the user entered
     */
    public void invalidStrategy(String p_strategyName) {
        System.out.println(p_strategyName + " is not a valid strategy name!");
    }

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

        // clean it
        //l_command = l_command.toLowerCase();
        String[] l_commandArgs = l_command.split("\\s+");

        // remove any '-' before any named argument
        for (int i = 0; i < l_commandArgs.length; i++) {
            if (l_commandArgs[i].startsWith("-"))
                l_commandArgs[i] = l_commandArgs[i].replace("-", "");
        }
        return l_commandArgs;
    }

    /**
     * Prints a statement for the invalid command
     */
    public void commandNotValid() {
        System.out.println("Please Enter a Valid Command!");
    }


    /**
     * Takes the Command from the user
     *
     * @return Array of arguments of the command
     */
    public String[] takeCommand() {
        String[] l_args;
        do {
            l_args = listenForStartupCommand();
            if (!CommandsParser.isValidCommand(l_args))
                commandNotValid();
        } while (!CommandsParser.isValidCommand(l_args));

        return l_args;
    }

    public void savedCheckpoint() {
        System.out.println("Checkpoint created!");
    }

    public void loadedCheckpoint() {
        System.out.println("Checkpoint loaded!");
    }

    public void modeSelection() {
        System.out.println("\n======Mode Selection======\n");
        System.out.println("1. Single Game Mode (enter singlegame)");
        System.out.println("2. Tournament Mode (enter tournament command)");
    }

    public void showTournamentResults(ArrayList<String> p_maps, int p_numGames, HashMap<String, ArrayList<String>> p_winners) {
        System.out.println("\n\n===== Tournament Results =====\n");
        for (String l_map : p_maps) {
            for (int l_gameNum = 0; l_gameNum < p_numGames; l_gameNum++) {
                System.out.println(l_map + ", Game " + l_gameNum + ": " + p_winners.get(l_map).get(l_gameNum));
            }
        }
    }

    public void invalidMode() {
        System.out.println("\n Invalid Mode Selected, Please select again!!\n");
    }
}
