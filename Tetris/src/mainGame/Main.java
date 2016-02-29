package mainGame;

import engine.Engine;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
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

	private Engine engine;
	private AnimationTimer timer;

	

	public static void main(String[] args) {	
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		//if we only make 1 board and it's in engine, we can always just receive the boardState 
		//from engine when we need it and we wont have to be translating boardStates
		this.engine = new Engine(new Board(BOARD_HEIGHT, BOARD_WIDTH));
		
		GridPane grid = new GridPane();
		
		//we havent been using this at all
		//but might want to for styling?
//		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
//		GraphicsContext g = canvas.getGraphicsContext2D();
		
		Scene boardScene = new Scene(grid, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		stage.setScene(boardScene);
		
		configureGrid(grid);
		
		if(debug){
			indicateFilled(grid);
		}
		
		engine.addBlock();
		
		stage.addEventFilter(KeyEvent.KEY_PRESSED,e -> {
			if(e.getCode() == KeyCode.ESCAPE){
				System.exit(0);
			} 
			//TODO
			else if (e.getCode() == KeyCode.RIGHT){
				engine.getBoard().pressed("right");
			} else if (e.getCode() == KeyCode.LEFT){
				engine.getBoard().pressed("left");
			} else if (e.getCode() == KeyCode.UP){
				engine.getBoard().pressed("up");
			} else if (e.getCode() == KeyCode.DOWN){
				engine.getBoard().pressed("down");
			}
		});  

		timer = new AnimationTimer(){
			private long pastTime;
			@Override
			public void start(){
				pastTime = System.currentTimeMillis();
				super.start();
			}
			
			@Override
			public void handle(long time){
				long now = System.currentTimeMillis();
				if(now-pastTime >= 1000){
					if (engine.getBoard().isFull()){
						timer.stop();
					}
					engine.update();
					indicateFilled(grid);
					pastTime = now;
				}
			}
		};
		timer.start();
		
		
		stage.show();

	}
	
	//obsolete but we might want to update it if it's useful
//	private void run(GridPane grid){
//		timer = new AnimationTimer(){
//			private long pastTime;
//			@Override
//			public void start(){
//				pastTime = System.currentTimeMillis();
//				super.start();
//			}
//			
//			@Override
//			public void handle(long time){
//				long now = System.currentTimeMillis();
//				if(now-pastTime >= 1000){
//					draw(engine.getBoardState(), grid);
//					if(debug){
//						System.out.println("updated " + (time%1e9));
//					}
//				}
//				pastTime = now;
//			}
//		};
//	}	

	private void configureGrid(GridPane grid) {
		for (int i = 0; i < BOARD_WIDTH; i++){
			grid.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH / BOARD_WIDTH));
		}
		for (int i = 0; i < BOARD_HEIGHT; i ++){
			grid.getRowConstraints().add(new RowConstraints(SCREEN_HEIGHT / BOARD_HEIGHT));
		}
		
		//very helpful for debugging purposes
		//maybe we want this even when not debugging?
		if(debug){
			grid.setGridLinesVisible(true);
		}
	}
	
	private void indicateFilled(GridPane grid){
		//just for debugging now, although a similar system could be used to display the actual blocks
		for (int i = 0; i < BOARD_HEIGHT; i++){
			for (int j = 0; j < BOARD_WIDTH; j++){
				
				//some super sketchy erasing
				if (engine.getBoardState().get(i).get(j).isFilled()){
					Text y = new Text("O");
					y.setFill(Color.WHITE);
					grid.add(y, j, i);
					Text x = new Text("X");
					grid.add(x, j, i);
				} else {
					Text y = new Text("X");
					y.setFill(Color.WHITE);
					grid.add(y, j, i);
					Text x = new Text("O");
					grid.add(x, j, i);
				}
			}
		}
		
	}

}
