package Utils;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the CommandParser
 */
public class CommandsParserTest {
    /**
     * Parses the JSON
     */
    @BeforeClass
    public static void init() {
        CommandsParser.parseJson();
    }

    /**
     * Tests whether the commands are valid or not
     */
    @Test
    public void isValidCommand() {
        // valid
        String[] l_cmd1 = {"gameplayer", "add", "Aman", "Human", "remove", "Adeetya"};
        String[] l_cmd2 = {"gameplayer", "add", "Aman", "Human"};
        String[] l_cmd3 = {"assigncountries"};
        String[] l_cmd4 = {"pass"};
        String[] l_cmd5 = {"deploy", "canada", "5"};
        String[] l_cmd6 = {"gameplayer", "add", "Mazen", "Human", "add", "Aman", "Human"};
        String[] l_cmd7 = {"tournament", "M", "solar.map", "asia.map", "P", "Cheater", "Aggressive", "G", "2", "D", "10"};
        String[] l_cmd8 = {"tournament", "M", "solar.map", "asia.map", "test.map", "P", "Cheater", "Aggressive", "Random", "G", "2", "D", "10"};
        String[] l_cmd9 = {"tournament", "P", "Cheater", "Aggressive", "Random", "M", "solar.map", "asia.map", "test.map", "G", "2", "D", "10"};
        String[] l_cmd10 = {"tournament", "G", "2", "P", "Cheater", "Aggressive", "Random", "M", "solar.map", "asia.map", "test.map", "D", "10"};
        String[] l_cmd11 = {"bomb", "canada"};
        String[] l_cmd12 = {"airlift", "egypt", "canada", "5"};
        String[] l_cmd13 = {"negotiate", "Aman"};
        String[] l_cmd14 = {"advance", "egpyt", "canada", "10"};
        String[] l_cmd15 = {"blockade", "egpyt"};
        assertTrue(CommandsParser.isValidCommand(l_cmd1));
        assertTrue(CommandsParser.isValidCommand(l_cmd2));
        assertTrue(CommandsParser.isValidCommand(l_cmd3));
        assertTrue(CommandsParser.isValidCommand(l_cmd4));
        assertTrue(CommandsParser.isValidCommand(l_cmd5));
        assertTrue(CommandsParser.isValidCommand(l_cmd6));
        assertTrue(CommandsParser.isValidCommand(l_cmd7));
        assertTrue(CommandsParser.isValidCommand(l_cmd8));
        assertTrue(CommandsParser.isValidCommand(l_cmd9));
        assertTrue(CommandsParser.isValidCommand(l_cmd10));
        assertTrue(CommandsParser.isValidCommand(l_cmd11));
        assertTrue(CommandsParser.isValidCommand(l_cmd12));
        assertTrue(CommandsParser.isValidCommand(l_cmd13));
        assertTrue(CommandsParser.isValidCommand(l_cmd14));
        assertTrue(CommandsParser.isValidCommand(l_cmd15));

        // invalid
        String[] l_cmd20 = {"gameplayer", "add"};
        String[] l_cmd21 = {"assigncountries", "remove"};
        String[] l_cmd22 = {"pass", "add"};
        String[] l_cmd23 = {"deploy", "5"};
        String[] l_cmd24 = {"gameplayer", "add", "Aman", "Human", "remove"};
        String[] l_cmd25 = {"gameplayer", "add", "Mazen", "Human", "Aman"};
        String[] l_cmd26 = {"dsaifbsafa", "afsnsajbfaf"};
        String[] l_cmd27 = {"gameplayer", "Adeetya", "Adeetya", "remove", "Aman"};
        String[] l_cmd28 = {"tournament", "M", "solar.map", "asia.map" ,"P", "G", "2", "D", "10"};
        String[] l_cmd29 = {"tournament", "P", "Cheater", "G", "2", "D", "10"};
        String[] l_cmd30 = {"tournament", "M", "solar.map", "asia.map", "P", "Cheater", "D", "10"};
        String[] l_cmd31 = {"bomb", "canada", "egypt"};
        String[] l_cmd32 = {"airlift", "egypt", "canada"};
        String[] l_cmd33 = {"negotiate"};
        String[] l_cmd34 = {"advance", "egpyt", "canada", "10", "s"};
        String[] l_cmd35 = {"blockade", "egpyt", "canada"};
        assertFalse(CommandsParser.isValidCommand(l_cmd20));
        assertFalse(CommandsParser.isValidCommand(l_cmd21));
        assertFalse(CommandsParser.isValidCommand(l_cmd22));
        assertFalse(CommandsParser.isValidCommand(l_cmd23));
        assertFalse(CommandsParser.isValidCommand(l_cmd24));
        assertFalse(CommandsParser.isValidCommand(l_cmd25));
        assertFalse(CommandsParser.isValidCommand(l_cmd26));
        assertFalse(CommandsParser.isValidCommand(l_cmd27));
        assertFalse(CommandsParser.isValidCommand(l_cmd28));
        assertFalse(CommandsParser.isValidCommand(l_cmd29));
        assertFalse(CommandsParser.isValidCommand(l_cmd30));
        assertFalse(CommandsParser.isValidCommand(l_cmd31));
        assertFalse(CommandsParser.isValidCommand(l_cmd32));
        assertFalse(CommandsParser.isValidCommand(l_cmd33));
        assertFalse(CommandsParser.isValidCommand(l_cmd34));
        assertFalse(CommandsParser.isValidCommand(l_cmd35));
    }

    /**
     * Tests whether the MapEditor commands are valid or not
     */
    @Test
    public void isMapEditorCommandValid() {

        //MapEditor valid commands
        String[] l_mCmd1 = {"editcontinent", "add", "Australia", "5", "remove", "Europe"};
        String[] l_mCmd2 = {"editcountry", "add", "Australia", "Australia", "remove", "Nepal"};
        String[] l_mCmd3 = {"editneighbor", "add", "India", "Pakistan", "remove", "Russia", "Spain"};
        String[] l_mCmd4 = {"savemap", "solar.map", "Domination"};
        String[] l_mCmd5 = {"showmap"};
        String[] l_mCmd6 = {"validatemap"};
        String[] l_mCmd7 = {"editcontinent", "add", "Australia", "5", "remove", "Europe", "add", "South America", "3"};
        String[] l_mCmd8 = {"editcountry", "add", "Australia", "Australia", "remove", "Nepal", "add", "India", "Asia"};
        String[] l_mCmd9 = {"editneighbor", "add", "India", "Pakistan"};
        String[] l_mCmd10 = {"editcountry", "remove", "Nepal"};
        String[] l_mCmd11 = {"editcontinent", "add", "Australia", "5", "add", "Europe", "5", "remove", "North America"};
        String[] l_mCmd12 = {"editmap", "file.map"};
        assertTrue(CommandsParser.isValidCommand(l_mCmd1));
        assertTrue(CommandsParser.isValidCommand(l_mCmd2));
        assertTrue(CommandsParser.isValidCommand(l_mCmd3));
        assertTrue(CommandsParser.isValidCommand(l_mCmd4));
        assertTrue(CommandsParser.isValidCommand(l_mCmd5));
        assertTrue(CommandsParser.isValidCommand(l_mCmd6));
        assertTrue(CommandsParser.isValidCommand(l_mCmd7));
        assertTrue(CommandsParser.isValidCommand(l_mCmd8));
        assertTrue(CommandsParser.isValidCommand(l_mCmd9));
        assertTrue(CommandsParser.isValidCommand(l_mCmd10));
        assertTrue(CommandsParser.isValidCommand(l_mCmd11));
        assertTrue(CommandsParser.isValidCommand(l_mCmd12));

        //MapEditor invalid commands
        String[] l_mInCmd1 = {"editcontinent", "add", "5", "remove", "Europe"};
        String[] l_mInCmd2 = {"editcountry", "India", "Asia", "remove", "Nepal"};
        String[] l_mInCmd3 = {"editneighbor", "add", "India", "remove", "Russia", "Spain"};
        String[] l_mInCmd4 = {"savemap"};
        String[] l_mInCmd5 = {"showmap", "file.map"};
        String[] l_mInCmd6 = {"validatemap", "file.map"};
        String[] l_mInCmd7 = {"editcontinent", "add", "Australia", "5", "remove", "Europe", "add"};
        String[] l_mInCmd8 = {"editcountry", "add", "Australia", "Australia", "remove", "add", "India", "Asia"};
        String[] l_mInCmd9 = {"editneighbor", "India", "Pakistan"};
        String[] l_mInCmd10 = {"editcountry", "add", "countryname"};
        String[] l_mInCmd11 = {"editcontinent", "add", "Australia", "add", "Europe", "5", "remove", "North America"};
        String[] l_mInCmd12 = {"editmap"};
        String[] l_mInCmd13 = {"editcontinent"};

        assertFalse((CommandsParser.isValidCommand(l_mInCmd1)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd2)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd3)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd4)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd5)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd6)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd7)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd8)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd9)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd10)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd11)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd12)));
        assertFalse((CommandsParser.isValidCommand(l_mInCmd13)));
    }

    /**
     * Tests whether the arguments of the command are equal to expected values
     */
    @Test
    public void getArguments() {
        HashMap<String, List<String>> l_correct;

        // test named arguments
        l_correct = new HashMap<>();
        String[] l_cmd1 = {"gameplayer", "add", "Aman", "Human", "remove", "Mazen", "add", "Shivangi", "Human"};
        l_correct.put("cmd", new ArrayList<>(Arrays.asList("gameplayer")));
        l_correct.put("add", new ArrayList<>(Arrays.asList("Aman", "Human", "Shivangi", "Human")));
        l_correct.put("remove", new ArrayList<>(Arrays.asList("Mazen")));
        assertEquals(l_correct, CommandsParser.getArguments(l_cmd1));

        l_correct = new HashMap<>();
        String[] l_cmd2 = {"gameplayer", "add", "Aman", "Human", "add", "Adeetya", "Human"};
        l_correct.put("cmd", new ArrayList<>(Arrays.asList("gameplayer")));
        l_correct.put("add", new ArrayList<>(Arrays.asList("Aman", "Human", "Adeetya", "Human")));
        assertEquals(l_correct, CommandsParser.getArguments(l_cmd2));

        // test unnamed arguments
        l_correct = new HashMap<>();
        String[] l_cmd3 = {"deploy", "Canada", "10"};
        l_correct.put("cmd", new ArrayList<>(Arrays.asList("deploy")));
        l_correct.put("country_name", new ArrayList<>(Arrays.asList("Canada")));
        l_correct.put("reinforcements_num", new ArrayList<>(Arrays.asList("10")));
        assertEquals(l_correct, CommandsParser.getArguments(l_cmd3));

        l_correct = new HashMap<>();
        String[] l_cmd4 = {"advance", "Canada", "Egypt", "10"};
        l_correct.put("cmd", new ArrayList<>(Arrays.asList("advance")));
        l_correct.put("country_name_from", new ArrayList<>(Arrays.asList("Canada")));
        l_correct.put("country_name_to", new ArrayList<>(Arrays.asList("Egypt")));
        l_correct.put("armies_num", new ArrayList<>(Arrays.asList("10")));
        assertEquals(l_correct, CommandsParser.getArguments(l_cmd4));
    }
}