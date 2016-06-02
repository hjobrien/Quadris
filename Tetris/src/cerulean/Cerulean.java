package cerulean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.DoubleStream;

import blocks.Block;
import blocks.Tile;
import engine.BoardFullException;
import engine.Engine;
import mainGame.Move;

/**
 * General AI class, handles game state analysis and move optimization
 * 
 * @author Hank
 *
 */

public class Cerulean {

  private long timeToGenStates = 0;
  private long timeToAnalyzeStates = 0;



  private double[] weights;// {HEIGHT_WEIGHT, VOID_WEIGHT, EDGE_WEIGHT,
  // ONE_LINE_WEIGHT, TWO_LINES_WEIGHT, THREE_LINES_WEIGHT, FOUR_LINES_WEIGHT


  private boolean analyzeTwo;

  private Engine boardAnalyzer;

  // keeps species that are too similar from breeding together, keeps GA from converging prematurely
  public static final double MAX_SIMILARITY_RATIO = 0.95;
  public static final double MAX_SIMILAR_WEIGHTS = 4; // ~= half the weights

  public Cerulean(int blocksToAnalyze) {
    this.analyzeTwo = (blocksToAnalyze == 2 ? true : false);
  }

  public Cerulean() {
    this.analyzeTwo = false;
  }

  /**
   * gives AI data for what block is active and the board state it is to be placed in
   * 
   * @param nextBlockType the kind of block to be analyzed
   * @param boardState the current board state
   * @throws BoardFullException if the AI cannot place a block without over-filling the board
   */
  public Move[] submitBlock(Block currentBlock, Block nextBlock, Tile[][] boardState)
      throws BoardFullException {
    timeToGenStates = 0;
    timeToAnalyzeStates = 0;
    long t1 = System.nanoTime();
    Move[] solution = computeBestPath(currentBlock, nextBlock, boardState);
    long total = (System.nanoTime() - t1);
//    System.out.println("Weight analysis took " + total / 1e9 + " seconds");
//    System.out.println("State creation took " + timeToGenStates / 1e9 + " seconds");
//    System.out.println("Analysis took " + timeToAnalyzeStates / 1e9 + " seconds");
//    System.out.println("\tUnaccounted for time "
//        + (total - timeToGenStates - timeToAnalyzeStates) / 1e9 + " seconds");
    return solution;
  }

  /**
   * Generalized move optimization method
   * 
   * @param currentBlock the block just introduced to the board
   * @param nextBlock the block that will be introduced after currentBlock, it is visible in the
   *        upper right corner of the application
   * @param boardState the board state without the block entered, all tiles are not active
   * 
   * @return an array of moves that positions the piece in to the optimal location
   * @throws BoardFullException if the board placement algorithm would have to over-fill the board
   *         to add a new block
   */
  private Move[] computeBestPath(Block currentBlock, Block nextBlock, Tile[][] boardState)
      throws BoardFullException {
    // TODO: change to clone (also 0,0 is wrong);

    // TODO not working
    // Block currentBlockCopy = currentBlock.clone();
    // System.out.println("Ceruelan is analyzing block of type " + currentBlock.getType());
    Block currentBlockCopy =
        new Block(currentBlock.getType(), new int[] {0, 0}, currentBlock.getRotationIndex());
    Block nextBlockCopy =
        new Block(nextBlock.getType(), new int[] {0, 0}, nextBlock.getRotationIndex());

    Move[] bestPath = new Move[] {};

    bestPath = convertToMovePath(getBestPath(currentBlockCopy, nextBlockCopy, boardState));
    return bestPath;
  }

  /**
   * 
   * @param currentBlock the active, falling block the AI is to manipulate
   * @param nextBlock a block taken into account for further move analysis so the AI can look two
   *        moves in advance
   * @param boardState the shape of the Board before the currentBlock is added
   * @return the best path the currentBlock can take as a composition of moves (left/right
   *         translations), rotations (executed before the translations), and slideCount
   *         (translations after the block had been fully dropped)
   * @throws BoardFullException if the board state that is produced by the addition of the block
   *         produces a filled board
   */
  public int[] getBestPath(Block currentBlock, Block nextBlock, Tile[][] boardState)
      throws BoardFullException {
    // block analysis for 2 blocks takes ~0.5 seconds
    // block analysis for 1 block takes ~0.005 seconds
    // evaluate weight takes ~12000ns per call and is called between 1800 and 7200 times per
    // analysis
    // for a total of about 0.04 seconds
    // get all states is called between 31 and 120 times and takes ~450000 ns per execution
    // for a total of about 3.15 seconds 0.036s
    int count = 0;
    // System.out.println(currentBlock.getType() + " " + nextBlock.getType());
    // Map<Path, Tile[][]> boardStatesWithFirstBlock = getAllStates(currentBlock, boardState);
    ArrayList<MoveResult> boardStatesWithFirstBlock = getAllStates(currentBlock, boardState);
    count++;
    // System.out.println("Number of states found: " + boardStatesWithFirstBlock.size());
    // long now = System.nanoTime();
    double bestWeight = Double.NEGATIVE_INFINITY;
    int[] bestMovePath = new int[] {};
    for (MoveResult possibleBoardState : boardStatesWithFirstBlock) {
      if (analyzeTwo) {
        cleanBoard(possibleBoardState.getBoardResult());
        ArrayList<MoveResult> boardStatesWithTwoBlocks =
            getAllStates(nextBlock, possibleBoardState.getBoardResult());
        count++;
        for (MoveResult futureBoardState : boardStatesWithTwoBlocks) {
          double boardWeight = evaluateWeight(futureBoardState.getBoardResult());
          count++;
          if (boardWeight > bestWeight) {

            // EXTREMELY HELPFUL FOR DEBUGGING, DO NOT ERASE
            // System.out.println("best weight = " + bestWeight);
            // System.out.println("board weight = " + boardWeight);
            // System.out.print("path = ");
            // String pathFormula = "";
            // for (int i : possibleBoardState.getKey().getPath()){
            // pathFormula += i + " ";
            // }
            // System.out.println(pathFormula);
            // Engine.printBoard(possibleBoardState.getValue());
            // System.out.println();

            bestWeight = boardWeight;
            // only sets move to how the first block was moved
            bestMovePath = possibleBoardState.getPath();
          }
        }
      } else {
        double boardWeight = evaluateWeight(possibleBoardState.getBoardResult());
        if (boardWeight > bestWeight) {
          bestWeight = boardWeight;
          // only sets move to how the first block was moved
          bestMovePath = possibleBoardState.getPath();
        }
      }
    }
    // long later = System.nanoTime();
    // System.out.println("Time taken to find best move: " + (later - now));
    // System.out.println("Average Time: " + (later - now) / boardStatesWithFirstBlock.size());
    // System.out.println("getAllStates called " + count + " times");
    return bestMovePath;
  }

  private void cleanBoard(Tile[][] boardState) {
    for (Tile[] row : boardState) {
      for (Tile t : row) {
        t.setActive(false);
      }
    }
  }

  /**
   * @param currentBlock the block to be added
   * @param boardState the shape of the inactive tiles before the currentBlock is added
   * @return All the possible board states that could exist given some block and some board state
   * @throws BoardFullException if any possible board state is a full board
   */
  private ArrayList<MoveResult> getAllStates(Block currentBlock, Tile[][] boardState)
      throws BoardFullException {
    long now = System.nanoTime();
    // Map<Path, Tile[][]> boardStates = new HashMap<Path, Tile[][]>();
    ArrayList<MoveResult> boardStates = new ArrayList<MoveResult>();
    // reduce loop reps TODO
    // TODO: create variable for blocks grid location so block can be reset to good (non 0) value
    for (int moveCount = 0; moveCount < 10; moveCount++) {
      for (int rotCount = 0; rotCount < currentBlock.getNumRotations(); rotCount++) {
        for (int slideCount = 0; slideCount < 3; slideCount++) {
          // TODO make sure this clone method works in all cases. If so, the comments below can be
          // removed
          Block tempBlock = currentBlock.clone();
          try {
            boardStates.add(new MoveResult(new int[] {moveCount, rotCount, slideCount},
                positionBlock(tempBlock, boardState, moveCount, rotCount, slideCount)));
          } catch (BoardFullException e) {
            // System.err.println("A board State is full " + Math.random());
            // TODO prob bug here
          }
          // TODO: change
          // currentBlock.setGridLocation(new int[] {0, 0});
        }
        currentBlock.rotateRight();
      }
    }
    this.timeToGenStates += (System.nanoTime() - now);
    return boardStates;
  }

  /**
   * wrapper for same method that takes multiple integers, this converts an array of ints into
   * single integers converts a path composed of integers into one of moves
   * 
   * @param bestPath the best path as an array of movement instructions
   * @return the best path as individual moves
   */
  private Move[] convertToMovePath(int[] bestPath) {
    return getPath(bestPath[0], bestPath[1], bestPath[2]);
  }

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
  private Move[] getPath(int moveCount, int rotCount, int slideCount) {
    ArrayList<Move> path = new ArrayList<Move>();
    for (int i = 0; i < rotCount; i++) {
      path.add(Move.ROT_RIGHT);
    }
    // TODO: change 6 to accurate num to shift
    // if (moveCount <= 6) {
    // for (int i = 0; i < (6 - moveCount); i++) {
    // path.add(Move.LEFT);
    // }
    // } else {
    // for (int i = 0; i < moveCount - 6; i++) {
    // path.add(Move.RIGHT);
    // }
    // }
    // puts the block into a constant known position
    for (int i = 0; i < 6; i++) {
      path.add(Move.LEFT);
    }
    for (int i = 0; i < moveCount; i++) {
      path.add(Move.RIGHT);
    }
    path.add(Move.DROP);

    if (slideCount == 0) {
      path.add(Move.LEFT);
    }
    // dont need to handle == 1, that's just left then right
    else if (slideCount == 2) {
      path.add(Move.RIGHT);
    }

    // ensures that the block is dropped as far as it can go

    path.add(Move.DROP);

    return path.toArray(new Move[] {});
  }

  /**
   * positions a block in a copy of the board based on translation and rotation parameters
   * 
   * @param blockToPosition the active block
   * @param boardState the state of non active tiles
   * @param moveRightCount the number of left/right moves in the test arrangement
   * @param rotCount the number of rotations in the test arrangement
   * @return the board with the block moved to a certain position
   * @throws BoardFullException if by adding a block the board would become over-filled, it doesn't
   *         know how to move in this situation
   */
  private Tile[][] positionBlock(Block blockToPosition, Tile[][] boardState, int moveRightCount,
      int rotCount, int slideCount) throws BoardFullException {
    // System.out.println(moveCount + " " + rotCount + " " + slideCount);
    // avoids reference issues
    Tile[][] tileCopy = new Tile[boardState.length][boardState[0].length];
    for (int i = 0; i < boardState.length; i++) {
      for (int j = 0; j < boardState[0].length; j++) {
        tileCopy[i][j] = new Tile(boardState[i][j].isActive(), boardState[i][j].isFilled(),
            boardState[i][j].getColor());
      }
    }

    if (boardAnalyzer == null) {
      boardAnalyzer = new Engine(tileCopy);
    } else {
      boardAnalyzer.setGameBoard(tileCopy);
    }

    boardAnalyzer.addBlock(blockToPosition);

    if (boardAnalyzer.hasFullBoard()) {
      boardAnalyzer.reset();
      throw new BoardFullException();
    }

    for (int i = 0; i < rotCount; i++) {
      boardAnalyzer.executeMove(Move.ROT_RIGHT);
    }

    // TODO make not 10, just what is necessary
    for (int i = 0; i < 10; i++) {
      boardAnalyzer.executeMove(Move.LEFT);
    }

    for (int i = 0; i < moveRightCount; i++) {
      boardAnalyzer.executeMove(Move.RIGHT);
    }

    boardAnalyzer.executeMove(Move.DROP);

    // don't need to handle slideCount = 1 because that would be moving the block left then right
    // (not moving at all)
    if (slideCount == 0) {
      boardAnalyzer.executeMove(Move.LEFT);
    } else if (slideCount == 2) {
      boardAnalyzer.executeMove(Move.RIGHT);
    }

    // ensures that the block is dropped as far as it can go
    boardAnalyzer.executeMove(Move.DROP);

    // for debugging purposes
    // TODO get rid of the gross 10 left moves, make lines clear for future
    // block drops/better AI
    // System.out.println("rotCount = " + rotCount + ", moveCount = " +
    // moveCount + ", slideCount = " + slideCount);
    // boardAnalyzer.printBoard();
    // System.out.println();

    return boardAnalyzer.getGameBoard();

  }

  /**
   * wrapper for method that returns an array of weights representing each sub-weight
   * 
   * @param boardCopy the board to be analyzed
   * @return the value of the boardState given the weights the AI is currently using
   */
  public double evaluateWeight(Tile[][] boardCopy) {
    return DoubleStream.of(evaluateEachWeight(boardCopy)).sum();
  }

  /**
   * evaluates the relative value of the board "fitness function" for machine learning
   * 
   * @param boardCopy the board to be analyzed
   * @return the value of the board
   */
  public double[] evaluateEachWeight(Tile[][] boardCopy) {
    long now = System.nanoTime();
    double[] weight = new double[4];
    boolean full = false;
    double voids = 0;
    double height = 0;
    // double heightScore = 0;
    double edges = 0;
    for (int i = 0; i < boardCopy[0].length; i++) {
      Tile[] colCopy = new Tile[boardCopy.length];
      for (int j = 0; j < boardCopy.length; j++) {
        colCopy[j] = boardCopy[j][i];
      }
      // TODO: this is a differing end-game condition than rest of game
      if (colCopy[3].isFilled()) {
        full = true;
      }
      // TODO: make sure this works
      // if (tempHeightScore > heightScore) {
      // heightScore = tempHeightScore;
      // }

      double voidCount = getNumVoids(colCopy);
      voids += weights[1] * voidCount;
      edges += weights[2] * Math.abs(((boardCopy[i].length - 1) / 2.0) - i) * getNumActive(colCopy);
    }
    double heightScore = getHeightScore(boardCopy);
    height = weights[0] * heightScore;

    // gets the composite lineScore, which will be entered in weight[3]
    // alternatively, we could make weight[] bigger and enter in each line weight for its
    // corresponding
    // weights[] spot, but that seems excessive and only relevant if we choose to add way more
    // weights
    int lineCount = getNumLines(boardCopy);
    double lineScore = 0;

    // returns the value of the line clearance based on how many lines were cleared
    if (lineCount > 0) {
      lineScore += weights[2 + lineCount];
    }

    if (!full) {
      weight[0] = voids;
      weight[1] = height;
      weight[2] = edges;

      // the sum score of the lines cleared; corresponds to weights[3] - weights[6]
      weight[3] = lineScore;
    } else {
      // penalizes for ending the game
      weight[0] = Integer.MIN_VALUE;
      weight[1] = Integer.MIN_VALUE;
      weight[2] = Integer.MIN_VALUE;
      weight[3] = Integer.MIN_VALUE;
    }
    this.timeToAnalyzeStates += (System.nanoTime() - now);
    return weight;
  }

  /**
   * returns the number of active tiles in a column
   * 
   * @param colCopy the column of tiles to analyze
   * @return the number of active tiles it contains
   */
  private double getNumActive(Tile[] colCopy) {
    int count = 0;
    for (Tile t : colCopy) {
      if (t.isActive()) {
        count++;
      }
    }
    return count;
  }

  /**
   * produces a score based on the height of a column
   * 
   * @param colCopy the column to be scored
   * @return the score of the column
   */
  private double getHeightScore(Tile[][] boardCopy) {

    int row = 0, column = 0, highestActiveTileIndex = 0, highestFilledTileIndex = 0;
    boolean foundHighestActiveTile = false, foundHighestFilledTile = false;
    while (row < boardCopy.length && (!foundHighestActiveTile && !foundHighestFilledTile)) {
      while (column < boardCopy[row].length
          && (!foundHighestActiveTile && !foundHighestFilledTile)) {
        if (!foundHighestActiveTile) {
          if (boardCopy[row][column].isActive()) {
            highestActiveTileIndex = row;
            foundHighestActiveTile = true;
          }
        }
        if (!foundHighestFilledTile) {
          if (boardCopy[row][column].isFilled()) {
            highestFilledTileIndex = row;
            foundHighestFilledTile = true;
          }
        }
        column++;
      }
      row++;
      column = 0;
    }

    return (boardCopy.length - highestFilledTileIndex)
        + ((boardCopy.length - highestActiveTileIndex) * 0.1);

    // // gets highest overall
    // int overallHeight = 0;
    //// for (int i = colCopy.length - 1; i >= 0; i--) {
    //// if (colCopy[i].isFilled()) {
    //// overallHeight = colCopy.length - i;
    //// }
    //// }
    // for(int i = 0; i < colCopy.length; i++){
    // if(colCopy[i].isFilled()){
    // return i;
    // }
    // }
    //
    // return overallHeight;
  }

  /**
   * returns the number of completed lines in the board
   * 
   * @param boardCopy the board to be analyzed. It should not have been processed to remove lines
   * @return the number of completed lines on the board (rows)
   */
  private int getNumLines(Tile[][] boardCopy) {
    int numLines = 0;
    for (int i = 0; i < boardCopy.length; i++) {
      boolean isFull = true;
      for (int j = 0; j < boardCopy[0].length; j++) {
        if (!boardCopy[i][j].isFilled()) {
          isFull = false;
        }
      }
      if (isFull) {
        numLines++;
      }
    }
    return numLines;
  }

  /**
   * analyzes a column of tiles for the number of voids it contains. A void is defined as any blank
   * space with a filled, non active tile at some point above it
   * 
   * @param tiles the column of tiles to be analyzed
   * @return the number of voids in the column
   */
  private double getNumVoids(Tile[] tiles) {
    boolean hasFoundBlock = false;
    double voidCount = 0;
    for (int i = 0; i < tiles.length; i++) {
      if (hasFoundBlock && !tiles[i].isFilled()) {
        voidCount++;
      }
      // TODO: maybe change to isActive
      // TODO: possibly only consider voids that this block made
      if (tiles[i].isFilled()) {
        hasFoundBlock = true;
      }
    }
    return voidCount;
  }

  /**
   * analyzes each column of the board to get their height score, only scores columns under the
   * maximum height
   * 
   * @param boardCopy the board to be analyzed
   * @return the sum of the score of all lines under the maximum one multiplied by 0.1
   */
  private double getExtraHeightScore(Tile[][] boardCopy) {

    double extraHeight = Double.POSITIVE_INFINITY;
    for (int column = 0; column < boardCopy[0].length; column++) {
      int colHeight = Integer.MAX_VALUE;
      for (int row = boardCopy.length - 1; row >= 0; row--) {
        if (boardCopy[row][column].isActive()) {
          colHeight = row;
        }
      }
      if (colHeight < extraHeight) {
        extraHeight = colHeight;
      }
    }
    return extraHeight * 0.1;
  }

  /**
   * allows the AI's weights to be changed for use in AI_TRAINING mode
   * 
   * @param newWeights the new weights to be used in the fitness function
   */
  public void setWeights(double[] newWeights) {
    weights = newWeights;
  }

  /**
   * gets the weights of the AI as a string for printing
   * 
   * @return the weights as a String
   */
  public String getWeights() {
    String s = "";
    for (double d : weights) {
      s += (d + " ");
    }
    return s;
  }

  /**
   * the part of the code that makes the AI an AI, this method takes in certain 'species' (the
   * weights) and their associated fitness. It then makes a new generation of species that share
   * some behavior with their parents A child is created by first 'crossing over' the two parent
   * genes at random points such that (parent 1) xxxxxxxx => (child) oxxooxxx (parent 2) oooooooo
   * then each gene is determined if it should be mutated (related to the mutationFactor) and then
   * if it is to be mutated, the value at that gene is multiplied by some value v such that 0.5 <= v
   * <= 1.5 the final list of children includes the parents so the AI never regresses (elitist
   * selection)
   * 
   * @param species the array of all possible parents
   * @param speciesAvgScore the related fitness of each of the possible parents
   * @param mutationFactor the probability a gene at a particular locus will mutate, should be
   *        between 0 and 1
   * @return an array of possible children whose fitness is to be reevaluated
   */
  public static double[][] breed(double[][] species, double[] speciesAvgScore,
      double mutationFactor) {
    double bestVal = Double.NEGATIVE_INFINITY;
    int bestIndex = 0;
    for (int i = 0; i < speciesAvgScore.length; i++) {
      if (speciesAvgScore[i] > bestVal) {
        bestIndex = i;
        bestVal = speciesAvgScore[i];
      }
    }
    int secondIndex = 0;
    double secondVal = Double.NEGATIVE_INFINITY;
    for (int i = 0; i < speciesAvgScore.length; i++) {
      if (speciesAvgScore[i] > secondVal
          && areSufficientlyDifferent(species[i], species[bestIndex])) {
        secondIndex = i;
        secondVal = speciesAvgScore[i];
      }
    }
    // ArrayList<Double> avgScores = new ArrayList<Double>();
    // for (double d : speciesAvgScore) {
    // avgScores.add(d);
    // }
    // int bestIndex = avgScores.indexOf(Collections.max(avgScores));
    // avgScores.remove(bestIndex);
    // int secondIndex = avgScores.indexOf(Collections.max(avgScores));
    // if (secondIndex >= bestIndex) {
    // secondIndex++;
    // }
    double[] bestCandidate = species[bestIndex];
    double[] secondCandidate = species[secondIndex];
    int numSpecies = species.length;
    double[][] newSpecies = new double[numSpecies][];
    Random rand = new Random();
    // ensures each generation is never worse than the last one be keeping
    // the top two species
    newSpecies[0] = bestCandidate;
    newSpecies[1] = secondCandidate;
    for (int i = 2; i < numSpecies; i++) {
      // single crossover algorithm (works better for long genomes?)
      // int crossoverLocus = rand.nextInt(numSpecies);// the place where
      // the genes will cross over
      // double[] child = Arrays.copyOf(bestCandidate,
      // bestCandidate.length);
      // for (int j = crossoverLocus; j < secondCandidate.length; j++) {
      // child[j] = secondCandidate[j];
      // }
      // many-cross algorithm. this seems to be 'fairer' in that earlier
      // weights change as much as
      // later ones
      double[] child = new double[bestCandidate.length];
      for (int j = 0; j < child.length; j++) {
        if (rand.nextBoolean()) {
          child[j] = bestCandidate[j];
        } else {
          child[j] = secondCandidate[j];
        }
      }
      for (int j = 0; j < child.length; j++) {
        double doMutate = rand.nextDouble();
        if (doMutate < mutationFactor) {
          // shifts the range from 0-1 to 0.5-1.5
          double mutationAmount = rand.nextDouble() + 0.5;
          child[j] *= mutationAmount;
        }
        // rounds to 2 decimal places safely
        child[j] = new BigDecimal(child[j]).setScale(2, RoundingMode.HALF_UP).doubleValue();
      }
      newSpecies[i] = child;
    }

    return newSpecies;
  }

  /**
   * tries to test if two candidate species are too similar to allow for successful breeding, it
   * attempts to prevent in-breading. This is based on how many of each species weights are within
   * some threshold of similarity
   * 
   * @param species1 the first species to compare
   * @param species2 the second species to compare
   * @return true if they are different enough for successful breeding, false otherwise
   */
  private static boolean areSufficientlyDifferent(double[] species1, double[] species2) {
    int numWithinSimilarityThreshold = 0;
    for (int i = 0; i < species1.length; i++) {
      if ((species1[i] - species2[i]) < ((1 - MAX_SIMILARITY_RATIO) * species1[i])) {
        numWithinSimilarityThreshold++;
      }
    }
    if (numWithinSimilarityThreshold < MAX_SIMILAR_WEIGHTS) {
      return true;
    } else {
      return false;
    }
  }

}
