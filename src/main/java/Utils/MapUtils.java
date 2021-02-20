package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class MapUtils {
    FileInputStream d_fis;
    byte[] d_data;

    public String readMapFile(File p_file) throws IOException {
        d_fis = new FileInputStream(p_file);
        d_data = new byte[(int) p_file.length()];
        d_fis.read(d_data);
        d_fis.close();
        String l_str = new String(d_data, StandardCharsets.UTF_8);
        l_str += " \n";
        return l_str;
    }

    public BufferedReader readMapFileByName(String p_fileName) throws IOException {
        Path l_File = Paths.get(this.getMapsPath() + p_fileName);
        BufferedReader l_first = Files.newBufferedReader(l_File);
        return l_first;
    }

    public String getMapsPath() {
        Path l_currentRelativePath = Paths.get("");
        //String l_mapsPath = l_currentRelativePath.toAbsolutePath().toString() + "\\maps\\";
        return "maps/";
    }

    public boolean areMapFilesEqual(String p_firstFile, String p_secondFile) throws IOException {

        String[] l_firstCountryNeighbors;
        String[] l_secondCountryNeighbors;
        BufferedReader l_first = readMapFileByName(p_firstFile);
        BufferedReader l_second = readMapFileByName(p_secondFile);

        for (int l_i = 0; l_i < 5; l_i++) {
            l_first.readLine();
            l_second.readLine();

        }

        String l_firstLine, l_secondLine;
        l_firstLine = l_first.readLine().trim();
        l_secondLine = l_second.readLine().trim();
        while (!(l_firstLine.equals("[borders]"))) {
            if (!(l_firstLine.equals(l_secondLine))) {
                return false;
            }
            l_firstLine = l_first.readLine().trim();
            l_secondLine = l_second.readLine().trim();
        }

        while ((l_firstLine = l_first.readLine()) != null) {
            l_firstLine = l_firstLine.trim();
            l_secondLine = l_second.readLine().trim();

            l_firstCountryNeighbors = l_firstLine.split(" ");
            l_secondCountryNeighbors = l_secondLine.split(" ");

            Arrays.sort(l_firstCountryNeighbors);
            Arrays.sort(l_secondCountryNeighbors);

            return Arrays.equals(l_firstCountryNeighbors, l_secondCountryNeighbors);
        }
        return true;
    }

}
