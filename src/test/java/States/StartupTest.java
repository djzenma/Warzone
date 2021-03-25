package States;

import Controller.GameEngine;
import Controller.GamePlayController;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test the startup phase
 */
public class StartupTest {
    /**
     * Object of the gameengine
     */
    GameEngine d_gameEngine;
    /**
     * Object of the gameplay controller
     */
    GamePlayController d_gamePlayController;

    /**
     * Tests the startup phase
     */
    @Test
    public void isStartup() {
        d_gameEngine = new GameEngine();
        d_gamePlayController = new GamePlayController(d_gameEngine);
        d_gameEngine.d_currentPhase.startup();
        assertEquals("Startup", d_gameEngine.d_currentPhase.getClass().getSimpleName());
    }
}