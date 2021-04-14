package States;

import Controller.GameEngine;
import EventListener.LogEntryBuffer;
import Model.Player;

/**
 * Issues the cards
 * It extends the GamePlayPhase
 */
public class IssueCards extends GamePlayPhase {
    /**
     * Constructor
     *
     * @param p_gameEngine Object for game engine controller
     */
    public IssueCards(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * Issues the cards
     */
    @Override
    public void issueCards() {
        for (Player l_player : this.d_gameEngine.d_gamePlayModel.getPlayers().values()) {
            String l_cardName = l_player.assignCards();
            triggerEvent(l_player, l_cardName);
        }
    }

    /**
     * Moves to the next phase
     */
    @Override
    public void next() {
        d_gameEngine.setPhase(new AssignReinforcements(d_gameEngine));
    }

    /**
     * Depicts the end of the game
     */
    @Override
    public void endGame() {
        d_gameEngine.setPhase(new EndGame(d_gameEngine));
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
}
