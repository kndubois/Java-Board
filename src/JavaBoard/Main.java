package JavaBoard;

/**
 * *    Main Class
 *
 * @author Katie Dubois
 */

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    private State state;          // declares shapes, cells, pieces
    private Board board;          // declares cell dimension and grid
    private Controls controls;    // declares controls with buttons

    /**
     * argument constructor for main to allow each parameter to be set.
     */

    public Main() {
        super();
        this.state = new State();
        this.board = new Board(this.state);
        this.controls = new Controls(this.state).setBoard(this.board);
        this.board.setControls(this.controls);
    }

    /**
     * Main program
     * This class is responsible of drawing the content for the program
     * @param primary - creates application
     */

    public void start(Stage primary) {

        primary.setTitle("Java Board"); // java title

        VBox root = new VBox();
        root.getChildren().add(this.board);
        root.getChildren().add(this.controls);
        root.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                board.setWidth(newValue.doubleValue());
            }
        });
        root.heightProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                board.setHeight(newValue.doubleValue() / 3 * 2);
            }
        });

        Rectangle2D bounds = Screen.getPrimary().getBounds();
        primary.setScene(new Scene(root, bounds.getWidth() / 2, bounds.getHeight() / 2));
        primary.show();

        this.board.populate();
    }

    /**
     * Test harness
     * @param args - used to launch program
     */

    public static void main(String[] args) {
        launch(args);
    }
}
