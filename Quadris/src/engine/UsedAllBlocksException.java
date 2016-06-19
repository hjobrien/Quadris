package engine;

public class UsedAllBlocksException extends Exception {

  public UsedAllBlocksException(String errorMessage) {
    super(errorMessage);
  }
  
  public UsedAllBlocksException(){
    super();
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

}
