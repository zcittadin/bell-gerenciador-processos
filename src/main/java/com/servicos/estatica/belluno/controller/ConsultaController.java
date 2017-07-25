package com.servicos.estatica.belluno.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.servicos.estatica.belluno.app.ControlledScreen;

import javafx.fxml.Initializable;

public class ConsultaController implements Initializable, ControlledScreen {

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
