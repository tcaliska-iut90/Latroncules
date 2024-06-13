package model;

import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;
import boardifier.model.Model;


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


    /**
     *Donne les coup possibles lorsque qu'un cavalier est sur une case fléchés
     * @param model Model du jeu
     * @param a1 Première flèche
     * @param a2 Deuxième flèche
     * @param row Ligne de la case du pion
     * @param col Colonne de la case du pion
     * @return
     */
    public int[][] getValidCell(Model model, Arrow a1, Arrow a2, int row, int col){
        ValidCell = new int[4][2];
        int indexi = 0;
        int colorEnemy = model.getCurrentPlayer().getColor() == 0 ? Pawn.PAWN_RED : Pawn.PAWN_BLUE;

        //Regarde le type de flèches en paramètres
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

        // Si un pion est sur les cases jouable on déplace la case jouable dans la même ligne
        for (int i = 0; i < ValidCell.length; i++) {
            Pawn p = (Pawn)this.getElement(ValidCell[i][0], ValidCell[i][1]);
            if (p != null && p.getColor() == colorEnemy && (ValidCell[i][0] > 0 && ValidCell[i][0] < 7) && (ValidCell[i][1] >  0 && ValidCell[i][1] < 7)){
                ValidCell[i][0] = ValidCell[i][0] + (ValidCell[i][0] - row);
                ValidCell[i][1] = ValidCell[i][1] + (ValidCell[i][1] - col);
            }
        }
        return ValidCell;
    }


    /**
     *Donne les coup possibles lorsque qu'un cavalier est sur une case non fléchés
     * @param model
     * @param row
     * @param col
     * @return
     */
    public int[][] getValidCell(Model model, int row, int col){
        ValidCell = new int[8][2];
        int colorEnemy = model.getCurrentPlayer().getColor() == 0 ? Pawn.PAWN_RED : Pawn.PAWN_BLUE;

        //Récupère tous les coups possibles autour du pion
        getCellVerticalArrow(row, col, 0);
        getCellHorizontalArrow(row, col, 2);
        getCellMajorDiagonalArrow(row, col, 4);
        getCellMinorDiagonalArrow(row, col, 6);

        // Si un pion est sur les cases jouable on déplace la case jouable dans la même ligne

        for (int i = 0; i < ValidCell.length; i++) {
            Pawn p = (Pawn)this.getElement(ValidCell[i][0], ValidCell[i][1]);
            if (p != null && p.getColor() == colorEnemy && (ValidCell[i][0] > 0 && ValidCell[i][0] < 7) && (ValidCell[i][1] >  0 && ValidCell[i][1] < 7)){
                ValidCell[i][0] = ValidCell[i][0] + (ValidCell[i][0] - row);
                ValidCell[i][1] = ValidCell[i][1] + (ValidCell[i][1] - col);
            }
        }
        return ValidCell;
    }

    public int [][] getValidCellFinal(Model model, int row, int col){
        Pawn p = (Pawn)this.getElement(row, col);
        if (p.getRole()==Pawn.HORSEMAN){
            Arrow [][] arrow1 = ((HoleStageModel)gameStageModel).getBoardArrows1();
            Arrow [][] arrow2 = ((HoleStageModel)gameStageModel).getBoardArrows2();
            Arrow a1 = arrow1[row][col];
            Arrow a2 = arrow2[row][col];
            if (a1 != null && a2 != null){
                return getValidCell(model, a1, a2, row, col);
            } else {
                return getValidCell(model, row, col);
            }
        } else if (p.getRole() == Pawn.INFANTRYMAN){
            int [][] validCell = new int[1][2];
            if (p.getColor()==Pawn.PAWN_BLUE){
                //Test mouvement possible en fonction du joueur
                //if (analyseCorrectMove(p.getCol(), p.getRow(), p.getCol(), p.getRow() + 1)) {
                    validCell[0][0] = p.getRow() + 1;
                    validCell[0][1] = p.getCol();
                //}
            } else if (p.getColor() == Pawn.PAWN_RED){
                //Test mouvement possible en fonction du joueur
                //if (analyseCorrectMove(p.getCol(), p.getRow(), p.getCol(), p.getRow() - 1)) {
                    validCell[0][0] = p.getRow() - 1;
                    validCell[0][1] = p.getCol();
                //}
            }
            return validCell;
        }
        return null;
    }

    /**
     * Récupère les cases jouables avec une flèches verticale
     * @param row
     * @param col
     * @param indexi
     */
    private void getCellVerticalArrow(int row, int col, int indexi){

        if (row == 0) ValidCell[indexi][0] = row;
        else ValidCell[indexi][0] = row - 1;
        ValidCell[indexi][1] = col;

        if (row == 7) ValidCell[indexi +1][0] = row;
        else ValidCell[indexi +1][0] = row + 1;
        ValidCell[indexi + 1][1] = col;
    }

    /**
     * Récupère les cases jouables avec une flèches Horizontal
     * @param row
     * @param col
     * @param indexi
     */
    private void getCellHorizontalArrow(int row, int col, int indexi){

        ValidCell[indexi][0] = row;
        if (col == 0) ValidCell[indexi][1] = col;
        else ValidCell[indexi][1] = col - 1;

        ValidCell[indexi + 1][0] = row;
        if (col == 7)ValidCell[indexi +1 ][1] = col;
        else ValidCell[indexi + 1][1] = col + 1;
    }

    /**
     * Récupère les cases jouables avec une flèches diagonal majeur(haut-gauche/bas-droit)
     * @param row
     * @param col
     * @param indexi
     */
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

    /**
     * Récupère les cases jouables avec une flèches diagonale mineur(haut-droit/bas-gauche)
     * @param row
     * @param col
     * @param indexi
     */
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

    /**
     * Verification si un pion autour du pion joueur est capturable
     * @param stageModel
     * @param board
     * @param model
     * @param row
     * @param col
     * @param colorPawn
     */
    public void takingPawn(HoleStageModel stageModel, HoleBoard board, Model model, int row, int col, int colorPawn, int colorEnemy){
        for (int i = row -1; i <= row +1; i++) {
            for (int j = col -1 ; j <= col +1; j++) {
               if ((i >= 0 && i <= 7) && (j >= 0 && j <= 7)) {
                   Pawn p = (Pawn) board.getElement(i, j);
                   if (checkPiece(board, i, j, colorEnemy) && isCapturable(board,i, j, colorPawn)){
                       deletePawnsTaking(stageModel, p, board, colorPawn);
                   }
               }
            }
        }
    }

    /**
     *Cette méthode renvoie un booléen
     * Lorsque le pion sur la case de coordonnée row,col est capturable par l'équipe de la couleur playerColor elle renvoie true
     * False dans le cas contraire
     * @param board
     * @param row
     * @param col
     * @param playerColor
     * @return
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
        if (row > 0 && row < 7 && col > 0 && col < 7 && checkPiece(board, row - 1, col - 1, playerColor) && checkPiece(board, row +1, col + 1, playerColor)) {
            return true;
        }
        // Vérifie alignement oblique haut-droit à bas-gauche
        if (row > 0 && row < 7 && col > 0 && col < 7 && checkPiece(board, row + 1, col - 1, playerColor) && checkPiece(board, row - 1, col + 1, playerColor)) {
            return true;
        }
        // Vérifie les coins pour former un L
        if ((row == 0 || row ==7) && (col == 0 || col == 7))
            if ((row > 0 && checkPiece(board, row - 1, col, playerColor) && ((col > 0 && checkPiece(board, row, col - 1, playerColor)) || (col < 7 && checkPiece(board, row, col + 1, playerColor)))) ||
                    (row < 7 && checkPiece(board, row + 1, col, playerColor) && ((col > 0 && checkPiece(board, row, col - 1, playerColor)) || (col < 7 && checkPiece(board, row, col + 1, playerColor))))) {
                return true;
            }
        return false;
    }



    private void deletePawnsTaking(HoleStageModel stageModel, Pawn pawnEnemy, HoleBoard board, int colorPawn) {
        board.removeElement(pawnEnemy);
        board.setCellReachable(pawnEnemy.getRow(), pawnEnemy.getCol(), true);
        if (colorPawn == Pawn.PAWN_BLUE){
            stageModel.removeRedPawns(pawnEnemy);
            stageModel.addBluePawnsTaking(pawnEnemy);
        }
        else {
            stageModel.removeBluePawns(pawnEnemy);
            stageModel.addRedPawnsTaking(pawnEnemy);
        }
    }

    public boolean checkPiece(HoleBoard board, int row, int col, int playerColor){
        Pawn p = (Pawn) board.getElement(row, col);
        return p != null && p.getColor() == playerColor;
    }
}
