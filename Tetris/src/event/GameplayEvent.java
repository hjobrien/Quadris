package event;

import javafx.event.Event;
import javafx.event.EventType;

public class GameplayEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final EventType<GameplayEvent> QUADRIS = new EventType<>(Event.ANY, "QUADRIS_STRING"); 

	public GameplayEvent(EventType<? extends Event> eventType) {
		super(eventType);
		// TODO Auto-generated constructor stub
	}
	
	public GameplayEvent() {
		super(QUADRIS);
	}
	
	public EventType<? extends GameplayEvent> getEventType() {
		return (EventType<? extends GameplayEvent>) super.getEventType();
	}

}
