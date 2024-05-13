package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

/**
 * A basic pawn element, with only 2 fixed parameters : number and color
 * There are no setters because the state of a Hole pawn is fixed.
 */
public class Pawn extends GameElement {

    private int role;
    private int color;
    public static int PAWN_BLUE = 0;
    public static int PAWN_RED = 1;

    public static int HORSEMAN = 0;
    public static int INFANTRYMAN = 1;

    public Pawn(int role, int color, GameStageModel gameStageModel) {
        super(gameStageModel);
        // registering element types defined especially for this game
        ElementTypes.register("pawn",50);
        type = ElementTypes.getType("pawn");
        this.role = role;
        this.color = color;
    }

    public int getRole() {
        return role;
    }
    //Modifier le nom de la méthode
    public int getColor() {
        return color;
    }

}
