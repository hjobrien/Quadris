package renderer;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class DualPlayRenderer extends Renderer {

  public static final Rectangle[][] GAME2_RECTS =
      new Rectangle[Renderer.VERTICAL_TILES][Renderer.HORIZONTAL_TILES];

  public static final int INFO_WIDTH = 175;

  public DualPlayRenderer(boolean doDebug) {
    super(doDebug);
    super.setUpRects(GAME2_RECTS);
  }

  public Scene makeGame() throws IOException {
    StackPane main = new StackPane();

    GridPane mainGameGrid = new GridPane();
    mainGameGrid.getColumnConstraints().add(new ColumnConstraints(Renderer.GAME_WIDTH));
    mainGameGrid.getColumnConstraints().add(new ColumnConstraints(20));
    mainGameGrid.getColumnConstraints().add(new ColumnConstraints(INFO_WIDTH));
    mainGameGrid.getColumnConstraints().add(new ColumnConstraints(20));
    mainGameGrid.getColumnConstraints().add(new ColumnConstraints(Renderer.GAME_WIDTH));
    
    for (int i = 0; i < 3; i++) {
      mainGameGrid.getRowConstraints().add(new RowConstraints(150));
    }
    Label scoreText = new Label("Score: " + 0 + "\nLines cleared: " + 0);
    StringProperty valueProperty = new SimpleStringProperty();
    valueProperty.setValue("0");
    scoreText.textProperty().bind(valueProperty);
    mainGameGrid.add(scoreText, 2, 2);
    
    main.getChildren().add(mainGameGrid);
    return new Scene(main);
  }

}
