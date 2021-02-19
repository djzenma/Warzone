package Controller;

import Model.*;
import Utils.CommandsParser;
import View.GameEngineView;

/**
 * Game Engine TODO::
 */
public class GameEngineController {

    private GameEngineModel d_model;
    private GameEngineView d_view;

    public GameEngineController(GameEngineModel p_model) {
        this.d_model = p_model;
        this.d_view = new GameEngineView();
    }

    public String[] startup() {
        String[] l_args;
        do {
            l_args = d_view.startup();
        } while(!CommandsParser.isValidCommand(l_args));
        return l_args;
    }

    public void run(){
        // Startup Phase
        startup();

        // GamePlay Loop
        d_model.assignReinforcements();

        while(d_model.issueOrders());

        while(d_model.executeOrders());
    }
}
