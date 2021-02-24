package Model.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Model.POJO class for the Named Arguments
 */
public class NamedArgument {
    /**
     * Name of the argument
     */
    @SerializedName(value = "name")
    public String d_name;
    /**
     * Number of arguments in the NamedArguments
     */
    @SerializedName(value = "argsNum")
    public int d_argsNum;
}
