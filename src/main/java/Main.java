import Controller.GameEngineController;

public class Main {
    public static void main(String[] args) {
        GameEngineController gameEngine = new GameEngineController();

        gameEngine.addPlayer("Aman");
        gameEngine.addPlayer("Mazen");
        gameEngine.addPlayer("Akshat");

        gameEngine.assignCountries();

        gameEngine.removePlayer("Aman");
    }
}
