package view;

import boardifier.model.GameElement;
import boardifier.view.ConsoleColor;
import boardifier.view.ElementLook;
import model.Arrow;

public class ArrowLook extends ElementLook {

    public ArrowLook(GameElement element){
        super(element, 1, 1);
    }

    @Override
    protected void render() {
        /*
        Arrow arrow = (Arrow) element;
        if (arrow.getDirection() == Arrow.VERTICAL){
            shape[0][0] = ConsoleColor.WHITE + ConsoleColor.RED_BACKGROUND + "\u2195" + ConsoleColor.RESET;
        } else if (arrow.getDirection() == Arrow.HORIZONTAL) {
            shape[0][0] = ConsoleColor.WHITE + ConsoleColor.RED_BACKGROUND + "\u2194" + ConsoleColor.RESET;
        } else if (arrow.getDirection() == Arrow.MAJOR_DIAGONAL) {
            shape[0][0] = ConsoleColor.WHITE + "\u2199" + "\u2197" + ConsoleColor.RESET;
        }
        else {
            shape[0][0] = ConsoleColor.WHITE + "\u2196" + "\u2198" +ConsoleColor.RESET;
        }

         */

    }
}
