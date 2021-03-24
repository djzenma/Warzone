package Model;

import EventListener.Observable;
import States.Phase;
import View.PlayerView;

import java.util.*;

/**
 * Issues the order and returns the next order
 * Maintains the HashMap of the countries, the reinforcements it owns for the current turn
 * and the armies that this player owns in each country
 */
public class Player extends Observable {
    private final PlayerView d_view;

    private String d_name;
    private int d_reinforcements;

    private final HashMap<String, CountryModel> d_myCountries;
    private final HashMap<Integer, Integer> d_armies;
    private final Queue<OrderModel> d_orderList;
    private final HashMap<String, Player> d_activeNegotiators;
    private final HashMap<String, Integer> d_cards;
    private boolean d_eligibleForCard;

    private Phase d_currentPhase;

    private String[] d_args;


    /**
     * Initialises the name of the player, reinforcements, orders, countries and PlayerView
     *
     * @param p_name Name of the player
     * @param p_view Object of the PlayerView
     */
    public Player(String p_name, PlayerView p_view, HashMap<String, CountryModel> p_countries) {
        this.setName(p_name);
        this.d_reinforcements = 0;
        this.d_orderList = new ArrayDeque<>();
        this.d_armies = new HashMap<>();
        this.d_myCountries = new HashMap<>();
        this.d_view = p_view;
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
    public Player(String p_name, PlayerView p_view, HashMap<String, CountryModel> p_countries, HashMap<String, Player> p_players) {
        this.setName(p_name);
        this.d_reinforcements = 0;
        this.d_orderList = new ArrayDeque<>();
        this.d_armies = new HashMap<>();
        this.d_myCountries = new HashMap<>();
        this.d_view = p_view;
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
    public HashMap<String, Player> getActiveNegotiators() {
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
    public void addNegotiator(Player p_negotiator) {
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


    /**
     * Player issues an order. In this various conditions are checked and the method is recursively called until user issues a valid order
     *
     * @return False if the order is invalid; Otherwise true
     */
    public boolean issueOrder() {
        String[] l_args = this.d_args;

        switch (l_args[0]) {
            case "pass":
                return this.d_currentPhase.pass(this);

            case "advance":
                return this.d_currentPhase.advance(l_args, this);

            case "bomb":
                return this.d_currentPhase.bomb(l_args, this);

            case "negotiate":
                return this.d_currentPhase.negotiate(l_args, this);

            case "deploy":
                return this.d_currentPhase.deploy(l_args, this);

            case "blockade":
                return this.d_currentPhase.blockade(l_args, this);

            case "airlift":
                return this.d_currentPhase.airlift(l_args, this);

            default:
                this.d_view.invalidOrder();
                return false;
        }
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

    /**
     * Accessor for the view
     */
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

    /**
     * Mutator for the current Phase
     */
    public void setPhase(Phase p_currentPhase) {
        this.d_currentPhase = p_currentPhase;
    }

    /**
     * Mutator for the command
     */
    public void setCommand(String[] p_args) {
        this.d_args = p_args;
    }
}
