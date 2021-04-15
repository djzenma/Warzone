package View;

import Utils.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Interacts with the user if anything is wrong while issuing orders
 */
public class PlayerView implements Serializable {
    /**
     * serial version id
     */
    private static final long SERIAL_VERSION_UID = 129348938L;

    /**
     * Reconstructs deploy
     *
     * @param p_args hashmap of command arguments
     */
    private void reconstructDeploy(HashMap<String, List<String>> p_args) {
        System.out.println("Command: deploy " + p_args.get("country_name").get(0) + " " + p_args.get("reinforcements_num").get(0));
    }

    /**
     * Prints if the reinforcements are not enough to be issued
     *
     * @param p_args           Hashmap of the command arguments
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
        System.out.println(p_player + ", You don't own " + p_countryName + "!");
    }

    /**
     * Prints if the number of reinforcements are invalid
     * @param p_args Hashmap of the command arguments
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

    /**
     * Prints if the countries are not adjacent
     *
     * @param p_args Hashmap of the command arguments
     */
    public void notNeighbor(HashMap<String, List<String>> p_args) {
        System.out.println(p_args.get("country_name_to").get(0) + " is not adjacent to " + p_args.get("country_name_from").get(0) + "!");
    }

    /**
     * Prints if the armies are insufficient
     *
     * @param d_args      Hashmap of the command arguments
     * @param p_srcArmies Number of the source armies
     */
    public void insufficientArmies(HashMap<String, List<String>> d_args, int p_srcArmies) {
        if (p_srcArmies == 0) {
            System.out.println("You cannot advance " + d_args.get("armies_num").get(0) + " because you do not have any armies!");
        } else {
            System.out.println("You cannot advance " + d_args.get("armies_num").get(0)
                    + " armies! You only have " + p_srcArmies
                    + " armies in " + d_args.get("country_name_from").get(0) + "!");
        }
    }

    /**
     * Prints if the advance order is invalid
     *
     * @param p_ownerName Name of the opponent
     */
    public void invalidAdvanceOrder(String p_ownerName) {
        System.out.println("You can't attack " + p_ownerName + "'s countries as you are currently negotiating with " + p_ownerName + "!!");
    }

    /**
     * Prints if the self negotiation is not possible
     */
    public void selfNegotiationNotPossible() {
        System.out.println("You can't add yourself as Negotiator");
    }

    /**
     * Prints if the player is invalid
     *
     * @param p_playerName name of the player
     */
    public void invalidPlayer(String p_playerName) {
        System.out.println(p_playerName + " does not exist!");
    }

    /**
     * Prints if the bomb order is invalid
     *
     * @param p_ownerName name of the player
     */
    public void invalidBombOrder(String p_ownerName) {
        System.out.println("You can't bomb " + p_ownerName + "'s countries as you are currently negotiating with " + p_ownerName + "!!");
    }

    /**
     * Prints if the country doesn't exists
     *
     * @param p_countryName name of the country
     */
    public void countryInExistent(String p_countryName) {
        System.out.println(p_countryName + " does not exist on the map!");
    }

    /**
     * Prints all the cards player owns
     *
     * @param p_cards HashMap of the cards
     */
    public void showCards(HashMap<String, Integer> p_cards) {
        System.out.println("You have following Cards: \n");
        for (String l_cardType : p_cards.keySet()) {
            System.out.println(l_cardType + ": " + p_cards.get(l_cardType));
        }
    }

    /**
     * Prints if the player doesn't have the particular card
     *
     * @param p_args command arguments
     */
    public void noCardAvailable(String[] p_args) {
        System.out.print("You issued ");
        for (String l_arg : p_args)
            System.out.print(l_arg + " ");
        System.out.println();
        System.out.println("You don't have a card to issue this order.");
    }

    /**
     * Listens for the startup commands from the user
     *
     * @return Array of arguments of the commands
     */
    public String[] listenForStartupCommand() {
        System.out.print("\n>> ");

        // take the command
        Scanner l_scanner = new Scanner(System.in);
        String l_command = l_scanner.nextLine();

        // clean it
        //l_command = l_command.toLowerCase();
        String[] l_commandArgs = l_command.split("\\s+");

        // remove any '-' before any named argument
        for (int i = 0; i < l_commandArgs.length; i++) {
            if (l_commandArgs[i].startsWith("-"))
                l_commandArgs[i] = l_commandArgs[i].replace("-", "");
        }
        return l_commandArgs;
    }

    /**
     * Prints a statement for the invalid command
     */
    public void commandNotValid() {
        System.out.println("Please Enter a Valid Command!");
    }

    /**
     * Takes the Command from the user
     *
     * @return Array of arguments of the command
     */
    public String[] takeCommand() {
        String[] l_args;
        do {
            l_args = listenForStartupCommand();
            if (!CommandsParser.isValidCommand(l_args))
                commandNotValid();
        } while (!CommandsParser.isValidCommand(l_args));

        return l_args;
    }

}
