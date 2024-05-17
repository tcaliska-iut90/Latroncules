package model;

import boardifier.control.Logger;
import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

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
/*
    public void setValidCells(int number) {
        Logger.debug("called",this);
        resetReachableCells(false);
        List<Point> valid = computeValidCells(number);
        if (valid != null) {
            for(Point p : valid) {
                reachableCells[p.y][p.x] = true;
            }
        }
    }*/

    public String[] getValidCell(Arrow a1, Arrow a2, int row, int col){
        String[] s = new String[4];
        if (a1.getDirection() == 0 || a2.getDirection() == 0){

        }
        return s;
    }


}
