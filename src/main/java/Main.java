import Controller.GameEngine;
import Utils.CommandsParser;

/**
 * Driver class for the Warzone
 */
public class Main {
    /**
     * Entry function
     *
     * @param args Not used
     */
    public static void main(String[] args) {
        CommandsParser.parseJson();

        GameEngine l_gameEngine = new GameEngine();
        l_gameEngine.run();
    }
}
