import Controller.GameEngineController;

public class Main {
    public static void main(String[] args) {
        GameEngineController gameEngine = new GameEngineController();

        gameEngine.addPlayer("Mazen");
        gameEngine.addPlayer("Aman");
        gameEngine.addPlayer("Akshat");

        gameEngine.assignCountries();
        gameEngine.assignReinforcements();

        try{
            gameEngine.removePlayer("Mazen");
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
