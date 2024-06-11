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

        // create a ClassicBoardLook (with borders and coordinates) for the main board.
        addLook(new HoleBoardLook(300, model.getBoard()));
        addLook(new BlackPawnPotLook(model.getHoleBluePawnPot()));
        addLook(new BlackPawnPotLook(model.getHoleRedPawnPot()));

        // create looks for all pawns depending on the scenario




        if(HoleStageFactory.testVict == 1){
            for(int i=0;i<2;i++) {
                addLook(new PawnLook(25, model.getBluePawns()[i]));
                addLook(new PawnLook(25,model.getRedPawns()[i]));
            }

        }else if(HoleStageFactory.testVict == 0){
            for(int i=0;i<16;i++) {
                addLook(new PawnLook(25,model.getBluePawns()[i]));
                addLook(new PawnLook(25,model.getRedPawns()[i]));
            }
        } else if (HoleStageFactory.testVict == 2) {
            for(int i=0;i<2;i++) {
                addLook(new PawnLook(25,model.getBluePawns()[i]));
            }
            addLook(new PawnLook(25,model.getRedPawns()[0]));
        } else if (HoleStageFactory.testVict == 3) {
            addLook(new PawnLook(25,model.getRedPawns()[0]));
            addLook(new PawnLook(25,model.getBluePawns()[0]));
        }


        for (int i = 0; i < ((HoleStageModel) gameStageModel).getArrows().length; i++) {
            addLook(new ArrowLook(model.getArrows()[i]));
        }

        // create a TextLook for the text element
        addLook(new TextLook(24, "0x000000", model.getPlayerName()));

        Logger.debug("finished creating game stage looks", this);
    }
}
