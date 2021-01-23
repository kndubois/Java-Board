package JavaBoard;

/**
 * *    Controls Class
 *
 * *    Represents Controls
 * *    Color selector, with 4 options of shapes
 * *    Populate, Delete, Add/Edit
 *
 * *    Extends VBox
 *
 * @author Katie Dubois
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;

public class Controls extends VBox {

    private static final Color initialColor = Color.RED; // initial color of color selector
    private State state;                    // declares shapes, cells, pieces
    private Board board;                    // declares cell dimension and grid

    private Button addEdit;                 // add/edit button
    private TextField row;                  // row
    private TextField column;               // column
    private ComboBox<String> pieceType;     // shape piece
    private ColorPicker colorPicker;        // color selector

    private TextField populatePercentage;   // populate percentage
    private TextField rows;                 // number of rows
    private TextField columns;              // number of columns

    private Button updateButton;            // update button
    private Button deleteSelected;          // delete selected button
    private Button populateButton;          // populate button

    private ChangeListener<String> selectedListener; // string

    /**
     * Creates Controls component
     * @param state - State to populate fields with
     */

    public Controls(final State state) {
        super();
        this.state = state;

        // horizontal cell representation: "Cell: [ROW] [COLUMN]"
        HBox selected = new HBox();
        selected.setAlignment(Pos.CENTER);
        // cell section
        selected.getChildren().add(new Label("Cell: ")); // creates label
        // updates selected cell when row/column is changed
        this.selectedListener = new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    final Point2D newCell = new Point2D(Integer.parseInt(column.getText()) - 1, Integer.parseInt(row.getText()) - 1);
                    if (state.isOutOfBounds(newCell) || state.getSelectedCell().equals(newCell)) return;
                    // only updates selected cell if cell is valid within bounds and not selected
                    state.setSelectedCell(newCell);
                    update();
                } catch (NumberFormatException e) { }
            }
        };

        this.row = new TextField();
        this.row.textProperty().addListener(this.selectedListener);
        selected.getChildren().add(this.row);

        this.column = new TextField();
        this.column.textProperty().addListener(this.selectedListener);
        selected.getChildren().add(this.column);

        this.getChildren().add(selected);

        // modifies current piece in this grid with shape/color
        GridPane content = new GridPane();
        content.setHgap(10);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(33);
        content.getColumnConstraints().add(cc);
        this.getChildren().add(content);
        GridPane pieceGrid = new GridPane();
        content.add(pieceGrid, 0, 0); // position of grid

        // shape section: <combo>
        Label shapeName = new Label("\tShape: "); // creates label
        shapeName.setMaxWidth(Double.MAX_VALUE);
        shapeName.setAlignment(Pos.BASELINE_RIGHT);
        pieceGrid.add(shapeName, 0, 0);
        this.pieceType = new ComboBox<String>(FXCollections.observableList((Arrays.asList(State.pieceTypes))));
        this.pieceType.setValue(State.pieceTypes[0]);
        pieceGrid.add(pieceType, 1, 0); // position of shape

        // color section: <change>
        Label colorName = new Label("Color: "); // creates label
        colorName.setMaxWidth(Double.MAX_VALUE);
        colorName.setAlignment(Pos.BASELINE_RIGHT);
        pieceGrid.add(colorName, 0, 1);
        this.colorPicker = new ColorPicker(Controls.initialColor);
        pieceGrid.add(this.colorPicker, 1, 1); // position of color

        // add / edit section: <add/edit>
        this.addEdit = new Button("Add / Edit"); // creates label
        this.addEdit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // row and column between 1 and state
                Integer rowInput = parseIntegerInput(
                        row,
                        1, state.rows,
                        -1, String.format("Row must be between 1 and %d.",
                                state.rows));
                if (rowInput == null) return;

                Integer colInput = parseIntegerInput(
                        column,
                        1, state.columns,
                        -1, String.format("Column must be between 1 and %d.",
                                state.columns));
                if (colInput == null) return;

                // gets type, cell and color
                String newType = pieceType.getValue();
                Point2D cell = new Point2D(colInput, rowInput);
                Color color = colorPicker.getValue();

                // if editing, update color or recreate as new type and copy value
                Piece existing = state.getPieceAt(cell);
                if (existing != null) {
                    if (existing.getClass().getSimpleName() == newType) {
                        existing.color = color;
                    } else {
                        boolean selected = existing.isSelected();
                        state.pieces.remove(existing);
                        existing = State.newPiece(newType, cell, color);
                        existing.setSelected(selected);
                        state.addPiece(existing);
                    }
                } else{
                    state.addPiece(State.newPiece(newType, cell, color));
                }
                update();
            }
        });
        pieceGrid.add(addEdit, 1, 2); // position of add/edit

        /*
         *  misc panel; population and deletion
         *  deleted selected [percentage] populate
         */

        // populate section
        HBox miscGrid = new HBox();
        miscGrid.setAlignment(Pos.CENTER);
        content.add(miscGrid,6,0);

        this.populateButton = new Button("Populate"); // creates label
        miscGrid.getChildren().add(this.populateButton);
        this.populateButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // gets percentage between 0 and 100 - button only enabled with empty cells
                Integer percentage = parseIntegerInput(
                        populatePercentage,
                        1,
                        100,
                        0,
                        "Percentage must be between 1 and 100");
                if (percentage == null) return;
                state.populate(percentage);
                update();
            }
        });

        this.populatePercentage = new TextField();
        miscGrid.getChildren().add(this.populatePercentage);
        // delete selected section
        this.deleteSelected = new Button("Delete Selected"); // creates label
        miscGrid.getChildren().add(this.deleteSelected);
        this.deleteSelected.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // deletes selected piece - button is disabled if there's no piece selected
                Piece deleting = state.getSelectedPiece();
                Point2D cell = deleting.getCell();
                boolean isSelectedCell = state.getSelectedCell().equals(cell);
                state.pieces.remove(deleting);
                if (isSelectedCell) state.setSelectedCell(cell);
                update();
            }
        });

        // size panel to update board dimensions section: [row] [column] <update>
        GridPane sizeGrid = new GridPane();
        sizeGrid.setAlignment(Pos.BASELINE_RIGHT);
        content.add(sizeGrid,18, 0);
        Label rowName = new Label("Rows: "); // creates label
        rowName.setMaxWidth(Double.MAX_VALUE);
        rowName.setAlignment(Pos.BASELINE_RIGHT);
        sizeGrid.add(rowName,2, 0);
        this.rows = new TextField();
        sizeGrid.add(this.rows, 4, 0);

        Label colName = new Label("Columns: "); // creates label
        colName.setMaxWidth(Double.MAX_VALUE);
        colName.setAlignment(Pos.BASELINE_RIGHT);
        sizeGrid.add(colName,2, 1);
        this.columns = new TextField();
        sizeGrid.add(this.columns, 4, 1);

        this.updateButton = new Button("Update");
        sizeGrid.add(this.updateButton, 4, 2);
        this.updateButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // gets number of rows and columns between 1 and 100
                Integer rowsInput = parseIntegerInput(
                        rows,
                        1,
                        100,
                        0,
                        "Rows must be between 1 and 100.");
                if (rowsInput == null) return;
                Integer columnsInput = parseIntegerInput(
                        columns,
                        1,
                        100,
                        0,
                        "Columns must be between 1 and 100.");
                if (columnsInput == null) return;
                state.rows = rowsInput;
                state.columns = columnsInput;
                // if selected cell is outside of bounds, obtain it and move it back to bounds
                Point2D selectedCell = state.getSelectedCell();
                state.removeOutOfBoundsPieces();
                if (state.isOutOfBounds(selectedCell)) {
                    selectedCell = new Point2D (
                            Math.min(selectedCell.getX(), state.columns - 1),
                            Math.min(selectedCell.getY(), state.rows - 1)
                    );
                }
                state.setSelectedCell(selectedCell);
                update();
            }
        });
        refresh();
    }

    /**
     * @param board - displays board
     * @return - board controls
     */

    public Controls setBoard(Board board) {
        this.board = board;
        return this;
    }

    /**
     * Parse input from field, rejects it if it's outside the accepted values; adding offset
     * Also shows a warning message upon rejection
     * @param field - Field to get value from
     * @param min - Maximum value
     * @param max - Minimum value
     * @param offset - Value to add to value
     * @param errorMessage - Message to display upon rejection
     * @return - Integer or null
     */

    private Integer parseIntegerInput(TextField field, int min, int max, int offset, String errorMessage) {
        String value = field.getText();
        try {
            int intValue = Integer.parseInt(value);
            return (intValue >= min && intValue <= max) ? intValue + offset : null;
        } catch(NumberFormatException ex) {
            // new Alert(Alert.AlertType.WARNING, errorMessage).showAndWait();
            final Stage dialog = new Stage();
            dialog.initModality(Modality.WINDOW_MODAL);
            VBox content = new VBox();
            Button closeButton = new Button("Ok");
            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    dialog.close();
                }
            });
            content.getChildren().addAll(new Text(errorMessage), closeButton);
            content.setAlignment(Pos.CENTER);
            content.setPadding(new Insets(15));
            dialog.setScene(new Scene(content));
            dialog.showAndWait();
            return null;
        }
    }

    /**
     * Notifies board state has changed and refresh components with updated state
     */

    private void update() {
        if (this.board != null) this.board.refresh();
        this.refresh();
    }

    /**
     * Updates components with state info
     */

    public void refresh() {
        Point2D selectedCell = state.getSelectedCell();
        String rowText = Integer.toString((int)selectedCell.getY() + 1);
        if (!this.row.getText().equals(rowText)) {
            this.row.textProperty().removeListener(this.selectedListener);
            this.row.setText(rowText);
            this.row.textProperty().addListener(this.selectedListener);
        }
        String columnText = Integer.toString((int)selectedCell.getX() + 1);
        if (!this.column.getText().equals(columnText)) {
            this.column.textProperty().removeListener(this.selectedListener);
            this.column.setText(columnText);
            this.column.textProperty().addListener(this.selectedListener);
        }
        Piece selected = this.state.getSelectedPiece();
        if (selected != null) {
            this.pieceType.setValue(selected.getClass().getSimpleName());
            this.colorPicker.setValue(selected.color);
        }
        this.deleteSelected.setDisable(this.state.getSelectedPiece() == null);
        this.populateButton.setDisable(this.state.pieces.size() >= this.state.rows * this.state.columns);
        this.rows.setText(Integer.toString(this.state.rows));
        this.columns.setText(Integer.toString(this.state.columns));
    }
}