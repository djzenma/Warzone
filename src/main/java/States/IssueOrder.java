package States;

import Controller.GameEngine;
import EventListener.LogEntryBuffer;
import Model.OrderModel;
import Model.Orders.*;
import Model.Player;
import Utils.CommandsParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Issues the orders added by the players
 * It extends the gameplay phase
 */
public class IssueOrder extends GamePlayPhase {

    /**
     * Constructor
     *
     * @param p_gameEngine Object of the gameengine
     */
    public IssueOrder(GameEngine p_gameEngine) {
        super(p_gameEngine);
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

        Object[] l_players = this.d_gameEngine.d_gamePlayModel.getPlayers().values().toArray();

        while (d_gameEngine.d_gamePlayModel.d_curPlayerNum < l_players.length) {

            Player l_player = (Player) l_players[d_gameEngine.d_gamePlayModel.d_curPlayerNum];

            if (l_player.getName().equals("Neutral")) {
                d_gameEngine.d_gamePlayModel.d_curPlayerNum++;
                continue;
            }

            l_player.flushActiveNegotiators();
            String l_cardName = l_player.assignCards();
            triggerEvent(l_player, l_cardName);
            this.d_gameEngine.d_gamePlayView.currentPlayer(l_player);
            l_isValidOrder = false;

            while (!l_isValidOrder) {
                l_player.setPhase(d_gameEngine.d_currentPhase);
                l_isValidOrder = l_player.issueOrder();
            }

            // TODO:
            /*
            if(CommandsParser.isLoadGame(new String[]{l_player.getLastIssuedOrder().getCmdName()})) {
                d_gameEngine.reset(d_gameEngine.d_currentPhase.loadGame(l_player.getLastIssuedOrder().getCmd()));
                d_gameEngine.setPhase(new IssueOrder(d_gameEngine));
                //l_moveToNextPhase = false;
            }*/

            // if the player issued an order
            if (!CommandsParser.isPass(l_player.getLastIssuedOrder().getCmdName()))
                l_moveToNextPhase = false;

            this.d_gameEngine.d_gamePlayModel.d_curPlayerNum++;
        }
        this.d_gameEngine.d_gamePlayModel.d_curPlayerNum = 0;

        return !l_moveToNextPhase;
    }

    /**
     * Deploys the armies on the countries
     *
     * @param p_args   Array of the command arguments
     * @param p_player Object of the player
     * @return true if the order is valid; otherwise false
     */
    @Override
    public OrderModel deploy(String[] p_args, Player p_player) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);

        if (!this.d_gameEngine.d_gamePlayModel.getCountries().containsKey(l_args.get("country_name").get(0))) {
            p_player.getView().countryInexistant(l_args.get("country_name").get(0));
            return null;
        }

        // validate that the number of reinforcements is a valid number
        if (!l_args.get("reinforcements_num").get(0).matches("[-+]?[0-9]*\\.?[0-9]+")) {
            p_player.getView().invalidNumber(l_args);
            return null;
        }

        // handle if the player has enough reinforcements to deploy
        int l_currentReinforcements = p_player.getReinforcements();
        int l_requestedReinforcements = Integer.parseInt(l_args.get("reinforcements_num").get(0));

        if (l_requestedReinforcements > l_currentReinforcements) {
            p_player.getView().notEnoughReinforcements(l_args, p_player.getReinforcements());
            return null; // impossible command
        }

        OrderModel l_order = new DeployModel(p_args, p_player, p_player.getView());

        p_player.setReinforcements(p_player.getReinforcements() - (int) (Float.parseFloat(CommandsParser.getArguments(p_args).get("reinforcements_num").get(0))));
        return l_order;
    }

    /**
     * Advances the armies on the countries
     *
     * @param p_args   Array of the command arguments
     * @param p_player Object of the player
     * @return true if the order is valid; otherwise false
     */
    @Override
    public OrderModel advance(String[] p_args, Player p_player) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);

        if (!this.d_gameEngine.d_gamePlayModel.getCountries().containsKey(l_args.get("country_name_from").get(0))) {
            p_player.getView().countryInexistant(l_args.get("country_name_from").get(0));
            return null;
        }
        if (!this.d_gameEngine.d_gamePlayModel.getCountries().containsKey(l_args.get("country_name_to").get(0))) {
            p_player.getView().countryInexistant(l_args.get("country_name_to").get(0));
            return null;
        }

        return new AdvanceModel(
                this.d_gameEngine.d_gamePlayModel.getCountries().get(l_args.get("country_name_from").get(0)),
                this.d_gameEngine.d_gamePlayModel.getCountries().get(l_args.get("country_name_to").get(0)),
                Integer.parseInt(l_args.get("armies_num").get(0)),
                p_player,
                p_player.getView(),
                p_args);
    }

    /**
     * Bombs the armies on the target country
     *
     * @param p_args   Array of the command arguments
     * @param p_player Object of the player
     * @return true if the order is valid; otherwise false
     */
    @Override
    public OrderModel bomb(String[] p_args, Player p_player) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);

        if (!this.d_gameEngine.d_gamePlayModel.getCountries().containsKey(l_args.get("target_country").get(0))) {
            p_player.getView().countryInexistant(l_args.get("target_country").get(0));
            return null;
        }

        // check if the player has a card to issue this order
        if (p_player.noOfCards(p_args[0]) == 0) {
            p_player.getView().noCardAvailable(p_args);
            return null;
        } else {
            p_player.removeCard(p_args[0]);
        }

        // if(this.d_orderList.contains(new DeployModel(CommandsParser.getArguments(p_args), this, this.d_view))) {
        return new BombModel(p_player,
                this.d_gameEngine.d_gamePlayModel.getCountries().get(l_args.get("target_country").get(0)),
                p_args);
    }

    /**
     * Blockade the armies on the country
     *
     * @param p_args Array of the command arguments
     * @param p_player Object of the player
     * @return true if the order is valid; otherwise false
     */
    @Override
    public OrderModel blockade(String[] p_args, Player p_player) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);

        if (!this.d_gameEngine.d_gamePlayModel.getCountries().containsKey(l_args.get("country_name").get(0))) {
            p_player.getView().countryInexistant(l_args.get("country_name").get(0));
            return null;
        }

        // check if the player has a card to issue this order
        if (p_player.noOfCards(p_args[0]) == 0) {
            p_player.getView().noCardAvailable(p_args);
            return null;
        } else {
            p_player.removeCard(p_args[0]);
        }

        return new BlockadeModel(
                p_player,
                this.d_gameEngine.d_gamePlayModel.getPlayers().get("Neutral"),
                this.d_gameEngine.d_gamePlayModel.getCountries().get(l_args.get("country_name").get(0)),
                p_args);
    }

    /**
     * Airlifts the armies on the countries
     *
     * @param p_args Array of the command arguments
     * @param p_player Object of the player
     * @return true if the order is valid; otherwise false
     */
    @Override
    public OrderModel airlift(String[] p_args, Player p_player) {
        HashMap<String, List<String>> l_args = CommandsParser.getArguments(p_args);

        if (!this.d_gameEngine.d_gamePlayModel.getCountries().containsKey(l_args.get("country_name_from").get(0))) {
            p_player.getView().countryInexistant(l_args.get("country_name_from").get(0));
            return null;
        }
        if (!this.d_gameEngine.d_gamePlayModel.getCountries().containsKey(l_args.get("country_name_to").get(0))) {
            p_player.getView().countryInexistant(l_args.get("country_name_to").get(0));
            return null;
        }

        // check if the player has a card to issue this order
        if (p_player.noOfCards(p_args[0]) == 0) {
            p_player.getView().noCardAvailable(p_args);
            return null;
        } else {
            p_player.removeCard(p_args[0]);
        }

        return new AirliftModel(
                this.d_gameEngine.d_gamePlayModel.getCountries().get(l_args.get("country_name_from").get(0)),
                this.d_gameEngine.d_gamePlayModel.getCountries().get(l_args.get("country_name_to").get(0)),
                Integer.parseInt(l_args.get("armies_num").get(0)),
                p_player,
                p_player.getView(),
                p_args);
    }

    /**
     * Negotiate with the opponents
     *
     * @param p_args Array of the command arguments
     * @param p_player Object of the player
     * @return true if the order is valid; otherwise false
     */
    @Override
    public OrderModel negotiate(String[] p_args, Player p_player) {
        // check if the player has a card to issue this order
        if (p_player.noOfCards(p_args[0]) == 0) {
            p_player.getView().noCardAvailable(p_args);
            return null;
        } else {
            p_player.removeCard(p_args[0]);
        }

        String l_targetPlayerName = CommandsParser.getArguments(p_args).get("target_player").get(0);
        if (l_targetPlayerName.equals(p_player.getName())) {
            p_player.getView().selfNegotiationNotPossible();
            return null;
        }
        if (!this.d_gameEngine.d_gamePlayModel.getPlayers().containsKey(l_targetPlayerName)) {
            p_player.getView().invalidPlayer(l_targetPlayerName);
            return null;
        }

        return new NegotiateModel(p_player,
                this.d_gameEngine.d_gamePlayModel.getPlayers().get(l_targetPlayerName),
                p_args);
    }

    /**
     * If player deployed all the armies and pass to next turn
     *
     * @param p_player Object of the player
     * @return true if the order is valid; otherwise false
     */
    @Override
    public OrderModel pass(String[] p_args, Player p_player) {
        // checks if the player is trying to pass/skip the turn
        if (p_player.getReinforcements() != 0) {
            p_player.getView().reinforcementsRemain(p_player.getReinforcements());
            return null; // impossible command
        }
        return new PassModel(p_player, p_args);
    }

    @Override
    public OrderModel loadgame(String[] p_args, Player p_player) {
        return new LoadGameModel(p_player, p_args);
    }

    /**
     * Loads the log entry buffer with the current order object
     * Notifies about the state change
     *
     * @param p_currentPlayer Object of the player
     * @param l_cardName      name of the card
     */
    private void triggerEvent(Player p_currentPlayer, String l_cardName) {
        if (l_cardName != null) {
            LogEntryBuffer l_entryBuffer = new LogEntryBuffer(p_currentPlayer, l_cardName, "Issue Cards");
            notifyObservers(l_entryBuffer);
        }
    }

    /**
     * Moves to the next phase
     */
    @Override
    public void next() {
        d_gameEngine.setPhase(new ExecuteOrders(d_gameEngine));
    }


    @Override
    public void saveGame(String[] p_args) {
        try {
            this.d_gameEngine.d_gamePlayModel.skipAssignReinforcements = true;
            serialize(p_args);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

}
