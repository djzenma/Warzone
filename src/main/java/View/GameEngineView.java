package View;

import Model.ContinentModel;
import Model.CountryModel;
import Model.PlayerModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.function.Consumer;

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
        showCountries(p_countries, p_continents);
    }

    public void showContinents(HashMap<String, ContinentModel> p_continents) {
        System.out.println("\n<ContinentID> <ContinentName> <ControlValue>\n");

        p_continents.values()
                .forEach(new Consumer<ContinentModel>() {
                    @Override
                    public void accept(ContinentModel continentModel) {
                        System.out.println("(" + continentModel.getId() + ") " +
                                continentModel.getName() +
                                " \"" + continentModel.getControlValue() + "\"");
                    }
                });
    }

    public void showCountries(HashMap<String, CountryModel> p_countries, HashMap<String, ContinentModel> p_continents) {
        int l_rowsNum = p_countries.values().stream().mapToInt(countryModel -> countryModel.getNeighbors().keySet().size()).sum();

        String l_border = "--------------------";
        String l_format = "%20s|%20s|%20s|%20s|%20s\n";
        final Object[][][] l_row = {new String[l_rowsNum][]};
        l_row[0][0] = new String[]{"Country Name".toUpperCase(),
                "Continent Name (CV)".toUpperCase(),
                "Number Of Armies".toUpperCase(),
                "Owner Name".toUpperCase(),
                "Neighbor Countries".toUpperCase()};
        System.out.println();
        System.out.format(l_format, l_row[0][0]);
        System.out.format(l_format, (Object[]) new String[]{l_border, l_border, l_border, l_border, l_border});

        final int[] i = {0};
        p_countries.values().forEach(countryModel -> {
            Iterator<String> l_it = countryModel.getNeighbors().keySet().iterator();
            if (countryModel.getNeighbors().keySet().isEmpty()) {
                l_row[0][i[0]] = new String[]{
                        countryModel.getName(),
                        countryModel.getContinentId(),
                        String.valueOf(countryModel.getArmies()),
                        (countryModel.getOwnerName() == null ? "Not Assigned" : countryModel.getOwnerName()),
                        "[No Neighbors]"
                };
                System.out.format(l_format, l_row[0][i[0]]);
                i[0]++;
            } else {
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
