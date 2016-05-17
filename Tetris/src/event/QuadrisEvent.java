package event;

import javafx.event.Event;
import javafx.event.EventType;


@Deprecated
public class QuadrisEvent extends Event {
	
	/**
	 * Who knows what this does
	 */
	private static final long serialVersionUID = 1L;
	
	public static final EventType<QuadrisEvent> QUADRIS_EVENT = new EventType<>(Event.ANY, "Quadris Event");

	public QuadrisEvent() {
		super(QUADRIS_EVENT);
		// TODO Auto-generated constructor stub
	}


}
