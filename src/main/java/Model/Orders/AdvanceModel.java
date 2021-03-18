package Model.Orders;

import Model.CountryModel;
import Model.OrderModel;
import Model.PlayerModel;

import java.util.HashMap;

public class AdvanceModel extends OrderModel {

    private CountryModel d_sourceCountry;
    private CountryModel d_targetCountry;

    private int d_numArmies;

    /**
     * Constructor for the AdvanceModel
     */
    public AdvanceModel(CountryModel p_sourceCountry, CountryModel p_targetCountry, int p_numArmies, PlayerModel p_currentPlayer) {
        super("advance");
        this.d_sourceCountry = p_sourceCountry;
        this.d_targetCountry = p_targetCountry;
        this.d_numArmies = p_numArmies;
        setCurrentPlayer(p_currentPlayer);
    }

    /**
     * Executes the advance command by placing the armies in the specified country
     *
     * @param p_countries HashMap of the countries
     */
    @Override
    public void execute(HashMap<String, CountryModel> p_countries) {
        if (this.getCurrentPlayer().getCountries().contains(this.d_targetCountry)) {
            this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - this.d_numArmies);
            this.d_targetCountry.setArmies(this.d_targetCountry.getArmies() + this.d_numArmies);
            p_countries.put(this.d_targetCountry.getName(), this.d_targetCountry);
        } else {
            boolean l_isNeighbor = false;
            for (CountryModel l_country : this.getCurrentPlayer().getCountries()) {
                if (l_country.getNeighbors().containsKey(this.d_targetCountry.getName())) {
                    l_isNeighbor = true;
                    break;
                }
            }
            if (l_isNeighbor) {
                //60% of attacking armies can kill while defending armies can kill 70% of attacking armies.
                int l_remainingAttackers, l_remainingDefenders;
                l_remainingAttackers = (int) Math.round(0.3 * this.d_targetCountry.getArmies());
                l_remainingDefenders = (int) Math.round(0.4 * this.d_sourceCountry.getArmies());
                if (l_remainingDefenders == 0) {
                    this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - this.d_numArmies);
                    this.d_targetCountry.setArmies(l_remainingAttackers);
                    this.getCurrentPlayer().addCountry(this.d_targetCountry);
                    //TODO check if needed:-
                    p_countries.put(this.d_targetCountry.getName(), this.d_targetCountry);
                    p_countries.put(this.d_sourceCountry.getName(), this.d_sourceCountry);
                } else {
                    this.d_sourceCountry.setArmies(this.d_sourceCountry.getArmies() - this.d_numArmies);
                    this.d_targetCountry.setArmies(l_remainingDefenders);
                    //TODO check if needed:-
                    p_countries.put(this.d_targetCountry.getName(), this.d_targetCountry);
                    p_countries.put(this.d_sourceCountry.getName(), this.d_sourceCountry);
                }
            }
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
