package engine;

import javafx.event.Event;
import javafx.event.EventType;

public class GameOverEvent extends Event{
  

  private static final long serialVersionUID = 1L;
  
  
  //Not currently used
  
  public static final EventType<GameOverEvent> GAME_OVER = new EventType<>(Event.ANY, "Game Over");
  
  public GameOverEvent(){
    super(GAME_OVER);
  }

}
