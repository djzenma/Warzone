package Utils;

import POJO.Command;
import POJO.Commands;
import POJO.NamedArgument;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class CommandsParser {

    private static Commands d_commands;

    public static void parse() {
        try {
            Gson l_gson = new Gson();
            JsonReader l_reader = new JsonReader(new FileReader("src/main/resources/json/commands.json"));
            d_commands = l_gson.fromJson(l_reader, Commands.class);

        } catch (FileNotFoundException l_e) {
            l_e.printStackTrace();
        }
    }

    public static boolean isValidCommand(String[] p_cmdArgs) {

        int l_i= 0;

        for (Command l_command: d_commands.commands) {
            // check for command name
            if(l_command.name.equals(p_cmdArgs[0])) {

                if(l_command.args == null && l_command.named_args == null)
                    return true;


                // check if the cmd has args
                if(l_command.args != null) {
                    if(p_cmdArgs.length - 1 == l_command.args.length)
                        return true;
                }


                // check if the cmd has named args
                if(l_command.named_args != null) {
                    l_i = 1;
                    while(l_i < p_cmdArgs.length) {
                        // ensure that the argument name is valid
                        if(isValidArgName(p_cmdArgs[0], p_cmdArgs[l_i])) {

                            // ensure that the correct number of arguments is passed for this argument name
                            int l_num = getNumberOfArguments(p_cmdArgs[0], p_cmdArgs[l_i]);

                            // handles when the required argument is NOT passed
                            if(l_i+l_num >= p_cmdArgs.length)
                                return false;
                            // handles when more arguments are passed than the number required
                            else if(l_i+l_num+1 < p_cmdArgs.length && !isValidArgName(p_cmdArgs[0], p_cmdArgs[l_i+l_num+1]))
                                    return false;
                            else
                                l_i += l_num + 1;
                        }
                    }
                    return true;
                }
            }
        }

        return false;
    }

    private static int getNumberOfArguments(String p_cmdName, String p_cmdArg) {
        for (Command l_command : d_commands.commands) {
            if(l_command.name.equals(p_cmdName)) {
                for (NamedArgument namedArg : l_command.named_args) {
                    if(namedArg.name.equals(p_cmdArg))
                        return namedArg.args_num;
                }
            }
        }
        return 0;
    }

    private static boolean isValidArgName(String p_cmdName, String p_cmdArg) {
        for (Command l_command : d_commands.commands) {
            if(l_command.name.equals(p_cmdName)) {

                for (NamedArgument namedArg : l_command.named_args) {
                    if(namedArg.name.equals(p_cmdArg))
                        return true;
                }
            }
        }
        return false;
    }
}
