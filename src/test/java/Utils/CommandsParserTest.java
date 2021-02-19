package Utils;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandsParserTest {
    @BeforeClass
    public static void init() {
        CommandsParser.parse();
    }

    @Test
    public void parse() {
    }

    @Test
    public void isValidCommand() {
        // valid
        String[] cmd1 = {"gameplayer", "add", "Aman", "remove", "Adeetya"};
        String[] cmd2 = {"gameplayer", "add", "Aman"};
        String[] cmd3 = {"assigncountries"};
        String[] cmd4 = {"pass"};
        String[] cmd5 = {"deploy", "canada", "5"};
        String[] cmd6 = {"gameplayer", "add", "Mazen", "add", "Aman"};
        assertTrue(CommandsParser.isValidCommand(cmd1));
        assertTrue(CommandsParser.isValidCommand(cmd2));
        assertTrue(CommandsParser.isValidCommand(cmd3));
        assertTrue(CommandsParser.isValidCommand(cmd4));
        assertTrue(CommandsParser.isValidCommand(cmd5));
        assertTrue(CommandsParser.isValidCommand(cmd6));


        // invalid
        String[] cmd10 = {"gameplayer", "add"};
        String[] cmd11 = {"assigncountries", "remove"};
        String[] cmd12 = {"pass", "add"};
        String[] cmd13 = {"deploy", "5"};
        String[] cmd14 = {"gameplayer", "add", "Aman", "remove"};
        String[] cmd15 = {"gameplayer", "add", "Mazen", "Aman"};
        String[] cmd16 = {"dsaifbsafa", "afsnsajbfaf"};
        assertFalse(CommandsParser.isValidCommand(cmd10));
        assertFalse(CommandsParser.isValidCommand(cmd11));
        assertFalse(CommandsParser.isValidCommand(cmd12));
        assertFalse(CommandsParser.isValidCommand(cmd13));
        assertFalse(CommandsParser.isValidCommand(cmd14));
        assertFalse(CommandsParser.isValidCommand(cmd15));
        assertFalse(CommandsParser.isValidCommand(cmd16));
    }
}