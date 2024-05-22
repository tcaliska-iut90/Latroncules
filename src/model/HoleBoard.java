package model;

import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;
import boardifier.model.Model;

import java.awt.*;


/**
 * Hole main board represent the element where pawns are put when played
 * Thus, a simple ContainerElement with 3 rows and 3 column is needed.
 * Nevertheless, in order to "simplify" the work for the controller part,
 * this class also contains method to determine all the valid cells to put a
 * pawn with a given value.
 */
public class HoleBoard extends ContainerElement {
    private int[][] ValidCell;
    public HoleBoard(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 8x8 grid, named "holeboard", and in x,y in space
        super("holeboard", x, y, 8 , 8, gameStageModel);
    }


    /*
    Donne les coup possibles lorsque qu'un cavalier est sur une case fléchés
     */
    public int[][] getValidCell(Model model, Arrow a1, Arrow a2, int row, int col){
        ValidCell = new int[4][2];
        int indexi = 0;
        int colorEnemy = model.getIdPlayer() == 0 ? Pawn.PAWN_RED : Pawn.PAWN_BLUE;

        if (a1.getDirection() == 0 || a2.getDirection() == 0){
            getCellVerticalArrow(row, col, indexi);
            indexi += 2;
        }
        if (a1.getDirection() == 1 || a2.getDirection() ==1) {
            getCellHorizontalArrow(row, col, indexi);
            indexi += 2;
        }
        if (a1.getDirection() == 2 || a2.getDirection() == 2){
            getCellMajorDiagonalArrow(row, col, indexi);
            indexi += 2;
        }
        if (a1.getDirection() == 3 || a2.getDirection() == 3){
            getCellMinorDiagonalArrow(row, col, indexi);
        }

        for (int i = 0; i < ValidCell.length; i++) {
            Pawn p = (Pawn)this.getElement(ValidCell[i][0], ValidCell[i][1]);
            if (p != null && p.getColor() == colorEnemy){
                ValidCell[i][0] = ValidCell[i][0] + (ValidCell[i][0] - row);
                ValidCell[i][1] = ValidCell[i][1] + (ValidCell[i][1] - col);
            }
        }
        return ValidCell;
    }


    /*
    Donne les coup possibles lorsque qu'un cavalier est sur une case non fléchés
     */
    public int[][] getValidCell(Model model, int row, int col){
        ValidCell = new int[8][2];
        int colorEnemy = model.getIdPlayer() == 0 ? Pawn.PAWN_RED : Pawn.PAWN_BLUE;

        getCellVerticalArrow(row, col, 0);
        getCellHorizontalArrow(row, col, 2);
        getCellMajorDiagonalArrow(row, col, 4);
        getCellMinorDiagonalArrow(row, col, 6);

        for (int i = 0; i < ValidCell.length; i++) {
            Pawn p = (Pawn)this.getElement(ValidCell[i][0], ValidCell[i][1]);
            if (p != null && p.getColor() == colorEnemy){
                ValidCell[i][0] = ValidCell[i][0] + (ValidCell[i][0] - row);
                ValidCell[i][1] = ValidCell[i][1] + (ValidCell[i][1] - col);
            }
        }
        return ValidCell;
    }

    private void getCellVerticalArrow(int row, int col, int indexi){

        if (row == 0) ValidCell[indexi][0] = row;
        else ValidCell[indexi][0] = row - 1;
        ValidCell[indexi][1] = col;

        if (row == 7) ValidCell[indexi +1][0] = row;
        else ValidCell[indexi +1][0] = row + 1;
        ValidCell[indexi + 1][1] = col;
    }

    private void getCellHorizontalArrow(int row, int col, int indexi){

        ValidCell[indexi][0] = row;
        if (col == 0) ValidCell[indexi][1] = col;
        else ValidCell[indexi][1] = col - 1;

        ValidCell[indexi + 1][0] = row;
        if (col == 7)ValidCell[indexi +1 ][1] = col;
        else ValidCell[indexi + 1][1] = col + 1;
    }

    private void getCellMajorDiagonalArrow(int row, int col, int indexi){
        if (row == 0 || col ==0){
            ValidCell[indexi][0] = row ;
            ValidCell[indexi][1] = col;
        }else {
            ValidCell[indexi][0] = row - 1;
            ValidCell[indexi][1] = col - 1;
        }

        if (row == 7 || col == 7){
            ValidCell[indexi + 1][0] = row;
            ValidCell[indexi + 1][1] = col;
        }else {
            ValidCell[indexi + 1][0] = row + 1;
            ValidCell[indexi + 1][1] = col + 1;
        }
    }

    private void getCellMinorDiagonalArrow(int row, int col, int indexi){
        if (row == 0 || col == 7){
            ValidCell[indexi][0] = row;
            ValidCell[indexi][1] = col;
        }else {
            ValidCell[indexi][0] = row - 1;
            ValidCell[indexi][1] = col + 1;
        }

        if (row == 7 || col == 0){
            ValidCell[indexi + 1][0] = row;
            ValidCell[indexi + 1][1] = col;
        }else {
            ValidCell[indexi + 1][0] = row + 1;
            ValidCell[indexi + 1][1] = col - 1;
        }
    }

    public void takingPawn(HoleStageModel stageModel, HoleBoard board, Model model, int row, int col, int colorPawn){
        int colorEnemy = model.getIdPlayer() == 0 ? Pawn.PAWN_RED : Pawn.PAWN_BLUE;

        for (int i = row -1; i <= row +1; i++) {
            for (int j = col -1 ; j <= col +1; j++) {
               if ((i >= 0 && i <= 7) && (j >= 0 && j <= 7)) {
                   Pawn p = (Pawn) board.getElement(i, j);
                   if (checkPiece(board, i, j, colorEnemy) && isCapturable(board,i, j, colorPawn)){
                       deletePawnsTaking(stageModel, p, board, colorPawn);
                       //System.out.println("Le joueur" + model.getCurrentPlayer().getName() + " à pris le pion se trouvant au coordoné " + (String.valueOf(col) + 'A') + (String.valueOf(row) + '1'));
                   }
               }
            }
        }
    }

    /*
    Cette méthode renvoie un booléen lorsque le pion sur la case de coordonnée row,col est capturable par l'équipe de la couleur playerColor
     */
    public boolean isCapturable(HoleBoard board, int row, int col, int playerColor) {
        // Vérifie alignement vertical
        if (row > 0 && row < 7 && checkPiece(board, row - 1, col, playerColor) && checkPiece(board, row + 1, col, playerColor)) {
            return true;
        }
        // Vérifie alignement horizontal
        if (col > 0 && col < 7 && checkPiece(board, row, col - 1, playerColor) && checkPiece(board, row, col + 1, playerColor)) {
            return true;
        }
        // Vérifie alignement oblique haut-gauche à bas-droite
        if (row > 0 && row < 7 && col > 0 && col < 7 && checkPiece(board, row - 1, col - 1, playerColor) && checkPiece(board, row + 1, col + 1, playerColor)) {
            return true;
        }
        // Vérifie alignement oblique haut-droit à bas-gauche
        if (row > 0 && row < 7 && col > 0 && col < 7 && checkPiece(board, row + 1, col - 1, playerColor) && checkPiece(board, row - 1, col + 1, playerColor)) {
            return true;
        }
        // Vérifie les coins pour former un L
        if ((row > 0 && checkPiece(board, row - 1, col, playerColor) && ((col > 0 && checkPiece(board, row, col - 1, playerColor)) || (col < 7 && checkPiece(board, row, col + 1, playerColor)))) ||
                (x < 7 && checkPiece(board, row + 1, col, playerColor) && ((col > 0 && checkPiece(board, row, col - 1, playerColor)) || (col < 7 && checkPiece(board, row, col + 1, playerColor))))) {
            return true;
        }
        return false;
    }



    private void deletePawnsTaking(HoleStageModel stageModel, Pawn pawnEnemy, HoleBoard board, int colorPawn) {
        board.removeElement(pawnEnemy);
        if (colorPawn == Pawn.PAWN_BLUE){
            stageModel.addBluePawnsTaking(pawnEnemy);
            stageModel.removeRedPawns(pawnEnemy);

        }
        else {
            stageModel.addRedPawnsTaking(pawnEnemy);
            stageModel.removeBluePawns(pawnEnemy);
        }

    }

    public boolean checkPiece(HoleBoard board, int row, int col, int playerColor){
        Pawn p = (Pawn) board.getElement(row, col);
        return p != null && p.getColor() == playerColor;
    }
}
