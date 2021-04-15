package States;

import Controller.GameEngine;
import Utils.MapUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Pre load phase
 * It extend the mapeditor
 */
public class PreLoad extends MapEditor {
    /**
     * Constructor
     *
     * @param p_gameEngine Object for game engine controller
     */
    public PreLoad(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * Edits the map
     *
     * @param l_args Array of the command arguments
     * @throws Exception Throws if there is some kind of exception
     */
    @Override
    public void editMap(String[] l_args) throws Exception {
        String l_fileName = MapUtils.getValidFileName(l_args);
        ArrayList l_fileData = MapUtils.getMapFile(l_fileName, false);
        if ((boolean) l_fileData.get(1))
            d_gameEngine.d_mapView.showMsg(l_fileName + " file does not exists!\nCreated new " + l_fileName + " file.");
        d_gameEngine.d_mapModel.editMap((File) l_fileData.get(0));

        //if existing map file is loaded, validate the map
        //i.e don't validate for newly created files
        if (((File) l_fileData.get(0)).length() != 0) {
            d_gameEngine.d_mapModel.validateMap();
            d_gameEngine.d_mapView.validMap(d_gameEngine.d_mapModel.isMapValid());
        }
        triggerEvent(l_args, "Map Editor Phase");
        this.next();
    }

    /**
     * Moves to the next phase
     */
    @Override
    public void next() {
        d_gameEngine.setPhase(new PostLoad(d_gameEngine));
    }
}
