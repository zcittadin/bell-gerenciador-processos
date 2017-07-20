package com.servicos.estatica.belluno.util;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HoverDataChart extends StackPane {

	public HoverDataChart(int priorValue, Double value) {
		setPrefSize(8, 8);
		setStyle("-fx-background-color: #8B008B; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 10px;");

		final Label label = createDataThresholdLabel(priorValue, value);

		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().setAll(label);
				setCursor(Cursor.NONE);
				toFront();
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().clear();
				setCursor(Cursor.CROSSHAIR);
			}
		});
	}

	private Label createDataThresholdLabel(int priorValue, Double value) {
		final Label label = new Label(value.toString() + "ºC");
		label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
		label.setStyle("-fx-font-size: 14; -fx-font-weight: bold; "
				+ "-fx-background-color: #8B008B; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 8px;");
		// if (priorValue == 0) {
		// label.setTextFill(Color.DARKGRAY);
		// } else if (value > priorValue) {
		label.setTextFill(Color.FORESTGREEN);
		// } else {
		// label.setTextFill(Color.FIREBRICK);
		// }

		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		return label;
	}
}
