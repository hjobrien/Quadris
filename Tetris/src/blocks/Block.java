package blocks;

import engine.Renderer;

public class Block {
  private Tile[][][] configurations = new Tile[][][] {};
  private int rotationIndex = 0;

  public int getRotationIndex() {
    return rotationIndex;
  }

  public boolean debug = false;
  // public boolean debug = true;

  private boolean falling = true;

  private BlockType type;

  // gives coordinates of block's bottom right tile in the form {row, column}
  private int[] locationInGrid = new int[2];

  /**
   * makes a new block instance with an array of configurations and the type of the block
   * 
   * @param configurations the array containing the shape of the block under all rotations
   * @param type the type of the block represented by this instance
   */
  public Block(Tile[][][] configurations, BlockType type) {
    int maxDim = Math.max(configurations[0].length, configurations[0][0].length);
    this.configurations = new Tile[configurations.length][maxDim][maxDim];
    // avoids pass by reference
    for (int i = 0; i < configurations.length; i++) {
      for (int j = 0; j < configurations[i].length; j++) {
        if (configurations[i][j].length < maxDim) {
          for (int k = 0; k < configurations[i][j].length; k++) {
            this.configurations[i][j][k] = configurations[i][j][k];
          }
          for (int k = configurations[i][j].length; k < maxDim; k++) {
            this.configurations[i][j][k] = new Tile();
          }
        }
        else{
          this.configurations[i][j] = configurations[i][j].clone();
        }
      }
    }
    this.type = type;
    // row, column of bottom right corner
    int startingRowIndex = configurations[rotationIndex].length - 1 + 3;
    int startingColumnIndex =
        (Renderer.HORIZONTAL_TILES - configurations[rotationIndex][0].length) / 2
            + configurations[rotationIndex][0].length - 1;
    locationInGrid = new int[] {startingRowIndex, startingColumnIndex};
  }

  /**
   * constructs a new instance of this object based on an existing block object
   * 
   * @param b the copied Block
   */
  // copy constructor?
  public Block(Block b) {
    this.configurations =
        new Tile[b.configurations.length][b.configurations[0].length][b.configurations[0][0].length];
    // avoids pass by reference
    for (int i = 0; i < b.configurations.length; i++) {
      for (int j = 0; j < b.configurations[i].length; j++) {
        for (int k = 0; k < b.configurations[i][j].length; k++) {
          if (configurations[i][j][k] != null) {
            this.configurations[i][j][k] = configurations[i][j][k];
          } else {
            configurations[i][j][k] = new Tile();
          }
        }
      }
    }
    this.rotationIndex = b.rotationIndex;
    this.locationInGrid = b.locationInGrid.clone();
  }

  /**
   * gets the shape of the block for its current rotation index
   * 
   * @return the shape of the block given its rotation index
   */
  public Tile[][] getShape() {
    return configurations[rotationIndex];
  }

  public boolean isFalling() {
    return falling;
  }

  public void setFalling() {
    falling = true;
  }

  public void stoppedFalling() {
    falling = false;
  }

  public String toString() {
    String s = "";
    for (Tile[] row : getShape()) {
      for (Tile t : row) {
        if (t.isFilled())
          s += "x";
        else
          s += "o";
      }
      s += "\n";
    }
    return s;
  }

  public void setGridLocation(int[] point) {
    this.locationInGrid = point;
  }

  public void setGridLocation(int x, int y) {
    this.locationInGrid = new int[] {x, y};
  }

  public int[] getGridLocation() {
    return this.locationInGrid;
  }

  public void moveRight() {
    this.locationInGrid = new int[] {locationInGrid[0], locationInGrid[1] + 1};
  }

  public void moveLeft() {
    this.locationInGrid = new int[] {locationInGrid[0], locationInGrid[1] - 1};
  }

  public void moveUp() {
    this.locationInGrid = new int[] {locationInGrid[0] - 1, locationInGrid[1]};
  }

  public void moveDown() {
    this.locationInGrid = new int[] {locationInGrid[0] + 1, locationInGrid[1]};
  }

  public void rotateRight() {
    rotationIndex--;
    if (rotationIndex < 0) {
      rotationIndex = configurations.length - 1;
    }
  }

  public void rotateLeft() {
    rotationIndex++;
    if (rotationIndex > configurations.length - 1) {
      rotationIndex = 0;
    }
  }

  public BlockType getType() {
    return type;
  }

}
