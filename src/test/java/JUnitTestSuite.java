import Model.GamePlayPhaseModelTest;
import Model.MapModelTest;
import Model.Orders.AdvanceModelTest;
import Model.Orders.BlockadeModelTest;
import Model.PlayerModelTest;
import Utils.CommandsParserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for the all the test classes
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdvanceModelTest.class,
        BlockadeModelTest.class,

        GamePlayPhaseModelTest.class,
        MapModelTest.class,
        PlayerModelTest.class,

        CommandsParserTest.class})
public class JUnitTestSuite {
}
