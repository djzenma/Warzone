package Controller;

import Model.GameEngineModel;
import Model.PlayerModel;
import Utils.CommandsParser;
import View.GameEngineView;

import java.util.HashMap;
import java.util.List;

/**
 * Game Engine TODO::
 */
public class GameEngineController {

    private final GameEngineModel d_model;
    private final GameEngineView d_view;

    public GameEngineController(GameEngineModel p_model, GameEngineView p_view) {
        this.d_model = p_model;
        this.d_view = p_view;
    }


    public void startup() {
        boolean l_end = false;
        String[] l_args;

        // stay in the STARTUP phase unless the user assignscountries which moves the game to the next phase
        while(!l_end) {
            // get a valid command from the user
            do {
                l_args = d_view.listenForStartupCommand();
                if(!CommandsParser.isValidCommand(l_args))
                    d_view.commandNotValid();
            } while (!CommandsParser.isValidCommand(l_args));

            try {
                // if the command entered is gameplayer
                if (CommandsParser.isGameplayer(l_args)) {
                    // get the players to be added or removed
                    HashMap<String, List<String>> l_gameplayerArgs = CommandsParser.getArguments(l_args);

                    // add all the players specified in the command
                    if(l_gameplayerArgs.get("add") != null) {
                        for (String l_player : l_gameplayerArgs.get("add"))
                            this.d_model.addPlayer(l_player);
                    }

                    // remove all the players specified in the command
                    if(l_gameplayerArgs.get("remove") != null) {
                        for (String l_player : l_gameplayerArgs.get("remove"))
                            this.d_model.removePlayer(l_player);
                    }
                }

                // if the command entered is assigncountries
                else if (CommandsParser.isAssignCountries(l_args)) {
                    this.d_model.assignCountries();
                    l_end = true;
                } else {
                    this.d_view.isMapEditorCommand();
                }
            } catch (Exception l_e) {
                d_view.exception(l_e.getMessage());
            }
        }
    }


    /**
     * Loops over each player to get the orders from them
     *
     * @return false if the player does not have any other orders to issue; otherwise true
     */
    public boolean issueOrders(){
        boolean l_end = true;

        for (PlayerModel l_player : this.d_model.getPlayers().values()) {
            this.d_view.currentPlayer(l_player);
            boolean issued = l_player.issueOrder();
            // if the player issued an order
            if (issued)
                l_end = false;
        }
        return !l_end;
    }

    public void run() {
        // Startup Phase
        d_view.startupPhase();
        this.startup();

        int l_turnNumber = 1;

        // GamePlay Loop
        d_view.gameplayPhase();

        while (true) {
            d_view.gamePlayTurnNumber(l_turnNumber);
            this.d_model.assignReinforcements();

            while (this.issueOrders()) ;

            while (this.d_model.executeOrders()) ;

            l_turnNumber++;
        }
    }
}
