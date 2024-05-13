package view;

import boardifier.model.GameElement;
import boardifier.view.ConsoleColor;
import boardifier.view.ElementLook;
import model.Pawn;

/**
 * The look of the Pawn is fixed, with a role
 * and a blue or red color.
 */
public class PawnLook extends ElementLook {

    public PawnLook(GameElement element) {
        super(element, 1, 1);
    }

    protected void render() {

        Pawn pawn = (Pawn)element;
        if (pawn.getColor() == Pawn.PAWN_BLUE && pawn.getRole() == Pawn.HORSEMAN) {
            shape[0][0] = ConsoleColor.WHITE + ConsoleColor.BLUE_BACKGROUND + pawn.getRole() + ConsoleColor.RESET;
        }

        else if (pawn.getColor() == Pawn.PAWN_RED && pawn.getRole() == Pawn.HORSEMAN) {
            shape[0][0] = ConsoleColor.WHITE + ConsoleColor.RED_BACKGROUND + pawn.getRole() + ConsoleColor.RESET;
        }
        else if (pawn.getColor() == Pawn.PAWN_BLUE && pawn.getRole() == Pawn.INFANTRYMAN) {
            shape[0][0] = ConsoleColor.WHITE + ConsoleColor.BLUE_BACKGROUND + pawn.getRole() + ConsoleColor.RESET;
        }
        else {
            shape[0][0] = ConsoleColor.WHITE + ConsoleColor.RED_BACKGROUND + pawn.getRole() + ConsoleColor.RESET;
        }
    }
}
