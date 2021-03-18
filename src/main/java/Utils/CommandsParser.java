package Utils;

import Model.POJO.Argument;
import Model.POJO.Command;
import Model.POJO.Commands;
import Model.POJO.NamedArgument;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * JSONParser to parse the commands and validates them
 */
public class CommandsParser {

    private static Commands d_commands;

    /**
     * Parses a JSON file
     */
    public static void parseJson() {
        try {
            Gson l_gson = new Gson();
            JsonReader l_reader = new JsonReader(new FileReader("src/main/resources/json/commands.json"));
            d_commands = l_gson.fromJson(l_reader, Commands.class);

        } catch (FileNotFoundException l_e) {
            l_e.printStackTrace();
        }
    }

    /**
     * Validates the commands entered by the user
     *
     * @param p_cmdArgs Array of the commands
     * @return true if the commands entered by the user are valid; otherwise false
     */
    public static boolean isValidCommand(String[] p_cmdArgs) {

        int l_i = 0;

        for (Command l_command : d_commands.d_commands) {
            // check for command name
            if (l_command.d_name.equals(p_cmdArgs[0])) {

                if (l_command.d_args == null && l_command.d_namedArgs == null)
                    return true;

                // check if the cmd has named args
                if (l_command.d_namedArgs != null) {
                    if (p_cmdArgs.length - 1 <= 0)
                        return false;
                }

                // check if the cmd has args
                if (l_command.d_args != null) {
                    if (p_cmdArgs.length - 1 == l_command.d_args.length)
                        return true;
                }

                // check if the cmd has named args
                if (l_command.d_namedArgs != null) {
                    l_i = 1;

                    while (l_i < p_cmdArgs.length) {

                        // ensure that the argument name is valid
                        if (isValidArgName(p_cmdArgs[0], p_cmdArgs[l_i])) {

                            // ensure that the correct number of arguments is passed for this argument name
                            int l_num = getNumberOfArguments(p_cmdArgs[0], p_cmdArgs[l_i]);

                            // handles when the required argument is NOT passed
                            if (l_i + l_num >= p_cmdArgs.length)
                                return false;

                                // handles when more arguments are passed than the number required
                            else if (l_i + l_num + 1 < p_cmdArgs.length && !isValidArgName(p_cmdArgs[0], p_cmdArgs[l_i + l_num + 1]))
                                return false;

                            else
                                l_i += l_num + 1;
                        } else
                            return false;
                    }
                    return true;
                }

            }
        }

        return false;
    }

    /**
     * Accessor for the number of arguments in a command
     *
     * @param p_cmdName name of the command entered by the user
     * @param p_cmdArg  argument entered after the name of the command
     * @return number of the arguments
     */
    private static int getNumberOfArguments(String p_cmdName, String p_cmdArg) {
        for (Command l_command : d_commands.d_commands) {
            if (l_command.d_name.equals(p_cmdName)) {
                for (NamedArgument namedArg : l_command.d_namedArgs) {
                    if (namedArg.d_name.equals(p_cmdArg))
                        return namedArg.d_argsNum;
                }
            }
        }
        return 0;
    }

    /**
     * Validates the name of the arguments in the commands entered by the user
     *
     * @param p_cmdName name of the command
     * @param p_cmdArg  arguments entered after the name of the command
     * @return true if the name of the argument is valid; otherwise false
     */
    private static boolean isValidArgName(String p_cmdName, String p_cmdArg) {
        for (Command l_command : d_commands.d_commands) {
            if (l_command.d_name.equals(p_cmdName)) {

                for (NamedArgument namedArg : l_command.d_namedArgs) {
                    if (namedArg.d_name.equals(p_cmdArg))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Accessor for the list of arguments
     *
     * @param p_cmd array of the commands
     * @return hashmap of the arguments
     */
    public static HashMap<String, List<String>> getArguments(String[] p_cmd) {
        HashMap<String, List<String>> l_args = new HashMap<>();
        String l_cmdName = p_cmd[0];

        for (Command l_command : d_commands.d_commands) {
            if (l_command.d_name.equals(l_cmdName)) {
                if (l_command.d_namedArgs != null) {
                    for (NamedArgument namedArg : l_command.d_namedArgs) {
                        for (int i = 1; i < p_cmd.length; i++) {
                            if (namedArg.d_name.equals(p_cmd[i])) {
                                // read old list
                                List<String> l_currentList = l_args.get(namedArg.d_name);
                                // update it
                                if (l_currentList == null)
                                    l_currentList = new ArrayList<>(Arrays.asList(p_cmd).subList(i + 1, i + namedArg.d_argsNum + 1));
                                else {
                                    List<String> l_subList = Arrays.asList(p_cmd).subList(i + 1, i + namedArg.d_argsNum + 1);
                                    l_currentList.addAll(l_subList);
                                }
                                // write it
                                l_args.put(namedArg.d_name, l_currentList);
                            }
                        }
                    }
                }
                if (l_command.d_args != null) {
                    int l_i = 1;
                    for (Argument unnamedArg : l_command.d_args) {
                        List<String> l_currentList = new ArrayList<>();
                        l_currentList.add(p_cmd[l_i]);
                        l_args.put(unnamedArg.d_name, l_currentList);
                        l_i++;
                    }
                }
            }
        }
        return l_args;
    }

    /**
     * Checks if the command entered is to add a game player
     *
     * @param l_args array of the arguments in a command
     * @return true if the name is valid; otherwise false
     */
    public static boolean isGameplayer(String[] l_args) {
        return l_args[0].equals(d_commands.d_commandNames.d_gameplayer);
    }

    /**
     * Checks if the command entered is to assign countries to the players
     *
     * @param l_args array of the arguments in a command
     * @return true if the name is valid; otherwise false
     */
    public static boolean isAssignCountries(String[] l_args) {
        return l_args[0].equals(d_commands.d_commandNames.d_assigncountries);
    }

    /**
     * Checks if the command entered is to load map
     *
     * @param l_args array of the arguments in a command
     * @return true if the name is valid; otherwise false
     */
    public static boolean isLoadMap(String[] l_args) {
        return l_args[0].equals(d_commands.d_commandNames.d_loadmap);
    }

    /**
     * Checks if the command entered is to show map
     *
     * @param l_args array of the arguments in a command
     * @return true if the name is valid; otherwise false
     */
    public static boolean isShowMap(String[] l_args) {
        return l_args[0].equals(d_commands.d_commandNames.d_showmap);
    }

    /**
     * Checks if the command entered is to pass
     *
     * @param l_args array of the arguments in a command
     * @return true if the name is valid; otherwise false
     */
    public static boolean isPass(String[] l_args) {
        return l_args[0].equals(d_commands.d_commandNames.d_pass);
    }

    /**
     * Checks if the command entered is to deploy
     *
     * @param l_args array of the arguments in a command
     * @return true if the name is valid; otherwise false
     */
    public static boolean isDeploy(String[] l_args) {
        return l_args[0].equals(d_commands.d_commandNames.d_deploy);
    }

}
