package States;

import Controller.GameEngineController;
import Model.PlayerModel;
import ObserverPattern.LogEntryBuffer;
import Utils.CommandsParser;

public class IssueOrder extends GamePlayPhase {

    public IssueOrder(GameEngineController p_gameEngineController) {
        super(p_gameEngineController);
    }

    /**
     * Loops over each player to get the orders from them
     *
     * @return false if the player does not have any other orders to issue; otherwise true
     */
    @Override
    public boolean issueOrders() {
        boolean l_isValidOrder;
        boolean l_moveToNextPhase = true;
        String[] l_args = null;

        for (PlayerModel l_player : this.d_gameEngineController.d_gamePlayModel.getPlayers().values()) {
            l_player.flushActiveNegotiators();
            String l_cardName = l_player.assignCards();
            triggerEvent(l_player, l_cardName);
            this.d_gameEngineController.d_gamePlayView.currentPlayer(l_player);
            l_isValidOrder = false;
            while (!l_isValidOrder) {
                l_args = this.d_gameEngineController.d_gamePlayView.takeCommand();

                // if the command is showmap
                if (CommandsParser.isShowMap(l_args)) {
                    // TODO: this.d_mapModel.getContinents(), this.d_mapModel.getCountries() ??
                    d_gameEngineController.d_currentPhase.showMap();
                }

                // if the command is showcards
                if (CommandsParser.isShowCards(l_args)) {
                    l_player.getView().showCards(l_player.getCards());
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

    private void triggerEvent(PlayerModel p_currentPlayer, String l_cardName) {
        if (l_cardName != null) {
            LogEntryBuffer l_entryBuffer = new LogEntryBuffer(p_currentPlayer, l_cardName, "Issue Cards");
            notifyObservers(l_entryBuffer);
        }
    }

    @Override
    public void next() {
        d_gameEngineController.setPhase(new ExecuteOrders(d_gameEngineController));
    }
}
