package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.HoleBoard;
import model.HoleStageModel;
import model.Pawn;

import java.awt.*;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class HoleDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public HoleDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of HoleStageModel
        HoleStageModel stage = (HoleStageModel)model.getGameStage();
        HoleBoard board = stage.getBoard(); // get the board
        GameElement pawn = null; // the pawn that is moved
        int rowDest = 0; // the dest. row in board
        int colDest = 0; // the dest. col in board



        ActionList actions = ActionFactory.generatePutInContainer( model, pawn, "holeboard", rowDest, colDest);
        //actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.

        return actions;
    }
}
