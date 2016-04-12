package engine;

import javafx.event.Event;
import javafx.event.EventType;
import mainGame.Move;

public class ComputationDoneEvent extends Event {

  private static final long serialVersionUID = 7386067995359616435L;
  
  public static final EventType<ComputationDoneEvent> COMPUTATION_DONE = new EventType<>(Event.ANY, "Computation Finished");

  private Move[] optimalPath;
  
  public ComputationDoneEvent(Move[] optimalPath){
    super(COMPUTATION_DONE);
    this.optimalPath = optimalPath;
  }

  public Move[] getOptimalPath() {
    return this.optimalPath;
  }
  
  
}
