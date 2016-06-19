package engine;

public class BoardFullException extends Exception {

  public BoardFullException(String errorMessage) {
    super(errorMessage);
  }
  
  public BoardFullException(){
    super();
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
}
