package hu.durie.memory;

import java.util.Arrays;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;

public class Table {

	private static final int ROWS = 3;
	private static final int COLUMNS = 4;

	private Card[] cards;
	private GridPane root;

	private SimpleObjectProperty<Card> currentTurned = new SimpleObjectProperty<>();

	public Table(GridPane root) {
		this.root = root;
	}

	Card[] initCards() {
		Card[] cards = new Card[ROWS * COLUMNS];
		for (int i = 0; i < ROWS * COLUMNS; i++) {
			final Card c = new Card(i);
			cards[i] = c;
			c.getCardGroup().setOnMousePressed(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					root.fireEvent(new CardSelectedEvent(c));
				}
			});
		}
		return cards;
	}

	public void init() {
		cards = initCards();
		cards = shuffleCards(cards);
		addCardsToGrid(root, cards);
	}

	void addCardsToGrid(GridPane grid, Card[] cards) {
		for (int x = 0; x < COLUMNS; x++) {
			for (int y = 0; y < ROWS; y++) {
				grid.add(cards[y * COLUMNS + x].getCardImage(), x, y);
			}
		}
	}

	// TODO make more efficient
	Card[] shuffleCards(Card[] cards) {
		List<Card> list = Arrays.asList(cards);
		// Collections.shuffle(list);
		return list.stream().toArray(Card[]::new);
	}

	public Card[] getCards() {
		return cards;
	}

	public SimpleObjectProperty<Card> getCurrentTurned() {
		return currentTurned;
	}

	public void setOnMouseHandler(EventHandler<Event> eh) {
		root.setOnMousePressed(eh);
	}

	public void setCardEventHandler(EventHandler<CardSelectedEvent> eh) {
		root.addEventHandler(CardSelectedEvent.SELECTED, eh);
	}

	public void unsetCardEventHandler(EventHandler<CardSelectedEvent> eh) {
		root.removeEventHandler(CardSelectedEvent.SELECTED, eh);
	}

}
