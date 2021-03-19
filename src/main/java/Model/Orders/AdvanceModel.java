package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;
import View.PlayerView;

import java.util.HashMap;
import java.util.List;

public class AdvanceModel extends OrderModel {

    private CountryModel d_sourceCountry;
    private CountryModel d_targetCountry;
    private final PlayerView d_playerView;
    private int d_numArmies;
    HashMap<String, List<String>> d_args;

    /**
     * Constructor for the AdvanceModel
     */
    public AdvanceModel(CountryModel p_sourceCountry, CountryModel p_targetCountry, int p_numArmies, PlayerModel p_currentPlayer, PlayerView p_playerView, HashMap<String, List<String>> p_args) {
        super("advance", p_currentPlayer);
        this.d_sourceCountry = p_sourceCountry;
        this.d_targetCountry = p_targetCountry;
        this.d_numArmies = p_numArmies;
        this.d_playerView = p_playerView;
        this.d_args = p_args;
        setCurrentPlayer(p_currentPlayer);
    }

    /**
     * Executes the advance command by placing the armies in the specified country
     *
     * @param p_countries HashMap of the countries
     * @return
     */
    @Override
    public boolean execute(HashMap<String, CountryModel> p_countries) {
        // if the source country doesn't belong to the player
        if (!this.getCurrentPlayer().getCountries().contains(this.d_sourceCountry)) {
            this.d_playerView.InvalidCountry(this.d_args);
            return false;
        }

        // check if the target is an adjacent to the source country
        boolean l_isNeighbor = this.d_sourceCountry.getNeighbors().containsKey(this.d_targetCountry.getName());

        if (!l_isNeighbor) {
            this.d_playerView.notNeighbor(this.d_args);
            return false;
        }

        // normal move without attack
        if (this.getCurrentPlayer().getCountries().contains(this.d_targetCountry)) {
            this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - this.d_numArmies);
            this.d_targetCountry.setArmies(this.d_targetCountry.getArmies() + this.d_numArmies);
            p_countries.put(this.d_targetCountry.getName(), this.d_targetCountry);
            return true;
        }

        // advance (attack) enemy country
        else {
            //60% of attacking armies can kill while defending armies can kill 70% of attacking armies.
            int l_remainingAttackers, l_remainingDefenders;
            l_remainingAttackers = (int) Math.round(0.3 * this.d_targetCountry.getArmies());
            l_remainingDefenders = (int) Math.round(0.4 * this.d_sourceCountry.getArmies());

            // everyone died
            if (l_remainingAttackers == 0 && l_remainingDefenders == 0) {
                this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - this.d_numArmies);
                this.d_targetCountry.setArmies(0);
            }
            // attacker conquered the target country
            else if (l_remainingDefenders == 0) {
                this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - this.d_numArmies);
                this.d_targetCountry.setArmies(l_remainingAttackers);
                System.out.println("Source country:" + (this.d_sourceCountry.getArmies() - this.d_numArmies));
                System.out.println("Target country armies:" + l_remainingAttackers);

                this.getCurrentPlayer().addCountry(this.d_targetCountry);
                this.d_targetCountry.getOwner().removeCountry(d_targetCountry);
                this.d_targetCountry.setOwner(this.d_currentPlayer);
                System.out.println("Target owner:" + this.d_targetCountry.getOwnerName());
                System.out.println("Source owner:" + this.d_sourceCountry.getOwnerName());
                //TODO check if needed:-
            }
            // attacker failed to conquer
            else {
                int l_originalAttackers = (int) Math.round(0.7 * this.d_targetCountry.getArmies());
                this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - l_originalAttackers);
                this.d_targetCountry.setArmies(l_remainingDefenders);
                //TODO check if needed:-
            }
            p_countries.put(this.d_targetCountry.getName(), this.d_targetCountry);
            p_countries.put(this.d_sourceCountry.getName(), this.d_sourceCountry);

            return true;
        }
    }

    public void setSourceCountry(CountryModel p_sourceCountry) {
        this.d_sourceCountry = p_sourceCountry;
    }

    public void setTargetCountry(CountryModel p_targetCountry) {
        this.d_targetCountry = p_targetCountry;
    }

    public void setNumArmies(int d_numArmies) {
        this.d_numArmies = d_numArmies;
    }

}
