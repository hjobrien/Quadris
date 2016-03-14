package mainGame;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	/**
	 * change these flags to change the run mode.
	 * any generalized distribution version should likely have all off
	 */
	
//	public static final boolean DEBUG_MODE = true;
	public static final boolean DEBUG_MODE = false;
	
	public static final boolean LOG_MODE = true;
//	public static final boolean LOG_MODE = false;

	
	

	
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
	
	private TextArea scoreList = null;
	
	private ArrayList<Integer> highScores = null; //need to put this here because i can't make a local variable passed to a method in the listener
	
	boolean paused = false;
	


	public static void main(String[] args) {	
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		initializeScorePrinter();
		StackPane main = new StackPane();
		
		
		//if we only make 1 board and it's in engine, we can always just receive the boardState 
		//from engine when we need it and we wont have to be translating boardStates
		GridPane grid = new GridPane();
		GridPane nextBlock = new GridPane();
		Engine.setMode(DEBUG_MODE, LOG_MODE);
		Board.setMode(DEBUG_MODE);
		Board gameBoard = new Board(VERTICAL_TILES, HORIZONTAL_TILES, grid);
		Board nextPieceBoard = new Board(4,4,nextBlock);
		Engine.setValues(gameBoard, nextPieceBoard);
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
		StackPane pauseView = constructPauseView(highScores);
		pauseView.setVisible(false);
		main.getChildren().add(pauseView);
		configureGrid(grid);
		
		Renderer.draw(Engine.getBoard());
		
		Engine.addBlock();
		stage.addEventFilter(KeyEvent.KEY_PRESSED,e -> {
			if(e.getCode() == KeyCode.ESCAPE){
				writeScores(highScores);
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
				updateHighScores(score, highScores); // updates ArrayList by reference, can't do it well because of 'final or effectively final' issue
				writeScores(highScores);
				scoreList.setText(
						"\n\tHigh Scores:\n\n"
						+ getScoresForDisplay(highScores));
				Engine.getBoard().clearBoard();
				Engine.addBlock();
				this.timeScore = 0;
				gameCounter++;
				timePerTurn = MAX_MILLIS_PER_TURN;
				timer.start();
				try {
					initializeScorePrinter();
				} catch (Exception e1) {
					throw new RuntimeException("Error on file re-generation");
				}
					
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
	 			} else if (DEBUG_MODE){
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
//				if(debug){
//					System.out.println(timePerTurn);
//				}
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
	
	private void initializeScorePrinter() throws IOException{
		File scoreFile = new File("src/gameLogs/High Scores");
		scoreReader = new Scanner(scoreFile);
		if(!scoreFile.exists()){
			scoreFile.createNewFile();
			highScores = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,0,0));
		}
		else{
			highScores = readScores(scoreReader);
		}		
		scorePrinter = new PrintStream(scoreFile);
	}
	
	private ArrayList<Integer> readScores(Scanner fileReader){
		ArrayList<Integer> scores = new ArrayList<Integer>(10);
		while(fileReader.hasNextInt()){
			scores.add(fileReader.nextInt());
		}
		return scores;
	}

	private void writeScores(ArrayList<Integer> highScores) {
		for(int i = 0; i < highScores.size() - 1; i++){
			scorePrinter.println(highScores.get(i));
		}
		scorePrinter.print(highScores.get(highScores.size() - 1)); // so we don't indent after the last score was printed
		
	}
	
	private String getScoresForDisplay(ArrayList<Integer> highScores){
		String scores = "";
		for(int i = 0; i < highScores.size(); i++){
			scores  = scores + (i+1) + ".\t\t\t\t\t\t" + highScores.get(i) + "\n";
		}
		return scores;
	}

	private void updateHighScores(int score, ArrayList<Integer> highScores) {
		if(DEBUG_MODE){
			System.out.println(score);
		}
		highScores.add(score);
		Collections.sort(highScores, Collections.reverseOrder());
		if(!highScores.isEmpty() && highScores.size() > 10){
			highScores.remove(highScores.size()-1); //removes 11th high score
		}
	}

	private int getScore() {
		return (timeScore + Engine.getBoard().getBoardScore());
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

	private StackPane constructPauseView(ArrayList<Integer> highScores) {
	    final Label nameLabel = new Label("Quadris");
	    nameLabel.setStyle("-fx-font: 90 Arial; -fx-text-fill: rgb(255,255,255); -fx-font-weight: bold; -fx-font-style: italic; -fx-padding: -300 0 0 0;");
	    GridPane pauseGrid = new GridPane();

	    pauseGrid.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH));
	    pauseGrid.getColumnConstraints().add(new ColumnConstraints(GAME_WIDTH - SCREEN_WIDTH));
	    pauseGrid.getRowConstraints().add(new RowConstraints(540));
	    
	    Button helpButton = new Button("Instructions");
	    helpButton.setMinWidth(175);
	    helpButton.setMinHeight(30);
	    helpButton.setStyle("-fx-background-color: rgba(108, 116, 118, 1);");	    

	    Button scoreButton = new Button("High Scores");
	    scoreButton.setMinWidth(175);
	    scoreButton.setMinHeight(30);
	    scoreButton.setStyle("-fx-background-color: rgba(108, 116, 118, 1);"); 
	    
	    pauseGrid.add(helpButton, 1,1);
	    pauseGrid.add(scoreButton, 1,2);
	    
	    StackPane helpPane = new StackPane();
	    StackPane scorePane = new StackPane();

	    helpPane.setMaxHeight(HELP_HEIGHT);
	    helpPane.setMaxWidth(HELP_WIDTH);

	    helpPane.setStyle("-fx-background-color: rgba(100, 0, 100, 0.5);");	  
	    helpPane.setVisible(false);
	    
	    TextArea buttonMapping = new TextArea(

	    	      "\n\n"

	    	    + getInstructions("Left Arrow:", "Move left")

	    	    + getInstructions("Right Arrow:", "Move right")

	    	    + getInstructions("Down Arrow:", "Speed down")

	    	    + getInstructions("Z:", "Rotate Left")

	    	    + getInstructions("X:", "Rotate Right")

	    	    + getInstructions("Spacebar:", "Drop Piece")

	    	    + getInstructions("R:", "Restart")

	    	    + getInstructions("P:", "Pause")

	    	    );
	    buttonMapping.setEditable(false); //keeps pesky users from typing in it
	    buttonMapping.getStylesheets().add("stylesheets/TextAreaStyle.css");
        buttonMapping.setStyle("-fx-text-fill: white;");
	    helpPane.getChildren().add(buttonMapping);
	    
	    helpButton.setOnAction(e ->{
	    	scorePane.setVisible(false);
	    	helpPane.setVisible(true);
	    });
	    
	    
	    scorePane.setMaxHeight(HELP_HEIGHT);
	    scorePane.setMaxWidth(HELP_WIDTH);
	    scorePane.setStyle("-fx-background-color: rgba(100, 0, 100, 0.5);");
	    scorePane.setVisible(false);
	    
	    scoreList = new TextArea(
	    		"\n\tHigh Scores:\n\n"
	    		+ getScoresForDisplay(highScores));
	    scoreList.getStylesheets().add("stylesheets/TextAreaStyle.css");
	    scoreList.setEditable(false); //keeps pesky users from typing in it
        scoreList.setStyle("-fx-text-fill: white;");
        StackPane.setAlignment(scoreList, Pos.CENTER);
        scorePane.getChildren().add(scoreList);
        
        scoreButton.setOnAction(e ->{
        	helpPane.setVisible(false);
	    	scorePane.setVisible(true);
	    });
	    
		StackPane glass = new StackPane();
	    StackPane.setAlignment(nameLabel, Pos.CENTER);
	    StackPane.setAlignment(helpPane, Pos.BOTTOM_CENTER);
	    StackPane.setAlignment(scorePane, Pos.BOTTOM_CENTER);
	    glass.getChildren().addAll(nameLabel, helpPane, scorePane, pauseGrid);
	    glass.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");	    
	    return glass;
	}

	public String getInstructions(String s1, String s2){
	    return "\n" + String.format("%20s\t%s", s1, s2);    
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
