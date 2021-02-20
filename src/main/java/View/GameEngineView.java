package View;

import Model.PlayerModel;

import java.util.Scanner;

public class GameEngineView {
    public String[] listenForStartupCommand() {
        System.out.print("\n>>");

        // take the command
        Scanner l_scanner = new Scanner(System.in);
        String l_command = l_scanner.nextLine();

        // clean it
        l_command = l_command.toLowerCase();
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
    }

    public void startupPhase() {
        System.out.println("\n********** STARTUP PHASE **********\n");
    }

    public void gameplayPhase() {
        System.out.println("\n********** GAMEPLAY PHASE **********\n");
    }

    public void commandNotValid() {
        System.out.println("Please Enter a Valid Command!");
    }
}
