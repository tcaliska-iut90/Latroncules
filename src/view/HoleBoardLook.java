package view;

import boardifier.model.ContainerElement;
import boardifier.view.ClassicBoardLook;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import utils.FileUtils;

public class HoleBoardLook extends ClassicBoardLook {

    public HoleBoardLook(int size, ContainerElement element) {
        // NB: To have more liberty in the design, GridLook does not compute the cell size from the dimension of the element parameter.
        // If we create the 3x3 board by adding a border of 10 pixels, with cells occupying all the available surface,
        // then, cells have a size of (size-20)/3
        super((size-20)/3, element, -1,Color.BEIGE, Color.DARKGRAY,0, Color.BLACK, 10, Color.BLACK, true);
    }

    @Override
    protected void render() {
        super.render();

        ImagePattern imagePattern = new ImagePattern(new Image("file:" + FileUtils.getFileFromResources("latroncules-assets/latroncules-plateau.png").getAbsolutePath()));

        Rectangle rectangle = new Rectangle((8*colWidth + gapXToCells) - 15 , (8*rowHeight + gapYToCells) - 22);
        rectangle.setX(gapXToCells - frameWidth);
        rectangle.setY(gapYToCells - frameWidth);
        rectangle.setFill(imagePattern);

        addShape(rectangle);
    }
}
