import Controller.GameEngineController;
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

        GameEngineController l_gameEngineController = new GameEngineController();
        l_gameEngineController.run();
    }
}
