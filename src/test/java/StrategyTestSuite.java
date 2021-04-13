import States.StartupTest;
import Strategy.AggressiveStrategyTest;
import Strategy.BenevolentStrategyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for the all the states test classes
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AggressiveStrategyTest.class,
        BenevolentStrategyTest.class
})
public class StrategyTestSuite {
}
