package cerulean;

import java.util.ArrayList;
import java.util.stream.DoubleStream;

import blocks.Block;
import blocks.Tile;
import mainGame.Board;
import mainGame.Move;

/**
 * General AI class, handles game state analysis and move optimization
 * 
 * @author Hank
 *
 */
//5 2 3 6 2 5 3 4 6 5 6 6 4 5 0 0 5 6 1 4 4 2 0 5 0 4 3 5 6 1 4 1 0 1 5 0 4 3 4 2 4 5 5 2 2 0 4 0 6 6 3 5 6 2 3 6 5 1 0 0 6 6 4 4 5 3 3 3 2 2 6 6 1 6 1 1 0 4 6 4 5 6 2 1 1 0 1 1 6 5 1 5 2 5 1 6 6 6 3 1 4 3 6 5 5 5 0 5 1 5 3 4 0 3 5 2 1 3 5 4 4 3 2 3 4 1 3 3 4 6 4 4 6 4 3 0 2 3 3 5 5 6 6 0 5 3 6 1 2 0 3 6 4 1 4 5 0 4 4 4 2 0 0 1 5 5 4 3 4 6 2 4 2 4 4 2 5 0 5 1 5 1 6 4 5 0 5 3 5 6 4 4 5 5 5 6 1 5 0 6 2 1 5 4 3 4 3 4 2 5 3 4 3 0 3 5 3 4 2 3 2 1 6 4 2 4 1 6 4 0 6 6 1 3 4 1 1 3 2 2 0 4 4 3 0 3 0 1 0 5 0 3 5 2 4 6 1 2 1 3 6 5 5 4 0 4 3 6 5 5 5 4 0 0 1 1 5 1 6 4 0 2 0 3 4 5 0 6 0 1 2 5 3 2 6 6 4 5 3 4 0 3 0 5 2 4 2 0 2 3 3 3 0 6 1 3 3 0 5 5 6 0 4 2 0 0 5 2 4 0 4 2 4 6 5 1 0 6 0 1 5 3 5 4 2 4 5 0 4 2 5 6 1 6 3 5 2 5 2 0 2 1 4 0 6 0 0 6 5 6 5 0 4 1 1 4 2 4 0 4 4 6 0 4 4 3 1 0 3 1 0 1 5 5 2 4 4 2 0 2 4 4 5 6 3 3 5 3 1 1 4 1 3 0 1 6 3 1 2 1 4 2 0 0 6 4 0 2 2 6 1 4 3 3 2 0 2 5 6 2 2 5 5 4 0 0 0 6 2 1 4 1 3 0 6 3 4 5 5 5 6 5 4 0 4 2 4 3 0 0 2 1 4 1 6 6 5 3 2 4 4 3 4 1 6 1 2 0 6 2 6 2 4 2 5 1 2 2 1 6 1 4 0 0 5 2 0 6 3 5 1 4 4 5 1 4 2 5 0 0 1 0 1 0 4 6 4 1 3 6 4 3 0 2 6 2 2 2 6 3 1 0 1 3 5 5 1 2 0 2 0 3 4 3 3 1 6 5 0 5 2 3 4 2 0 3 6 5 2 0 5 6 0 4 3 1 3 1 6 1 6 1 1 5 5 2 2 2 0 3 5 3 1 6 1 1 5 3 5 0 
//2 1 6 2 3 2 5 2 3 0 1 6 6 0 6 4 0 4 1 4 0 2 3 0 1 0 0 6 4 3 3 0 5 6 6 0 6 0 2 0 2 0 1 4 3 3 5 5 4 6 0 3 0 5 2 6 3 6 0 1 3 3 5 3 2 3 3 0 0 1 3 5 0 3 2 3 4 0 6 4 4 2 0 1 1 2 0 5 0 2 2 5 4 1 6 5 0 2 6 6 3 0 6 6 3 5 1 4 2 4 5 2 4 1 4 5 1 4 3 2 0 1 0 4 4 2 0 4 1 5 0 0 2 4 2 5 5 4 6 4 3 4 2 5 3 5 2 0 6 0 4 3 1 2 1 2 5 0 5 1 4 4 6 4 4 5 6 2 0 4 5 3 6 3 4 6 1 6 3 5 4 3 0 0 2 2 5 3 0 2 0 4 4 5 0 0 3 5 1 0 6 1 5 3 3 2 2 3 1 4 4 4 0 4 2 6 5 2 1 4 4 4 4 5 3 5 5 3 3 6 2 3 3 0 6 1 6 2 3 2 3 4 4 3 4 6 1 0 4 5 1 0 6 0 2 5 2 3 0 3 4 6 0 6 3 2 5 1 2 0 2 3 2 6 4 0 3 5 6 4 0 2 0 4 0 0 5 0 3 6 3 1 6 6 1 3 1 4 4 0 4 0 6 2 2 6 5 3 3 1 1 1 5 0 4 2 2 0 1 2 3 3 5 3 6 4 6 0 4 6 0 6 6 6 4 2 0 4 4 4 1 3 5 2 0 3 5 4 3 6 2 5 5 4 0 6 4 1 1 4 1 6 0 0 2 0 4 2 5 1 4 0 5 4 1 3 3 6 5 3 1 1 3 6 3 5 1 5 1 1 5 6 0 3 2 5 2 1 3 3 6 4 1 1 3 4 0 1 2 2 2 5 5 6 3 5 5 6 0 3 6 6 0 0 5 1 4 6 0 3 5 4 1 1 2 4 3 3 3 2 5 5 5 2 0 1 2 3 6 6 2 0 1 6 5 6 4 3 0 2 3 6 0 1 1 1 3 2 2 4 3 1 6 3 4 6 0 1 5 0 4 5 3 3 4 4 0 6 6 6 6 6 1 0 2 1 6 0 3 6 2 5 1 2 4 3 6 5 6 2 2 0 3 1 4 5 0 2 5 5 1 1 3 0 5 1 6 2 3 3 4 5 1 4 4 0 6 3 2 3 2 0 1 1 1 5 6 6 5 2 2 6 2 5 4 6 5 0 3 1 2 2 6 4 1 4 6 4 0 0 3 1 4 1 1 2 0 0 1 3 6 5 4 0 0 2 1 0 2 6 5 0 3 4 0 3 3 5 5 5 

public class Cerulean {

  // Board Weight constants
  // negative means its a bad thing being weighted (overall board height)
  // positive means its a good thing (full lines);
  // good weights: -100, -50, 70 : ~8000x
  // private static final double HEIGHT_WEIGHT = -100;
  // private static final double VOID_WEIGHT = -50;
  // private static final double LINE_WEIGHT = 70;

  private static final double HEIGHT_WEIGHT = -70;
  private static final double VOID_WEIGHT = -70;
  private static final double LINE_WEIGHT = 500;
  
  private static double[] weights = new double[]{HEIGHT_WEIGHT, VOID_WEIGHT, LINE_WEIGHT};

  // private static final double HEIGHT_WEIGHT = -70;
  // private static final double VOID_WEIGHT = -100;
  // private static final double LINE_WEIGHT = 700;


  // change for how important multiple of an occurrence is
  // negative numbers mean as the quantity gets higher, it gets less important
  // numbers between 0 and |1| mean as the quantity get higher, it increases in importance more
  // slowly
  // numbers > |1| mean as the quantity gets higher, it increases in importance more quickly
  private static final double HEIGHT_POW = 1;
  private static final double VOID_POW = 1;
  private static final double LINE_POW = 1;

  // TODO: add a positive weight for how full each row is?

  private static Move[] solutionPath = new Move[] {Move.RIGHT}; // partially filled to prevent
                                                                // errors later on

  /**
   * called when a solution is needed for a given block
   * 
   * @return an array of moves needed to get piece to the optimal location, should be some form of
   *         left/right, rotate, drop
   */
  public static Move[] getSolution() {
    return solutionPath;
  }

  /**
   * gives AI data for what block is active and the board state it is to be placed in
   * 
   * @param nextBlockType the kind of block to be analyzed
   * @param boardState the current board state
   */
  public static Move[] submitBlock(Block nextBlock, Tile[][] boardState) {
    // TODO:
    // ideal behavior: blocks drop normally but an array is generated each time a block is added to
    // the game
    // long t1 = System.currentTimeMillis();
    return computeBestPath(nextBlock, boardState);
    // System.out.println("x Weight analysis took " + (System.currentTimeMillis() - t1) + "
    // Milli(s)");
    // using grid from engine as event target, should change to something else
    // TODO: make board extend GridPane? it'd be a node then
    // Event.fireEvent(Engine.getBoard().getGrid(), new ComputationDoneEvent(solutionPath));
  }

  /**
   * Generalized move optimization method
   * 
   * @param nextBlock the block just introduced to the board
   * @param boardState the board state without the block entered, all tiles are not active
   * @return an array of moves that positions the piece in to the optimal location
   */
  private static Move[] computeBestPath(Block nextBlock, Tile[][] boardState) {

    double maxWeight = Double.NEGATIVE_INFINITY;
    // Block nextBlockCopy = new Block(nextBlock.getType(),
    // new int[] {nextBlock.getGridLocation()[0], nextBlock.getGridLocation()[1]});
    // int startingRowIndex = nextBlockCopy.getShape().length - 1 + 3;
    // int startingColumnIndex = (Renderer.HORIZONTAL_TILES - nextBlockCopy.getShape()[0].length) /
    // 2
    // + nextBlockCopy.getShape()[0].length - 1;
    // nextBlockCopy.setGridLocation(startingRowIndex, startingColumnIndex);
    Block nextBlockCopy = new Block(nextBlock.getType(), new int[] {0, 0});

    Move[] bestPath = new Move[] {};
    // TODO: reduce number of loops reps
    for (int moveCount = 0; moveCount < 10; moveCount++) {
      // 10 possible worst case left/right options
      for (int rotCount = 0; rotCount < nextBlockCopy.getNumRotations(); rotCount++) {
        for (int slideCount = 0; slideCount < 3; slideCount++) {  //from slide one to left, no slide, and one to right;
//           positions
//          System.out.println(moveCount + " " + rotCount + " " + slideCount);
          Tile[][] testState = positionBlock(nextBlockCopy, boardState, moveCount, rotCount, 1);
          double[] testWeights = evaluateWeight(testState);
          double testWeight = DoubleStream.of(testWeights).sum();
          // System.out.print(moveCount + " " + rotCount);
          if (testWeight > maxWeight) {
            // printBoard(testState);
            // System.out.println(testWeights[0] + " " + testWeights[1] + " " + testWeights[2]);
            maxWeight = testWeight;
            bestPath = getPath(moveCount, rotCount, 1);
          }
          nextBlockCopy.setGridLocation(new int[] {0, 0});

        }


        // nextBlockCopy.setGridLocation(new int[] {startingRowIndex, startingColumnIndex});
        nextBlockCopy.rotateRight();
      }

    }
    return bestPath;
  }

  // private static void printBoard(Tile[][] testState) {
  // for (int i = 0; i < testState.length; i++) {
  // for (int j = 0; j < testState[i].length; j++) {
  // System.out.print((testState[i][j].isFilled() ? "x " : "o ")); // Ternary operator,
  // // basically an if statement
  // }
  // System.out.println();
  // }
  // System.out.println();
  //
  // }

  /**
   * converts integer representations of moves into array of individual moves
   * 
   * @param moveCount starting from the far left, how many times the block must be moved to the
   *        right
   * @param rotCount starting from the default configuration, the number of rotations to the right
   *        are required
   * @return an array of moves that first shifts the block to the left, then some amount to the
   *         right, rotates, and issues a 'drop' command to terminate the sequence x
   */
  private static Move[] getPath(int moveCount, int rotCount, int slideCount) {
    ArrayList<Move> path = new ArrayList<Move>();
    for (int i = 0; i < rotCount; i++) {
      path.add(Move.ROT_RIGHT);
    }
    for (int i = 0; i < 6; i++) { // puts the block into a constant, known position
      path.add(Move.LEFT);
    }
    for (int i = 0; i < moveCount; i++) {
      path.add(Move.RIGHT);
    }
    path.add(Move.DROP);
    path.add(Move.LEFT);
    for(int i = 0; i < slideCount; i++){
      path.add(Move.RIGHT);
    }
    return path.toArray(new Move[] {});
  }

  /**
   * positions a block in a copy of the board based on translation and rotation parameters
   * 
   * @param nextBlock the active block
   * @param boardState the state of non active tiles
   * @param moveCount the number of left/right moves in the test arrangement
   * @param rotCount the number of rotations in the test arrangement
   * @return the board with the block moved to a certain position
   */
  private static Tile[][] positionBlock(Block nextBlock, Tile[][] boardState, int moveCount,
      int rotCount, int slideCount) {
    // avoids reference issues
    Tile[][] tileCopy = new Tile[boardState.length][boardState[0].length];
    for (int i = 0; i < boardState.length; i++) {
      for (int j = 0; j < boardState[0].length; j++) {
        tileCopy[i][j] = new Tile(boardState[i][j].isActive(), boardState[i][j].isFilled(),
            boardState[i][j].getColor());
      }
    }

    Board boardCopy = new Board(tileCopy, nextBlock);

    for (int i = 0; i < 10; i++) {
      boardCopy.pressed(Move.LEFT);
    }

    for (int i = 0; i < moveCount; i++) {
      boardCopy.pressed(Move.RIGHT);
    }

    for (int i = 0; i < rotCount; i++) {
      boardCopy.pressed(Move.ROT_RIGHT);
    }

    boardCopy.pressed(Move.DROP);
    //slide parts after block is at bottom, doesn't work if drop terminates block
    boardCopy.pressed(Move.LEFT);
    for(int i = 0; i < slideCount; i++){
      boardCopy.pressed(Move.RIGHT);
    }

    return boardCopy.getBoardState();

  }

  /**
   * evaluates the relative value of the board
   * 
   * @param boardCopy the board to be analyzed
   * @return the value of the board
   */
  public static double[] evaluateWeight(Tile[][] boardCopy) {
    double[] weight = new double[3];
    double voids = 0;
    double height = 0;
    int maxHeight = 0;
    for (int i = 0; i < boardCopy[0].length; i++) {
      Tile[] colCopy = new Tile[boardCopy.length];
      for (int j = 0; j < boardCopy.length; j++) {
        colCopy[j] = boardCopy[j][i];
      }
      if (getHeight(colCopy) > maxHeight) {
        maxHeight = getHeight(colCopy);
      }
      // height += (HEIGHT_WEIGHT * getHeight(colCopy));
      // double voidCount = Math.pow(getNumVoids(colCopy), 2);
      // voids += (VOID_WEIGHT * (voidCount == 0 ? 0 : ( 1.0 / voidCount)));
      double voidCount = getNumVoids(colCopy);
      voids +=
          (weights[1] * Math.pow((voidCount == 0 ? 0.0000000000000001 : voidCount), VOID_POW)); // keeps
                                                                                                 // the
      // value form
      // being 0 in
      // Ternary
    }
    height = weights[0] * Math.pow((maxHeight == 0 ? 0.000000000000001 : maxHeight), HEIGHT_POW);
    double lines = 0;
    for (int i = 0; i < boardCopy[0].length; i++) {
      double lineCount = getNumLines(boardCopy);
      lines +=
          (weights[2] * Math.pow((lineCount == 0 ? 0.00000000000000001 : lineCount), LINE_POW));
    }
    // System.out.println("voids: " + voids + " heights: " + height + " lines: " + lines);
    weight[0] = voids;
    weight[1] = height;
    weight[2] = lines;
    return weight;
  }

  /**
   * returns the number of completed lines in the board
   * 
   * @param boardCopy the board to be analyzed. It should not have been processed to remove lines
   * @return the number of completed lines on the board (rows)
   */
  private static int getNumLines(Tile[][] boardCopy) {
    int numLines = 0;
    for (int i = 0; i < boardCopy.length; i++) {
      boolean isFull = true;
      boolean isEmpty = true;
      for (int j = 0; j < boardCopy[0].length; j++) {
        if (boardCopy[i][j].isFilled()) {
          isEmpty = false;
        }
        if (!boardCopy[i][j].isFilled()) {
          isFull = false;
        }
      }
      if (isFull && !isEmpty) {
        numLines++;
      }
    }
    // if (numLinexrintln(numLines);
    return numLines;
  }

  /**
   * analyzes a column of tiles for the number of voids it contains. A void is defined as any blank
   * space with a filled, non active tile at some point above it
   * 
   * @param tiles the column of tiles to be analyzed
   * @return the number of voids in the column
   */
  private static double getNumVoids(Tile[] tiles) {
    boolean hasFoundBlock = false;
    double voidCount = 0;
    for (int i = 0; i < tiles.length; i++) {
      if (hasFoundBlock && !tiles[i].isFilled()) {
        voidCount++;
      }
      if (tiles[i].isFilled()) {
        hasFoundBlock = true;
      }
    }
    return voidCount;
  }

  /**
   * gets the height of a column of tiles. The height is defined as the index of the highest filled,
   * non active tile in the column
   * 
   * @param tiles the column to be analyzed
   * @return the height of the column
   */
  private static int getHeight(Tile[] tiles) {
    int height = 0;
    for (int i = tiles.length - 1; i >= 0; i--) {
      if (tiles[i].isFilled()) {
        height = tiles.length - i;
      }
    }
    return height;
  }

  public static void updateWeights(double[] newWeights) {
    weights = newWeights;
  }

}


