package States;

import Controller.GameEngine;
import Model.ContinentModel;
import Utils.CommandsParser;
import Utils.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Startup phase
 * It extend the gameplay phase
 */
public class Startup extends GamePlayPhase {
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
        triggerEvent(l_args, "Startup Phase");
        this.d_gameEngine.d_mapModel.loadOnlyValidMap(new File(new MapUtils().getMapsPath() + l_args[1]));
        this.d_gameEngine.d_gamePlayModel.setContinents(new ArrayList<ContinentModel>(this.d_gameEngine.d_mapModel.getContinents().values()));
        this.d_gameEngine.d_gamePlayModel.setCountries(this.d_gameEngine.d_mapModel.getCountries());
    }

    /**
     * Shows the map in the startup phase
     */
    @Override
    public void showMap() {
        triggerEvent(new String[]{"showmap"}, "Startup Phase");
        this.d_gameEngine.d_gamePlayView.showMap(this.d_gameEngine.d_mapModel.getContinents(), this.d_gameEngine.d_mapModel.getCountries());
    }

    /**
     * Adds/removes the gameplayer
     *
     * @param l_args Array of the command arguments
     * @throws Exception Throws if there is some kind of exception
     */
    @Override
    public void gameplayer(String[] l_args) throws Exception {
        triggerEvent(l_args, "Startup Phase");
        // get the players to be added or removed
        HashMap<String, List<String>> l_gameplayerArgs = CommandsParser.getArguments(l_args);

        // add all the players specified in the command
        if (l_gameplayerArgs.get("add") != null) {
            //TODO:: 10 Duplicate Players
            for (String l_player : l_gameplayerArgs.get("add")) {
                if (l_player.equals("Neutral")) {
                    this.d_gameEngine.d_gamePlayView.invalidPlayerName();
                } else {
                    if (this.d_gameEngine.d_gamePlayModel.getPlayers().containsKey(l_player)) {
                        this.d_gameEngine.d_gamePlayView.duplicatePlayer(l_player);
                    } else {
                        this.d_gameEngine.d_gamePlayModel.addPlayer(l_player);
                    }
                }
            }
        }

        // remove all the players specified in the command
        if (l_gameplayerArgs.get("remove") != null) {
            for (String l_player : l_gameplayerArgs.get("remove"))
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
        triggerEvent(new String[]{"assigncountries"}, "Startup Phase");
        if (this.d_gameEngine.d_gamePlayModel.isInValidCommand()) {
            this.d_gameEngine.d_gamePlayView.isInvalidAssignment();
            return false;
        } else {
            this.d_gameEngine.d_gamePlayModel.assignCountries();
            this.d_gameEngine.d_gamePlayModel.addPlayer("Neutral");
            return true;
        }
    }

    /**
     * Moves to the next phase
     */
    @Override
    public void next() {
        d_gameEngine.setPhase(new AssignReinforcements(d_gameEngine));
    }
}
