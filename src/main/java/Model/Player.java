package Model;

import EventListener.Observable;
import States.Phase;
import Strategy.Strategy;
import View.PlayerView;

import java.io.Serializable;
import java.util.*;

/**
 * Issues the order and returns the next order
 * Maintains the HashMap of the countries, the reinforcements it owns for the current turn
 * and the armies that this player owns in each country
 */
public class Player extends Observable implements Serializable {
    private static final long serialversionUID = 129348938L;
    /**
     * Object of the playerview
     */
    private final PlayerView d_view;
    /**
     * Hashmap of the countries owned by the player
     */
    private final HashMap<String, CountryModel> d_myCountries;
    /**
     * Hashmap of the armies
     */
    private final HashMap<Integer, Integer> d_armies;
    /**
     * Queue of the orders issued by the player
     */
    private final Queue<OrderModel> d_orderQueue;
    /**
     * Hashmap of the active negotiators
     */
    private final HashMap<String, Player> d_activeNegotiators;
    /**
     * Hashmap of the cards
     */
    private final HashMap<String, Integer> d_cards;
    /**
     * Name of the player
     */
    private String d_name;
    /**
     * Number of reinforcements
     */
    private int d_reinforcements;
    /**
     * Boolean for the eligibility of the card
     */
    private boolean d_eligibleForCard;

    /**
     * Object of the phase- current phase
     */
    private Phase d_currentPhase;
    /**
     * Array of the command arguments
     */
    private String[] d_args;

    private Strategy d_strategy;


    /**
     * Initialises the name of the player, reinforcements, orders, countries and PlayerView
     *
     * @param p_name Name of the player
     * @param p_view Object of the PlayerView
     */
    public Player(String p_name, PlayerView p_view) {
        this.setName(p_name);
        this.d_reinforcements = 0;
        this.d_orderQueue = new ArrayDeque<>();
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
     * Accessor for the strategy of the player
     *
     * @return Strategy of the player
     */
    public Strategy getStrategy() {
        return d_strategy;
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
        this.d_orderQueue.add(p_order);
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
        OrderModel l_order = this.d_strategy.createOrder();

        if (l_order != null) {
            this.addOrder(l_order);
            return true;
        }

        return false;
    }

    /**
     * Takes the next order from the player's list of orders
     *
     * @return next order from the list of the orders
     */
    public OrderModel nextOrder() {
        OrderModel l_order = this.d_orderQueue.peek();
        this.d_orderQueue.poll();
        return l_order;
    }

    /**
     * Accessor for the view
     *
     * @return object of the playerview
     */
    public PlayerView getView() {
        return this.d_view;
    }

    /**
     * Assigns a randomly generated card if the player is eligible to get one
     *
     * @return name of the card
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
     *
     * @param p_currentPhase Object of the current phase
     */
    public void setPhase(Phase p_currentPhase) {
        this.d_currentPhase = p_currentPhase;
    }

    /**
     * Mutator for the command
     *
     * @param p_args Array of the command arguments
     */
    public void setCommand(String[] p_args) {
        this.d_args = p_args;
    }

    /**
     * Accessor for the command
     */
    public String[] getCommand() {
        return this.d_args;
    }

    /**
     * Accessor for the current phase
     */
    public Phase getCurrentPhase() {
        return d_currentPhase;
    }

    /**
     * Mutator for this player's strategy
     *
     * @param p_strategy the strategy that the player will follow
     */
    public void setStrategy(Strategy p_strategy) {
        this.d_strategy = p_strategy;
    }

    /**
     * @return
     */
    public OrderModel getLastIssuedOrder() {
        List<OrderModel> l_ordersList = new ArrayList<>(this.d_orderQueue);
        return l_ordersList.get(l_ordersList.size() - 1);
    }
}
