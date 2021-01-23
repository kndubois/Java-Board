package JavaBoard;

/**
 * *    Circle Class
 *
 * *    Creates the shape of a Circle
 *
 * *    Extends Piece
 *
 * @author Katie Dubois
 */

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle extends Piece {

    /**
     * 2 argument constructor to allow each parameter to be set.
     * @param cell - cell grid
     * @param color - color selector
     */

    public Circle(Point2D cell, Color color) {
        super(cell, color);
    }

    /**
     * Override draw components with Circle as Oval
     * @param g - graphics context to draw
     * @param cell - x width and y height doubled
     */

    @Override
    public void draw(GraphicsContext g, Rectangle2D cell) {
        double width = cell.getWidth() - (cell.getWidth() / 5);
        double height = cell.getHeight() - (cell.getHeight() / 5);
        double x = (this.cell.getX() * cell.getWidth()) + (cell.getWidth() / 10);
        double y = (this.cell.getY() * cell.getHeight() + (cell.getHeight() / 10));
        g.setFill(this.color);
        g.fillOval(x, y, width, height);
        g.setStroke(this.selected ? Piece.selectedBorder : Piece.unselectedBorder);
        g.setLineWidth(2);
        g.strokeOval(x, y, width, height);
    }
}