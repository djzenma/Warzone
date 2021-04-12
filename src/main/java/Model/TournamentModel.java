package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TournamentModel implements Serializable {

    private final HashMap<String, ArrayList<String>> d_winners;
    private ArrayList<String> d_maps;
    private ArrayList<String> d_playerStrategies;
    private int d_numGames;
    private int d_maxTurns;

    public TournamentModel() {
        d_maps = new ArrayList<>();
        d_playerStrategies = new ArrayList<>();
        d_numGames = 0;
        d_maxTurns = 0;
        d_winners = new HashMap<>();
    }

    public ArrayList<String> getMaps() {
        return d_maps;
    }

    public ArrayList<String> getPlayerStrategies() {
        return d_playerStrategies;
    }

    public int getNumGames() {
        return d_numGames;
    }

    public int getMaxTurns() {
        return d_maxTurns;
    }

    public HashMap<String, ArrayList<String>> getWinners() {
        return d_winners;
    }

    private void initializeWinners() {
        for (String l_map : this.d_maps) {
            this.d_winners.put(l_map, new ArrayList<>());
        }
    }

    public void prepareTournament(HashMap<String, List<String>> p_args) {
        d_maps = (ArrayList<String>) p_args.get("M");
        d_playerStrategies = (ArrayList<String>) p_args.get("P");
        d_numGames = Integer.parseInt(p_args.get("G").get(0));
        d_maxTurns = Integer.parseInt(p_args.get("D").get(0));
        initializeWinners();
    }

    public void recordWinner(String p_map, int p_gameNum, String p_playerName) {
        d_winners.get(p_map).add(p_gameNum, p_playerName);
    }
}
