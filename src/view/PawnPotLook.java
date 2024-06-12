package view;

import boardifier.model.ContainerElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


public class PawnPotLook extends GridLook {

    // the array of rectangle composing the grid
    private Rectangle[][] cells;

    public PawnPotLook(int height, int width,  ContainerElement element) {
        super(height/4, width, element, -1, 1, Color.BLACK);

    }

    protected void render() {
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
        cells = new Rectangle[2][8];
        // create the rectangles.
        for(int i=0;i<2;i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new Rectangle(colWidth, rowHeight, Color.WHITE);
                cells[i][j].setStrokeWidth(3);
                cells[i][j].setStrokeMiterLimit(10);
                cells[i][j].setStrokeType(StrokeType.CENTERED);
                cells[i][j].setStroke(Color.valueOf("0x333333"));
                cells[i][j].setX(colWidth * i + borderWidth);
                cells[i][j].setY(j * rowHeight + borderWidth);
                addShape(cells[i][j]);
            }
        }
    }

}
