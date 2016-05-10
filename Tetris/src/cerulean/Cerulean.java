package cerulean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.DoubleStream;

import blocks.Block;
import blocks.Tile;
import engine.Engine;
import mainGame.Move;

/**
 * General AI class, handles game state analysis and move optimization
 * 
 * @author Hank
 *
 */

public class Cerulean {

  // Board Weight constants
  // negative means its a bad thing being weighted (overall board height)
  // positive means its a good thing (full lines);

  private static final double HEIGHT_WEIGHT = -70;
  private static final double VOID_WEIGHT = -97.85;
  private static final double LINE_WEIGHT = 306.77;
  private static final double EDGE_WEIGHT = 5;

  private double[] weights = new double[] {HEIGHT_WEIGHT, VOID_WEIGHT, LINE_WEIGHT, EDGE_WEIGHT};


  // change for how important multiple of an occurrence is
  // negative numbers mean as the quantity gets higher, it gets less important
  // numbers between 0 and |1| mean as the quantity get higher, it increases in importance more
  // slowly
  // numbers > |1| mean as the quantity gets higher, it increases in importance more quickly
  private static final double HEIGHT_POW = 1;
  private static final double VOID_POW = 1;
  private static final double LINE_POW = 1;

  // TODO: add a positive weight for how full each row is?

  private Move[] solutionPath = new Move[] {Move.RIGHT}; // partially filled to prevent
                                                         // errors later on
  private Engine boardAnalyzer;

  /**
   * called when a solution is needed for a given block
   * 
   * @return an array of moves needed to get piece to the optimal location, should be some form of
   *         left/right, rotate, drop
   */
  public Move[] getSolution() {
    return solutionPath;
  }

  /**
   * gives AI data for what block is active and the board state it is to be placed in
   * 
   * @param nextBlockType the kind of block to be analyzed
   * @param boardState the current board state
   */
  public Move[] submitBlock(Block nextBlock, Tile[][] boardState) {
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
  private Move[] computeBestPath(Block nextBlock, Tile[][] boardState) {

    double maxWeight = Double.NEGATIVE_INFINITY;
    Block nextBlockCopy =
        new Block(nextBlock.getType(), new int[] {0, 0}, nextBlock.getRotationIndex());

    Move[] bestPath = new Move[] {};
    // TODO: reduce number of loops reps
    for (int moveCount = 0; moveCount < 10; moveCount++) {
      // 10 possible worst case left/right options
      for (int rotCount = 0; rotCount < nextBlockCopy.getNumRotations(); rotCount++) {
        for (int slideCount = 0; slideCount < 3; slideCount++) { // from slide one to left, no
                                                                 // slide, and one to right;
          // positions
          // System.out.println(moveCount + " " + rotCount + " " + slideCount);
          Tile[][] testState =
              positionBlock(nextBlockCopy, boardState, moveCount, rotCount, slideCount);
          double[] testWeights = evaluateWeight(testState);
          double testWeight = DoubleStream.of(testWeights).sum();
          // System.out.print(moveCount + " " + rotCount);
          if (testWeight > maxWeight) {
            // printBoard(testState);
            // System.out.println(testWeights[0] + " " + testWeights[1] + " " + testWeights[2]);
            maxWeight = testWeight;
            bestPath = getPath(moveCount, rotCount, slideCount);
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
  private Move[] getPath(int moveCount, int rotCount, int slideCount) {
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
    for (int i = 0; i < slideCount; i++) {
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
  private Tile[][] positionBlock(Block nextBlock, Tile[][] boardState, int moveCount, int rotCount,
      int slideCount) {
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
      boardAnalyzer = new Engine(tileCopy, false, false);
    }
    boardAnalyzer.setGameBoard(tileCopy);
    boardAnalyzer.addBlock(nextBlock);
    for (int i = 0; i < rotCount; i++) {
      boardAnalyzer.executeMove(Move.ROT_RIGHT);
    }
    for (int i = 0; i < 10; i++) {
      boardAnalyzer.executeMove(Move.LEFT);
    }

    for (int i = 0; i < moveCount; i++) {
      boardAnalyzer.executeMove(Move.RIGHT);
    }



    boardAnalyzer.executeMove(Move.DROP);

    // slide parts after block is at bottom, shouldn't work if drop terminates block
    boardAnalyzer.executeMove(Move.LEFT);
    for (int i = 0; i < slideCount; i++) {
      boardAnalyzer.executeMove(Move.RIGHT);
    }

    return boardAnalyzer.getGameBoard();

  }

  /**
   * evaluates the relative value of the board "fitness function" for machine learning
   * 
   * @param boardCopy the board to be analyzed
   * @return the value of the board
   */
  public double[] evaluateWeight(Tile[][] boardCopy) {
    double[] weight = new double[4];
    boolean full = false;
    double voids = 0;
    double height = 0;
    double heightScore = 0;
    double edges = 0;
    for (int i = 0; i < boardCopy[0].length; i++) {
      Tile[] colCopy = new Tile[boardCopy.length];
      for (int j = 0; j < boardCopy.length; j++) {
        colCopy[j] = boardCopy[j][i];
      }
      if (colCopy[0].isFilled()) {
        full = true;
      }

      double tempHeightScore = getHeightScore(colCopy) + getExtraHeightScore(boardCopy);
      if (tempHeightScore > heightScore) {
        heightScore = tempHeightScore;
      }

      double voidCount = getNumVoids(colCopy);
      voids += (weights[1] * Math.pow((voidCount == 0 ? 0.0000000000000001 : voidCount), VOID_POW)); // keeps
                                                                                                     // the
      // value from
      // being 0 in
      // Ternary


      edges += weights[3] * Math.abs((boardCopy[i].length / 2) - i) * getNumActive(colCopy);
    }
    height =
        weights[0] * Math.pow((heightScore == 0 ? 0.000000000000001 : heightScore), HEIGHT_POW);

    double lines = 0;
    for (int i = 0; i < boardCopy[0].length; i++) {
      double lineCount = getNumLines(boardCopy);
      lines +=
          (weights[2] * Math.pow((lineCount == 0 ? 0.00000000000000001 : lineCount), LINE_POW));
    }
    if (!full) {
      weight[0] = voids;
      weight[1] = height;
      weight[2] = lines;
      weight[3] = edges;
    } else {
      weight[0] = -10000000;
      weight[1] = -10000000;
      weight[2] = -10000000;
      weight[3] = -10000000;
    }
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
  private double getHeightScore(Tile[] colCopy) {
    // gets highest overall
    int overallHeight = 0;
    for (int i = colCopy.length - 1; i >= 0; i--) {
      if (colCopy[i].isFilled()) {
        overallHeight = colCopy.length - i;
      }
    }

    return overallHeight;
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
  public double[][] breed(double[][] species, double[] speciesAvgScore, double mutationFactor) {
    ArrayList<Double> avgScores = new ArrayList<Double>();
    for (double d : speciesAvgScore) {
      avgScores.add(d);
    }
    int bestIndex = avgScores.indexOf(Collections.max(avgScores));
    avgScores.remove(bestIndex);
    int secondIndex = avgScores.indexOf(Collections.max(avgScores));
    if (secondIndex >= bestIndex) {
      secondIndex++;
    }
    double[] bestCandidate = species[bestIndex];
    double[] secondCandidate = species[secondIndex];
    int numSpecies = species.length;
    double[][] newSpecies = new double[numSpecies][];
    Random rand = new Random();
    // ensures each generation is never worse than the last one be keeping the top two species
    newSpecies[0] = bestCandidate;
    newSpecies[1] = secondCandidate;
    for (int i = 2; i < numSpecies; i++) {
      // single crossover algorithm (works better for long genomes?)
      // int crossoverLocus = rand.nextInt(numSpecies);// the place where the genes will cross over
      // double[] child = Arrays.copyOf(bestCandidate, bestCandidate.length);
      // for (int j = crossoverLocus; j < secondCandidate.length; j++) {
      // child[j] = secondCandidate[j];
      // }
      // many-cross algorithm. this seems to be 'fairer' in that earlier weights change as much as
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
          double mutationAmount = rand.nextDouble() + 0.5; // shifts the random from 0-1 to 0.5-1.5
          child[j] *= mutationAmount;
        }
        // rounds to 2 decimal places safely
        child[j] = new BigDecimal(child[j]).setScale(2, RoundingMode.HALF_UP).doubleValue();
      }
      newSpecies[i] = child;
    }

    return newSpecies;
  }

}


