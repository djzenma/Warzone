import Model.GamePlayPhaseModelTest;
import Model.MapModelTest;
import Model.PlayerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for the all the test classes
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GamePlayPhaseModelTest.class,
        MapModelTest.class,
        PlayerTest.class,
})
public class ModelTestSuite {
}
