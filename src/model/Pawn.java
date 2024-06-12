package model;

import boardifier.control.Logger;
import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.animation.Animation;
import boardifier.model.animation.AnimationStep;

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

    public void setRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }
    //Modifier le nom de la méthode
    public int getColor() {
        return color;
    }

    public int getRow() {
        return (int)((this.getY()-1)/2);
    }

    public int getCol() {
        return (int)((this.getX()-3)/6);
    }

    public int getRow2() {
        return (int)(this.getY()/80.1);
    }

    public int getCol2() {
        return (int)(this.getX()/71.1);
    }

    public void update() {
        // if must be animated, move the pawn
        if (animation != null) {
            AnimationStep step = animation.next();
            if (step == null) {
                animation = null;
            }
            else if (step == Animation.NOPStep) {
                Logger.debug("nothing to do", this);
            }
            else {
                Logger.debug("move animation", this);
                setLocation(step.getInt(0), step.getInt(1));
            }
        }
    }
}
