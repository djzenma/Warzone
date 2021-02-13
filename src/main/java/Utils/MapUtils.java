package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapUtils {

    public String readMapFile(File p_file) throws IOException {
        File l_file = p_file;
        FileInputStream l_fis = new FileInputStream(l_file);
        byte[] l_data = new byte[(int) l_file.length()];
        l_fis.read(l_data);
        l_fis.close();
        String l_str = new String(l_data, StandardCharsets.UTF_8);
        l_str += " \n";
        return l_str;
    }

    public String getMapsPath() {
        Path l_currentRelativePath = Paths.get("");
        String l_mapsPath = l_currentRelativePath.toAbsolutePath().toString() + "\\maps\\";
        return l_mapsPath;
    }
}
