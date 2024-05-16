package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class Arrow extends GameElement {

    public static int VERTICAL = 0;
    public static int HORIZONTAL = 1;
    public static int MAJOR_DIAGONAL = 2;
    public static int MINOR_DIAGONAL = 3;

    
    private int direction;

    public Arrow(int direction ,GameStageModel gameStageModel){
        super(gameStageModel);
        ElementTypes.register("arrow",51);
        type = ElementTypes.getType("arrow");
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
