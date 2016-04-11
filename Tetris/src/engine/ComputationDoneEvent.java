package engine;

import javafx.event.Event;
import javafx.event.EventType;

public class ComputationDoneEvent extends Event {

  private static final long serialVersionUID = 7386067995359616435L;
  
  public static final EventType<ComputationDoneEvent> COMPUTATION_DONE = new EventType<>(Event.ANY, "Computation Finished");

  public ComputationDoneEvent(){
    super(COMPUTATION_DONE);
  }
}
