package Controller;

import Model.GamePlayModel;
import Model.MapModel;
import States.GamePlayPhase;
import States.Phase;
import View.GamePlayView;
import View.MapView;

public class GameEngineController {
    public final GamePlayView d_gamePlayView;
    public final GamePlayModel d_gamePlayModel;
    public final MapModel d_mapModel;
    public final MapView d_mapView;

    public Phase d_currentPhase;

    public GameEngineController() {
        this.d_gamePlayView = new GamePlayView();
        this.d_gamePlayModel = new GamePlayModel();
        this.d_mapModel = new MapModel();
        this.d_mapView = new MapView();
    }

    public void setPhase(Phase p_currentPhase) {
        this.d_currentPhase = p_currentPhase;
    }

    public void run() {
        setPhase(new GamePlayPhase(this));
    }
}
