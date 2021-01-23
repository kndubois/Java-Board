package JavaBoard;


/**
 * *    Board Class
 *
 * *    Creates Canvas of board grid
 *
 * *    Extends Canvas
 *
 * @author Katie Dubois
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Board extends Canvas {

    private static final int percentageFilled = 25; // percentage of empty cells to fill
    public static final int initialCellSize = 50;   // initial width and height of cells
    private static final Color backgroundColor = Color.rgb(245, 229, 205); // grid background color
    private static final Color selectedCellColor = Color.rgb(215, 199, 175); // selected cell color

    private State state;        // declares shapes, cells, pieces
    private Controls controls;  // declares controls with buttons

    private Rectangle2D cellDimension; // declares cell dimension

    /**
     * Creates board
     * @param state - State to draw the board
     */

    public Board(final State state) {
        super();
        this.state = state;
        ChangeListener<Number> resizeListener = new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                refresh();
            }
        };
        this.heightProperty().addListener(resizeListener);
        this.widthProperty().addListener(resizeListener);
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                Point2D cell = coordinateToCell(e.getX(), e.getY());
                if (state.isOutOfBounds(cell)) return;
                state.setSelectedCell(cell);
                update();
            }
        });
    }

    /**
     * @param controls - sets controls
     * @return controls
     */

    public Board setControls(Controls controls) {
        this.controls = controls;
        return this;
    }

    /**
     * Notifies consumer that state has changed and refreshes
     */

    private void update() {
        if (this.controls != null) this.controls.refresh();
        this.refresh();
    }

    /**
     * Recalculates cell dimensions and redraw the board
     */

    public void refresh() {
        this.calculateCellDimensions();
        this.draw(this.getGraphicsContext2D());
    }

    /**
     * Converts x and y coordination to board cell
     * @param x - X position
     * @param y - Y position
     * @return - Cell of X and Y
     */

    private Point2D coordinateToCell(double x, double y) {
        return new Point2D (
                Math.floor(x / this.cellDimension.getWidth()),
                Math.floor(y / this.cellDimension.getHeight())
        );
    }

    /**
     * Calculates cell dimension based on board size and number of rows/columns
     */

    public void calculateCellDimensions() {
        this.cellDimension = new Rectangle2D(
                0,
                0,
                this.getWidth() / this.state.columns,
                this.getHeight() / this.state.rows);
    }

    /**
     * Automatically sets rows and columns based on size, and populate.
     */

    public void populate() {
        this.state.autoSize(this.getWidth(), this.getHeight(), Board.initialCellSize);
        this.calculateCellDimensions();
        this.state.populate(Board.percentageFilled);
        this.update();
    }

    /**
     * draws graphicscontent board with pixels in grid and cells
     * @param g - graphicscontent board
     */

    // cell boxes
    private void draw(GraphicsContext g) {
        // clears board with background color
        g.setFill(Board.backgroundColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        // draw column and row lines - board grid
        g.setStroke(Color.BLACK);
        g.setLineWidth(1);
        for (int c = 0; c <= this.state.columns; c++){
            g.strokeLine(
                    c * this.cellDimension.getWidth(),
                    0,
                    c * this.cellDimension.getWidth(),
                    this.cellDimension.getHeight() * this.state.rows);
        }
        for (int r = 0; r <= this.state.rows; r++){
            g.strokeLine(
                    0,
                    r * this.cellDimension.getHeight(),
                    this.cellDimension.getWidth() * this.state.columns,
                    r * this.cellDimension.getHeight());
        }
        // draw the selected cell differently one pixel larger in each direction to other cells
        Point2D selectedCell = this.state.getSelectedCell();
        if (selectedCell != null){
            g.setFill(Board.selectedCellColor);
            g.fillRect(
                    // fits cell background with border
                    selectedCell.getX() * this.cellDimension.getWidth(),
                    selectedCell.getY() * this.cellDimension.getHeight(),
                    this.cellDimension.getWidth()+1,
                    this.cellDimension.getHeight()+1
            );
            g.setStroke(Color.BLACK);
            g.strokeRect(
                    // fits cell background with border
                    selectedCell.getX() * this.cellDimension.getWidth()-1,
                    selectedCell.getY() * this.cellDimension.getHeight()-1,
                    this.cellDimension.getWidth()+2,
                    this.cellDimension.getHeight()+2
            );
        }
        // draws all pieces
        for (Piece piece: this.state.pieces){
            piece.draw(g, this.cellDimension);
        }
    }
}