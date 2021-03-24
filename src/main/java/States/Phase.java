package States;

import Controller.GameEngine;
import EventListener.EventListener;
import EventListener.LogEntryBuffer;
import EventListener.Observable;
import Model.Player;

import java.io.IOException;

public abstract class Phase extends Observable {
    protected GameEngine d_gameEngine;

    public Phase(GameEngine p_gameEngine) {
        this.d_gameEngine = p_gameEngine;
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

    public boolean deploy(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return false;
    }

    public boolean advance(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return false;
    }

    public boolean bomb(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return false;
    }

    public boolean blockade(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return false;
    }

    public boolean airlift(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return false;
    }

    public boolean negotiate(String[] p_args, Player p_player) {
        printInvalidCommandMessage();
        return false;
    }

    public boolean pass(Player p_player) {
        printInvalidCommandMessage();
        return false;
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
