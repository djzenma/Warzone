package Utils;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class CommandsParserTest {
    @BeforeClass
    public static void init() {
        CommandsParser.parseJson();
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

    @Test
    public void getArguments() {
        HashMap<String, List<String>> l_correct;

        l_correct = new HashMap<>();
        String[] cmd1 = {"gameplayer", "add", "Aman", "remove", "Mazen", "add", "Shivangi"};
        l_correct.put("add", new ArrayList<>(Arrays.asList("Aman", "Shivangi")));
        l_correct.put("remove", new ArrayList<>(Arrays.asList("Mazen")));
        assertEquals(l_correct, CommandsParser.getArguments(cmd1));

        l_correct = new HashMap<>();
        String[] cmd2 = {"gameplayer", "add", "Aman", "add", "Adeetya"};
        l_correct.put("add", new ArrayList<>(Arrays.asList("Aman", "Adeetya")));
        assertEquals(l_correct, CommandsParser.getArguments(cmd2));
    }
}