package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.Arrow;
import model.Pawn;

public class ArrowLook extends ElementLook {

    private Line line;
    private int col;
    private int row;
    public ArrowLook(GameElement element){
        super(element);
    }

    @Override
    protected void render() {
        /*
        Arrow arrow = (Arrow) element;


        if (arrow.getDirection() == Arrow.VERTICAL) {
            line = new Line(200, 50, 200, 350);
        }
        else if (arrow.getDirection() == Arrow.HORIZONTAL){
            line = new Line(50, 200, 350, 200);
        }
        else if (arrow.getDirection() == Arrow.MAJOR_DIAGONAL) {
            line =new Line(50, 50, 350, 350);
        }
        else if (arrow.getDirection() == Arrow.MINOR_DIAGONAL) {
            line = new Line(350, 50, 50, 350);
        }

        line = new Line(30, 30, 130, 130);
        addShape(line);
        */

    }
}
