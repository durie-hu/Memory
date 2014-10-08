package hu.durie.memory;

import javafx.event.Event;
import javafx.event.EventType;

public class CardSelectedEvent extends Event {

	public final static EventType<CardSelectedEvent> SELECTED = new EventType<CardSelectedEvent>(
			ANY, "SELECTED");

	private Card card;

	public CardSelectedEvent(EventType<? extends Event> eventType) {
		super(eventType);
	}
	
	public CardSelectedEvent(Card card){
		this(SELECTED);
		this.card = card;
	}

	private static final long serialVersionUID = 9129514159791734873L;

	public Card getCard() {
		return card;
	}

}
