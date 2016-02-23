package mainGame;

import java.util.Random;
import engine.Engine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tetrominoes.Block;
import tetrominoes.LeftL;
import tetrominoes.LeftS;
import tetrominoes.RightL;
import tetrominoes.RightS;
import tetrominoes.Square;
import tetrominoes.StraightLine;
import tetrominoes.T;

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
		
		//just for debugging now, although a similar system could be used to display the actual blocks
		for (int i = 0; i < BOARD_HEIGHT; i++){
			for (int j = 0; j < BOARD_WIDTH; j++){
				if (board.valueOf(i, j) == false){
					Text x = new Text("O");
					grid.add(x, j, i);
				} else {
					Text x = new Text("X");
					grid.add(x, j, i);
				}
			}
		}
		
		stage.addEventFilter(KeyEvent.KEY_PRESSED,e -> {
			if(e.getCode() == KeyCode.ESCAPE){
				System.exit(0);
			} else if(e.getCode() == KeyCode.N){
				//still unsure how to communicate this to the engine. 
				//i was thinking boolean variables such as "nWasPressed," 
				//but hopefully there's a better way
				Block b = generateRandomBlock();
				board.add(b);
			}
		});  
		
//		this.board.update(10, 10, true);
//		this.board.display();
		
		//Are we supposed to do this?
		Engine e = new Engine(board);
		
		stage.show();

	}

	private Block generateRandomBlock() {
		Random r = new Random();
		int i = r.nextInt(7);
		switch (i){
		case 0:
			return new LeftL();
		case 1:
			return new RightL();
		case 2:
			return new LeftS();
		case 3: 
			return new RightS();
		case 4:
			return new StraightLine();
		case 5:
			return new T();
		case 6: 
			return new Square();
		}
		
		//shouldnt ever happen
		return new StraightLine();
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
