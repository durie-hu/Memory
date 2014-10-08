package hu.durie.memory;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;

enum PlayerState {
	STARTED, BEFORE_FIRST_CARD, FIRST_CARD_TURNED, SECOND_CARD_TURNED, FINISHED
}

public class Player {

	private EventHandler<CardSelectedEvent> cardEventHandler = new EventHandler<CardSelectedEvent>() {

		@Override
		public void handle(CardSelectedEvent event) {
			Card newValue = event.getCard();
			switch (state.getValue()) {
			case BEFORE_FIRST_CARD:
				if (newValue.getState().getValue() == CardState.BACK) {
					cardFirst = newValue;
					cardFirst.getState().setValue(CardState.FRONT);
					state.setValue(PlayerState.FIRST_CARD_TURNED);
				}
				break;
			case FIRST_CARD_TURNED:
				if (newValue.getState().getValue() == CardState.BACK
						&& newValue != cardFirst) {
					cardSecond = newValue;
					cardSecond.getState().setValue(CardState.FRONT);
					state.setValue(PlayerState.SECOND_CARD_TURNED);
					System.out.println("First card: " + cardFirst.getKind()
							+ ". Second card: " + cardSecond.getKind());
				}
				break;
			case SECOND_CARD_TURNED:
				if (cardFirst.getKind() == cardSecond.getKind()) {
					dropCards();
					state.setValue(PlayerState.BEFORE_FIRST_CARD);
				} else {
					resetCards();
					state.setValue(PlayerState.FINISHED);
				}
				cardFirst = null;
				cardSecond = null;
				break;
			default:
				break;
			}
		}
	};

	private String name;
	private SimpleObjectProperty<PlayerState> state = new SimpleObjectProperty<>(
			PlayerState.STARTED);
	private int score = 0;
	private Card cardFirst;
	private Card cardSecond;

	Player(String name) {
		this.name = name;

		state.addListener(new ChangeListener<PlayerState>() {

			@Override
			public void changed(
					ObservableValue<? extends PlayerState> observable,
					PlayerState oldValue, PlayerState newValue) {
				System.out.println(name + ": " + oldValue + "->" + newValue);
			}
		});
	}

	public void resetCards() {
		if (cardFirst != null) {
			cardFirst.getState().setValue(CardState.BACK);
		}
		if (cardSecond != null) {
			cardSecond.getState().setValue(CardState.BACK);
		}
	}

	public void dropCards() {
		score += 2;
		cardFirst.getState().setValue(CardState.DROPPED);
		cardSecond.getState().setValue(CardState.DROPPED);
	}

	public SimpleObjectProperty<PlayerState> getState() {
		return state;
	}

	public void printScore() {
		System.out.println("SCORE " + name + ": " + score);
	}

	public EventHandler<CardSelectedEvent> getCardEventHandler() {
		return cardEventHandler;
	}

}
