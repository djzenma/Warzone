package Model;

import Model.Orders.*;
import ObserverPattern.Observable;
import Utils.CommandsParser;
import View.PlayerView;

import java.util.*;

/**
 * Issues the order and returns the next order
 * Maintains the HashMap of the countries, the reinforcements it owns for the current turn
 * and the armies that this player owns in each country
 */
public class PlayerModel extends Observable {
    private final PlayerView d_view;

    private String d_name;
    private int d_reinforcements;

    private final HashMap<String, CountryModel> d_myCountries;
    private final HashMap<Integer, Integer> d_armies;
    private final Queue<OrderModel> d_orderList;
    private final HashMap<String, PlayerModel> d_activeNegotiators;
    private final HashMap<String, Integer> d_cards;
    private boolean d_eligibleForCard;

    private final HashMap<String, CountryModel> d_allMapCountries;
    private HashMap<String, PlayerModel> d_allGamePlayers;


    /**
     * Initialises the name of the player, reinforcements, orders, countries and PlayerView
     *
     * @param p_name Name of the player
     * @param p_view Object of the PlayerView
     */
    public PlayerModel(String p_name, PlayerView p_view, HashMap<String, CountryModel> p_countries) {
        this.setName(p_name);
        this.d_reinforcements = 0;
        this.d_orderList = new ArrayDeque<>();
        this.d_armies = new HashMap<>();
        this.d_myCountries = new HashMap<>();
        this.d_view = p_view;
        this.d_allMapCountries = p_countries;
        this.d_activeNegotiators = new HashMap<>();
        this.d_cards = new HashMap<>();
        this.initiateCards();
        this.d_eligibleForCard = false;
    }

    /**
     * Initialises the name of the player, reinforcements, orders, countries and PlayerView
     *
     * @param p_name Name of the player
     * @param p_view Object of the PlayerView
     */
    public PlayerModel(String p_name, PlayerView p_view, HashMap<String, CountryModel> p_countries, HashMap<String, PlayerModel> p_players) {
        this.setName(p_name);
        this.d_reinforcements = 0;
        this.d_orderList = new ArrayDeque<>();
        this.d_armies = new HashMap<>();
        this.d_myCountries = new HashMap<>();
        this.d_view = p_view;
        this.d_allMapCountries = p_countries;
        this.d_allGamePlayers = p_players;
        this.d_activeNegotiators = new HashMap<>();
        this.d_cards = new HashMap<>();
        this.initiateCards();
        this.d_eligibleForCard = false;
    }

    /**
     * Initiates cards for player
     */
    private void initiateCards() {
        this.d_cards.put("bomb", 0);
        this.d_cards.put("blockade", 0);
        this.d_cards.put("airlift", 0);
        this.d_cards.put("negotiate", 0);
    }

    /**
     * Accesses the number of cards player has given the type of a card
     *
     * @param p_cardType Type of a card
     * @return Number od cards of a particular type
     */
    public int noOfCards(String p_cardType) {
        return d_cards.get(p_cardType);
    }

    /**
     * Accessor for the eligibility of receiving card
     *
     * @return True, if eligible; false otherwise.
     */
    public boolean isEligibleForCard() {
        return d_eligibleForCard;
    }

    /**
     * Mutator for the name of the eligibility of receiving card
     *
     * @param p_eligibleForCard eligibility of obtaining a card
     */
    public void setEligibleForCard(boolean p_eligibleForCard) {
        this.d_eligibleForCard = p_eligibleForCard;
    }

    /**
     * Accessor for the active negotiators
     *
     * @return active negotiators
     */
    public HashMap<String, PlayerModel> getActiveNegotiators() {
        return d_activeNegotiators;
    }

    /**
     * Removes all the active negotiators
     * Triggered on each new turn
     */
    public void flushActiveNegotiators() {
        this.d_activeNegotiators.clear();
    }

    /**
     * Adds the negotiator to the list of active negotiators
     *
     * @param p_negotiator Object of the PlayerModel
     */
    public void addNegotiator(PlayerModel p_negotiator) {
        this.d_activeNegotiators.put(p_negotiator.getName(), p_negotiator);
    }

    /**
     * Adds the country to the list of countries
     *
     * @param p_countryModel Object of the CountryModel
     */
    public void addCountry(CountryModel p_countryModel) {
        this.d_myCountries.put(p_countryModel.getName(), p_countryModel);
    }

    /**
     * Removes the country from the list of countries
     *
     * @param p_countryModel Object of the CountryModel
     */
    public void removeCountry(CountryModel p_countryModel) {
        this.d_myCountries.remove(p_countryModel.getName());
    }

    /**
     * Accessor for the name of the player
     *
     * @return Name of the player
     */
    public String getName() {
        return d_name;
    }

    /**
     * Mutator for the name of the player
     *
     * @param p_name Name of the player
     */
    public void setName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Accessor for the Id of the country
     *
     * @param p_countryId Id of the country
     * @return Country object
     */
    public CountryModel getCountryById(int p_countryId) {
        return d_myCountries.get(p_countryId);
    }

    /**
     * Gets the list of the countries
     *
     * @return The list of the countries
     */
    public ArrayList<CountryModel> getCountries(){
        return new ArrayList<CountryModel>(this.d_myCountries.values());
    }

    /**
     * Checks if the player owns a particular country or not
     *
     * @param p_countryName Id of the country
     * @return True if the country belongs the player; otherwise false
     */
    public boolean containsCountry(String p_countryName){
        return d_myCountries.containsKey(p_countryName);
    }

    /**
     * Adds an order to the list of the orders
     *
     * @param p_order Object of the OrderController
     */
    public void addOrder(OrderModel p_order) {
        this.d_orderList.add(p_order);
    }

    /**
     * Accessor for the number of armies in the specified country
     *
     * @param p_countryId Id of the country
     * @return Number of armies in the specified country
     */
    public int getArmies(int p_countryId) {
        return d_armies.get(p_countryId);
    }

    /**
     * Mutator for the number of armies in the specified country
     *
     * @param p_countryId Id of the country
     * @param p_armies    Number of the armies in the specified country
     */
    public void setArmies(int p_countryId, int p_armies) {
        this.d_armies.put(p_countryId, p_armies);
    }

    /**
     * Accessor for the reinforcements
     *
     * @return Number of reinforcements
     */
    public int getReinforcements() {
        return d_reinforcements;
    }

    /**
     * Mutator for the reinforcements
     *
     * @param p_reinforcements Number of reinforcements
     */
    public void setReinforcements(int p_reinforcements) {
        this.d_reinforcements = p_reinforcements;
    }

    //TODO:: Make it alot of things- ask Mazen but he is an ant so ask adeetya then drop the course, just add orders
    // don't validate, validate in the execute :(

    /**
     * Player issues an order. In this various conditions are checked and the method is recursively called until user issues a valid order
     *
     * @param p_args Arguments of the command
     * @return False if the order is invalid; Otherwise true
     */
    public boolean issueOrder(String[] p_args) {
        OrderModel l_order;

        // checks if the player issued any other order
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);
        switch (p_args[0]) {
            case "pass":
                // checks if the player is trying to pass/skip the turn
                if (this.getReinforcements() != 0) {
                    this.d_view.ReinforcementsRemain(this.getReinforcements());
                    return false; // impossible command
                }
                break;
            case "advance":
                if (!this.d_allMapCountries.containsKey(l_args.get("country_name_from").get(0))) {
                    this.d_view.invalidCountry(l_args.get("country_name_from").get(0));
                    return false;
                }
                if (!this.d_allMapCountries.containsKey(l_args.get("country_name_to").get(0))) {
                    this.d_view.invalidCountry(l_args.get("country_name_to").get(0));
                    return false;
                }
                l_order = new AdvanceModel(this.d_allMapCountries.get(l_args.get("country_name_from").get(0)),
                        this.d_allMapCountries.get(l_args.get("country_name_to").get(0)),
                        Integer.parseInt(l_args.get("armies_num").get(0)),
                        this, this.d_view, p_args);
                this.addOrder(l_order);
                break;
            case "bomb":

                // check if the player has a card to issue this order
                if (this.noOfCards(p_args[0]) == 0) {
                    d_view.noCardAvailable();
                    return false;
                } else {
                    this.removeCard(p_args[0]);
                }

                // if(this.d_orderList.contains(new DeployModel(CommandsParser.getArguments(p_args), this, this.d_view))) {
                l_order = new BombModel(this, this.d_allMapCountries.get(l_args.get("target_country").get(0)), p_args);
                this.addOrder(l_order);
                break;
            case "negotiate":

                // check if the player has a card to issue this order
                if (this.noOfCards(p_args[0]) == 0) {
                    d_view.noCardAvailable();
                    return false;
                } else {
                    this.removeCard(p_args[0]);
                }

                String l_targetPlayerName = CommandsParser.getArguments(p_args).get("target_player").get(0);
                if (l_targetPlayerName.equals(this.d_name)) {
                    this.d_view.selfNegotiationNotPossible();
                    return false;
                }
                if (!this.d_allGamePlayers.containsKey(l_targetPlayerName)) {
                    this.d_view.InvalidPlayer(l_targetPlayerName);
                    return false;
                }
                l_order = new NegotiateModel(this, this.d_allGamePlayers.get(l_targetPlayerName), p_args);
                this.addOrder(l_order);
                break;
            case "deploy":
                // validate that the number of reinforcements is a valid number
                if (!l_args.get("reinforcements_num").get(0).matches("[-+]?[0-9]*\\.?[0-9]+")) {
                    this.d_view.InvalidNumber(l_args);
                    return false;
                }

                // handle if the player has enough reinforcements to deploy
                int l_currentReinforcements = this.getReinforcements();
                int l_requestedReinforcements = Integer.parseInt(l_args.get("reinforcements_num").get(0));

                if (l_requestedReinforcements > l_currentReinforcements) {
                    this.d_view.NotEnoughReinforcements(l_args, this.getReinforcements());
                    return false; // impossible command
                }

                l_order = new DeployModel(p_args, this, this.d_view);
                this.addOrder(l_order);

                this.setReinforcements(this.getReinforcements() - (int) (Float.parseFloat(CommandsParser.getArguments(p_args).get("reinforcements_num").get(0))));
                break;
            case "blockade":

                // check if the player has a card to issue this order
                if (this.noOfCards(p_args[0]) == 0) {
                    d_view.noCardAvailable();
                    return false;
                } else {
                    this.removeCard(p_args[0]);
                }

                l_order = new BlockadeModel(this, d_allGamePlayers.get("Neutral"),
                        this.d_allMapCountries.get(l_args.get("country_name").get(0)), p_args);
                this.addOrder(l_order);
                break;
            case "airlift":

                // check if the player has a card to issue this order
                if (this.noOfCards(p_args[0]) == 0) {
                    d_view.noCardAvailable();
                    return false;
                } else {
                    this.removeCard(p_args[0]);
                }

                if (!this.d_allMapCountries.containsKey(l_args.get("country_name_from").get(0))) {
                    this.d_view.invalidCountry(l_args.get("country_name_from").get(0));
                    return false;
                }
                if (!this.d_allMapCountries.containsKey(l_args.get("country_name_to").get(0))) {
                    this.d_view.invalidCountry(l_args.get("country_name_to").get(0));
                    return false;
                }
                l_order = new AirliftModel(this.d_allMapCountries.get(l_args.get("country_name_from").get(0)),
                        this.d_allMapCountries.get(l_args.get("country_name_to").get(0)),
                        Integer.parseInt(l_args.get("armies_num").get(0)),
                        this, this.d_view, p_args);
                this.addOrder(l_order);
                break;
            default:
                this.d_view.invalidOrder();
                return false;
        }
        return true;
    }

    /**
     * Takes the next order from the player's list of orders
     *
     * @return next order from the list of the orders
     */
    public OrderModel nextOrder() {
        OrderModel l_order = this.d_orderList.peek();
        this.d_orderList.poll();
        return l_order;
    }

    public PlayerView getView() {
        return this.d_view;
    }

    /**
     * Assigns a randomly generated card if the player is eligible to get one
     */
    public String assignCards() {
        if (this.d_eligibleForCard) {
            Random l_cardGenerator = new Random();
            Object[] l_cardNames = this.d_cards.keySet().toArray();
            String l_cardName = (String) l_cardNames[l_cardGenerator.nextInt(l_cardNames.length)];
            this.d_cards.put(l_cardName, this.d_cards.get(l_cardName) + 1);
            this.d_eligibleForCard = false;
            return l_cardName;
        }
        return null;
    }

    /**
     * Removes the specific type of card after using it
     *
     * @param p_cardType Type of card
     */
    public void removeCard(String p_cardType) {
        if (this.d_cards.get(p_cardType) > 0)
            this.d_cards.put(p_cardType, this.d_cards.get(p_cardType) - 1);
    }

    /**
     * Assigns a specific cards for testing purposes
     *
     * @param p_cardType Type of card
     */
    public void assignSpecificCard(String p_cardType) {
        this.d_cards.put(p_cardType, this.d_cards.get(p_cardType) + 1);
    }

    /**
     * Accessor for the cards
     *
     * @return Cards
     */
    public HashMap<String, Integer> getCards() {
        return d_cards;
    }
}
