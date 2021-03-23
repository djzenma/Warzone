package States;

import Controller.GameEngineController;
import ObserverPattern.EventListener;
import ObserverPattern.LogEntryBuffer;
import ObserverPattern.Observable;

import java.io.IOException;

public abstract class Phase extends Observable {
    protected GameEngineController d_gameEngineController;

    public Phase(GameEngineController p_gameEngineController) {
        this.d_gameEngineController = p_gameEngineController;
        attach(new EventListener());
    }

    // Map editor
    public void showMap() {
        printInvalidCommandMessage();
    }

    public void showCountries() {
        printInvalidCommandMessage();
    }

    public void showContinents() {
        printInvalidCommandMessage();
    }

    public void showCommands() {
        printInvalidCommandMessage();
    }

    public void editContinent(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    public void editCountry(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    public void editNeighbor(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    public void editMap(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    public void validateMap(String[] l_args) {
        printInvalidCommandMessage();
    }

    public void listMaps() throws IOException {
        printInvalidCommandMessage();
    }

    public boolean saveMap(String[] l_args) throws Exception {
        printInvalidCommandMessage();
        return false;
    }

    public void exit() {
        printInvalidCommandMessage();
    }


    // Gameplay
    public void startup() {
        printInvalidCommandMessage();
    }

    public boolean issueOrders() {
        printInvalidCommandMessage();
        return false;
    }

    public boolean executeOrders() {
        printInvalidCommandMessage();
        return false;
    }

    public void loadMap(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }

    public void gameplayer(String[] l_args) throws Exception {
        printInvalidCommandMessage();
    }


    public boolean assignCountries() {
        printInvalidCommandMessage();
        return false;
    }

    public void assignReinforcements() {
        printInvalidCommandMessage();
    }

    public void deploy(String[] l_args) {
        printInvalidCommandMessage();
    }

    public void advance(String[] l_args) {
        printInvalidCommandMessage();
    }

    public void pass() {
        printInvalidCommandMessage();
    }

    public void endGame() {
        printInvalidCommandMessage();
    }

    // go to next phase
    public void next() {
        printInvalidCommandMessage();
    }

    // methods common to all states
    public void printInvalidCommandMessage() {
        System.out.println("Invalid command in state "
                + this.getClass().getSimpleName());
    }

    public void triggerEvent(String[] p_command, String p_phase) {
        LogEntryBuffer l_entryBuffer = new LogEntryBuffer(p_command, p_phase);
        notifyObservers(l_entryBuffer);
    }

}
