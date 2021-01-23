package JavaBoard;

/**
 * *    Rectangle Class
 *
 * *    Creates the shape of a Rectangle
 * *
 * *    Extends Piece
 *
 * @author Katie Dubois
 */

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends Piece {

    /**
     * 2 argument constructor to allow each parameter to be set.
     * @param cell - cell grid
     * @param color - color selector
     */

    public Rectangle(Point2D cell, Color color) {
        super(cell, color);
    }

    /**
     * Override draw components with Rectangle as Rect
     * @param g - graphics context to draw
     * @param cell - x width and y height doubled
     */

    @Override
    public void draw(GraphicsContext g, Rectangle2D cell) {
        double width = cell.getWidth() - (cell.getWidth() / 5); // 5% padding on all sides, bottom and right
        double height = cell.getHeight() - (cell.getHeight() / 5); // 5% padding on all sides, top and left
        double x = (this.cell.getX() * cell.getWidth()) + (cell.getWidth() / 10);
        double y = (this.cell.getY() * cell.getHeight() + (cell.getHeight() / 10));
        g.setFill(this.color);
        g.fillRect(x, y, width, height);
        g.setStroke(this.selected ? Piece.selectedBorder : Piece.unselectedBorder);
        g.setLineWidth(2);
        g.strokeRect(x, y, width, height);
    }
}