package States;

import Controller.GameEngineController;
import Utils.MapUtils;

import java.io.File;
import java.util.ArrayList;

public class PreLoad extends MapEditor {

    public PreLoad(GameEngineController p_gameEngineController) {
        super(p_gameEngineController);
    }

    @Override
    public void editMap(String[] l_args) throws Exception {
        triggerEvent(l_args, "Map Editor Phase");
        String l_fileName = MapUtils.getValidFileName(l_args);
        ArrayList l_fileData = MapUtils.getMapFile(l_fileName, false);
        if ((boolean) l_fileData.get(1))
            d_gameEngineController.d_mapView.showMsg(l_fileName + " file does not exists!\nCreated new " + l_fileName + " file.");
        d_gameEngineController.d_mapModel.editMap((File) l_fileData.get(0));

        //if existing map file is loaded, validate the map
        //i.e don't validate for newly created files
        if (((File) l_fileData.get(0)).length() != 0) {
            d_gameEngineController.d_mapModel.validateMap();
            d_gameEngineController.d_mapView.validMap(d_gameEngineController.d_mapModel.isMapValid());
        }
        this.next();
    }

    @Override
    public void next() {
        d_gameEngineController.setPhase(new PostLoad(d_gameEngineController));
    }

}
