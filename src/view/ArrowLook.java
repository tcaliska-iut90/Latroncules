package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.shape.Line;


public class ArrowLook extends ElementLook {

    private Line line;
    private int col;
    private int row;
    public ArrowLook(GameElement element){
        super(element);
    }

    @Override
    protected void render() {

    }
}
