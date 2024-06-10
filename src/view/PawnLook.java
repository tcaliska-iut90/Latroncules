package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Pawn;

public class PawnLook extends ElementLook {
    private Circle circle;
    private int radius;

    private Canvas canvas;

    public PawnLook(int radius, GameElement element) {
        super(element);
        this.radius = radius;
        render();
    }

    @Override
    public void onSelectionChange() {
        Pawn pawn = (Pawn)getElement();
        /*
        if (pawn.isSelected()) {
            circle.setStrokeWidth(3);
            circle.setStrokeMiterLimit(10);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.valueOf("0x333333"));
        }
        else {
            circle.setStrokeWidth(0);
        }

         */
    }

    @Override
    public void onFaceChange() {
    }

    protected void render() {
        Pawn pawn = (Pawn)element;
        Color color;
        Group root = new Group();
        if (pawn.getColor() == Pawn.PAWN_RED) color = Color.RED;
        else color = Color.BLUE;
        if (pawn.getRole() == Pawn.INFANTRYMAN) {
            // Calculate the center of the canvas
            double centerX = 0 / 2;
            double centerY = 0 / 2;
            // Draw the hat
            Rectangle hat = new Rectangle(centerX - 7, centerY - 22, 14, 4);
            hat.setFill(color);

            // Draw the head
            Circle head = new Circle(centerX, centerY - 14, 6);
            head.setFill(color);

            // Draw the body
            Rectangle body = new Rectangle(centerX - 7, centerY - 8, 14, 20);
            body.setFill(color);

            // Draw the shield
            Rectangle shield = new Rectangle(centerX - 12, centerY - 8, 6, 20);
            shield.setFill(color);
            shield.setStroke(color);

            // Draw the sword
            Line sword = new Line(centerX + 7, centerY - 8, centerX + 18, centerY - 8);
            sword.setStroke(color);

            // Draw the legs
            Rectangle leg1 = new Rectangle(centerX - 7, centerY + 12, 5, 15);
            leg1.setFill(color);
            Rectangle leg2 = new Rectangle(centerX + 2, centerY + 12, 5, 15);
            leg2.setFill(color);

            // Add all shapes to the root group
            root.getChildren().addAll(hat, head, body, shield, sword, leg1, leg2);
            addNode(root);
        }else {
            circle = new Circle();
            circle.setRadius(radius);
            if (pawn.getColor() == Pawn.PAWN_RED) {
                circle.setFill(Color.RED);
            }
            else {
                circle.setFill(Color.BLUE);
            }
            addShape(circle);

        }

    }
}
