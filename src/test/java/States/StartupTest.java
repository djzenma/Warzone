package States;

import Controller.GameEngine;
import Controller.GamePlayController;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StartupTest {
    GameEngine d_gameEngine;
    GamePlayController d_gamePlayController;

    @Test
    public void isStartup() {
        d_gameEngine = new GameEngine();
        d_gamePlayController = new GamePlayController(d_gameEngine);
        d_gameEngine.d_currentPhase.startup();
        assertEquals("Startup", d_gameEngine.d_currentPhase.getClass().getSimpleName());
    }
}