import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for the all the test classes
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdapterTestSuite.class,
        ControllerTestSuite.class,
        ModelTestSuite.class,
        OrdersTestSuite.class,
        StatesTestSuite.class,
        StrategyTestSuite.class,
        UtilsTestSuite.class,
})
public class AllTestSuite {
}
