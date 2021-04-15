package Controller;

import States.Startup;
import Utils.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests Tournament Mode
 */
public class TournamentControllerTest {

    /**
     * Object of the GameEngine
     */
    private static GameEngine d_GameEngine;

    /**
     * Initializes tournament
     *
     * @throws Exception If some sort of Exception occurs
     */
    @Before
    public void init() throws Exception {
        CommandsParser.parseJson();
        String[] d_tournamentCommand = {
                "tournament",
                "M", "solar.map", "Africa.map",
                "P", "cheater", "benevolent",
                "G", "2",
                "D", "50"
        };

        d_GameEngine = new GameEngine();
        d_GameEngine.setPhase(new Startup(d_GameEngine));
        TournamentController d_tournamentController = new TournamentController(d_GameEngine, CommandsParser.getArguments(d_tournamentCommand));
        d_tournamentController.run();
    }

    /**
     * Tests the tournament maps
     */
    @Test
    public void checkMaps() {
        assertEquals(2, d_GameEngine.d_tournamentModel.getMaps().size());
    }

    /**
     * Tests the tournament players
     */
    @Test
    public void checkPlayers() {
        assertEquals(2, d_GameEngine.d_tournamentModel.getPlayerStrategies().size());
    }

    /**
     * Tests the tournament winners
     */
    @Test
    public void checkWinners() {
        assertEquals(2, d_GameEngine.d_tournamentModel.getWinners().get("solar.map").size());
        assertEquals(2, d_GameEngine.d_tournamentModel.getWinners().get("Africa.map").size());
        assertEquals("cheater", d_GameEngine.d_tournamentModel.getWinners().get("solar.map").get(0));
        assertEquals("cheater", d_GameEngine.d_tournamentModel.getWinners().get("solar.map").get(1));
        assertEquals("cheater", d_GameEngine.d_tournamentModel.getWinners().get("Africa.map").get(0));
        assertEquals("cheater", d_GameEngine.d_tournamentModel.getWinners().get("Africa.map").get(1));
    }

    /**
     * Tests the tournament validity
     */
    @Test
    public void invalidTournament() {
        try {
            String[] d_tournamentCommand = {
                    "tournament",
                    "M", "solar.map", "Africa.map",
                    "P", "cheater",
                    "G", "2",
                    "D", "50"
            };

            d_GameEngine = new GameEngine();
            d_GameEngine.setPhase(new Startup(d_GameEngine));
            TournamentController d_tournamentController = new TournamentController(d_GameEngine, CommandsParser.getArguments(d_tournamentCommand));
            d_tournamentController.run();
        } catch (Exception l_e) {
            assertEquals(l_e.getMessage(), "Num of players entered must be between 2 to 4.");
        }
    }
}