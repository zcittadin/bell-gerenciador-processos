package com.servicos.estatica.belluno.shared;

import javafx.beans.property.SimpleBooleanProperty;

public class CicloControleSelecionado {

	private static SimpleBooleanProperty cicloChanged = new SimpleBooleanProperty();

	public static SimpleBooleanProperty cicloChangedProperty() {
		return cicloChanged;
	}

	public static Boolean getCicloChanged() {
		return cicloChanged.get();
	}

	public static void setCicloChanged(Boolean cicloChanged) {
		cicloChangedProperty().set(cicloChanged);
	}
}
