package States;

import Controller.GameEngineController;
import Controller.GamePlayController;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StartupTest {
    GameEngineController d_gameEngineController;
    GamePlayController d_gamePlayController;

    @Test
    public void isStartup() {
        d_gameEngineController = new GameEngineController();
        d_gamePlayController = new GamePlayController(d_gameEngineController);
        d_gameEngineController.d_currentPhase.startup();
        assertEquals("Startup", d_gameEngineController.d_currentPhase.getClass().getSimpleName());
    }
}