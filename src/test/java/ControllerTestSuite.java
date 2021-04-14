import Controller.GamePlayControllerTest;
import Controller.TournamentControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for the all the controller test classes
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GamePlayControllerTest.class,
        TournamentControllerTest.class
})
public class ControllerTestSuite {
}
