package View;

import Model.ContinentModel;
import Model.CountryModel;

import java.util.*;

public class MapView {
    Scanner d_scanner = new Scanner(System.in);

    public String[] listenForCommands() {
        System.out.print("\n>> ");

        // take the command
        String l_command = d_scanner.nextLine().trim();

        // clean it
        String[] l_commandArgs = l_command.split("\\s+");

        // remove any '-' before any named argument
        for (int i = 0; i < l_commandArgs.length; i++) {
            if (l_commandArgs[i].startsWith("-"))
                l_commandArgs[i] = l_commandArgs[i].replace("-", "");
        }
        return l_commandArgs;
    }

    public void exception(String message) {
        System.out.println(message);
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
        System.out.println("******************************************WARZONE********************************************");
        System.out.println("---------------------------------------------------------------------------------------------");
    }

    public void mapNotLoaded() {
        System.out.println("Domination map file is not loaded currently. \n" +
                "Must load the domination map file using editmap command.");
    }

    public void validMap(boolean validateMap) {
        if (validateMap){
            System.out.println("The map is valid!");
        }
        else{
            System.out.println("The map is invalid!");
        }
    }

    public void mapEditorPhase() {
        System.out.print("\n**************************************MAP-EDITOR PHASE***************************************\n\n");
        this.showAvailableCommands(false);
    }

    public void showAvailableCommands(boolean p_withTitle){

        if(p_withTitle) {
            System.out.println("\n=====================================Map-Editor Commands=====================================".toUpperCase());
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
        System.out.println("11. showallfiles");
        System.out.println("12. exit");
    }

    public void showAvailableFiles(LinkedHashMap<String, String> p_allFilesValidation) {

        if(p_allFilesValidation == null) return;

        System.out.println("\n============Available Domination Map Files==========".toUpperCase());
        System.out.print("----------------------------------------------------\n");

        int l_longest_name_size = 0;

        for (String l_fileName:p_allFilesValidation.keySet()) {
            if(l_fileName.length() > l_longest_name_size)
                l_longest_name_size = l_fileName.length();
        }
        String l_format = "|%2s|%"+ l_longest_name_size +"s|%9s|\n";

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
        this.showContinents(p_continents);
        this.showCountries(p_continents, p_countries);
    }

    public void showContinents(HashMap<String, ContinentModel> p_continents) {
        String l_border = "---------------";
        String[] l_separator = new String[]{l_border, l_border, l_border, l_border};

        String l_format = "|%15s|%15s|%15s|%15s|\n";
        final Object[][][] l_row = {new String[p_continents.size()][]};
        l_row[0][0] = new String[]{"Continent ID".toUpperCase(),
                "Continent Name".toUpperCase(),
                "Control Value".toUpperCase(),
                "# of Countries".toUpperCase()};

        System.out.println();
        System.out.format(l_format, l_separator);
        System.out.format(l_format, l_row[0][0]);
        System.out.format(l_format, l_separator);

        int[] i = {0};
        p_continents.values()
                .forEach(continentModel -> {
                    l_row[0][i[0]] = new String[]{
                            String.valueOf(continentModel.getId()),
                            continentModel.getName(),
                            String.valueOf(continentModel.getControlValue()),
                            String.valueOf(continentModel.getCountries().size())};

                    System.out.format(l_format, l_row[0][i[0]]);
                    i[0]++;
                });
        System.out.format(l_format, l_separator);
        System.out.println();
    }

    public void showCountries(HashMap<String, ContinentModel> p_continents, HashMap<String, CountryModel> p_countries) {
        int l_rowsNum = p_countries.values().stream().mapToInt(countryModel -> countryModel.getNeighbors().keySet().size()).sum();

        String l_border = "--------------------";
        String[] l_separator = new String[]{l_border, l_border, l_border};

        String l_format = "|%20s|%20s|%20s|\n";
        final Object[][][] l_row = {new String[l_rowsNum][]};
        l_row[0][0] = new String[]{"Country Name".toUpperCase(),
                "Continent Name (CV)".toUpperCase(),
                "Neighbor Countries".toUpperCase()};
        System.out.println();
        System.out.format(l_format, l_separator);
        System.out.format(l_format, l_row[0][0]);
        System.out.format(l_format, l_separator);

        final int[] i = {0};
        p_countries.values().forEach(countryModel -> {
            Iterator<String> l_it = countryModel.getNeighbors().keySet().iterator();
            if (countryModel.getNeighbors().keySet().isEmpty()) {
                l_row[0][i[0]] = new String[]{
                        countryModel.getName(),
                        countryModel.getContinentId() + "(" + p_continents.get(countryModel.getContinentId()).getControlValue() + ")",
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
                                (j + 1) + ". " + l_it.next()
                        };
                    else
                        l_row[0][i[0]] = new String[]{
                                countryModel.getName(),
                                countryModel.getContinentId() + "(" + p_continents.get(countryModel.getContinentId()).getControlValue() + ")",
                                (j + 1) + ". " + (countryModel.getNeighbors().keySet().isEmpty() ? "[No Neighbors]" : l_it.next())
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
