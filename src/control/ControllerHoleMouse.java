package control;

import boardifier.control.*;
import boardifier.model.Coord2D;
import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import boardifier.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.HoleBoard;
import model.HolePawnPot;
import model.HoleStageModel;
import model.Pawn;
import view.HoleView;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A basic mouse controller that just grabs the mouse clicks and prints out some informations.
 * It gets the elements of the scene that are at the clicked position and prints them.
 */
public class ControllerHoleMouse extends ControllerMouse implements EventHandler<MouseEvent> {

    private final CheckMoveController checkMoveController;

    public ControllerHoleMouse(Model model, View view, Controller control, CheckMoveController checkMoveController) {
        super(model, view, control);
        this.checkMoveController = checkMoveController;

    }

    @Override
    public void handle(MouseEvent event) {
        // if mouse event capture is disabled in the model, just return
        if (!model.isCaptureMouseEvent()) return;

        // get the clic x,y in the whole scene (this includes the menu bar if it exists)
        Coord2D clic = new Coord2D(event.getSceneX(),event.getSceneY());
        // get elements at that position
        List<GameElement> list = control.elementsAt(clic);
        // for debug, uncomment next instructions to display x,y and elements at that postion
        /*
        Logger.debug("click in "+event.getSceneX()+","+event.getSceneY());
        for(GameElement element : list) {
            Logger.debug(element);
        }
         */
        HoleStageModel stageModel = (HoleStageModel) model.getGameStage();

        if (stageModel.getState() == HoleStageModel.STATE_SELECTPAWN && checkSTATE_SELECTPAWN(list, stageModel)) {
            return;
        }
        else if (stageModel.getState() == HoleStageModel.STATE_SELECTDEST && !checkSTATE_SELECTDEST(list, stageModel)) {
            return;
        }
        changePositionPawn(stageModel, clic);
    }

    private void changePositionPawn(HoleStageModel stageModel, Coord2D clic){
        HoleBoard board = stageModel.getBoard();
        HoleView holeView = (HoleView) view;
        // by default get black pot

        Pawn pawn;
        try {
            pawn = (Pawn) model.getSelected().get(0);
        }catch (IndexOutOfBoundsException e){
           holeView.dialogError("Le pion n'est pas votre pion");
            return;
        }
        int colorEnemy = model.getCurrentPlayer().getColor() == 0 ? Pawn.PAWN_RED : Pawn.PAWN_BLUE;

        // thirdly, get the clicked cell in the 3x3 board
        GridLook lookBoard = (GridLook) control.getElementLook(board);
        int[] dest = lookBoard.getCellFromSceneLocation(clic);
        // get the cell in the pot that owns the selected pawn
        int[] from = board.getElementCell(pawn);
        Logger.debug("try to move pawn from pot "+from[0]+","+from[1]+ " to board "+ dest[0]+","+dest[1]);
        // if the destination cell is valid for for the selected pawn
        if (board.canReachCell(dest[0], dest[1])) {
            if ((pawn.getRole() == Pawn.INFANTRYMAN && checkMoveController.verifPawnMove(board, pawn.getColor(), from[1], from[0], dest[0], dest[1]))
                || pawn.getRole() == Pawn.HORSEMAN && checkMoveController.verifMoveCavalier(board, pawn.getColor(), from[1], from[0], dest[0], dest[1], stageModel)) {

                Logger.debug("move pawn from pot "+from[0]+","+from[1]+ " to board "+ dest[0]+","+dest[1]);
                pawn.setRow(dest[0]);
                pawn.setCol(dest[1]);

                ActionList actions = ActionFactory.generatePutInContainer(control, model, pawn, "holeboard", dest[0], dest[1], AnimationTypes.MOVE_LINEARPROP, 10);
                actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
                stageModel.unselectAll();
                stageModel.setState(HoleStageModel.STATE_SELECTPAWN);
                ActionPlayer play = new ActionPlayer(model, control, actions);

                ExecutorService executor = Executors.newFixedThreadPool(1);
                Future<?> futureTask1 = executor.submit(() ->{
                    play.start();
                    try {
                        play.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                executor.submit(() -> {
                    try {
                        futureTask1.get();
                        board.setCellReachable(from[0], from[1], true);
                        board.setCellReachable(dest[0], dest[1], false);
                        board.takingPawn(stageModel, board, model, dest[0], dest[1], pawn.getColor(), colorEnemy);
                        checkMoveController.changeInfantrymanToHorseman(pawn, dest[0]);
                        checkMoveController.moveIsOk(stageModel, board);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                });
                executor.shutdown();

            }
        }else {
            holeView.dialogError("Un pion se trouve sur cette case");
        }
    }

    private boolean checkSTATE_SELECTPAWN(List<GameElement> list, HoleStageModel stageModel){
        for (GameElement element : list) {
            if (element.getType() == ElementTypes.getType("pawn")) {
                Pawn pawn = (Pawn)element;
                // check if color of the pawn corresponds to the current player id
                if (pawn.getColor() == model.getCurrentPlayer().getColor()) {
                    element.toggleSelected();
                    stageModel.setState(HoleStageModel.STATE_SELECTDEST);
                    // call method green rectangle not full at the cell selected by the player
                    System.out.println("x et y du pion choisi : "+pawn.getX()+"  "+pawn.getY()+"   Col : "+pawn.getCol()+" Row : "+pawn.getRow());
                    // Change la couleur du rectangle seléctionné en vert
                    // ...

                    // Change la couleur des cases atteignables en orange
                    stageModel.getBoard().setCellReachable(pawn.getCol(), pawn.getRow(), true);
                    boolean[][] reachable = stageModel.getBoard().getReachableCells();
                    for (int i = 0; i < reachable.length; i++) {
                        for (int j = 0; j < reachable[i].length; j++) {
                            if (reachable[i][j]) {
                                System.out.println("x et y des cases atteignables : "+i+"  "+j);
                                // ...
                            }
                        }
                    }

                    return true; // do not allow another element to be selected
                }
            }
        }
        return false;
    }

    private boolean checkSTATE_SELECTDEST(List<GameElement> list, HoleStageModel stageModel){
        // first check if the click is on the current selected pawn. In this case, unselect it
        for (GameElement element : list) {
            if (element.isSelected()) {
                element.toggleSelected();
                stageModel.setState(HoleStageModel.STATE_SELECTPAWN);
                return false;
            }
        }
        // secondly, search if the board has been clicked. If not just return
        boolean boardClicked = false;
        for (GameElement element : list) {
            if (element == stageModel.getBoard()) {
                boardClicked = true; break;
            }
        }
        return boardClicked;
        // get the board, pot,  and the selected pawn to simplify code in the following
    }

}

