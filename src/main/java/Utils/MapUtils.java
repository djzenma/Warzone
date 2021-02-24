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

/**
 * Utility class for Maps
 */
public class MapUtils {

    FileInputStream d_fis;
    byte[] d_data;

    /**
     * Reads map file specified by file object
     * @param p_file Map file object
     * @return Data of the map file
     * @throws IOException If I/O exception of some sort has occurred
     */
    public String readMapFile(File p_file) throws IOException {
        d_fis = new FileInputStream(p_file);
        d_data = new byte[(int) p_file.length()];
        d_fis.read(d_data);
        d_fis.close();
        String l_str = new String(d_data, StandardCharsets.UTF_8);
        l_str += " \n ";
        return l_str;
    }

    /**
     * Read map file specified by filename
     * @param p_fileName Name of the map file
     * @return Buffer reader for the map file
     * @throws IOException If I/O exception of some sort has occurred
     */
    public BufferedReader readMapFileByName(String p_fileName) throws IOException {
        Path l_File = Paths.get(this.getMapsPath() + p_fileName);
        BufferedReader l_first = Files.newBufferedReader(l_File);
        return l_first;
    }

    /**
     * Gets the path of the "maps/" directory
     * @return Path of the "maps/" directory
     */
    public String getMapsPath() {
        return "maps/";
    }

    /**
     * Checks whether both map files are identical or not
     * @param p_firstFile Name of first map file
     * @param p_secondFile Name of second map file
     * @return True, if both map files are identical; False otherwise
     * @throws IOException If I/O exception of some sort has occurred
     */
    public boolean areMapFilesEqual(String p_firstFile, String p_secondFile) throws IOException {

        String[] l_firstCountryNeighbors;
        String[] l_secondCountryNeighbors;
        BufferedReader l_first = readMapFileByName(p_firstFile);
        BufferedReader l_second = readMapFileByName(p_secondFile);

        // discards first five lines of the file
        for (int l_i = 0; l_i < 5; l_i++) {
            l_first.readLine();
            l_second.readLine();
        }

        String l_firstLine, l_secondLine;
        l_firstLine = l_first.readLine().trim();
        l_secondLine = l_second.readLine().trim();

        // check if all lines in both files are equal until borders section
        while (!(l_firstLine.equals("[borders]"))) {
            if (!(l_firstLine.equals(l_secondLine))) {
                return false;
            }
            l_firstLine = l_first.readLine().trim();
            l_secondLine = l_second.readLine().trim();
        }

        // check borders section of both files
        while ((l_firstLine = l_first.readLine()) != null) {
            l_firstLine = l_firstLine.trim();
            l_secondLine = l_second.readLine().trim();

            // get individual country ids of neighbors
            l_firstCountryNeighbors = l_firstLine.split(" ");
            l_secondCountryNeighbors = l_secondLine.split(" ");

            // check if both files have same neighbors
            Arrays.sort(l_firstCountryNeighbors);
            Arrays.sort(l_secondCountryNeighbors);
            return Arrays.equals(l_firstCountryNeighbors, l_secondCountryNeighbors);
        }
        return true;
    }

}
