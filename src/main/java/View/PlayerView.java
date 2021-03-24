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
    public void notEnoughReinforcements(HashMap<String, List<String>> p_args, int p_reinforcements) {
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
    public void reinforcementsRemain(int p_reinforcements) {
        System.out.println("You still have " + p_reinforcements + " reinforcements remaining! Please deploy them all!");
    }

    /**
     * Prints if the current player does not own the specified country
     *
     * @param p_player      the current player name
     * @param p_countryName the country name
     */
    public void invalidCountry(String p_player, String p_countryName) {
        System.out.println(p_player + " don't own " + p_countryName + "!");
    }

    /**
     * Prints if the number of reinforcements are invalid
     * @param p_args
     */
    public void invalidNumber(HashMap<String, List<String>> p_args) {
        System.out.println("Please enter a valid number!");
    }

    /**
     * Prints if the order is invalid
     */
    public void invalidOrder() {
        System.out.println("Please enter a valid order!");
    }

    public void notNeighbor(HashMap<String, List<String>> p_args) {
        System.out.println(p_args.get("country_name_to").get(0) + " is not adjacent to " + p_args.get("country_name_from").get(0) + "!");
    }

    public void insufficientArmies(HashMap<String, List<String>> d_args, int p_srcArmies) {
        if (p_srcArmies == 0) {
            System.out.println("You cannot advance " + d_args.get("armies_num").get(0) + " because you do not have any armies!");
        } else {
            System.out.println("You cannot advance " + d_args.get("armies_num").get(0)
                    + " armies! You only have " + p_srcArmies
                    + " armies in " + d_args.get("country_name_from").get(0) + "!");
        }
    }

    public void invalidAdvanceOrder(String p_ownerName) {
        System.out.println("You can't attack " + p_ownerName + "'s countries as you are currently negotiating with " + p_ownerName + "!!");
    }

    public void selfNegotiationNotPossible() {
        System.out.println("You can't add yourself as Negotiator");
    }

    public void invalidPlayer(String p_playerName) {
        System.out.println(p_playerName + " does not exist!");
    }

    public void invalidBombOrder(String p_ownerName) {
        System.out.println("You can't bomb " + p_ownerName + "'s countries as you are currently negotiating with " + p_ownerName + "!!");
    }

    public void countryInexistant(String p_countryName) {
        System.out.println(p_countryName + " does not exist on the map!");
    }

    public void showCards(HashMap<String, Integer> p_cards) {
        System.out.println("You have following Cards: \n");
        for (String l_cardType : p_cards.keySet()) {
            System.out.println(l_cardType + ": " + p_cards.get(l_cardType));
        }
    }

    public void noCardAvailable() {
        System.out.println("You don't have a card to issue this order.");
    }
}
