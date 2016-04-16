package engine;

import javafx.event.Event;
import javafx.event.EventType;
import mainGame.Move;

public class ComputationDoneEvent extends Event {

  private static final long serialVersionUID = 7386067995359616435L;
  
  public static final EventType<ComputationDoneEvent> COMPUTATION_DONE = new EventType<>(Event.ANY, "Computation Finished");

  private Move[] optimalPath;
  
  /**
   * constructs an event when the AI has finished processing the block
   * @param optimalPath the path the active block should take to arrive at its destination
   */
  public ComputationDoneEvent(Move[] optimalPath){
    super(COMPUTATION_DONE);
    for(Move m : optimalPath){
      System.out.print(m + ", ");
    }
    System.out.println();
    this.optimalPath = optimalPath;
  }

  public Move[] getOptimalPath() {
    return this.optimalPath;
  }
  
  
}
