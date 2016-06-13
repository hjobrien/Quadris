package util;

import blocks.Tile;

public class BoardUtil {

  /**
   * copies a board by value
   * 
   * @param mainBoard the board to be copied
   * @return a new board with the values of the original
   */
  public static Tile[][] deepCopy(Tile[][] mainBoard) {
    Tile[][] copy = new Tile[mainBoard.length][mainBoard[0].length];
    for (int i = 0; i < mainBoard.length; i++) {
      for (int j = 0; j < mainBoard[i].length; j++) {
        if (mainBoard[i][j] != null) {
          copy[i][j] = new Tile(mainBoard[i][j].isActive(), mainBoard[i][j].isFilled(),
              mainBoard[i][j].getColor());
        } else {
          copy[i][j] = new Tile();
        }
      }
    }
    return copy;
  }
}
