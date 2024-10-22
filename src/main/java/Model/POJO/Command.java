package Model.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * POJO class for a Command
 */
public class Command {
    /**
     * Name of the command
     */
    @SerializedName(value = "name")
    public String d_name;

    /**
     * Array of the arguments
     */
    @SerializedName(value = "args")
    public Argument[] d_args;

    /**
     * Array of the named arguments
     */
    @SerializedName(value = "namedArgs")
    public NamedArgument[] d_namedArgs;

    /**
     * Array of the variable arguments
     */
    @SerializedName(value = "varArgs")
    public VarArgument[] d_varArgs;
}


