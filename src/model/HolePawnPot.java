package model;

import boardifier.model.ContainerElement;
import boardifier.model.GameStageModel;

/**
 * Hole pot for pawns represent the element where pawns are stored at the beginning of the party.
 * Thus, a simple ContainerElement with 4 rows and 1 column is needed.
 */
public class HolePawnPot extends ContainerElement {
    public HolePawnPot(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 4x1 grid, named "pawnpot", and in x,y in space
        super("pawnpot", x, y, 1, 32, gameStageModel);
    }
}
