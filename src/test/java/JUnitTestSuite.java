import Controller.PlayerControllerTest;
import Model.GameEngineModelTest;
import Model.MapModelTest;
import Utils.CommandsParserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GameEngineModelTest.class,
        PlayerControllerTest.class,
        GameEngineModelTest.class,
        MapModelTest.class,
        CommandsParserTest.class})
public class JUnitTestSuite {
}
