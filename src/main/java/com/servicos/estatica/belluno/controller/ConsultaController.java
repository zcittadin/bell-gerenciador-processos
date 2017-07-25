package com.servicos.estatica.belluno.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.servicos.estatica.belluno.app.ControlledScreen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ConsultaController implements Initializable, ControlledScreen {

	@FXML
	Rectangle recConsulta;
	
	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		recConsulta.setFill(Color.TRANSPARENT);

	}

}
