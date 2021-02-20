package View;

import java.util.Scanner;

public class MapView {

    public String[] listenForCommands() {
        System.out.print("\n>>");

        // take the command
        Scanner l_scanner = new Scanner(System.in);
        String l_command = l_scanner.nextLine();

        // clean it
        l_command = l_command.toLowerCase();
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
        System.out.println("\n********** MAPEDITOR PHASE **********\n");
    }

    public void commandNotValid() {
        System.out.println("Please Enter a Valid Command!");
    }
}
