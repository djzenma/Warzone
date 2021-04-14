package Model;

import java.io.Serializable;
import java.util.*;

/**
 * Model for the tournament mode
 */
public class TournamentModel implements Serializable {

    /**
     * Hashmap of the winners
     */
    private final HashMap<String, ArrayList<String>> d_winners;
    /**
     * Arraylist of maps
     */
    private ArrayList<String> d_maps;
    /**
     * Arraylist of player strategies
     */
    private ArrayList<String> d_playerStrategies;
    /**
     * Number of the games
     */
    private int d_numGames;
    /**
     * Maximum number of turns
     */
    private int d_maxTurns;

    /**
     * Instantiates maps, player strategies, number of games, maximum number of turns, winners
     */
    public TournamentModel() {
        d_maps = new ArrayList<>();
        d_playerStrategies = new ArrayList<>();
        d_numGames = 0;
        d_maxTurns = 0;
        d_winners = new HashMap<>();
    }

    /**
     * Accessor for the maps
     *
     * @return arraylist of the maps
     */
    public ArrayList<String> getMaps() {
        return d_maps;
    }

    /**
     * Accessor for the player strategies
     *
     * @return arraylist of the player strategies
     */
    public ArrayList<String> getPlayerStrategies() {
        return d_playerStrategies;
    }

    /**
     * Accessor for the number of games
     *
     * @return number of games
     */
    public int getNumGames() {
        return d_numGames;
    }

    /**
     * Accessor for the maximum number of turns
     *
     * @return maximum number of turns
     */
    public int getMaxTurns() {
        return d_maxTurns;
    }

    /**
     * Accessor for the winners hashmap
     *
     * @return winners hashmap
     */
    public HashMap<String, ArrayList<String>> getWinners() {
        return d_winners;
    }

    /**
     * Initialises the winner
     */
    private void initializeWinners() {
        for (String l_map : this.d_maps) {
            this.d_winners.put(l_map, new ArrayList<>());
        }
    }

    /**
     * Prepare tournament
     *
     * @param p_args hashmap of the command arguments
     * @throws Exception Exception of any sort
     */
    public void prepareTournament(HashMap<String, List<String>> p_args) throws Exception {
        d_maps = (ArrayList<String>) p_args.get("M");
        addPlayers(p_args.get("P"));
        d_numGames = Integer.parseInt(p_args.get("G").get(0));
        d_maxTurns = Integer.parseInt(p_args.get("D").get(0));
        initializeWinners();
        validateTournament();
    }

    /**
     * Add Players
     *
     * @param p_playerStrategies list of player strategies
     */
    private void addPlayers(List<String> p_playerStrategies) {
        for (String l_playerStrategy : p_playerStrategies) {
            if(!this.d_playerStrategies.contains(l_playerStrategy)) {
                this.d_playerStrategies.add(l_playerStrategy);
            }
        }
    }

    /**
     * Validate tournament
     *
     * @throws Exception Exception of any sort
     */
    private void validateTournament() throws Exception {
        if(d_maps.size() < 1 || d_maps.size() > 5) {
            throw new Exception("Num of maps entered must be between 1 to 5.");
        }

        if(d_playerStrategies.size() < 2 || d_playerStrategies.size() > 4) {
            throw new Exception("Num of players entered must be between 2 to 4.");
        }

        if(d_numGames < 1 || d_numGames > 5) {
            throw new Exception("Num of games entered must be between 1 to 5.");
        }

        if(d_maxTurns < 10 || d_maxTurns > 50) {
            throw new Exception("Num of maximum turns entered must be between 10 to 50.");
        }
    }

    /**
     * Records winner
     *
     * @param p_map map name
     * @param p_gameNum number of games
     * @param p_playerName name of the player
     */
    public void recordWinner(String p_map, int p_gameNum, String p_playerName) {
        d_winners.get(p_map).add(p_gameNum, p_playerName);
    }
}
