package hu.durie.memory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Game extends Thread {

	private enum GameState {
		STARTED, FINISHED
	};

	private Table table;
	private List<Player> players;
	private Iterator<Player> playerIterator;
	private SimpleObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();
	private int numberOfPlayers;
	private GameState state = GameState.STARTED;

	Game(Table table, int numberOfPlayers) {
		this.table = table;
		this.numberOfPlayers = numberOfPlayers;
		players = new ArrayList<>(numberOfPlayers);
		initPlayers();
		playerIterator = players.iterator();
	}

	private void initPlayers() {
		for (int i = 0; i < numberOfPlayers; i++) {
			players.add(new Player("PLAYER_" + i));
		}
	}

	public void run() {
		while (state != GameState.FINISHED) {
			nextPlayer();
		}
		System.out.println("--- GAME OVER ---");
		System.out.println("SCORES:");
		for (Player p : players) {
			p.printScore();
		}
	}

	private boolean checkCards() {
		return getNumberOfDroppedCards() < table.getCards().length;
	}

	private void playerTurn(Player p) {
		if (state == GameState.FINISHED) {
			return;
		}
		p.getState().setValue(PlayerState.BEFORE_FIRST_CARD);
		table.setCardEventHandler(p.getCardEventHandler());
		p.getState().addListener(new ChangeListener<PlayerState>() {

			@Override
			public void changed(
					ObservableValue<? extends PlayerState> observable,
					PlayerState oldValue, PlayerState newValue) {
				if (!checkCards()) {
					state = GameState.FINISHED;
					synchronized (Game.this) {
						Game.this.notifyAll();
					}				}
				if (newValue == PlayerState.FINISHED) {
					table.unsetCardEventHandler(p.getCardEventHandler());
					synchronized (Game.this) {
						Game.this.notifyAll();
					}
				}
			}
		});
	}

	private void nextPlayer() {
		if (!playerIterator.hasNext()) {
			playerIterator = players.iterator();
		}
		Player p = playerIterator.next();
		currentPlayer.setValue(p);
		playerTurn(p);
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	private int getNumberOfDroppedCards() {
		int ret = 0;
		for (Card c : table.getCards()) {
			if (c.getState().getValue() == CardState.DROPPED) {
				ret++;
			}
		}
		System.out.println("*** END OF TURN ***");
		System.out.println("Cards number: " + table.getCards().length
				+ ", dropped: " + ret);
		return ret;
	}
}
