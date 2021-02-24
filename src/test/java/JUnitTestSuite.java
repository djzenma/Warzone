import Model.GameEngineModelTest;
import Model.MapModelTest;
import Model.PlayerModelTest;
import Utils.CommandsParserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GameEngineModelTest.class,
        PlayerModelTest.class,
        MapModelTest.class,
        CommandsParserTest.class})
public class JUnitTestSuite {
}
