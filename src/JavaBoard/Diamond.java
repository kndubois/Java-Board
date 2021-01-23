package JavaBoard;

/**
 * *    Diamond Class
 *
 * *    Creates the shape of a Diamond
 *
 * *    Extends Piece
 *
 * @author Katie Dubois
 */

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Diamond extends Piece {

    /**
     * 2 argument constructor to allow each parameter to be set.
     * @param cell - cell grid
     * @param color - color selector
     */

    public Diamond(Point2D cell, Color color) {
        super(cell, color);
    }

    /**
     * Override draw components with Diamond as polygon
     * @param g - graphics context to draw
     * @param cell - x width and y height doubled with offset
     */

    @Override
    public void draw(GraphicsContext g, Rectangle2D cell) {
        double width = cell.getWidth() - (cell.getWidth() / 5); // 5% padding on all sides, bottom and right
        double height = cell.getHeight() - (cell.getHeight() / 5); // 5% padding on all sides, top and left
        double x = (this.cell.getX() * cell.getWidth()) + (cell.getWidth() / 10);
        double y = (this.cell.getY() * cell.getHeight() + (cell.getHeight() / 10));
        double topOffset = height / 3;
        y += topOffset / 2;

        double xPoints[] = new double[] { x + width / 30, x + width / 4, x + width - width / 4, x + width - width / 30, x + width / 2 };
        double yPoints[] = new double [] { y + height / 5, y + height / 30, y + height / 30, y + height / 5, y + topOffset*2 };

        g.setFill(this.color);
        g.fillPolygon(xPoints, yPoints, 5);
        g.setStroke(this.selected ? Piece.selectedBorder : Piece.unselectedBorder);
        g.setLineWidth(2);
        g.strokePolygon(xPoints, yPoints, 5);
    }
}
