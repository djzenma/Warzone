package Controller;

import Model.GamePlayModel;
import Model.MapModel;
import States.GamePlayPhase;
import States.Phase;
import View.GamePlayView;

public class GameEngineController {
    public final GamePlayView d_gamePlayView;
    public final GamePlayModel d_gamePlayModel;
    public final MapModel d_mapModel;

    public Phase d_currentPhase;

    public GameEngineController() {
        this.d_gamePlayView = new GamePlayView();
        this.d_gamePlayModel = new GamePlayModel();
        this.d_mapModel = new MapModel();
    }

    public void setPhase(Phase p_currentPhase) {
        this.d_currentPhase = p_currentPhase;
    }

    public void run() {
        setPhase(new GamePlayPhase(this));
    }
}
