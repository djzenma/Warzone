import Model.Orders.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for the all the orders test classes
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdvanceModelTest.class,
        AirliftModelTest.class,
        BlockadeModelTest.class,
        BombModelTest.class,
        DeployModelTest.class,
        NegotiateModelTest.class,
})
public class OrdersTestSuite {
}
