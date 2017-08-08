package com.servicos.estatica.belluno.util;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HoverDataChart extends StackPane {

	public HoverDataChart(int priorValue, Number value) {
		final Label label = createDataThresholdLabel(priorValue, value);
		configEvents(this, label);
	}

	private Label createDataThresholdLabel(int priorValue, Number value) {
		final Label label = new Label(value.toString() + "ºC");
		label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
		label.setStyle("-fx-font-size: 14; -fx-font-weight: bold; "
				+ "-fx-background-color: #8B008B; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 8px;");
		label.setTextFill(Color.FORESTGREEN);
		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		return label;
	}

	private void configEvents(HoverDataChart data, Label label) {
		data.setPrefSize(8, 8);
		data.setStyle(
				"-fx-background-color: #8B008B; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 10px;");

		data.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().setAll(label);
				setCursor(Cursor.NONE);
				toFront();
			}
		});
		data.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().clear();
			}
		});
	}
}
