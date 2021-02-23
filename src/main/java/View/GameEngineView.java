package View;

import Model.ContinentModel;
import Model.CountryModel;
import Model.PlayerModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class GameEngineView {
    public String[] listenForStartupCommand() {
        System.out.print("\n>> ");

        // take the command
        Scanner l_scanner = new Scanner(System.in);
        String l_command = l_scanner.nextLine();

        // clean it
        //l_command = l_command.toLowerCase();
        String[] l_commandArgs = l_command.split("\\s+");

        // remove any '-' before any named argument
        for (int i=0; i<l_commandArgs.length; i++) {
            if(l_commandArgs[i].startsWith("-"))
                l_commandArgs[i] = l_commandArgs[i].replace("-", "");
        }
        return l_commandArgs;
    }

    public void exception(String message) {
        System.out.println(message);
    }

    public void currentPlayer(PlayerModel p_player) {
        System.out.println("\n" + p_player.getName() + "'s Turn");
        System.out.println("You have " + p_player.getReinforcements() + " reinforcements");
        System.out.println("Country Name " + "\t" + "Number of Reinforcements");
        for (CountryModel l_countryModel : p_player.getCountries()) {
            System.out.println(l_countryModel.getName() + "\t" + l_countryModel.getArmies());
        }
    }

    public void startupPhase() {
        System.out.println("\n********** STARTUP PHASE **********\n");
    }

    public void gameplayPhase() {
        System.out.println("\n********** GAMEPLAY PHASE **********\n");
    }

    public void gamePlayTurnNumber(int p_turnNumber) {
        System.out.println("\nTurn #" + p_turnNumber);
    }

    public void commandNotValid() {
        System.out.println("Please Enter a Valid Command!");
    }

    public void isMapEditorCommand() {
        System.out.println("Please enter a valid Game Engine Startup Command");
    }

    public void mapNotLoaded() {
        System.out.println("You have not loaded any map yet. Please load a map first!");
    }


    public void showMap(HashMap<String, ContinentModel> p_continents, HashMap<String, CountryModel> p_countries) {
        // Number of Entries  = Number of Neighbors of every country or 1 if it has none
        int l_rowsNum = p_countries
                .values()
                .stream()
                .mapToInt(countryModel -> (countryModel.getNeighbors().size() == 0) ?
                        1 :
                        countryModel.getNeighbors().size())
                .sum();


        // The format of the table row
        String l_format = "%20s|%20s|%20s|%20s|%20s\n";
        String l_border = "--------------------";

        // the table
        final Object[][][] l_row = {new String[l_rowsNum][]};

        // The Column Names
        l_row[0][0] = new String[]{"Country Name".toUpperCase(),
                "Continent Name (CV)".toUpperCase(),
                "Number Of Armies".toUpperCase(),
                "Owner Name".toUpperCase(),
                "Neighbor Countries".toUpperCase()};
        System.out.println();
        System.out.format(l_format, l_row[0][0]);
        System.out.format(l_format, (Object[]) new String[]{l_border, l_border, l_border, l_border, l_border});

        // iterate over all the countries
        final int[] i = {0};
        p_countries.values().forEach(countryModel -> {
            // get its next neighbor
            Iterator<String> l_it = countryModel.getNeighbors().keySet().iterator();
            // if no neighbors
            if (countryModel.getNeighbors().keySet().isEmpty()) {
                // Display all country's information
                l_row[0][i[0]] = new String[]{
                        countryModel.getName(),
                        countryModel.getContinentId(),
                        String.valueOf(countryModel.getArmies()),
                        (countryModel.getOwnerName() == null ? "Not Assigned" : countryModel.getOwnerName()),
                        "[No Neighbors]"
                };
                System.out.format(l_format, l_row[0][i[0]]);
                i[0]++;
            }
            // If the country has neighbors Display all country's information
            else {
                // display the neighbors line by line
                for (int j = 0; j < countryModel.getNeighbors().keySet().size(); j++) {
                    if (j != 0)
                        l_row[0][i[0]] = new String[]{
                                " ",
                                " ",
                                " ",
                                " ",
                                (j + 1) + ". " + l_it.next()
                        };
                    else
                        l_row[0][i[0]] = new String[]{
                                countryModel.getName(),
                                countryModel.getContinentId() + "(" + p_continents.get(countryModel.getContinentId()).getControlValue() + ")",
                                String.valueOf(countryModel.getArmies()),
                                (countryModel.getOwnerName() == null ? "Not Assigned" : countryModel.getOwnerName()),
                                (j + 1) + ". " + (countryModel.getNeighbors().keySet().isEmpty() ? "[No Neighbors]" : l_it.next())
                        };

                    System.out.format(l_format, l_row[0][i[0]]);
                    i[0]++;
                }
            }

            System.out.format(l_format, (Object[]) new String[]{l_border, l_border, l_border, l_border, l_border});
        });
        System.out.println();
    }

}
