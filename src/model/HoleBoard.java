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
    public HoleBoard(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 8x8 grid, named "holeboard", and in x,y in space
        super("holeboard", x, y, 8 , 8, gameStageModel);
    }


    public int[][] getValidCell(Model model, Arrow a1, Arrow a2, int row, int col){
        int[][] result = new int[4][2];
        int indexi = 0;
        int[][] temp;
        int colorEnemy = model.getIdPlayer() == 0 ? Pawn.PAWN_RED : Pawn.PAWN_BLUE;

        if (a1.getDirection() == 0 || a2.getDirection() == 0){
            temp = getCellVerticalArrow(row, col);
            result[indexi][0] = temp[0][0];
            result[indexi][1] = temp[0][1];
            result[indexi + 1][0] = temp[1][0];
            result[indexi + 1][1] = temp[1][1];
            indexi +=2;
        }
        if (a1.getDirection() == 1 || a2.getDirection() ==1) {
            temp = getCellHorizontalArrow(row, col);
            result[indexi][0] = temp[0][0];
            result[indexi][1] = temp[0][1];
            result[indexi + 1][0] = temp[1][0];
            result[indexi + 1][1] = temp[1][1];
            indexi +=2;
        }
        
        if (a1.getDirection() == 2 || a2.getDirection() == 2){
            temp = getCellMajorDiagonalArrow(row, col);
            result[indexi][0] = temp[0][0];
            result[indexi][1] = temp[0][1];
            result[indexi + 1][0] = temp[1][0];
            result[indexi + 1][1] = temp[1][1];
            indexi +=2;
        }

        if (a1.getDirection() == 3 || a2.getDirection() == 3){
            temp = getCellMinorDiagonalArrow(row, col);

            result[indexi][0] = temp[0][0];
            result[indexi][1] = temp[0][1];
            result[indexi + 1][0] = temp[1][0];
            result[indexi + 1][1] = temp[1][1];
        }

        for (int i = 0; i < result.length; i++) {
            Pawn p = (Pawn)this.getElement(result[i][0], result[i][1]);
            if (p != null && p.getColor() == colorEnemy){
                result[i][0] = result[i][0] + (result[i][0] - row);
                result[i][1] = result[i][1] + (result[i][1] - col);
            }
        }
        return result;
    }

    private int[][] getCellVerticalArrow(int row, int col){
        int indexi = 0;
        int [][] result =new int[2][2];

        if (row == 0) result[indexi][0] = row;
        else result[indexi][0] = row - 1;
        result[indexi][1] = col;

        indexi += 1;

        if (row == 8) result[indexi][0] = row;
        else result[indexi][0] = row + 1;
        result[indexi][1] = col;

        return result;
    }

    private int[][] getCellHorizontalArrow(int row, int col){
        int indexi = 0;
        int [][] result =new int[2][2];

        result[indexi][0] = row;
        if (col == 0) result[indexi][1] = col;
        else result[indexi][1] = col - 1;

        indexi += 1;

        result[indexi][0] = row + 1;
        if (col == 8)result[indexi][1] = col;
        else result[indexi][1] = col + 1;

        return result;
    }

    private int[][] getCellMajorDiagonalArrow(int row, int col){
        int [][] result =new int[2][2];
        int indexi = 0;

        if (row == 0 || col ==0){
            result[indexi][0] = row;
            result[indexi][1] = col;
        }else {
            result[indexi][0] = row - 1;
            result[indexi][1] = col - 1;
        }
        indexi +=1;

        if (row == 8 || col == 8){
            result[indexi][0] = row;
            result[indexi][1] = col;
        }else {
            result[indexi][0] = row + 1;
            result[indexi][1] = col + 1;
        }
        return result;
    }

    private int[][] getCellMinorDiagonalArrow(int row, int col){
        int [][] result =new int[2][2];
        int indexi = 0;

        if (row == 0 || col ==0){
            result[indexi][0] = row;
            result[indexi][1] = col;
        }else {
            result[indexi][0] = row - 1;
            result[indexi][1] = col + 1;
        }
        indexi +=1;

        if (row == 8 || col == 8){
            result[indexi][0] = row;
            result[indexi][1] = col;
        }else {
            result[indexi][0] = row + 1;
            result[indexi][1] = col - 1;
        }
        return result;
    }
}
