package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import blocks.Block;
import blocks.Tile;

public class Logger {
	
	private static final String LOG_FILE = "src/gameLogs/Game Data";
	private static final int BOARD_HEIGHT = 20;
	private static final int BOARD_WIDTH = 10;
	private static File logFile = null;
	private static PrintStream logger = null;
	
	
	
	//only called once when the class is first loaded. apparently they are to be used sparingly
	//can't throw exception here
	/**
	 * initialize the file objects, only called once when the object is first loaded into memory.
	 */
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
	
	/**
	 * writes info to logging file for later analysis
	 * @param boardState    a Tile[][] array denoting which spots are full
	 * @param fallingBlock the block that is currently falling
	 * @param nextBlock    the next block that is set to fall
	 */
	public static void log(Tile[][] boardState, Block fallingBlock, Block nextBlock){
		boolean[][] isFilled = tileToBoolean(boardState);
		int falling = fallingBlock.getType().getValue();
		int next = nextBlock.getType().getValue();
		logger.print("\n" + falling + ", " + fallingBlock.getRotationIndex() + ", " + fallingBlock.getGridLocation()[0] + ", " + fallingBlock.getGridLocation()[1]  + ", " + next + ", " + toPrintableVersion(isFilled));
	}

	/**
	 * converts a boolean[][] to a string that prints nicely
	 * @param isFilled  data array for the game state
	 * @return a string that reflects the data in the array
	 */
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
	
	
  /**
   * converts Tile[][] to boolean[][]
   * @param boardState the data reflecting what tiles are filled
   * @return    a boolean array reflecting the same data
   */
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
