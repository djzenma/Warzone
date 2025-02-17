package Utils;

import Model.POJO.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * JSONParser to parse the commands and validates them
 */
public class CommandsParser {

    /**
     * Commands object
     */
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

                if (l_command.d_args == null && l_command.d_namedArgs == null && l_command.d_varArgs == null)
                    return true;

                // check if the cmd has named args
                if (l_command.d_namedArgs != null) {
                    if (p_cmdArgs.length - 1 <= 0)
                        return false;
                }

                // check if the cmd has args
                if (l_command.d_args != null) {
                    if (p_cmdArgs.length - 1 != l_command.d_args.length)
                        return false;
                }

                // check if the cmd has named args
                if (l_command.d_namedArgs != null) {
                    boolean isValid;
                    if(l_command.d_varArgs == null) {
                        isValid = isValidOnlyNamedCommand(p_cmdArgs, l_command);
                        if(!isValid)
                            return false;
                    }
                    else {
                        isValid = isValidMixedNamedCommand(p_cmdArgs, l_command);
                        if(!isValid)
                            return false;
                    }
                }

                // check if the cmd has variable args
                if (l_command.d_varArgs != null) {
                    boolean l_argFound;
                    // iterate over the variable args that should be there
                    for (VarArgument l_varArg : l_command.d_varArgs) {
                        l_argFound = false;
                        // look for this vararg inside the entered command
                        l_i = 1;
                        while (l_i < p_cmdArgs.length) {
                            // if the varArg name is found
                            if (p_cmdArgs[l_i].equals(l_varArg.d_name)) {
                                // if no values passed to this argument name
                                if (((l_i + 1) < p_cmdArgs.length && isValidArgName(p_cmdArgs[0], p_cmdArgs[l_i + 1])) ||
                                        (l_i + 1) >= (p_cmdArgs.length - 1))
                                    return false;
                                l_argFound = true;
                                break;
                            } else
                                l_i++;
                        }

                        // the current varArg is not found, return false immediately because at least 1 varArg is missing
                        if (!l_argFound)
                            return false;
                    }
                    // all varArgs are found
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Checks the validity of the mixed named command
     *
     * @param p_cmdArgs command arguments
     * @param p_command command
     * @return true if it is a valid mixed named command; otherwise false
     */
    private static boolean isValidMixedNamedCommand(String[] p_cmdArgs, Command p_command) {
        int l_numNamedArgs = 0;
        int l_i = 1;
        while (l_i < p_cmdArgs.length) {
            // ensure that the argument name is valid
            if (isNamedArgName(p_cmdArgs[0], p_cmdArgs[l_i])) {

                // ensure that the correct number of arguments is passed for this argument name
                int l_num = getNumberOfArguments(p_cmdArgs[0], p_cmdArgs[l_i]);

                // handles when the required argument is NOT passed
                if (l_i + l_num >= p_cmdArgs.length)
                    return false;

                    // handles when more arguments are passed than the number required
                else if (l_i + l_num + 1 < p_cmdArgs.length && !isValidArgName(p_cmdArgs[0], p_cmdArgs[l_i + l_num + 1]))
                    return false;

                else {
                    l_i += l_num + 1;
                    l_numNamedArgs++;
                }
            }
            else
                l_i++;
        }
        if(l_numNamedArgs != p_command.d_namedArgs.length)
            return false;
        return true;
    }

    /**
     * Checks the validity of the named command
     *
     * @param p_cmdArgs command arguments
     * @param p_command command
     * @return true if it is a valid named command; otherwise false
     */
    private static boolean isValidOnlyNamedCommand(String[] p_cmdArgs, Command p_command) {
        int l_i = 1;
        while (l_i < p_cmdArgs.length) {
            // ensure that the argument name is valid
            if (isNamedArgName(p_cmdArgs[0], p_cmdArgs[l_i])) {

                // ensure that the correct number of arguments is passed for this argument name
                int l_num = getNumberOfArguments(p_cmdArgs[0], p_cmdArgs[l_i]);

                // handles when the required argument is NOT passed
                if (l_i + l_num >= p_cmdArgs.length)
                    return false;

                    // handles when more arguments are passed than the number required
                else if (l_i + l_num + 1 < p_cmdArgs.length && !isValidArgName(p_cmdArgs[0], p_cmdArgs[l_i + l_num + 1]))
                    return false;

                else {
                    l_i += l_num + 1;
                }
            }
            else
                return false;
        }
        return true;
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
    private static boolean isNamedArgName(String p_cmdName, String p_cmdArg) {
        for (Command l_command : d_commands.d_commands) {
            if (l_command.d_name.equals(p_cmdName)) {

                if (l_command.d_namedArgs != null) {
                    for (NamedArgument l_namedArg : l_command.d_namedArgs) {
                        if (l_namedArg.d_name.equals(p_cmdArg))
                            return true;
                    }
                }
            }
        }
        return false;
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

                if (l_command.d_namedArgs != null) {
                    for (NamedArgument l_namedArg : l_command.d_namedArgs) {
                        if (l_namedArg.d_name.equals(p_cmdArg))
                            return true;
                    }
                }

                if (l_command.d_varArgs != null) {
                    for (VarArgument l_varArg : l_command.d_varArgs) {
                        if (l_varArg.d_name.equals(p_cmdArg))
                            return true;
                    }
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
        l_args.put("cmd", Collections.singletonList(l_cmdName));

        for (Command l_command : d_commands.d_commands) {
            if (l_command.d_name.equals(l_cmdName)) {
                if (l_command.d_namedArgs != null) {

                    for (int i = 1; i < p_cmd.length; i++) {
                        for (NamedArgument namedArg : l_command.d_namedArgs) {
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

                                break;
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

                // check if the cmd has variable args
                if (l_command.d_varArgs != null) {
                    // iterate over the variable args that should be there
                    for (VarArgument l_varArg : l_command.d_varArgs) {
                        // look for this vararg inside the entered command
                        int l_i = 1;
                        while (l_i < p_cmd.length) {
                            // if the varArg name is found
                            if (p_cmd[l_i].equals(l_varArg.d_name)) {
                                // store the values of the argument until you find a new varArg name or end of the command
                                int l_j = l_i + 1;
                                while ((l_j < p_cmd.length) && !isValidArgName(p_cmd[0], p_cmd[l_j])) {
                                    List<String> l_currentList;
                                    if (l_args.get(l_varArg.d_name) == null) {
                                        l_currentList = new ArrayList<>();
                                    } else {
                                        l_currentList = l_args.get(l_varArg.d_name);
                                    }
                                    l_currentList.add(p_cmd[l_j]);
                                    l_args.put(l_varArg.d_name, l_currentList);
                                    l_j++;
                                }
                                break;
                            } else
                                l_i++;
                        }
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
     * @param l_cmdName array of the arguments in a command
     * @return true if the name is valid; otherwise false
     */
    public static boolean isPass(String l_cmdName) {
        return l_cmdName.equals(d_commands.d_commandNames.d_pass);
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

    /**
     * Checks if the command entered is to show cards
     *
     * @param l_args array of the arguments in a command
     * @return true if the name is valid; otherwise false
     */
    public static boolean isShowCards(String[] l_args) {
        return l_args[0].equals(d_commands.d_commandNames.d_showcards);
    }

    /**
     * Checks if the command entered is to load game
     *
     * @param l_args array of the arguments in a command
     * @return true if the name is valid; otherwise false
     */
    public static boolean isLoadGame(String[] l_args) {
        return l_args[0].equals(d_commands.d_commandNames.d_loadgame);
    }
}
