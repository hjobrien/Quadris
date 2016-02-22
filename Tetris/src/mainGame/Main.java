package mainGame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import tetrominoes.Block;

public class Main extends Application{
	
	//height should be double width
	private static final int SCREEN_WIDTH = 300;
	private static final int SCREEN_HEIGHT = SCREEN_WIDTH * 2;
	
	public static final int BOARD_HEIGHT = 20;
	public static final int BOARD_WIDTH = 10;

	
	private Board board;

	public static void main(String[] args) {	
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.board = new Board(BOARD_HEIGHT, BOARD_WIDTH);
	
		GridPane grid = new GridPane();
//		Group root = new Group();
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		Scene boardScene = new Scene(grid, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		stage.setScene(boardScene);
		
		configureGrid(grid);
		
		stage.addEventFilter(KeyEvent.KEY_PRESSED,e -> {
			if(e.getCode() == KeyCode.ESCAPE){
				System.exit(0);
			} else if(e.getCode() == KeyCode.N){
				Block b = generateRandomBlock();
			}
		});  
		
//		this.board.update(10, 10, true);
//		this.board.display();
		
		stage.show();

	}

	private Block generateRandomBlock() {
		// TODO Auto-generated method stub
		return null;
	}

	private void configureGrid(GridPane grid) {
		for (int i = 0; i < BOARD_WIDTH; i++){
			grid.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH / BOARD_WIDTH));
		}
		for (int i = 0; i < BOARD_HEIGHT; i ++){
			grid.getRowConstraints().add(new RowConstraints(SCREEN_HEIGHT / BOARD_HEIGHT));
		}
		
		//very helpful for debugging purposes
		grid.setGridLinesVisible(true);
	}

}
