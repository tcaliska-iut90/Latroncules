package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.GameElement;
import boardifier.model.ContainerElement;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import model.HoleBoard;
import model.HoleStageModel;
import model.Pawn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HoleController extends Controller {

    BufferedReader consoleIn;
    boolean firstPlayer;

   public HoleController(Model model, View view) {
        super(model, view);
        firstPlayer = true;
    }

    /**
     * Defines what to do within the single stage of the single party
     * It is pretty straight forward to write :
     */
    public void stageLoop() {
        consoleIn = new BufferedReader(new InputStreamReader(System.in));
        update();
        while(! model.isEndStage()) {
            playTurn();
            endOfTurn();
            update();
        }
        endGame();
    }

    private void playTurn() {
        // get the new player
        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            HoleDecider decider = new HoleDecider(model,this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        }
        else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName()+ " > ");
                try {
                    String line = consoleIn.readLine();
                    if (line.length() == 4) {
                        ok = analyseAndPlay(line);
                    }
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                }
                catch(IOException e) {}
            }
        }
    }

    public void endOfTurn() {

        model.setNextPlayer();
        // get the new player to display its name
        Player p = model.getCurrentPlayer();
        HoleStageModel stageModel = (HoleStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());
    }
    private boolean analyseAndPlay(String line) {
        HoleStageModel gameStage = (HoleStageModel) model.getGameStage();
        HoleBoard board = gameStage.getBoard();


        // Obtenir les coordonnées du pion
        int colPawn = (int) (line.charAt(0) - 'A');
        int rowPawn = (int)(line.charAt(1) - '1');
        if ((colPawn<0)||(colPawn>8) || (rowPawn < 0) || (rowPawn > 8)) return false;

        // Obtenir les coordonnées d'arrivé du pion
        int finCol = (int) (line.charAt(2) - 'A');
        int finRow = (int) (line.charAt(3) - '1');

        // check coords validity
        if ((finRow<0)||(finRow>8)) return false;
        if ((finCol<0)||(finCol>8)) return false;

        // check if the pawn is the good color
        int color ;
        if (model.getIdPlayer() == 0) {
           color = Pawn.PAWN_BLUE;
        }
        else {
            color = Pawn.PAWN_RED;
        }

        Pawn p = (Pawn) board.getElement(rowPawn, colPawn);
        if (p.getColor() != color) return false;


        //Vérifier le type du pion
        if (p.getRole() == Pawn.INFANTRYMAN) {
            if (!verifPawnMove(p, color,colPawn, rowPawn, finRow, finCol)) return false;
        }


        // compute valid cells for the chosen pawn
        //Vérifier si le type du pion, les flèches et voir les déplacement possibles

        //gameStage.getBoard().setValidCells(pawnIndex+1);
        //if (!gameStage.getBoard().canReachCell(row,col)) return false;

        ActionList actions = ActionFactory.generatePutInContainer(model, p, "holeboard", finRow, finCol);
        actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
        return true;
    }

    private boolean verifPawnMove(Pawn p, int color, int colPawn, int rowPawn, int finRow, int finCol){
            if (color == Pawn.PAWN_BLUE ) {
                if (colPawn != finCol || rowPawn + 1 != finRow ) {
                    System.out.println("Un pion peut aller que tout droit");
                    return false;
                }
            }
            if (color == Pawn.PAWN_RED && (colPawn != finCol || rowPawn - 1 != finRow)){
                System.out.println("Un pion peut aller que tout droit");
                return false;
            }
            return true;
    }
}
