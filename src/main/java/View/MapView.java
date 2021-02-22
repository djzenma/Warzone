package View;

import java.util.Scanner;

public class MapView {

    public void gameName() {
        System.out.println("***********************WARZONE***********************");
        System.out.println("-----------------------------------------------------");
    }

    public String[] listenForCommands() {
        System.out.print("\n>> ");

        // take the command
        Scanner l_scanner = new Scanner(System.in);
        String l_command = l_scanner.nextLine();

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

    public void mapEditorPhase() {
        System.out.print("\n********** MAP EDITOR PHASE **********\n");
    }

    public void commandNotValid() {
        System.out.println("Please Enter a Valid Command!");
    }

    public void mapNotLoaded() {
        System.out.println("Domination map file is not loaded currently. \n" +
                "Must load the domination map file to be edited first!");
    }

    public void validMap(boolean validateMap) {
        if (validateMap){
            System.out.println("The map is valid!");
        }
        else{
            System.out.println("The map is invalid!");
        }
    }

    public void showMsg(String p_msg) {
        System.out.println(p_msg);
    }
}
