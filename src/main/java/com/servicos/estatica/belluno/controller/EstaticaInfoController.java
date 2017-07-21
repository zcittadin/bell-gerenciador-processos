package com.servicos.estatica.belluno.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class EstaticaInfoController implements Initializable {

	@FXML
	private Button btVoltar;
	@FXML
	private ImageView imgEstatica;
	@FXML
	private Hyperlink linkEstatica;
	@FXML
	private Rectangle rectMoldura;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		imgEstatica.setImage(new Image("/com/servicos/estatica/belluno/style/logotipo.png"));
		rectMoldura.setFill(Color.TRANSPARENT);
	}

	@FXML
	public void voltar() {
		Stage stage = (Stage) btVoltar.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void openWebLink() {
		try {
			URI uri = new URI(linkEstatica.getText());
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(uri);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * WebView browser = new WebView(); WebEngine webEngine =
		 * browser.getEngine(); webEngine.load(linkEstatica.getText());
		 */
	}

}
