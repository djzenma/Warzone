package States;

import Controller.GameEngine;

public class GamePlayPhase extends Phase {

    public GamePlayPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    @Override
    public void startup() {
        d_gameEngine.setPhase(new Startup(d_gameEngine));
    }

    @Override
    public void assignReinforcements() {
        d_gameEngine.setPhase(new AssignReinforcements(d_gameEngine));
    }

    @Override
    public boolean issueOrders() {
        d_gameEngine.setPhase(new IssueOrder(d_gameEngine));
        return false;
    }

    @Override
    public boolean executeOrders() {
        d_gameEngine.setPhase(new ExecuteOrders(d_gameEngine));
        return false;
    }

    @Override
    public void showMap() {
        this.d_gameEngine.d_gamePlayView.showMap(this.d_gameEngine.d_mapModel.getContinents(), this.d_gameEngine.d_mapModel.getCountries());
    }

}
