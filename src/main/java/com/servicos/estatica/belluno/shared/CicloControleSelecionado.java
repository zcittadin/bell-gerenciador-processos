package com.servicos.estatica.belluno.shared;

import com.servicos.estatica.belluno.model.CicloControle;

import javafx.beans.property.SimpleObjectProperty;

public class CicloControleSelecionado {

	private static SimpleObjectProperty<CicloControle> cicloSelecionado = new SimpleObjectProperty<>();

	public static SimpleObjectProperty<CicloControle> cicloControleProperty() {
		return cicloSelecionado;
	}

	public static CicloControle getCicloControle() {
		return cicloSelecionado.get();
	}

	public static void setCicloControle(CicloControle cicloSelecionado) {
		cicloControleProperty().set(cicloSelecionado);
	}
}
