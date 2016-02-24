package mainGame;

import engine.Engine;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{
	
	
	//change this to enable debug configurations, allows for fast switching between releases and development versions
	boolean debug = true;
	
	//height should be double width
	private static final int SCREEN_WIDTH = 300;
	private static final int SCREEN_HEIGHT = SCREEN_WIDTH * 2;
	
	public static final int BOARD_HEIGHT = 20;
	public static final int BOARD_WIDTH = 10;
	private Board board = new Board(BOARD_HEIGHT, BOARD_WIDTH);

	private Engine engine = new Engine(board);

	

	public static void main(String[] args) {	
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {	
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
				engine.addBlock();
			}
		});  
		
//		this.board.update(10, 10, true);
//		this.board.display();
		
		//Are we supposed to do this?
		
		
		
		
		
		
		
		
		
		run(grid);
		
		stage.show();

	}
	
	private void run(GridPane board){
		new AnimationTimer(){
			@Override
			public void handle(long time){
				if(time % (1e3 * 5) == 0){
					draw(engine.getBoardState(), board);
					if(debug){
						System.out.println("updated " + time%1e9);
					}
				}
			}
		}.start();
	}

	private void draw(boolean[][] state, GridPane board) {
		for(int i = 0; i < state.length; i++){
			for(int j = 0; j < state[i].length; j++){
				Rectangle r = new Rectangle();
				r.setFill(Color.RED);
				board.add(r, i, j);
			}
		}
	}

	

	private void configureGrid(GridPane grid) {
		for (int i = 0; i < BOARD_WIDTH; i++){
			grid.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH / BOARD_WIDTH));
		}
		for (int i = 0; i < BOARD_HEIGHT; i ++){
			grid.getRowConstraints().add(new RowConstraints(SCREEN_HEIGHT / BOARD_HEIGHT));
		}
		
		//very helpful for debugging purposes
		if(debug){
			grid.setGridLinesVisible(true);
		}
	}

}
