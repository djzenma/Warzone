package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.Player;
import Utils.CommandsParser;
import View.PlayerView;

import java.util.HashMap;
import java.util.List;

/**
 * Model for Advance Command
 * It inherits the OrderModel and overwrites the abstract execute method
 */
public class AdvanceModel extends OrderModel {
    /**
     * Object of the countrymodel for source country
     */
    private final CountryModel d_sourceCountry;
    /**
     * Object of the countrymodel for target country
     */
    private final CountryModel d_targetCountry;
    /**
     * Object of the playerview
     */
    private final PlayerView d_playerView;
    /**
     * Number of the armies
     */
    private final int d_numArmies;
    /**
     * Object of the command arguments
     */
    HashMap<String, List<String>> d_args;

    /**
     * Constructor for the AdvanceModel
     *
     * @param p_targetCountry target country on which the army has to be advanced
     * @param p_sourceCountry source country from which the army has to be advanced
     * @param p_playerView    to check the validity of the command
     * @param p_numArmies     the number of armies that have to be advanced
     * @param p_currentPlayer player that advances the armies
     * @param p_args          Gets the user command
     */
    public AdvanceModel(CountryModel p_sourceCountry, CountryModel p_targetCountry, int p_numArmies, Player p_currentPlayer, PlayerView p_playerView, String[] p_args) {
        super("advance", p_currentPlayer, p_args);
        this.d_sourceCountry = p_sourceCountry;
        this.d_targetCountry = p_targetCountry;
        this.d_numArmies = p_numArmies;
        this.d_playerView = p_playerView;
        this.d_args = CommandsParser.getArguments(p_args);
        setCurrentPlayer(p_currentPlayer);
    }

    /**
     * Executes the advance command by placing the armies in the specified country
     *
     * @param p_countries HashMap of the countries
     * @return true if order is valid; otherwise false
     */
    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        if (this.d_currentPlayer.getActiveNegotiators().containsKey(this.d_targetCountry.getOwnerName())) {
            this.d_playerView.invalidAdvanceOrder(this.d_targetCountry.getOwnerName());
            return false;
        }

        // if the source country doesn't have sufficient armies
        if (this.d_numArmies > this.d_sourceCountry.getArmies()) {
            this.d_playerView.insufficientArmies(this.d_args, this.d_sourceCountry.getArmies());
            return false;
        }

        // if the source country doesn't belong to the player
        if (!this.getCurrentPlayer().getCountries().contains(this.d_sourceCountry)) {
            this.d_playerView.invalidCountry(d_currentPlayer.getName(), this.d_sourceCountry.getName());
            return false;
        }

        // check if the target is an adjacent to the source country
        boolean l_isNeighbor = this.d_sourceCountry.getNeighbors().containsKey(this.d_targetCountry.getName());

        if (!l_isNeighbor) {
            this.d_playerView.notNeighbor(this.d_args);
            return false;
        }

        triggerEvent(true);

        // normal move without attack
        if (this.getCurrentPlayer().getCountries().contains(this.d_targetCountry)) {
            this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - this.d_numArmies);
            this.d_targetCountry.setArmies(this.d_targetCountry.getArmies() + this.d_numArmies);
            p_countries.put(this.d_targetCountry.getName(), this.d_targetCountry);
            return true;
        }

        // advance (attack) enemy country
        //60% of attacking armies can kill while defending armies can kill 70% of attacking armies.
        int l_remainingAttackers, l_remainingDefenders;

        l_remainingAttackers = this.d_numArmies - (int) Math.round(0.7 * this.d_targetCountry.getArmies());
        l_remainingDefenders = this.d_targetCountry.getArmies() - (int) Math.round(0.6 * this.d_numArmies);

        l_remainingDefenders = Math.max(l_remainingDefenders, 0);
        l_remainingAttackers = Math.max(l_remainingAttackers, 0);

        if (l_remainingDefenders > this.d_targetCountry.getArmies())
            l_remainingDefenders = this.d_targetCountry.getArmies();

        // everyone died
        if (l_remainingAttackers == 0 && l_remainingDefenders == 0) {
            this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - this.d_numArmies);
            this.d_targetCountry.setArmies(0);
        }

        // attacker conquered the target country
        else if (l_remainingDefenders == 0) {
            this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - this.d_numArmies);
            this.d_targetCountry.setArmies(l_remainingAttackers);

            this.getCurrentPlayer().addCountry(this.d_targetCountry);
            this.d_targetCountry.getOwner().removeCountry(d_targetCountry);
            this.d_targetCountry.setOwner(this.d_currentPlayer);
            this.d_currentPlayer.setEligibleForCard(true);
        }
        // attacker failed to conquer
        else {
            int l_newSourceArmies;
            int l_lostAttackers = (int) Math.round(0.7 * this.d_targetCountry.getArmies());
            l_newSourceArmies = Math.max(this.d_sourceCountry.getArmies() - l_lostAttackers, 0);
            this.d_sourceCountry.setArmies(l_newSourceArmies);
            this.d_targetCountry.setArmies(l_remainingDefenders);
        }
        return true;
    }
}