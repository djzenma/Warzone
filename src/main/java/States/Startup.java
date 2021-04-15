package States;

import Controller.GameEngine;
import Model.ContinentModel;
import Model.Player;
import Strategy.*;
import Utils.*;
import Utils.MapUtils;
import View.PlayerView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Startup phase
 * It extend the gameplay phase
 */
public class Startup extends GamePlayPhase {
    /**
     * Constructor
     *
     * @param p_gameEngine Object for game engine controller
     */
    public Startup(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * Loads the map
     *
     * @param l_args Array of the command arguments
     * @throws Exception Throws if there is some kind of exception
     */
    @Override
    public void loadMap(String[] l_args) throws Exception {
        this.d_gameEngine.d_mapModel.loadOnlyValidMap(new File(new MapUtils().getMapsPath() + l_args[1]));
        this.d_gameEngine.d_gamePlayModel.setContinents(new ArrayList<ContinentModel>(this.d_gameEngine.d_mapModel.getContinents().values()));
        this.d_gameEngine.d_gamePlayModel.setCountries(this.d_gameEngine.d_mapModel.getCountries());
        triggerEvent(l_args, "Startup Phase");
    }

    /**
     * Shows the map in the startup phase
     */
    @Override
    public void showMap() {
        this.d_gameEngine.d_gamePlayView.showMap(this.d_gameEngine.d_mapModel.getContinents(), this.d_gameEngine.d_mapModel.getCountries());
        triggerEvent(new String[]{"showmap"}, "Startup Phase");
    }

    /**
     * Adds/removes the gameplayer
     *
     * @param l_args Array of the command arguments
     * @return true if the command is game player; otherwise false
     * @throws Exception Throws if there is some kind of exception
     */
    @Override
    public boolean gameplayer(String[] l_args) throws Exception {
        // get the players to be added or removed
        HashMap<String, List<String>> l_gameplayerArgs = CommandsParser.getArguments(l_args);

        boolean l_added = addPlayers(l_gameplayerArgs);
        if (!l_added)
            return false;

        removePlayers(l_gameplayerArgs);
        triggerEvent(l_args, "Startup Phase");
        return true;
    }

    /**
     * Adds the players specified in the command entered
     *
     * @param p_gameplayerArgs hashmap of game player arguments
     * @return true if the players are added successfully; otherwise false
     */
    private boolean addPlayers(HashMap<String, List<String>> p_gameplayerArgs) {
        // add all the players specified in the command
        if (p_gameplayerArgs.get("add") != null) {
            String l_playerName = null;

            for (int l_i = 0; l_i < p_gameplayerArgs.get("add").size(); l_i++) {
                // get name
                if (l_i % 2 == 0) {
                    l_playerName = p_gameplayerArgs.get("add").get(l_i);

                    if (l_playerName.equals("Neutral")) {
                        this.d_gameEngine.d_gamePlayView.invalidPlayerName();
                        // ignore the behaviour;
                        l_i++;
                    } else {
                        if (this.d_gameEngine.d_gamePlayModel.getPlayers().containsKey(l_playerName)) {
                            this.d_gameEngine.d_gamePlayView.duplicatePlayer(l_playerName);
                            // ignore the behaviour;
                            l_i++;
                        }
                    }
                }
                // get strategy
                else {
                    String l_strategyName = p_gameplayerArgs.get("add").get(l_i);

                    Player l_player = new Player(l_playerName, new PlayerView());
                    Strategy l_strategy = getStrategy(l_strategyName, l_player);
                    // invalid strategy
                    if (l_strategy == null) {
                        this.d_gameEngine.d_gamePlayView.invalidStrategy(l_strategyName);
                        return false;
                    } else {
                        l_player.setStrategy(l_strategy);
                        this.d_gameEngine.d_gamePlayModel.addPlayer(l_player);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Removes the players specified in the entered command
     *
     * @param p_gameplayerArgs hashmap of game player arguments
     * @throws Exception If some sort of exception occurred
     */
    private void removePlayers(HashMap<String, List<String>> p_gameplayerArgs) throws Exception {
        // remove all the players specified in the command
        if (p_gameplayerArgs.get("remove") != null) {
            for (String l_player : p_gameplayerArgs.get("remove"))
                if (l_player.equals("Neutral")) {
                    this.d_gameEngine.d_gamePlayView.invalidPlayerName();
                } else {
                    if (!this.d_gameEngine.d_gamePlayModel.getPlayers().containsKey(l_player)) {
                        this.d_gameEngine.d_gamePlayView.noPlayerFound(l_player);
                    } else {
                        this.d_gameEngine.d_gamePlayModel.removePlayer(l_player);
                    }
                }
        }
    }

    /**
     * Assign the countries to the players
     *
     * @return true if the command is valid; otherwise false
     */
    @Override
    public boolean assignCountries() {
        if (this.d_gameEngine.d_gamePlayModel.isInValidCommand()) {
            this.d_gameEngine.d_gamePlayView.isInvalidAssignment();
            return false;
        } else {
            this.d_gameEngine.d_gamePlayModel.assignCountries();

            // create the neutral player and add it to the players
            Player l_neutralPlayer = new Player("Neutral", new PlayerView());
            l_neutralPlayer.setStrategy(
                    new NeutralStrategy(l_neutralPlayer,
                            this.d_gameEngine.d_gamePlayModel.getCountries(),
                            this.d_gameEngine.d_gamePlayModel.getPlayers()));
            this.d_gameEngine.d_gamePlayModel.addPlayer(l_neutralPlayer);
            triggerEvent(new String[]{"assigncountries"}, "Startup Phase");
            return true;
        }
    }

    /**
     * Moves to the next phase
     */
    @Override
    public void next() {
        this.d_gameEngine.setPhase(new AssignReinforcements(this.d_gameEngine));
    }

    /**
     * Convert entered string strategy to its corresponding strategy object
     *
     * @param p_strategyName the strategy name
     * @param p_player       the player that will be assigned the strategy
     * @return
     */
    private Strategy getStrategy(String p_strategyName, Player p_player) {
        switch (p_strategyName.toLowerCase()) {
            case "human":
                return new HumanStrategy(p_player,
                        this.d_gameEngine.d_gamePlayModel.getCountries(),
                        this.d_gameEngine.d_gamePlayModel.getPlayers());
            case "random":
                return new RandomStrategy(p_player,
                        this.d_gameEngine.d_gamePlayModel.getCountries(),
                        this.d_gameEngine.d_gamePlayModel.getPlayers());
            case "benevolent":
                return new BenevolentStrategy(p_player,
                        this.d_gameEngine.d_gamePlayModel.getCountries(),
                        this.d_gameEngine.d_gamePlayModel.getPlayers());
            case "aggressive":
                return new AggressiveStrategy(p_player,
                        this.d_gameEngine.d_gamePlayModel.getCountries(),
                        this.d_gameEngine.d_gamePlayModel.getPlayers());
            case "cheater":
                return new CheaterStrategy(p_player,
                        this.d_gameEngine.d_gamePlayModel.getCountries(),
                        this.d_gameEngine.d_gamePlayModel.getPlayers());
            default:
                return null;
        }
    }

    /**
     * Loads the game from the serialised object file
     *
     * @param p_args arguments of the command
     * @return GameEngine object
     */
    @Override
    public GameEngine loadGame(String[] p_args) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);
        try {
            FileInputStream fileIn = new FileInputStream("checkpoint/" + l_args.get("filename").get(0) + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            d_gameEngine = (GameEngine) in.readObject();
            in.close();
            fileIn.close();
            d_gameEngine.d_gamePlayView.loadedCheckpoint();
            this.d_gameEngine.d_gamePlayModel.isLoadedGame = true;
            triggerEvent(p_args, "Startup Phase");
            return d_gameEngine;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("GameEngine class not found!");
            c.printStackTrace();
            return null;
        }
    }
}
