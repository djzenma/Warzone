package ObserverPattern;

import Model.PlayerModel;

public class LogEntryBuffer extends Observable {
    private final String[] d_commandArgs;
    private PlayerModel d_currentPlayer = null;
    private final String d_phase;
    private boolean d_isExec = false;

    public LogEntryBuffer(String[] p_commandArgs, String p_phase) {
        this.d_commandArgs = p_commandArgs;
        this.d_phase = p_phase;
    }

    public LogEntryBuffer(boolean p_isExec, String[] p_commandArgs, PlayerModel p_currentPlayer, String p_phase) {
        this.d_commandArgs = p_commandArgs;
        this.d_currentPlayer = p_currentPlayer;
        this.d_phase = p_phase;
        this.d_isExec = p_isExec;
    }

    public String[] getCommandArgs() {
        return this.d_commandArgs;
    }

    public PlayerModel getCurrentPlayer() {
        return this.d_currentPlayer;
    }

    public String getPhase() {
        return this.d_phase;
    }

    public boolean getIsExec() {
        return this.d_isExec;
    }
}
