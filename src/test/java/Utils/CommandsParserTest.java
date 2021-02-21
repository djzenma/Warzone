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

    /**
     * Tests whether the commands are valid or not
     */
    @Test
    public void isValidCommand() {
        // valid
        String[] l_cmd1 = {"gameplayer", "add", "Aman", "remove", "Adeetya"};
        String[] l_cmd2 = {"gameplayer", "add", "Aman"};
        String[] l_cmd3 = {"assigncountries"};
        String[] l_cmd4 = {"pass"};
        String[] l_cmd5 = {"deploy", "canada", "5"};
        String[] l_cmd6 = {"gameplayer", "add", "Mazen", "add", "Aman"};
        assertTrue(CommandsParser.isValidCommand(l_cmd1));
        assertTrue(CommandsParser.isValidCommand(l_cmd2));
        assertTrue(CommandsParser.isValidCommand(l_cmd3));
        assertTrue(CommandsParser.isValidCommand(l_cmd4));
        assertTrue(CommandsParser.isValidCommand(l_cmd5));
        assertTrue(CommandsParser.isValidCommand(l_cmd6));

        // invalidK
        String[] l_cmd10 = {"gameplayer", "add"};
        String[] l_cmd11 = {"assigncountries", "remove"};
        String[] l_cmd12 = {"pass", "add"};
        String[] l_cmd13 = {"deploy", "5"};
        String[] l_cmd14 = {"gameplayer", "add", "Aman", "remove"};
        String[] l_cmd15 = {"gameplayer", "add", "Mazen", "Aman"};
        String[] l_cmd16 = {"dsaifbsafa", "afsnsajbfaf"};
        String[] l_cmd17 = {"gameplayer", "Adeetya", "Adeetya", "remove", "Aman"};
        assertFalse(CommandsParser.isValidCommand(l_cmd10));
        assertFalse(CommandsParser.isValidCommand(l_cmd11));
        assertFalse(CommandsParser.isValidCommand(l_cmd12));
        assertFalse(CommandsParser.isValidCommand(l_cmd13));
        assertFalse(CommandsParser.isValidCommand(l_cmd14));
        assertFalse(CommandsParser.isValidCommand(l_cmd15));
        assertFalse(CommandsParser.isValidCommand(l_cmd16));
        assertFalse(CommandsParser.isValidCommand(l_cmd17));
    }

    /**
     * Tests whether the MapEditor commands are valid or not
     */
    @Test
    public void isMapEditorCommandValid() {

        //MapEditor valid commands
        String[] mCmd1 = {"editcontinent", "add", "Australia", "5", "remove", "Europe"};
        String[] mCmd2 = {"editcountry", "add", "Australia", "Australia", "remove", "Nepal"};
        String[] mCmd3 = {"editneighbor", "add", "India", "Pakistan", "remove", "Russia", "Spain"};
        String[] mCmd4 = {"savemap", "solar.map"};
        String[] mCmd5 = {"showmap"};
        String[] mCmd6 = {"validatemap"};
        String[] mCmd7 = {"editcontinent", "add", "Australia", "5", "remove", "Europe", "add", "South America", "3"};
        String[] mCmd8 = {"editcountry", "add", "Australia", "Australia", "remove", "Nepal", "add", "India", "Asia"};
        String[] mCmd9 = {"editneighbor", "add", "India", "Pakistan"};
        String[] mCmd10 = {"editcountry", "remove", "Nepal"};
        String[] mCmd11 = {"editcontinent", "add", "Australia", "5", "add", "Europe", "5", "remove", "North America"};
        String[] mCmd12 = {"editmap", "file.map"};
        assertTrue(CommandsParser.isValidCommand(mCmd1));
        assertTrue(CommandsParser.isValidCommand(mCmd2));
        assertTrue(CommandsParser.isValidCommand(mCmd3));
        assertTrue(CommandsParser.isValidCommand(mCmd4));
        assertTrue(CommandsParser.isValidCommand(mCmd5));
        assertTrue(CommandsParser.isValidCommand(mCmd6));
        assertTrue(CommandsParser.isValidCommand(mCmd7));
        assertTrue(CommandsParser.isValidCommand(mCmd8));
        assertTrue(CommandsParser.isValidCommand(mCmd9));
        assertTrue(CommandsParser.isValidCommand(mCmd10));
        assertTrue(CommandsParser.isValidCommand(mCmd11));
        assertTrue(CommandsParser.isValidCommand(mCmd12));

        //MapEditor invalid commands
        String[] mInCmd1 = {"editcontinent", "add", "5", "remove", "Europe"};
        String[] mInCmd2 = {"editcountry", "India", "Asia", "remove", "Nepal"};
        String[] mInCmd3 = {"editneighbor", "add", "India", "remove", "Russia", "Spain"};
        String[] mInCmd4 = {"savemap"};
        String[] mInCmd5 = {"showmap", "file.map"};
        String[] mInCmd6 = {"validatemap", "file.map"};
        String[] mInCmd7 = {"editcontinent", "add", "Australia", "5", "remove", "Europe", "add"};
        String[] mInCmd8 = {"editcountry", "add", "Australia", "Australia", "remove", "add", "India", "Asia"};
        String[] mInCmd9 = {"editneighbor", "India", "Pakistan"};
        String[] mInCmd10 = {"editcountry", "add", "countryname"};
        String[] mInCmd11 = {"editcontinent", "add", "Australia", "add", "Europe", "5", "remove", "North America"};
        String[] mInCmd12 = {"editmap"};
        String[] mInCmd13 = {"editcontinent"};

        assertFalse((CommandsParser.isValidCommand(mInCmd1)));
        assertFalse((CommandsParser.isValidCommand(mInCmd2)));
        assertFalse((CommandsParser.isValidCommand(mInCmd3)));
        assertFalse((CommandsParser.isValidCommand(mInCmd4)));
        assertFalse((CommandsParser.isValidCommand(mInCmd5)));
        assertFalse((CommandsParser.isValidCommand(mInCmd6)));
        assertFalse((CommandsParser.isValidCommand(mInCmd7)));
        assertFalse((CommandsParser.isValidCommand(mInCmd8)));
        assertFalse((CommandsParser.isValidCommand(mInCmd9)));
        assertFalse((CommandsParser.isValidCommand(mInCmd10)));
        assertFalse((CommandsParser.isValidCommand(mInCmd11)));
        assertFalse((CommandsParser.isValidCommand(mInCmd12)));
        assertFalse((CommandsParser.isValidCommand(mInCmd13)));
    }

    /**
     * Tests whether the arguments of the command are equal to expected values
     */
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