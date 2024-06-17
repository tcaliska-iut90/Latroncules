package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Logger;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;
import model.HoleBoard;
import model.HoleStageModel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ControllerHole extends Controller {

    public ControllerHole(Model model, View view) {
        super(model, view);
        setControlKey(new ControllerHoleKey(model, view, this));
        setControlMouse(new ControllerHoleMouse(model, view, this, new CheckMoveController(model, view)));
        setControlAction (new ControllerHoleAction(model, view, this));
    }

    public void endOfTurn() {
        // use the default method to compute next player
        model.setNextPlayer();
        // get the new player
        Player p = model.getCurrentPlayer();
        Player adversary = model.getAdversary();

        CheckMoveController checkMove = new CheckMoveController(model, view);

        HoleStageModel stageModel = (HoleStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());

        HoleBoard board = stageModel.getBoard();

        if (p.getType() == Player.COMPUTER) {
            Logger.debug("COMPUTER PLAYS");
            HoleDecider decider = new HoleDecider(model, this, p, adversary, checkMove);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);

            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<?> futureTask1 = executor.submit(() -> {
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
                    board.setCellReachable(decider.getRowDest(), decider.getColDest(), true);
                    board.setCellReachable(decider.getRowDest(), decider.getColDest(), false);
                    board.takingPawn(stageModel, board, model, decider.getRowDest(), decider.getColDest(), decider.getPawn().getColor(), adversary.getColor());
                    checkMove.changeInfantrymanToHorseman(decider.getPawn(), decider.getRowDest());
                    checkMove.moveIsOk(stageModel, board, decider.getPawn());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

            });
            executor.shutdown();
        }
        else {
            Logger.debug("PLAYER PLAYS");
        }
    }
}
