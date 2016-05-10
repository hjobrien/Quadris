package mainGame;

public enum ScoreMode {
  HANK_LIAM(0),
  NINTENDO(1),
  SIMPLE(2);
  
  private final int value;

  private ScoreMode(int value) {
      this.value = value;
  }

  public int getValue() {
      return value;
  }
}
