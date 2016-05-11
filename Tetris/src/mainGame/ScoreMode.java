package mainGame;

public enum ScoreMode {
	//don't really need to assign them numbers, but it can't hurt
	
  /**
   * * 1 Line = 100*(level + 1) points
   * 2 Lines = 200*(level + 1) points
   * 3 Lines = 300*(level + 1) points
   * 4 Lines = 900 points (aka a Tetris)
   * Every row fallen = 1 point
   * Every row moved down = 2 points
   * Every row dropped = 3 points
   * Every time update = TODO
   */
  HANK_LIAM(0),
  
  /** 
   * 1 Line = 50 points
   * 2 Lines = 150 points
   * 3 Lines = 350 points
   * 4 Lines = 1000 points (aka a Tetris)
   * Clear the board = 2000
   * Every piece = 10 points
   */
  NINTENDO(1),
  
  /**
   * 1 Line = 10 points
   * 2 Lines = 30 points
   * 3 Lines = 60 points
   * 4 Lines = 100 points (aka a Tetris)
   * Every piece = 1 point
   */
  SIMPLE(2);
  
  private final int value;

  private ScoreMode(int value) {
      this.value = value;
  }

  public int getValue() {
      return value;
  }
}
