package States;

import Controller.GameEngineController;
import Model.OrderModel;
import Model.Orders.*;
import Model.PlayerModel;
import ObserverPattern.LogEntryBuffer;
import Utils.CommandsParser;

import java.util.HashMap;
import java.util.List;

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
            if (l_player.getName().equals("Neutral"))
                continue;

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
                else if (CommandsParser.isShowCards(l_args)) {
                    l_player.getView().showCards(l_player.getCards());
                }

                // if the command is an order
                else {
                    l_player.setCommand(l_args);
                    l_player.setPhase(d_gameEngineController.d_currentPhase);
                    l_isValidOrder = l_player.issueOrder();
                }
            }

            // if the player issued an order
            if (!CommandsParser.isPass(l_args))
                l_moveToNextPhase = false;
        }
        return !l_moveToNextPhase;
    }


    @Override
    public boolean deploy(String[] p_args, PlayerModel p_player) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);

        // validate that the number of reinforcements is a valid number
        if (!l_args.get("reinforcements_num").get(0).matches("[-+]?[0-9]*\\.?[0-9]+")) {
            p_player.getView().invalidNumber(l_args);
            return false;
        }

        // handle if the player has enough reinforcements to deploy
        int l_currentReinforcements = p_player.getReinforcements();
        int l_requestedReinforcements = Integer.parseInt(l_args.get("reinforcements_num").get(0));

        if (l_requestedReinforcements > l_currentReinforcements) {
            p_player.getView().NotEnoughReinforcements(l_args, p_player.getReinforcements());
            return false; // impossible command
        }

        OrderModel l_order = new DeployModel(p_args, p_player, p_player.getView());
        p_player.addOrder(l_order);

        p_player.setReinforcements(p_player.getReinforcements() - (int) (Float.parseFloat(CommandsParser.getArguments(p_args).get("reinforcements_num").get(0))));
        return true;
    }

    @Override
    public boolean advance(String[] p_args, PlayerModel p_player) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);

        if (!this.d_gameEngineController.d_gamePlayModel.getCountries().containsKey(l_args.get("country_name_from").get(0))) {
            p_player.getView().invalidCountry(l_args.get("country_name_from").get(0));
            return false;
        }
        if (!this.d_gameEngineController.d_gamePlayModel.getCountries().containsKey(l_args.get("country_name_to").get(0))) {
            p_player.getView().invalidCountry(l_args.get("country_name_to").get(0));
            return false;
        }

        OrderModel l_order = new AdvanceModel(
                this.d_gameEngineController.d_gamePlayModel.getCountries().get(l_args.get("country_name_from").get(0)),
                this.d_gameEngineController.d_gamePlayModel.getCountries().get(l_args.get("country_name_to").get(0)),
                Integer.parseInt(l_args.get("armies_num").get(0)),
                p_player,
                p_player.getView(),
                p_args);

        p_player.addOrder(l_order);
        return true;
    }

    @Override
    public boolean bomb(String[] p_args, PlayerModel p_player) {
        // check if the player has a card to issue this order
        if (p_player.noOfCards(p_args[0]) == 0) {
            p_player.getView().noCardAvailable();
            return false;
        } else {
            p_player.removeCard(p_args[0]);
        }

        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);

        // if(this.d_orderList.contains(new DeployModel(CommandsParser.getArguments(p_args), this, this.d_view))) {
        OrderModel l_order = new BombModel(p_player,
                this.d_gameEngineController.d_gamePlayModel.getCountries().get(l_args.get("target_country").get(0)),
                p_args);
        p_player.addOrder(l_order);
        return true;
    }

    @Override
    public boolean blockade(String[] p_args, PlayerModel p_player) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);

        // check if the player has a card to issue this order
        if (p_player.noOfCards(p_args[0]) == 0) {
            p_player.getView().noCardAvailable();
            return false;
        } else {
            p_player.removeCard(p_args[0]);
        }

        OrderModel l_order = new BlockadeModel(
                p_player,
                this.d_gameEngineController.d_gamePlayModel.getPlayers().get("Neutral"),
                this.d_gameEngineController.d_gamePlayModel.getCountries().get(l_args.get("country_name").get(0)),
                p_args);
        p_player.addOrder(l_order);
        return true;
    }

    @Override
    public boolean airlift(String[] p_args, PlayerModel p_player) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);

        // check if the player has a card to issue this order
        if (p_player.noOfCards(p_args[0]) == 0) {
            p_player.getView().noCardAvailable();
            return false;
        } else {
            p_player.removeCard(p_args[0]);
        }

        if (!this.d_gameEngineController.d_gamePlayModel.getCountries().containsKey(l_args.get("country_name_from").get(0))) {
            p_player.getView().invalidCountry(l_args.get("country_name_from").get(0));
            return false;
        }
        if (!this.d_gameEngineController.d_gamePlayModel.getCountries().containsKey(l_args.get("country_name_to").get(0))) {
            p_player.getView().invalidCountry(l_args.get("country_name_to").get(0));
            return false;
        }

        OrderModel l_order = new AirliftModel(
                this.d_gameEngineController.d_gamePlayModel.getCountries().get(l_args.get("country_name_from").get(0)),
                this.d_gameEngineController.d_gamePlayModel.getCountries().get(l_args.get("country_name_to").get(0)),
                Integer.parseInt(l_args.get("armies_num").get(0)),
                p_player,
                p_player.getView(),
                p_args);
        p_player.addOrder(l_order);
        return true;
    }

    @Override
    public boolean negotiate(String[] p_args, PlayerModel p_player) {
        // check if the player has a card to issue this order
        if (p_player.noOfCards(p_args[0]) == 0) {
            p_player.getView().noCardAvailable();
            return false;
        } else {
            p_player.removeCard(p_args[0]);
        }


        String l_targetPlayerName = CommandsParser.getArguments(p_args).get("target_player").get(0);
        if (l_targetPlayerName.equals(p_player.getName())) {
            p_player.getView().selfNegotiationNotPossible();
            return false;
        }
        if (!this.d_gameEngineController.d_gamePlayModel.getPlayers().containsKey(l_targetPlayerName)) {
            p_player.getView().invalidPlayer(l_targetPlayerName);
            return false;
        }

        OrderModel l_order = new NegotiateModel(p_player, this.d_gameEngineController.d_gamePlayModel.getPlayers().get(l_targetPlayerName), p_args);
        p_player.addOrder(l_order);
        return true;
    }

    @Override
    public boolean pass(PlayerModel p_player) {
        // checks if the player is trying to pass/skip the turn
        if (p_player.getReinforcements() != 0) {
            p_player.getView().ReinforcementsRemain(p_player.getReinforcements());
            return false; // impossible command
        }
        return true;
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
