package JavaBoard;

/**
 * *    Piece Class
 *
 * *    Represents pieces and cells
 *
 * @author Katie Dubois
 */

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

abstract public class Piece {

    protected static final Color selectedBorder = Color.BLACK;     // selected border black
    protected static final Color unselectedBorder = Color.BLACK;   // unselected border black

    protected Point2D cell;    // cell dimension
    protected Color color;     // color

    protected boolean selected = false;   // selected cells

    /**
     * 2 argument constructor for Piece to allow each parameter to be set
     *
     * Creates piece
     * @param cell - Cell of piece
     * @param color - Color of piece
     */

    public Piece(Point2D cell, Color color) {
        this.cell = cell;
        this.color = color;
    }

    /**
     * Draws the piece
     * @param g - Graphics to draw with
     * @param cell - Dimensions of the cell to draw within
     */

    abstract public void draw(GraphicsContext g, Rectangle2D cell);

    /**
     * Return if selected
     * @return - If selected
     */

    public boolean isSelected() { return this.selected; }

    /**
     * Set if selected
     * @param selected - If selected
     */

    public void setSelected(boolean selected) { this.selected = selected; }

    /**
     * Gets Cell
     * @return - Cell
     */

    public Point2D getCell() { return this.cell; }

    /**
     * Sets cell
     * @param cell - Cell
     */

    public void setCell(Point2D cell) { this.cell = cell; }
}
