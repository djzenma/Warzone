package ObserverPattern;

import Model.PlayerModel;
import Utils.CommandsParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class EventListener extends Observer {
    private HashMap<String, List<String>> d_args;
    private LogEntryBuffer d_logEntryBuffer;
    private PlayerModel d_currentPlayer;

    @Override
    public void update(Observable p_observable) {
        d_logEntryBuffer = (LogEntryBuffer) p_observable;
        if (d_logEntryBuffer.getCommandArgs() != null)
            d_args = CommandsParser.getArguments(d_logEntryBuffer.getCommandArgs());
        d_currentPlayer = d_logEntryBuffer.getCurrentPlayer();

        String l_event = "\n" + d_logEntryBuffer.getPhase().toUpperCase() + "\n";

        if (d_logEntryBuffer.getPhase().equals("Game Play Phase")) {
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
            } else {
                l_event += d_currentPlayer.getName() + " issued: " + String.join(" ", d_logEntryBuffer.getCommandArgs());
            }
        } else if (d_logEntryBuffer.getPhase().equals("Startup Phase")) {
            //l_event += String.join(" ", d_logEntryBuffer.getCommandArgs());

            switch (d_args.get("cmd").get(0)) {
                case "loadmap":
                    l_event += d_logEntryBuffer.getCommandArgs()[1] + " is loaded";
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
        } else if (d_logEntryBuffer.getPhase().equals("Issue Cards")) {
            l_event += d_currentPlayer.getName() + " obtained " + d_logEntryBuffer.getCardType() + "!!";
        } else {
            l_event += String.join(" ", d_logEntryBuffer.getCommandArgs());
        }

        createLogFile(l_event);
        System.out.println(l_event);
    }

    private void createLogFile(String l_event) {
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
