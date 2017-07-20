package com.servicos.estatica.belluno.properties;

import javafx.beans.property.SimpleBooleanProperty;

public class MarkLineChartProperty {

	private static SimpleBooleanProperty markProperty = new SimpleBooleanProperty();

	public static SimpleBooleanProperty markProperty() {
		return markProperty;
	}

	public static Boolean getMark() {
		return markProperty().get();
	}

	public static void setMark(Boolean mark) {
		markProperty().set(mark);
	}

}
