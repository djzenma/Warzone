package Model.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * POJO class for the Commands
 */
public class Commands {
    /**
     * Array of the commands
     */
    @SerializedName(value = "commands")
    public Command[] d_commands;

    @SerializedName(value = "commandNames")
    public CommandNames d_commandNames;
}
