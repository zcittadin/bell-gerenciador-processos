package com.servicos.estatica.belluno.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Chronometer {

	private Timeline timeline;

	public void start(Label label) {
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (label.getText().contains(":")) {
					String[] fields = label.getText().split(":");
					Integer hours = Integer.parseInt(fields[0]);
					Integer minutes = Integer.parseInt(fields[1]);
					Integer seconds = Integer.parseInt(fields[2]);

					seconds++;
					if (seconds == 60) {
						seconds = 0;
						minutes++;
						if (minutes == 60) {
							minutes=0;
							hours++;
						}
					}

					String strHours = hours < 10 ? "0" + hours.toString() : hours.toString();
					String strMinutes = minutes < 10 ? "0" + minutes.toString() : minutes.toString();
					String strSeconds = seconds < 10 ? "0" + seconds.toString() : seconds.toString();

					label.setText(strHours + ":" + strMinutes + ":" + strSeconds);
				} else {
					throw new IllegalArgumentException(
							"\nO texto configurado é incompatível com o padrão de horário local (00:00:00).");
				}

			}
		}), new KeyFrame(Duration.seconds(1)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	public void stop() {
		timeline.stop();
	}
}

class StringUtilities {

	public static String pad(int fieldWidth, char padChar, String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = s.length(); i < fieldWidth; i++) {
			sb.append(padChar);
		}
		sb.append(s);
		return sb.toString();
	}
}
