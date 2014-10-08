package hu.durie.memory;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

enum CardState {
	FRONT, BACK, DROPPED
}

public class Card {
	private int id;
	private int kind;
	private Rectangle cardImage;
	private Text cardLabel;
	private Pane cardGroup;
	private SimpleObjectProperty<CardState> state = new SimpleObjectProperty<CardState>(
			CardState.FRONT);

	public Card() {
		cardGroup = new StackPane();
		createCardImage();
		cardGroup.getChildren().add(cardImage);
		state.addListener(new ChangeListener<CardState>() {

			@Override
			public void changed(
					ObservableValue<? extends CardState> observable,
					CardState oldValue, CardState newValue) {
				switch (newValue) {
				case BACK:
					cardImage.setFill(Color.PALEVIOLETRED);
					if (cardLabel != null) {
						cardLabel.setVisible(false);
					}
					break;
				case FRONT:
					cardImage.setFill(Color.ALICEBLUE);
					if (cardLabel != null) {
						cardLabel.setVisible(true);
					}
					break;
				case DROPPED:
				default:
					cardGroup.setVisible(false);
					break;
				}
			}
		});
		state.setValue(CardState.BACK);
	}

	public Card(int i) {
		this();
		id = i;
		kind = i / 2;
		createCardLabel(kind);
		cardLabel.setVisible(false);
		cardGroup.getChildren().add(cardLabel);
		state.addListener(new ChangeListener<CardState>() {

			@Override
			public void changed(
					ObservableValue<? extends CardState> observable,
					CardState oldValue, CardState newValue) {
				System.out.println("CARD " + id + ": " + oldValue + "->"
						+ newValue);
			}
		});
	}

	private Rectangle createCardImage() {
		if (cardImage == null) {
			Rectangle r = new Rectangle();
			r.setArcHeight(10);
			r.setArcWidth(10);
			r.setHeight(100);
			r.setWidth(50);
			r.setFill(Color.ALICEBLUE);
			r.setStrokeWidth(2.0);
			r.setStroke(Color.DARKGRAY);
			cardImage = r;
		}
		return cardImage;
	}

	private void createCardLabel(int kind) {
		cardLabel = new Text(Integer.toString(kind));
		cardLabel.setFont(new Font(20));
	}

	public Node getCardImage() {
		return cardGroup;
	}

	public void toggleState() {
		if (state.getValue() == CardState.FRONT) {
			state.setValue(CardState.BACK);
		} else if (state.getValue() == CardState.BACK) {
			state.setValue(CardState.FRONT);
		}
	}

	public void drop() {
		state.setValue(CardState.DROPPED);
	}

	public SimpleObjectProperty<CardState> getState() {
		return state;
	}

	public Pane getCardGroup() {
		return cardGroup;
	}

	public void setState(SimpleObjectProperty<CardState> state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public int getKind() {
		return kind;
	}

}
