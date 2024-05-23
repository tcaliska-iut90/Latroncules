package view;

import boardifier.control.Logger;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.view.ClassicBoardLook;
import boardifier.view.ContainerLook;
import boardifier.view.GameStageView;

import boardifier.view.TextLook;
import model.HoleStageFactory;
import model.HoleStageModel;


public class HoleStageView extends GameStageView {
    public HoleStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    @Override
    public void createLooks() {
        HoleStageModel model = (HoleStageModel)gameStageModel;

        // create a TextLook for the text element
        addLook(new TextLook(model.getPlayerName()));
        // create a ClassicBoardLook (with borders and coordinates) for the main board.
        addLook(new ClassicBoardLook(2, 6, model.getBoard(), 1, 1, true));
        addLook(new BlackPawnPotLook(model.getHoleBluePawnPot()));
        addLook(new BlackPawnPotLook(model.getHoleRedPawnPot()));

        // create looks for all pawns depending on the scenario




        if(HoleStageFactory.testVict == 1){
            for(int i=0;i<2;i++) {
                addLook(new PawnLook(model.getBluePawns()[i]));
                addLook(new PawnLook(model.getRedPawns()[i]));
            }

        }else if(HoleStageFactory.testVict == 0){
            for(int i=0;i<16;i++) {
                addLook(new PawnLook(model.getBluePawns()[i]));
                addLook(new PawnLook(model.getRedPawns()[i]));
            }
        } else if (HoleStageFactory.testVict == 2) {
            for(int i=0;i<2;i++) {
                addLook(new PawnLook(model.getBluePawns()[i]));
            }
            addLook(new PawnLook(model.getRedPawns()[0]));
        } else if (HoleStageFactory.testVict == 3) {
            addLook(new PawnLook(model.getRedPawns()[0]));
            addLook(new PawnLook(model.getBluePawns()[0]));
        }


        for (int i = 0; i < ((HoleStageModel) gameStageModel).getArrows().length; i++) {
            addLook(new ArrowLook(model.getArrows()[i]));
        }






        /*
        Example using a main container (see HoleStageFactory),


        // create a look for the main container element, with flexible cell size and no borders
        // NB: no need to recreate spanning for the container look: they are deduced from the main container element
        ContainerLook mainLook = new ContainerLook(model.getMainContainer(), -1);
        // set padding to 1
        mainLook.setPadding(1);
        // center the looks within the cells
        mainLook.setVerticalAlignment(ContainerLook.ALIGN_MIDDLE);
        mainLook.setHorizontalAlignment(ContainerLook.ALIGN_CENTER);
        // add the look to the gane stage view.
        addLook(mainLook);
        */
        Logger.debug("finished creating game stage looks", this);
    }
}
