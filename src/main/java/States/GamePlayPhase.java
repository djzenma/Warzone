package States;

import Controller.GameEngineController;

public class GamePlayPhase extends Phase {

    public GamePlayPhase(GameEngineController p_gameEngineController) {
        super(p_gameEngineController);
    }

    @Override
    public void startup() {
        d_gameEngineController.setPhase(new Startup(d_gameEngineController));
    }

    @Override
    public void assignReinforcements() {
        d_gameEngineController.setPhase(new AssignReinforcements(d_gameEngineController));
    }

    @Override
    public boolean issueOrders() {
        d_gameEngineController.setPhase(new IssueOrder(d_gameEngineController));
        return false;
    }

    @Override
    public boolean executeOrders() {
        d_gameEngineController.setPhase(new ExecuteOrders(d_gameEngineController));
        return false;
    }

    @Override
    public void showMap() {
        this.d_gameEngineController.d_gamePlayView.showMap(this.d_gameEngineController.d_mapModel.getContinents(), this.d_gameEngineController.d_mapModel.getCountries());
    }

}
