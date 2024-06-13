package view;

import boardifier.control.Logger;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.TextElement;
import boardifier.view.ClassicBoardLook;
import boardifier.view.ContainerLook;
import boardifier.view.GameStageView;

import boardifier.view.TextLook;
import javafx.scene.text.Text;
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
        addLook(new HoleBoardLook(250, model.getBoard()));
        addLook(new PawnPotLook(310, 60, model.getHoleBluePawnPot()));
        addLook(new PawnPotLook(310, 60, model.getHoleRedPawnPot()));

        // create looks for all pawns depending on the scenario



        if(HoleStageFactory.testVict == 1){
            for(int i=0;i<2;i++) {
                addLook(new PawnLook(20, model.getBluePawns()[i]));
                addLook(new PawnLook(20,model.getRedPawns()[i]));
            }

        }else if(HoleStageFactory.testVict == 0){
            for(int i=0;i<16;i++) {
                addLook(new PawnLook(20,model.getBluePawns()[i]));
                addLook(new PawnLook(20,model.getRedPawns()[i]));
            }
        } else if (HoleStageFactory.testVict == 2) {
            for(int i=0;i<2;i++) {
                addLook(new PawnLook(20,model.getBluePawns()[i]));
            }
            addLook(new PawnLook(20,model.getRedPawns()[0]));
        } else if (HoleStageFactory.testVict == 3) {
            addLook(new PawnLook(20,model.getRedPawns()[0]));
            addLook(new PawnLook(20,model.getBluePawns()[0]));
        }


        for (int i = 0; i < ((HoleStageModel) gameStageModel).getArrows().length; i++) {
            addLook(new ArrowLook(model.getArrows()[i]));
        }

        // create a TextLook for the text element
        addLook(new TextLook(24, "0x000000", model.getPlayerName()));
        TextElement textElement = new TextElement("Pion capturé \npar les rouges", gameStageModel);
        textElement.setLocation(20,10);
        addLook(new TextLook(12, "0x000000", textElement));

        TextElement textElement2 = new TextElement("Pion capturé \npar les bleus", gameStageModel);
        textElement2.setLocation(870,10);
        addLook(new TextLook(12, "0x000000", textElement2));

        Logger.debug("finished creating game stage looks", this);
    }
}
