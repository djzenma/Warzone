package ObserverPattern;

import Model.PlayerModel;

/**
 * Maintains the information about the effect of user actions
 * It is a subclass of Observable
 */
public class LogEntryBuffer extends Observable {
    private String[] d_commandArgs = null;
    private PlayerModel d_currentPlayer;
    private String d_cardType = null;
    private final String d_phase;
    private boolean d_isExec = false;

    /**
     * Instantiates command arguments, phase
     *
     * @param p_commandArgs Array of commands
     * @param p_phase       Name of phase
     */
    public LogEntryBuffer(String[] p_commandArgs, String p_phase) {
        this.d_commandArgs = p_commandArgs;
        this.d_phase = p_phase;
    }

    /**
     * Instantiates command arguments, current player, phase
     *
     * @param p_isExec        Is execution phase or not
     * @param p_commandArgs   Array of arguments
     * @param p_currentPlayer Current player
     * @param p_phase         Name of phase
     */
    public LogEntryBuffer(boolean p_isExec, String[] p_commandArgs, PlayerModel p_currentPlayer, String p_phase) {
        this.d_commandArgs = p_commandArgs;
        this.d_currentPlayer = p_currentPlayer;
        this.d_phase = p_phase;
        this.d_isExec = p_isExec;
    }

    /**
     * Instantiates current player, phase, card type
     *
     * @param p_currentPlayer Current player
     * @param p_cardType      Card type
     * @param p_phase         Current phase
     */
    public LogEntryBuffer(PlayerModel p_currentPlayer, String p_cardType, String p_phase) {
        this.d_currentPlayer = p_currentPlayer;
        this.d_cardType = p_cardType;
        this.d_phase = p_phase;
    }

    /**
     * Accessor for command arguments
     *
     * @return Array of command arguments
     */
    public String[] getCommandArgs() {
        return this.d_commandArgs;
    }

    /**
     * Accessor for the current player
     *
     * @return current player
     */
    public PlayerModel getCurrentPlayer() {
        return this.d_currentPlayer;
    }

    /**
     * Accessor for the current phase
     *
     * @return current phase
     */
    public String getPhase() {
        return this.d_phase;
    }

    /**
     * Is execution phase or not
     *
     * @return True, if execution phase; otherwise False
     */
    public boolean getIsExec() {
        return this.d_isExec;
    }

    /**
     * Accessor for the type of card
     *
     * @return card type
     */
    public String getCardType() {
        return d_cardType;
    }
}
