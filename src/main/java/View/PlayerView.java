package View;


import java.util.Scanner;

public class PlayerView {

    public static String[] issueOrderView() {
        System.out.print(">> ");

        Scanner scanner = new Scanner(System.in);
        String order = scanner.nextLine();

        return order.split("\\s+");
    }

    public void NotEnoughReinforcements(int p_reinforcements) {
        if (p_reinforcements != 0)
            System.out.println("You don't have enough reinforcements! You only have " + p_reinforcements);
        else
            System.out.println("You don't have any reinforcements remaining!");
    }

    public void ReinforcementsRemain(int p_reinforcements) {
        System.out.println("You still have " + p_reinforcements + " reinforcements remaining! Please deploy them all!");
    }

    public void InvalidCountry() {
        System.out.println("You don't own this country!");
    }
}
