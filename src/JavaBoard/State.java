package JavaBoard;

/**
 * *    State Class
 *
 * *    Rows and Columns
 *
 * *    Declares piece types
 *      - Circle
 *      - Rectangle
 *      - Triangle
 *      - Diamond
 *
 * @author Katie Dubois
 */

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class State {

    public static final String[] pieceTypes = {"Circle", "Rectangle", "Triangle", "Diamond"}; // piece types
    public ArrayList<Piece> pieces = new ArrayList<Piece>(); // array list

    public int rows = 1;     // number of rows
    public int columns = 1;  // number of columns

    private Point2D selectedCell = new Point2D(0, 0); // pieces of selected cells

    /**
     * Gets random piece types
     * @param random - random
     * @return - randomized type of pieces
     */

    public static String randompieceType(Random random) {
        return State.pieceTypes[random.nextInt(State.pieceTypes.length)];
    }

    /**
     * Creates new piece of type, with cell and color
     * @param type - Type of piece to make
     * @param cell - Cell of piece
     * @param color - Color of piece
     * @return - Piece of type with cell and color
     */

    public static Piece newPiece(String type, Point2D cell, Color color) {
        if (type.equals("Circle")) return new Circle(cell, color);
        if (type.equals("Rectangle")) return new Rectangle(cell, color);
        if (type.equals("Triangle")) return new Triangle(cell, color);
        if (type.equals("Diamond")) return new Diamond(cell, color);
        return null;
    }

    /**
     * Gets random color
     * @param random - integer bound at 255
     * @return - Random color
     */

    public static Color randomColor(Random random) {
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    /**
     * Gets currently selected piece
     * @return - Selected piece
     */

    public Piece getSelectedPiece() {
        for (Piece piece : this.pieces) {
            if (piece.isSelected()) return piece;
        }
        return null;
    }

    /**
     * Gets piece located at cell
     * @param cell - tyoe of piece at cell
     * @return - Piece located at cell
     */

    public Piece getPieceAt(final Point2D cell) {
        for (Piece piece : this.pieces) {
            if (piece.getCell().equals(cell)) return piece;
        }
        return null;
    }

    /**
     * Picks up selected cell
     * @return - selected cell
     */

    public Point2D getSelectedCell() {
        if (this.selectedCell != null) return this.selectedCell;
        Piece selectedPiece = this.getSelectedPiece();
        return selectedPiece == null ? null : selectedPiece.getCell();
    }

    /**
     * Selects a piece, automatically unselecting other pieces and updating the selected cell
     * @param piece - Piece to select
     */

    public void selectPiece(Piece piece) {
        for (Piece lpiece : this.pieces) {
            if (lpiece.isSelected()) lpiece.setSelected(false);
        }
        piece.setSelected(true);
        if (this.selectedCell != null && this.selectedCell.equals(piece.getCell())) this.selectedCell = null;
    }

    /**
     * Adds piece, automatically selecting it if it's within selected cell
     * @param piece - Piece to add
     */

    public void addPiece(Piece piece) {
        this.pieces.add(piece);
        if (this.selectedCell != null && piece.getCell().equals(this.selectedCell)) this.selectPiece(piece);
    }

    /**
     * Sets currently selected cell
     * @param cell - Cell to select
     */

    public void setSelectedCell(Point2D cell) {
        this.selectedCell = null;
        Piece piece = this.getPieceAt(cell);
        if (piece != null) this.selectPiece(piece);
        else this.selectedCell = cell;
    }

    /**
     * Sets number of rows and columns to be a certain size
     * @param width - sets a certain size
     * @param height - sets a certain size
     * @param desiredSize - width and height of cell
     */

    public void autoSize(double width, double height, int desiredSize) {
        this.rows = (int) Math.max(1, height / desiredSize);
        this.columns = (int) Math.max(1, width / desiredSize);
        this.removeOutOfBoundsPieces();
    }

    /**
     * Removes pieces that are out of bounds
     */

    public void removeOutOfBoundsPieces() {
        this.pieces.removeIf(piece -> this.isOutOfBounds(piece.getCell()));
    }

    /**
     * Return if cell is out of bounds
     * @param cell - cell to check
     * @return - if cell is out of bounds
     */

    public boolean isOutOfBounds(Point2D cell) {
        return cell.getX() < 0 || cell.getY() < 0 || cell.getX() >= this.columns || cell.getY() >= rows;
    }

    /**
     * Populates 0-100 percentage from empty cells with random pieces
     * @param percentage - percentage 0-100 percentage from empty cells to populate
     */

    public void populate(int percentage) {
        Random random = new Random();
        int cellsToFill = pieces.size() + (int) (this.rows * this.columns * ((double) percentage / 100));
        if (cellsToFill >= this.rows * this.columns) cellsToFill = this.rows * this.columns;
        while (pieces.size() < cellsToFill) {
            Point2D cell = new Point2D(random.nextInt(this.columns), random.nextInt(this.rows));
            if (this.getPieceAt(cell) != null) continue;
            this.addPiece(State.newPiece(State.randompieceType(random), cell, State.randomColor(random)));
        }
    }
}
