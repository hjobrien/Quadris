package mainGame;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import engine.Engine;
import engine.Renderer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application{
	
//	boolean debug = true;
	boolean debug = false;

	
	//height should be double width
	private static final int SCREEN_WIDTH = 300;
	private static final int SCREEN_HEIGHT = SCREEN_WIDTH * 2;
	
	private static final int GAME_WIDTH = SCREEN_WIDTH + 175;
	private static final int GAME_HEIGHT = SCREEN_HEIGHT;
	
	private static final int HELP_HEIGHT = GAME_HEIGHT - 210;
	private static final int HELP_WIDTH = GAME_WIDTH;
	
	public static final int VERTICAL_TILES = 20;
	public static final int HORIZONTAL_TILES = 10;
	
	public static final int MAX_MILLIS_PER_TURN = 1000;
	public static final int MIN_MILLIS_PER_TURN = 100;

	private AnimationTimer timer;
	private PrintStream scorePrinter;
	private Scanner scoreReader;
	private int gameCounter = 1;
	private	int timeScore = 0;
	private double timePerTurn = MAX_MILLIS_PER_TURN;
	
	boolean paused = false;
	


	public static void main(String[] args) {	
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		File scoreFile = new File("src/gameLogs/High Scores");
		if(!scoreFile.exists()){
			scoreFile.createNewFile();
			scorePrinter = new PrintStream(scoreFile);
			for(int i = 0; i < 10; i++){
				scorePrinter.println("0");
			}
		}
		scoreReader = new Scanner(scoreFile);
		ArrayList<Integer> highScores = getHighScores(scoreReader);
		scorePrinter = new PrintStream(scoreFile);
		printScores(highScores);
		
		StackPane main = new StackPane();
		
		
		//if we only make 1 board and it's in engine, we can always just receive the boardState 
		//from engine when we need it and we wont have to be translating boardStates
		GridPane grid = new GridPane();
		GridPane nextBlock = new GridPane();
		Engine.setValues(new Board(VERTICAL_TILES, HORIZONTAL_TILES, grid), new Board(4,4,nextBlock));
		GridPane mainGame = new GridPane();
		mainGame.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH));
		mainGame.getColumnConstraints().add(new ColumnConstraints(20));
		mainGame.getColumnConstraints().add(new ColumnConstraints(150));
		
		for(int i = 0; i < 3; i++){
			mainGame.getRowConstraints().add(new RowConstraints(150));
		}
		Label scoreText = new Label("Score: " + timeScore + 
				"\nLines cleared: " + Engine.getBoard().getNumOfFullRows());
		StringProperty valueProperty = new SimpleStringProperty();
		valueProperty.setValue("0");
		scoreText.textProperty().bind(valueProperty);
		mainGame.add(scoreText, 2,2);
		
		for (int i = 0; i < 4; i++){
			nextBlock.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH / HORIZONTAL_TILES));
		}
		for (int i = 0; i < 4; i ++){
			nextBlock.getRowConstraints().add(new RowConstraints(SCREEN_HEIGHT / VERTICAL_TILES));
		}
		nextBlock.setGridLinesVisible(true);
		mainGame.add(nextBlock, 2,0);
		mainGame.add(grid, 0, 0,1,4);
		main.getChildren().add(mainGame);
		Scene boardScene = new Scene(main, GAME_WIDTH, GAME_HEIGHT);
		stage.setScene(boardScene);
		StackPane pauseView = constructPauseView();
		pauseView.setVisible(false);
		main.getChildren().add(pauseView);
		configureGrid(grid);
		
		Renderer.draw(Engine.getBoard());
		
		Engine.addBlock();
		
		stage.addEventFilter(KeyEvent.KEY_PRESSED,e -> {
			if(e.getCode() == KeyCode.ESCAPE){
				scorePrinter.close();
				scoreReader.close();
				System.exit(0);
			} else if(e.getCode() == KeyCode.P){
				paused = Engine.togglePause();
				if(paused){
					pause(pauseView);
				}
				else{
					unpause(pauseView);
				}
			} else if (e.getCode() == KeyCode.R){
					int score = getScore();
					System.out.println("Game " + this.gameCounter + " score: " + score  + "\n");
					updateHighScores(score, highScores); // updatesArray List by reference
					printScores(highScores);
					Engine.getBoard().clearBoard();
					Engine.addBlock();
					this.timeScore = 0;
					gameCounter++;
					timePerTurn = MAX_MILLIS_PER_TURN;
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
				if(debug){
					System.out.println(timePerTurn);
				}
				if(!paused && now-pastTime >= timePerTurn){
					timeScore++;
					valueProperty.set("\tScore: " + (timeScore + Engine.getBoard().getBoardScore()) + 
							"\nLines cleared: " + Engine.getBoard().getNumOfFullRows());
					Engine.update();
					if (Engine.getBoard().isFull()){
						timer.stop();
					}
					Renderer.draw(Engine.getBoard());
					pastTime = now;
				}
				timePerTurn = updateTime(timePerTurn);


			}
			private double updateTime(double turnTime) {
				if(turnTime > MIN_MILLIS_PER_TURN){
					return MAX_MILLIS_PER_TURN - 0.09 * (timeScore + Engine.getBoard().getBoardScore());
				}
				else{
					return MIN_MILLIS_PER_TURN;
				}
			}
		};
		timer.start();
		
		
		stage.show();

	}

	private void printScores(ArrayList<Integer> highScores) {
		for(int i = 0; i < highScores.size() - 1; i++){
			scorePrinter.println(highScores.get(i));
		}
		scorePrinter.print(highScores.get(highScores.size() -1)); // so we don't indent after the last score was printed
		
	}

	private void updateHighScores(int score, ArrayList<Integer> highScores) {
		highScores.add(score);
		Collections.sort(highScores, Collections.reverseOrder());
		if(!highScores.isEmpty()){
			highScores.remove(highScores.size()); //removes 11th high score
		}
	}

	private int getScore() {
		return (timeScore + Engine.getBoard().getBoardScore());
	}

	private ArrayList<Integer> getHighScores(Scanner scoreReader2) {
		ArrayList<Integer> scores = new ArrayList<Integer>(10);
		while(scoreReader2.hasNextInt()){
			scores.add(scoreReader2.nextInt());
		}
		return scores;
	}

	//maybe do more here, for right now it's its own method
	private void pause(StackPane pauseView) {
		pauseView.setVisible(true);
		
	}

	private void unpause(StackPane pauseView) {
		pauseView.setVisible(false);
		for(Node child : pauseView.getChildren()){
			if((child instanceof StackPane)){
				child.setVisible(false);
			}
		}
		
	}

	private StackPane constructPauseView() {
	    final Label label = new Label("Quadris");
	    label.setStyle("-fx-font: 90 Arial; -fx-text-fill: rgb(255,255,255); -fx-font-weight: bold; -fx-font-style: italic; -fx-padding: -300 0 0 0;");
	    GridPane pausePane = new GridPane();

	    pausePane.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH));
	    pausePane.getColumnConstraints().add(new ColumnConstraints(GAME_WIDTH - SCREEN_WIDTH));
	    pausePane.getRowConstraints().add(new RowConstraints(540));
	    
	    Button helpButton = new Button("Instructions");
	    helpButton.setMinWidth(175);
	    helpButton.setMinHeight(30);
	    helpButton.setStyle("-fx-background-color: rgba(108, 116, 118, 1);");	    

	    Button scoreButton = new Button("High Scores");
	    scoreButton.setMinWidth(175);
	    scoreButton.setMinHeight(30);
	    scoreButton.setStyle("-fx-background-color: rgba(108, 116, 118, 1);"); 
	    
	    pausePane.add(helpButton, 1,1);
	    pausePane.add(scoreButton, 1,2);
	    
	    StackPane helpPane = new StackPane();
	    helpPane.setMaxHeight(HELP_HEIGHT);
	    helpPane.setMaxWidth(HELP_WIDTH);

	    helpPane.setStyle("-fx-background-color: rgba(100, 0, 100, 0.5);");	  
	    helpPane.setVisible(false);
	    
	    TextArea buttonMapping = new TextArea(
	    		  "\n\n\n"
	    		+ "Left Arrow:\t     Move Left\n"
	    		+ "Right Arrow:\t   Move Right\n"
	    		+ "Down Arrow:\t Speed Down\n"
	    		+ "Z:\t\t\t   Rotate Left\n"
	    		+ "X:\t\t\t Rotate Right\n"
	    		+ "Spacebar:\t   Drop Piece\n"
	    		+ "R:\t\t\t\t Restart\n"
	    		+ "P:\t\t\t\t   Pause");
	    buttonMapping.getStylesheets().add("stylesheets/TextAreaStyle.css");
        buttonMapping.setStyle("-fx-text-fill: white;");
	    StackPane.setAlignment(buttonMapping, Pos.CENTER);
	    helpPane.getChildren().add(buttonMapping);
	    
	    helpButton.setOnAction(e ->{
	    	helpPane.setVisible(true);
	    });
	    
	    
	    
	    
		StackPane glass = new StackPane();
	    StackPane.setAlignment(label, Pos.CENTER);
	    StackPane.setAlignment(helpPane, Pos.BOTTOM_CENTER);
	    glass.getChildren().addAll(helpPane, label, pausePane);
	    glass.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");	    
	    return glass;
	}

	private void configureGrid(GridPane grid) {
		for (int i = 0; i < HORIZONTAL_TILES; i++){
			grid.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH / HORIZONTAL_TILES));
		}
		for (int i = 0; i < VERTICAL_TILES; i ++){
			grid.getRowConstraints().add(new RowConstraints(SCREEN_HEIGHT / VERTICAL_TILES));
		}
		grid.setGridLinesVisible(true);
	}
}
