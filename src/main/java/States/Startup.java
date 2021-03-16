package States;

import Controller.GameEngineController;
import Model.ContinentModel;
import Utils.CommandsParser;
import Utils.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Startup extends GamePlayPhase {


    public Startup(GameEngineController p_gameEngineController) {
        super(p_gameEngineController);
    }

    @Override
    public void loadMap(String[] l_args) throws Exception {
        this.d_gameEngineController.d_mapModel.loadOnlyValidMap(new File(new MapUtils().getMapsPath() + l_args[1]));
        this.d_gameEngineController.d_gamePlayModel.setContinents(new ArrayList<ContinentModel>(this.d_gameEngineController.d_mapModel.getContinents().values()));
        this.d_gameEngineController.d_gamePlayModel.setCountries(this.d_gameEngineController.d_mapModel.getCountries());
    }

    @Override
    public void showMap() {
        this.d_gameEngineController.d_gamePlayView.showMap(this.d_gameEngineController.d_mapModel.getContinents(), this.d_gameEngineController.d_mapModel.getCountries());
    }

    @Override
    public void gameplayer(String[] l_args) throws Exception {
        // get the players to be added or removed
        HashMap<String, List<String>> l_gameplayerArgs = CommandsParser.getArguments(l_args);

        // add all the players specified in the command
        if (l_gameplayerArgs.get("add") != null) {
            for (String l_player : l_gameplayerArgs.get("add"))     //TODO:: 10 Duplicate Players
                this.d_gameEngineController.d_gamePlayModel.addPlayer(l_player);
        }

        // remove all the players specified in the command
        if (l_gameplayerArgs.get("remove") != null) {
            for (String l_player : l_gameplayerArgs.get("remove"))
                this.d_gameEngineController.d_gamePlayModel.removePlayer(l_player);
        }
    }

    @Override
    public boolean assignCountries() {
        if (this.d_gameEngineController.d_gamePlayModel.isInValidCommand()) {
            this.d_gameEngineController.d_gamePlayView.isInvalidAssignment();
            return false;
        } else {
            this.d_gameEngineController.d_gamePlayModel.assignCountries();
            return true;
        }
    }

    @Override
    public void next() {
        d_gameEngineController.setPhase(new AssignReinforcements(d_gameEngineController));
    }
}
