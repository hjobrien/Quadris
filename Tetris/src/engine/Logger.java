package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import tetrominoes.Block;
import tetrominoes.Tile;

public class Logger {
	
	private static final String LOG_FILE = "src/gameLogs/Game Data";
	private static final int BOARD_HEIGHT = 20;
	private static final int BOARD_WIDTH = 10;
	private static File logFile = null;
	private static PrintStream logger = null;
	
	
	
	//only called once when the class is first loaded. apparently they are to be used sparingly
	//can't throw exception here
	static{
		// should make a file first and ensure it exists
		logFile = new File(LOG_FILE);
		if(!logFile.exists()){
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				System.err.println("could not create log file");
			}
		}
		//should use a file that the above ensured exist to print to
		try {
			logger = new PrintStream(new FileOutputStream(LOG_FILE, true));
		} catch (FileNotFoundException e) {
			System.err.println("could not find log file");
		}
	}
	
	public static void log(Tile[][] boardState, Block fallingBlock, Block nextBlock){
		boolean[][] isFilled = tileToBoolean(boardState);
		int falling = fallingBlock.getType().getValue();
		int next = nextBlock.getType().getValue();
		logger.print("\n" + falling + ", " + next + ", " + toPrintableVersion(isFilled));
	}

	private static String toPrintableVersion(boolean[][] isFilled) {
		String filledString = "{";
		for(boolean[] b : isFilled){
			filledString += "{";
			for(int i = 0; i < b.length; i++){
				filledString += ((b[i]) ? "1" : "0"); //if bb = true, return "1" else return "0"
				filledString += ", ";
			}
			filledString += "}";
		}
		filledString += "}";
		return filledString;
	}

	private static boolean[][] tileToBoolean(Tile[][] boardState) {
		boolean state[][] = new boolean[20][10];
		for(int i = 0; i < BOARD_HEIGHT; i++){
			for(int j = 0; j < BOARD_WIDTH; j++){
				state[i][j] = boardState[i][j].isFilled();
			}
		}
		return state;
	}
}
