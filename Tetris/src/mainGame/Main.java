package mainGame;

import engine.Engine;
import engine.Renderer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.TransformChangedEvent;
import javafx.stage.Stage;

public class Main extends Application{
	
//	boolean debug = true;
	boolean debug = false;

	
	//height should be double width
	private static final int SCREEN_WIDTH = 300;
	private static final int SCREEN_HEIGHT = SCREEN_WIDTH * 2;
	
	private static final int GAME_WIDTH = SCREEN_WIDTH + 175;
	private static final int GAME_HEIGHT = SCREEN_HEIGHT;
	
	public static final int BOARD_HEIGHT = 20;
	public static final int BOARD_WIDTH = 10;

	private AnimationTimer timer;
	private int gameCounter = 1;
	private	int score = 0;
	
	boolean paused = false;
	


	public static void main(String[] args) {	
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		StackPane main = new StackPane();
		
		
		//if we only make 1 board and it's in engine, we can always just receive the boardState 
		//from engine when we need it and we wont have to be translating boardStates
		GridPane grid = new GridPane();
		GridPane nextBlock = new GridPane();
		Engine.setValues(new Board(BOARD_HEIGHT, BOARD_WIDTH, grid), new Board(4,4,nextBlock));
		GridPane mainGame = new GridPane();
		mainGame.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH));
		mainGame.getColumnConstraints().add(new ColumnConstraints(20));
		mainGame.getColumnConstraints().add(new ColumnConstraints(150));
		
		for(int i = 0; i < 3; i++){
			mainGame.getRowConstraints().add(new RowConstraints(150));
		}
		Label scoreText = new Label("Score: " + score + 
				"\nLines cleared: " + Engine.getBoard().getNumOfFullRows());
		StringProperty valueProperty = new SimpleStringProperty();
		valueProperty.setValue("0");
		scoreText.textProperty().bind(valueProperty);
		mainGame.add(scoreText, 2,3);
		
		for (int i = 0; i < 4; i++){
			nextBlock.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH / BOARD_WIDTH));
		}
		for (int i = 0; i < 4; i ++){
			nextBlock.getRowConstraints().add(new RowConstraints(SCREEN_HEIGHT / BOARD_HEIGHT));
		}
		nextBlock.setGridLinesVisible(true);
		mainGame.add(nextBlock, 2,0);
		mainGame.add(grid, 0, 0,1,4);
		main.getChildren().add(mainGame);
		Scene boardScene = new Scene(main, GAME_WIDTH, GAME_HEIGHT);
		stage.setScene(boardScene);
		StackPane pauseView = showPausedView();
		
		configureGrid(grid);
		
		Renderer.draw(Engine.getBoard());
		
		Engine.addBlock();
		
		stage.addEventFilter(KeyEvent.KEY_PRESSED,e -> {
			if(e.getCode() == KeyCode.ESCAPE){
				System.exit(0);
			} else if(e.getCode() == KeyCode.P){
				paused = Engine.togglePause();
				if(paused){
					main.getChildren().add(pauseView);
				}
				else{
					main.getChildren().remove(pauseView);
				}
			} else if (e.getCode() == KeyCode.R){
					System.out.println("Game " + this.gameCounter + " score: " + (score + Engine.getBoard().getBoardScore()) + "\n");
					Engine.getBoard().clearBoard();
					Engine.addBlock();
					this.score = 0;
					gameCounter++;
					timer.start();
			} else if (!paused && Engine.getBoard().rowsAreNotFalling() && !Engine.getBoard().full){
				if (e.getCode() == KeyCode.RIGHT){
					Engine.getBoard().pressed(Move.RIGHT);
				} else if (e.getCode() == KeyCode.LEFT){
					Engine.getBoard().pressed(Move.LEFT);
				} else if (e.getCode() == KeyCode.X){
					Engine.getBoard().pressed(Move.ROT_RIGHT);
				} else if (e.getCode() == KeyCode.Z){
					Engine.getBoard().pressed(Move.ROT_LEFT);
	 			} else if (e.getCode() == KeyCode.DOWN){
	 				Engine.getBoard().pressed(Move.DOWN);
	 			} else if (e.getCode() == KeyCode.SPACE){
	 				Engine.getBoard().pressed(Move.FULL_DOWN);
	 			} else if (debug){
	 				if (e.getCode() == KeyCode.UP){
	 					Engine.getBoard().pressed(Move.UP);
	 				} 
	 			}
				if (!paused){
					Renderer.draw(Engine.getBoard());
				}
			}
		});  
		
		int startingTimePerTurn = 500;

		timer = new AnimationTimer(){
			private int timePerTurn = startingTimePerTurn;
			private long pastTime;
			@Override
			public void start(){
				pastTime = System.currentTimeMillis();
				super.start();
			}
			@Override
			public void handle(long time){
				long now = System.currentTimeMillis();
				if(!paused && now-pastTime >= timePerTurn){
					score++;
//					timePerTurn -= 25;
					valueProperty.set("\tScore: " + (score + Engine.getBoard().getBoardScore()) + 
							"\nLines cleared: " + Engine.getBoard().getNumOfFullRows());
					Engine.update();
					if (Engine.getBoard().isFull()){
						timer.stop();
					}
					Renderer.draw(Engine.getBoard());
					pastTime = now;
				}
			}
		};
		timer.start();
		
		
		stage.show();

	}

	private StackPane showPausedView() {
	    final Label label = new Label("Quadris");
	    label.setStyle("-fx-font: 90 Arial; -fx-text-fill: rgb(255,255,255); -fx-font-weight: bold; -fx-font-style: italic; -fx-padding: -300 0 0 0;");

		StackPane glass = new StackPane();
	    StackPane.setAlignment(label, Pos.CENTER);
	    glass.getChildren().addAll(label);
	    glass.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");	    
	    return glass;
	}

	private void configureGrid(GridPane grid) {
		for (int i = 0; i < BOARD_WIDTH; i++){
			grid.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH / BOARD_WIDTH));
		}
		for (int i = 0; i < BOARD_HEIGHT; i ++){
			grid.getRowConstraints().add(new RowConstraints(SCREEN_HEIGHT / BOARD_HEIGHT));
		}
		grid.setGridLinesVisible(true);
	}
}
