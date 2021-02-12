package View;

import Model.PlayerModel;

import java.util.Scanner;

public class PlayerView {

    public static String[] issueOrderView() {
        System.out.println("enter your command:");

        Scanner scanner = new Scanner(System.in);
        String order = scanner.nextLine();

        order = order.toLowerCase();

        return order.split("\\s+");
    }

    public static void NotEnoughReinforcements(int p_reinforcements) {
        System.out.println("You don't have enough reinforcements! You only have " + p_reinforcements);
    }

    public static void ReinforcementsRemain(int p_reinforcements) {
        System.out.println("You still have " + p_reinforcements + " reinforcements remaining! Please deploy them all!");
    }

    public static void CurrentPlayer(PlayerModel p_player) {
        System.out.println(p_player.getName() + "'s Turn");
    }

    public static void InvalidCountry() {
        System.out.println("You don't own this country!");
    }

}
