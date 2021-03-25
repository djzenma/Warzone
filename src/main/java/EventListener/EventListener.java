package EventListener;

import Model.Player;
import Utils.CommandsParser;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Maintains the log file of user inputs
 * It is a subclass of Observer
 */
public class EventListener extends Observer {
    /**
     * Object of the command arguments
     */
    private HashMap<String, List<String>> d_args;
    /**
     * Object of the log entry buffer
     */
    private LogEntryBuffer d_logEntryBuffer;
    /**
     * Object of the player
     */
    private Player d_currentPlayer;

    /**
     * Updates the log entry buffer
     *
     * @param p_observable Object of observable class
     */
    @Override
    public void update(Observable p_observable) {
        d_logEntryBuffer = (LogEntryBuffer) p_observable;
        if (d_logEntryBuffer.getCommandArgs() != null)
            d_args = CommandsParser.getArguments(d_logEntryBuffer.getCommandArgs());
        d_currentPlayer = d_logEntryBuffer.getCurrentPlayer();

        String l_event = "\n" + d_logEntryBuffer.getPhase().toUpperCase() + "\n";

        // Logs the game play phase
        if (d_logEntryBuffer.getPhase().equals("Game Play Phase")) {

            // Logs the execution of orders in gameplay phase
            if (d_logEntryBuffer.getIsExec()) {
                switch (d_args.get("cmd").get(0)) {
                    case "advance":
                        l_event += d_currentPlayer.getName() + " advanced " + d_args.get("armies_num").get(0) + " armies from " + d_args.get("country_name_from").get(0) + " to " + d_args.get("country_name_to").get(0);
                        break;
                    case "deploy":
                        l_event += d_currentPlayer.getName() + " deployed " + d_args.get("reinforcements_num").get(0) + " armies on " + d_args.get("country_name").get(0);
                        break;
                    case "airlift":
                        l_event += d_currentPlayer.getName() + " airlifted " + d_args.get("armies_num").get(0) + " armies from " + d_args.get("country_name_from").get(0) + " to " + d_args.get("country_name_to").get(0);
                        break;
                    case "bomb":
                        l_event += d_currentPlayer.getName() + " bombed " + d_args.get("target_country").get(0);
                        break;
                    case "blockade":
                        l_event += d_currentPlayer.getName() + " blockaded " + d_args.get("country_name").get(0);
                        break;
                    case "negotiate":
                        l_event += d_currentPlayer.getName() + " negotiated with " + d_args.get("target_player").get(0);
                        break;
                }
            }

            // Logs the issuing of orders in gameplay phase
            else {
                l_event += d_currentPlayer.getName() + " issued: " + String.join(" ", d_logEntryBuffer.getCommandArgs());
            }
        }

        // Logs the startup phase
        else if (d_logEntryBuffer.getPhase().equals("Startup Phase")) {
            switch (d_args.get("cmd").get(0)) {
                case "loadmap":
                    l_event += d_logEntryBuffer.getCommandArgs()[1] + " is loaded";
                    break;
                case "showmap":
                    l_event += "Player entered showmap";
                    break;
                case "gameplayer":
                    if (d_args.get("add") != null) {
                        l_event += "Player(s) added:";
                        for (String l_player : d_args.get("add")) {
                            l_event += " " + l_player;
                        }
                    }
                    if (d_args.get("remove") != null) {
                        l_event += "Player(s) removed:";
                        for (String l_player : d_args.get("remove")) {
                            l_event += " " + l_player;
                        }
                    }
                    break;
                case "assigncountries":
                    l_event += "Countries are assigned to the players";
                    break;
            }
        }

        // Logs the issuance of cards
        else if (d_logEntryBuffer.getPhase().equals("Issue Cards")) {
            l_event += d_currentPlayer.getName() + " obtained " + d_logEntryBuffer.getCardType() + "!!";
        }

        // Logs the map editor phase
        else {
            switch (d_args.get("cmd").get(0)) {
                case "showmap":
                    l_event += "Player entered showmap";
                    break;
                case "editcontinent":
                    if (d_args.get("add") != null) {
                        l_event += "Continent(s) added:";
                        for (String l_continent : d_args.get("add")) {
                            l_event += " " + l_continent;
                        }
                    }
                    if (d_args.get("remove") != null) {
                        l_event += "Continent(s) removed:";
                        for (String l_continent : d_args.get("remove")) {
                            l_event += " " + l_continent;
                        }
                    }
                    break;
                case "editcountry":
                    if (d_args.get("add") != null) {
                        l_event += "Country(s) added:";
                        for (String l_country : d_args.get("add")) {
                            l_event += " " + l_country;
                        }
                    }
                    if (d_args.get("remove") != null) {
                        l_event += "Country(s) removed:";
                        for (String l_country : d_args.get("remove")) {
                            l_event += " " + l_country;
                        }
                    }
                    break;
                case "editneighbor":
                    if (d_args.get("add") != null) {
                        l_event += "Neighbor(s) added:";
                        for (String l_neighbor : d_args.get("add")) {
                            l_event += " " + l_neighbor;
                        }
                    }
                    if (d_args.get("remove") != null) {
                        l_event += "Neighbor(s) removed:";
                        for (String l_neighbor : d_args.get("remove")) {
                            l_event += " " + l_neighbor;
                        }
                    }
                    break;
                case "savemap":
                    l_event += d_logEntryBuffer.getCommandArgs()[1] + " is saved";
                    break;
                case "editmap":
                    l_event += d_logEntryBuffer.getCommandArgs()[1] + " is loaded/created";
                    break;
                case "validatemap":
                    l_event += "Player asked to validate the map";
                    break;
                default:
                    l_event += String.join(" ", d_logEntryBuffer.getCommandArgs());
                    break;
            }
        }
        createLogFile(l_event);
        System.out.println(l_event);
    }

    /**
     * Creates the text file for logs
     *
     * @param l_event log
     */
    private void createLogFile(String l_event) {
        File l_logDir = new File("log");
        if (!l_logDir.exists())
            l_logDir.mkdir();
        try (FileWriter l_logFile = new FileWriter("log/log.txt", true);
             BufferedWriter l_bWriter = new BufferedWriter(l_logFile);
             PrintWriter l_pWriter = new PrintWriter(l_bWriter)) {
            l_pWriter.println(l_event);
        } catch (IOException l_e) {
            System.out.println(l_e.getMessage());
            l_e.printStackTrace();
        }
    }
}
