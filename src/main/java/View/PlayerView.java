package View;

import java.util.HashMap;
import java.util.List;

/**
 * Interacts with the user if anything is wrong while issuing orders
 */
public class PlayerView {

    private void reconstructDeploy(HashMap<String, List<String>> p_args) {
        System.out.println("Command: deploy " + p_args.get("country_name").get(0) + " " + p_args.get("reinforcements_num").get(0));
    }
    /**
     * Prints if the reinforcements are not enough to be issued
     *
     * @param p_args
     * @param p_reinforcements Requested number of reinforcements
     */
    public void NotEnoughReinforcements(HashMap<String, List<String>> p_args, int p_reinforcements) {
        reconstructDeploy(p_args);
        if (p_reinforcements != 0)
            System.out.println("You don't have enough reinforcements! You only have " + p_reinforcements);
        else
            System.out.println("You don't have any reinforcements remaining!");
    }

    /**
     * Prints the remaining number of reinforcements
     *
     * @param p_reinforcements Remaining number of reinforcements
     */
    public void ReinforcementsRemain(int p_reinforcements) {
        System.out.println("You still have " + p_reinforcements + " reinforcements remaining! Please deploy them all!");
    }

    /**
     * Prints if the current player does not own the specified country
     * @param p_args
     */
    public void InvalidCountry(HashMap<String, List<String>> p_args) {
        reconstructDeploy(p_args);
        System.out.println("You don't own this country!");
    }

    /**
     * Prints if the number of reinforcements are invalid
     * @param p_args
     */
    public void InvalidNumber(HashMap<String, List<String>> p_args) {
        reconstructDeploy(p_args);
        System.out.println("Please enter a valid number!");
    }

    /**
     * Prints if the order is invalid
     */
    public void invalidOrder() {
        System.out.println("Please enter a valid order!");
    }
}
