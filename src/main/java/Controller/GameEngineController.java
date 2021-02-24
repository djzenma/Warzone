package Controller;

import Model.ContinentModel;
import Model.GameEngineModel;
import Model.MapModel;
import Model.PlayerModel;
import Utils.CommandsParser;
import Utils.MapUtils;
import View.GameEngineView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Game Engine TODO::
 */
public class GameEngineController {

    private final GameEngineModel MODEL;
    private final GameEngineView VIEW;
    private final MapModel MAP_MODEL;

    /**
     * Initialises GameEngineModel, GameEngineView and MapModel
     *
     * @param p_model Object of the GameEngineModel
     * @param p_view  Object of the GameEngineView
     */
    public GameEngineController(GameEngineModel p_model, GameEngineView p_view) {
        this.MODEL = p_model;
        this.VIEW = p_view;
        this.MAP_MODEL = new MapModel();
    }

    /**
     * Takes the Command from the user
     *
     * @return Array of arguments of the command
     */
    private String[] takeCommand() {
        String[] l_args;
        do {
            l_args = VIEW.listenForStartupCommand();
            if (!CommandsParser.isValidCommand(l_args))
                VIEW.commandNotValid();
        } while (!CommandsParser.isValidCommand(l_args));

        return l_args;
    }

    /**
     * Startup Phase of the game.
     * User stays in this phase until it is ready to assign countries to the player.
     */
    public void startup() {
        boolean l_end = false;
        boolean l_isMapLoaded = false;
        String[] l_args;

        // stay in the STARTUP phase unless the user assigns countries which moves the game to the next phase
        while (!l_end) {

            // get a valid command from the user
            l_args = takeCommand();

            // if map is not yet loaded keep asking to load map first
            if(!l_isMapLoaded && !CommandsParser.isLoadMap(l_args)){
                this.VIEW.mapNotLoaded();
                continue;
            }

            try {
                // if the command entered is gameplayer
                if (CommandsParser.isGameplayer(l_args)) {

                    // get the players to be added or removed
                    HashMap<String, List<String>> l_gameplayerArgs = CommandsParser.getArguments(l_args);

                    // add all the players specified in the command
                    if(l_gameplayerArgs.get("add") != null) {
                        for (String l_player : l_gameplayerArgs.get("add"))
                            this.MODEL.addPlayer(l_player);
                    }

                    // remove all the players specified in the command
                    if(l_gameplayerArgs.get("remove") != null) {
                        for (String l_player : l_gameplayerArgs.get("remove"))
                            this.MODEL.removePlayer(l_player);
                    }
                }

                // if the command entered is assigncountries
                else if (CommandsParser.isAssignCountries(l_args)) {
                    if (MODEL.isInValidCommand()) {
                        this.VIEW.isInvalidAssignment();
                        l_end = false;
                    } else {
                        this.MODEL.assignCountries();
                        l_end = true;
                    }
                }

                // if the command entered is loadmap
                else if (CommandsParser.isLoadMap(l_args)) {
                    this.MAP_MODEL.loadOnlyValidMap(new File(new MapUtils().getMapsPath() + l_args[1]));
                    l_isMapLoaded = true;
                    this.MODEL.setContinents(new ArrayList<ContinentModel>(this.MAP_MODEL.getContinents().values()));
                    this.MODEL.setCountries(this.MAP_MODEL.getCountries());
                }

                // if the command entered is showmap
                else if (CommandsParser.isShowMap(l_args)) {
                    this.VIEW.showMap(this.MAP_MODEL.getContinents(), this.MAP_MODEL.getCountries());
                } else {
                    this.VIEW.isMapEditorCommand();
                }
            } catch (Exception l_e) {
                VIEW.exception(l_e.getMessage());
            }
        }
    }


    /**
     * Loops over each player to get the orders from them
     *
     * @return false if the player does not have any other orders to issue; otherwise true
     */
    public boolean issueOrders() {
        boolean l_isValidOrder;
        boolean l_moveToNextPhase = true;
        String[] l_args = null;

        for (PlayerModel l_player : this.MODEL.getPlayers().values()) {
            this.VIEW.currentPlayer(l_player);
            l_isValidOrder = false;
            while (!l_isValidOrder) {
                l_args = takeCommand();

                // if the command is showmap
                if (CommandsParser.isShowMap(l_args)) {
                    this.VIEW.showMap(this.MAP_MODEL.getContinents(), this.MAP_MODEL.getCountries());
                }

                // if the command is an order
                else
                    l_isValidOrder = l_player.issueOrder(l_args);
            }

            // if the player issued an order
            if (!CommandsParser.isPass(l_args))
                l_moveToNextPhase = false;
        }
        return !l_moveToNextPhase;
    }

    /**
     * Loads the Startup Phase and loops over Gameplay phase
     */
    public void run() {
        // Startup Phase
        VIEW.startupPhase();
        this.startup();

        int l_turnNumber = 1;

        // Gameplay Loop
        VIEW.gameplayPhase();
        while (true) {
            VIEW.gameplayTurnNumber(l_turnNumber);
            this.MODEL.assignReinforcements();

            while (this.issueOrders()) ;

            while (this.MODEL.executeOrders()) ;

            l_turnNumber++;
        }
    }
}
