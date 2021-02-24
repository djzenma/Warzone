package View;

/**
 * Interacts with the user if anything is wrong while issuing orders
 */
public class PlayerView {
    /**
     * Prints if the reinforcements are not enough to be issued
     *
     * @param p_reinforcements Requested number of reinforcements
     */
    public void NotEnoughReinforcements(int p_reinforcements) {
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
     */
    public void InvalidCountry() {
        System.out.println("You don't own this country!");
    }

    /**
     * Prints if the number of reinforcements are invalid
     */
    public void InvalidNumber() {
        System.out.println("Please enter a valid number!");
    }
}
