package control;

import boardifier.model.Model;
import model.HoleBoard;
import model.HoleStageModel;
import model.Pawn;

public class CheckMoveController {
    private Model model;

    public CheckMoveController(Model model){
        this.model= model;
    }


    /**
     * Vérifie le coup pour un infantryman
     * @param board Grille de jeu
     * @param color couleur du pion
     * @param colPawn Colonne du pion joueur
     * @param rowPawn Ligne du pion joueur
     * @param finRow Colonne final du pion joueur
     * @param finCol Ligne final du pion joueur
     * @return true si coup possible, false sinon
     */
    public boolean verifPawnMove(HoleBoard board, int color, int colPawn, int rowPawn, int finRow, int finCol) {
        //Test mouvement possible en fonction de la couleur
        if ((color == Pawn.PAWN_BLUE && (colPawn != finCol || rowPawn + 1 != finRow)) || (color == Pawn.PAWN_RED && (colPawn != finCol || rowPawn - 1 != finRow))) {
            System.out.println("Un pion peut aller que tout droit");
            return false;
        }


        return testCoupInterdit(finCol, finRow, board, color);
    }

    /**
     * Vérifie le coup pour un cavalier
     * @param board Grille de jeu
     * @param colPawn Colonne du pion joueur
     * @param rowPawn Ligne du pion joueur
     * @param finRow Colonne final du pion joueur
     * @param finCol Ligne final du pion joueur
     * @param holeStageModel
     * @return true si coup possible, false sinon
     */
    public boolean verifMoveCavalier(HoleBoard board,int color,  int colPawn, int rowPawn, int finRow, int finCol, HoleStageModel holeStageModel) {
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
        if ((finRow == 0 || finRow == 7) && (finCol == 0 || finCol == 7)){
            return checkCoupInterditCoin(finCol, finRow, board, color);
        }
        if (finRow > 0 && finRow < 7 && !checkCoupInterditVertical(finCol, finRow, board, color)) return false;

        if (finCol > 0 && finCol < 7 && !checkCoupInterditHorizontal(finCol, finRow, board, color))return false;

        if ((finCol > 0 && finCol < 7) && (finRow > 0 && finRow < 7) && !checkCoupInterditDiagonal(finCol, finRow, board, color))return false;

        if (finRow > 0 && finRow < 7 && finCol > 0 && finCol < 7 && !checkCoupInterditVertical(finCol, finRow, board, color)) return false;

        return true;
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

    private boolean checkCoupInterditCoin(int finCol, int finRow, HoleBoard board, int color) {
        // Vérification pour le coin supérieur gauche (0, 0)
        if (finCol == 0 && finRow == 0) {
            return checkCoinSupérieurGauche(finCol, finRow, board, color);
        }
        // Vérification pour le coin supérieur droit (0, 7)
        if (finCol == 7 && finRow == 0) {
            return checkCoinSupérieurDroit(finCol, finRow, board, color);
        }
        // Vérification pour le coin inférieur gauche (7, 0)
        if (finCol == 0 && finRow == 7) {
            return checkCoinInférieurGauche(finCol, finRow, board, color);
        }
        // Vérification pour le coin inférieur droit (7, 7)
        if (finCol == 7 && finRow == 7) {
            return checkCoinInférieurDroit(finCol, finRow, board, color);
        }
        return true;
    }

    private boolean checkCoinSupérieurGauche(int finCol, int finRow, HoleBoard board, int color) {
        Pawn p1 = (Pawn) board.getElement(finRow, finCol + 1);
        Pawn p2 = (Pawn) board.getElement(finRow + 1, finCol);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow, finCol + 1, color) || !board.isCapturable(board, finRow + 1, finCol, color)) {
                System.out.println("Impossible, coup interdit au coin supérieur gauche");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoinSupérieurDroit(int finCol, int finRow, HoleBoard board, int color) {
        Pawn p1 = (Pawn) board.getElement(finRow, finCol - 1);
        Pawn p2 = (Pawn) board.getElement(finRow + 1, finCol);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow, finCol - 1, color) || !board.isCapturable(board, finRow + 1, finCol, color)) {
                System.out.println("Impossible, coup interdit au coin supérieur droit");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoinInférieurGauche(int finCol, int finRow, HoleBoard board, int color) {
        Pawn p1 = (Pawn) board.getElement(finRow - 1, finCol);
        Pawn p2 = (Pawn) board.getElement(finRow, finCol + 1);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow - 1, finCol, color) || !board.isCapturable(board, finRow, finCol + 1, color)) {
                System.out.println("Impossible, coup interdit au coin inférieur gauche");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoinInférieurDroit(int finCol, int finRow, HoleBoard board, int color) {
        Pawn p1 = (Pawn) board.getElement(finRow - 1, finCol);
        Pawn p2 = (Pawn) board.getElement(finRow, finCol - 1);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow - 1, finCol, color) || !board.isCapturable(board, finRow, finCol - 1, color)) {
                System.out.println("Impossible, coup interdit au coin inférieur droit");
                return false;
            }
        }
        return true;
    }

    /**
     * Permet de savoir si l'adversaire peut encore joué
     * @param stage
     * @param board
     * @return
     */
    public boolean moveIsOk(HoleStageModel stage, HoleBoard board){
        boolean result = false;

        if (model.getCurrentPlayer().getColor() == 0){
            result = moveIsOkRed(stage, board);
        }
        else {
            result = moveIsOkBlue(stage, board);
        }

        if (!result) callPartyResult(stage, HoleStageModel.Equality);
        System.out.println(result);
        return result;
    }

    public boolean moveIsOkBlue(HoleStageModel stage, HoleBoard board) {
        boolean result = false;
        for (int i = 0; i < stage.getBluePawns().length; i++) {
            if (stage.getBluePawns()[i] != null) {
                if (stage.getBluePawns()[i].getRole() == Pawn.INFANTRYMAN && moveIsOkInfantryman(stage.getBluePawns()[i], stage.getBluePawns()[i].getRow(), stage.getBluePawns()[i].getCol(), board)) {
                    result = true;
                } else if (stage.getBluePawns()[i].getRole() == Pawn.HORSEMAN && moveIsOkHorseman(stage.getBluePawns()[i], stage.getBluePawns()[i].getRow(), stage.getBluePawns()[i].getCol(), board, stage)) {
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean moveIsOkRed(HoleStageModel stage, HoleBoard board) {
        boolean result = false;
        for (int i = 0; i < stage.getRedPawns().length; i++) {
            if (stage.getRedPawns()[i] != null) {
                if (stage.getRedPawns()[i].getRole() == Pawn.INFANTRYMAN && moveIsOkInfantryman(stage.getRedPawns()[i], stage.getRedPawns()[i].getRow(), stage.getRedPawns()[i].getCol(), board)) {
                    result = true;
                } else if (stage.getRedPawns()[i].getRole() == Pawn.HORSEMAN && moveIsOkHorseman(stage.getRedPawns()[i], stage.getRedPawns()[i].getRow(), stage.getRedPawns()[i].getCol(), board, stage)) {
                    result = true;
                }
            }
        }
        return result;
    }

    public void callPartyResult(HoleStageModel stage, int idWinner){
        stage.computePartyResult(idWinner);
    }

    public boolean moveIsOkInfantryman(Pawn pawn, int row, int col, HoleBoard board){
        if (pawn.getColor() == Pawn.PAWN_BLUE) return verifPawnMove(board, pawn.getColor(), col, row, row+1, col);
        else return verifPawnMove(board, pawn.getColor(), col, row, row-1, col);
    }

    public boolean moveIsOkHorseman(Pawn pawn, int row, int col, HoleBoard board, HoleStageModel holeStageModel){

        int[][] temp;
        boolean result = false;

        if (holeStageModel.getBoardArrows1()[row][col] != null) {
            temp = board.getValidCell(model, holeStageModel.getBoardArrows1()[row][col], holeStageModel.getBoardArrows2()[row][col], row, col);
        } else {
            temp = board.getValidCell(model, row, col);
        }

        int i = 0;
        for (int j = 0; j < temp.length; j++){
            if (verifMoveCavalier(board,pawn.getColor(),col, row, temp[i][0], temp[i][1], holeStageModel)) {
                result = true;
            }
            i++;
        }
        return result;
    }
}
