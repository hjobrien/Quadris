package engine;

public class LongBoards {

  public boolean isOccupied(int x, int y, long[] board) {
    int column = getColumn(x, board);
    return ((column >>> y) & 1) != 0; // 0 or 1, then compared to 1 to see if filled or unfilled !=
                                      // 0 is faster than == 1
  }

  static final int MASK = (1 << 25) - 1; // 2^25 -1 = 25 1's

  public int getColumn(int colIndex, long[] board) {
    int longIndex = colIndex / 2;
    int inLongIndex = (colIndex % 2) * 25; // either 0 or 25
    return (int) ((board[longIndex] >>> inLongIndex) & MASK); // unsigned bit shift = >>>, then AND
                                                              // it with the MASK (0's are blocked,
                                                              // selects the lowest 25 bits
  }

  //only works if the row being removed is the very bottom row
  public void shiftDown(long[] board, int spacesToDrop) {
    int mask;
    int squaresToKeep = 25 - spacesToDrop;
    // for low order bits, squares to keep mask is just squaresToKeep 1s
    mask = (1 << squaresToKeep) - 1; // gives us squaresToKeep 1's
    mask = mask | (mask << 25); // moves the mask up to apply first version of mask to high order
                                // bits while maintaining the mask for low order bits
    board[0] = (board[0] >>> spacesToDrop) & mask;
    board[1] = (board[1] >>> spacesToDrop) & mask;
    board[2] = (board[2] >>> spacesToDrop) & mask;
    board[3] = (board[3] >>> spacesToDrop) & mask;
    board[4] = (board[4] >>> spacesToDrop) & mask;

  }
}
