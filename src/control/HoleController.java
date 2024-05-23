package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
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

    public BufferedReader consoleIn;
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
        int i = 1;
        while (!model.isEndStage()) {

            System.out.println("Tour numéro : " + i);
            i++;

            if(checkWinner() == 1){
                model.setIdWinner(1);
                stopStage();
                System.out.println("state = STATE_ENDSTAGE, donc model.isEndStage()=true");
            } else if (checkWinner() == 0) {
                model.setIdWinner(0);
                stopStage();
                System.out.println("state = STATE_ENDSTAGE, donc model.isEndStage()=true ");
            }
            else {
                playTurn();
                update();
                endOfTurn();
            }
        }

        System.out.println("Game over");
        endGame();

    }


    public int checkWinner(){
        HoleStageModel gameStage = (HoleStageModel) model.getGameStage();

        if (gameStage.isBlueMissing()){
            return 1;
        }
        else if (gameStage.isRedMissing()) {
            return 0;
        }
        return -1;
    }

    public String playTurn() {
        // get the new player
        Player p = model.getCurrentPlayer();
        Player adversary = model.getAdversary();
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            HoleStageModel gameStage = (HoleStageModel) model.getGameStage();
            HoleBoard board = gameStage.getBoard();
            HoleDecider decider = new HoleDecider(model, this, p, adversary, this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
            int finRow = decider.getRowDest();
            int finCol = decider.getColDest();
            Pawn pawn = decider.getPawn();
            board.takingPawn(gameStage, board, model, finRow, finCol, pawn.getColor());
            changeInfantrymanToHorseman(pawn, finRow);
            return "Computer";
        } else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName() + " > ");
                try {
                    String line = consoleIn.readLine();
                    if (line.length() == 4) {
                        ok = analyseAndPlay(line);
                    }
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                } catch (IOException e) {
                }
            }
            return "Human";
        }
    }

    @Override
    public void endOfTurn() {

        model.setNextPlayer();
        // get the new player to display its name
        Player p = model.getCurrentPlayer();
        HoleStageModel stageModel = (HoleStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());
    }

    /**
     * Analyse si le coup est possible et le joue ensuite
     * @param line Case du pion voulant être jouer et case ou le déplacer(Ex: A1A2)
     * @return False si coup non jouable
     */
    public boolean analyseAndPlay(String line) {
        HoleStageModel gameStage = (HoleStageModel) model.getGameStage();
        HoleBoard board = gameStage.getBoard();


        // Obtenir les coordonnées du pion
        int colPawn = (int) (line.charAt(0) - 'A');
        int rowPawn = (int) (line.charAt(1) - '1');
        if ((colPawn < 0) || (colPawn > 8) || (rowPawn < 0) || (rowPawn > 8)) return false;

        // Obtenir les coordonnées d'arrivé du pion
        int finCol = (int) (line.charAt(2) - 'A');
        int finRow = (int) (line.charAt(3) - '1');

        // check coords validity
        if ((finRow < 0) || (finRow > 8) || (finCol < 0) || (finCol > 8)) return false;

        // check if the pawn is the good color
        int color;
        if (model.getIdPlayer() == 0) {
            color = Pawn.PAWN_BLUE;
        } else {
            color = Pawn.PAWN_RED;
        }

        Pawn p = (Pawn) board.getElement(rowPawn, colPawn);
        try {
            if (p.getColor() != color) return false;
        } catch (NullPointerException e) {
            System.out.println("Aucun pion n'est présent sur cette case");
            return false;
        }

        //Vérifier le type du pion
        if (p.getRole() == Pawn.INFANTRYMAN) {
            if (!verifPawnMove(board, color, colPawn, rowPawn, finRow, finCol)) return false;
        } else {
            if (!verifMoveCavalier(board, colPawn, rowPawn, finRow, finCol, gameStage, color)) return false;
        }

        //System.out.println("Le role avant est " + p.getRole() + " et sa colone est " + rowPawn);

        ActionList actions = ActionFactory.generatePutInContainer(model, p, "holeboard", finRow, finCol);
        actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
        board.takingPawn(gameStage, board, model, finRow, finCol, color);
        changeInfantrymanToHorseman(p, finRow);

        return true;
    }

    /**
     * Vérifie le coup pour un infantryman
     * @param board Grille de jeu
     * @param color couleur du pion
     * @param colPawn Colonne du pion joueur
     * @param rowPawn Ligne du pion joueur
     * @param finRow Colonne final du pion joueur
     * @param finCol Ligne final du pion joueur
     * @return true si coup possible
     */
    public boolean verifPawnMove(HoleBoard board, int color, int colPawn, int rowPawn, int finRow, int finCol) {

        //Test mouvement possible en fonction de la couleur
        if ((color == Pawn.PAWN_BLUE && (colPawn != finCol || rowPawn + 1 != finRow)) || (color == Pawn.PAWN_RED && (colPawn != finCol || rowPawn - 1 != finRow))) {
            System.out.println("Un pion peut aller que tout droit");
            return false;
        }
        //Test pion devant le pion joueur
        if (board.getElement(finRow, finCol) != null) {
            System.out.println("Un pion se trouve devant ce pion");
            return false;
        }
        //Test mouvement impossible
        if (finCol > 0 && finCol < 7) {
            Pawn p1 = (Pawn) board.getElement(finRow, finCol - 1);
            Pawn p2 =(Pawn) board.getElement(finRow, finCol + 1);
            if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
                System.out.println("Impossible, coup interdit");
                return false;
            }
        }

        return true;
    }

    /**
     * Vérifie le coup pour un cavalier
     * @param board Grille de jeu
     * @param colPawn Colonne du pion joueur
     * @param rowPawn Ligne du pion joueur
     * @param finRow Colonne final du pion joueur
     * @param finCol Ligne final du pion joueur
     * @param holeStageModel
     * @return true si coup possible
     */
    public boolean verifMoveCavalier(HoleBoard board, int colPawn, int rowPawn, int finRow, int finCol, HoleStageModel holeStageModel, int color) {
        int[][] temp;
        boolean valueFound = false;

        if (holeStageModel.getBoardArrows1()[rowPawn][colPawn] != null) {
            temp = board.getValidCell(model, holeStageModel.getBoardArrows1()[rowPawn][colPawn], holeStageModel.getBoardArrows2()[rowPawn][colPawn], rowPawn, colPawn);
        } else {
            temp = board.getValidCell(model, rowPawn, colPawn);
        }

        Pawn p = (Pawn) board.getElement(finRow, finCol);
        for (int i = 0; i < temp.length; i++) {
            if (temp[i][0] == finRow && temp[i][1] == finCol && p == null) {
                valueFound = true;
                break;
            }
        }
        if (!valueFound) {
            System.out.println("Mouvement impossible sur cette case");
            return false;
        }
        return testCoupInterdit(finCol, finRow, board, color);

    }

    /**
     * Modifie de role du pion si un infantryman est
     * @param p
     * @param finRow
     */
    public void changeInfantrymanToHorseman(Pawn p, int finRow){
        //regarde le role d'un pion et si c'est un fantassin et qu'il a atteint l'autre extrême du plateau, le change en cavalier
        if((p.getRole() == Pawn.INFANTRYMAN && (p.getColor() == Pawn.PAWN_BLUE && finRow == 7) || (p.getColor() == Pawn.PAWN_RED && finRow == 0))){
            p.setRole(Pawn.HORSEMAN);
        }
    }

    private boolean testCoupInterdit(int finCol, int finRow, HoleBoard board, int color){
        if (finRow > 0 && finRow < 7 && !checkCoupInterditVertical(finCol, finRow, board, color)) return false;

        if (finCol > 0 && finCol < 7 && !checkCoupInterditHorizontal(finCol, finRow, board, color))return false;

        if ((finCol > 0 && finCol < 7) && (finRow > 0 && finRow < 7) && !checkCoupInterditDiagonal(finCol, finRow, board, color))return false;

        return finRow <= 0 || finRow >= 7 || finCol <= 0|| finCol >= 7 || checkCoupInterditVertical(finCol, finRow, board, color);
    }

    private Boolean checkCoupInterditHorizontal(int finCol, int finRow, HoleBoard board, int color){

        Pawn p1 = (Pawn) board.getElement(finRow, finCol - 1);
        Pawn p2 =(Pawn) board.getElement(finRow, finCol + 1);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow, finCol - 1, color) || !board.isCapturable(board, finRow, finCol + 1, color)) {
                System.out.println("Impossible, coup interdit");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoupInterditVertical(int finCol, int finRow, HoleBoard board, int color){
        Pawn p1 = (Pawn) board.getElement(finRow - 1, finCol );
        Pawn p2 =(Pawn) board.getElement(finRow + 1, finCol);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow + 1, finCol, color) || !board.isCapturable(board, finRow - 1, finCol, color)) {
                System.out.println("Impossible, coup interdit");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoupInterditDiagonal(int finCol, int finRow, HoleBoard board, int color){
        Pawn p1 = (Pawn) board.getElement(finRow - 1, finCol-1 );
        Pawn p2 =(Pawn) board.getElement(finRow + 1, finCol+1);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow + 1, finCol +1, color) || !board.isCapturable(board, finRow - 1, finCol - 1, color)) {
                System.out.println("Impossible, coup interdit");
                return false;
            }
        }

        p1 = (Pawn) board.getElement(finRow - 1, finCol+1 );
        p2 =(Pawn) board.getElement(finRow + 1, finCol-1);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow - 1, finCol +1, color) || !board.isCapturable(board, finRow + 1, finCol - 1, color)) {
                System.out.println("Impossible, coup interdit");
                return false;
            }
        }
        return true;
    }


}
