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


    public int[][] getValidCell(Model model, Arrow a1, Arrow a2, int row, int col){
        System.out.println(a1.getDirection() + ", " + a2.getDirection());
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

        if (row == 8) ValidCell[indexi +1][0] = row;
        else ValidCell[indexi +1][0] = row + 1;
        ValidCell[indexi + 1][1] = col;
    }

    private void getCellHorizontalArrow(int row, int col, int indexi){

        ValidCell[indexi][0] = row;
        if (col == 0) ValidCell[indexi][1] = col;
        else ValidCell[indexi][1] = col - 1;

        ValidCell[indexi + 1][0] = row;
        if (col == 8)ValidCell[indexi +1 ][1] = col;
        else ValidCell[indexi + 1][1] = col + 1;
    }

    private void getCellMajorDiagonalArrow(int row, int col, int indexi){
        if (row == 0 || col ==0){
            ValidCell[indexi][0] = row;
            ValidCell[indexi][1] = col;
        }else {
            ValidCell[indexi][0] = row - 1;
            ValidCell[indexi][1] = col - 1;
        }

        if (row == 8 || col == 8){
            ValidCell[indexi + 1][0] = row;
            ValidCell[indexi + 1][1] = col;
        }else {
            ValidCell[indexi + 1][0] = row + 1;
            ValidCell[indexi + 1][1] = col + 1;
        }
    }

    private void getCellMinorDiagonalArrow(int row, int col, int indexi){
        if (row == 0 || col ==0){
            ValidCell[indexi][0] = row;
            ValidCell[indexi][1] = col;
        }else {
            ValidCell[indexi][0] = row - 1;
            ValidCell[indexi][1] = col + 1;
        }

        if (row == 8 || col == 8){
            ValidCell[indexi + 1][0] = row;
            ValidCell[indexi + 1][1] = col;
        }else {
            ValidCell[indexi + 1][0] = row + 1;
            ValidCell[indexi + 1][1] = col - 1;
        }
    }

    public void takingPawn(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

            }
        }
    }
}
