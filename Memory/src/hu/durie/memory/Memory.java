package hu.durie.memory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Memory extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane root = new GridPane();
			root.setHgap(10);
			root.setVgap(10);
			root.setLayoutX(50);
			root.setLayoutY(50);
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			Table table = new Table(root);
			table.init();
			Game game = new Game(table, 2);
			game.start();

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
