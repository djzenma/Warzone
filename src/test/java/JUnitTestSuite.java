import Model.GamePlayPhaseModelTest;
import Model.MapModelTest;
import Model.PlayerModelTest;
import Utils.CommandsParserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for the all the test classes
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GamePlayPhaseModelTest.class,
        PlayerModelTest.class,
        MapModelTest.class,
        CommandsParserTest.class})
public class JUnitTestSuite {
}
