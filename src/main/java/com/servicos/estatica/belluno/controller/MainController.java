package com.servicos.estatica.belluno.controller;

import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.util.Optional;
import java.util.ResourceBundle;

import com.servicos.estatica.belluno.properties.CurrentScreenProperty;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import zan.inc.custom.components.ImageViewResizer;

public class MainController implements Initializable {

	public static String screenInicioID = "inicio";
	public static String screenInicioFile = "/com/servicos/estatica/belluno/app/PaginaInicial.fxml";
	//public static String screenConsultaID = "consulta";
	//public static String screenConsultaFile = "/com/servicos/estatica/resicolor/app/Consulta.fxml";

	@FXML
	private AnchorPane mainPane;
	@FXML
	private Pane centralPane;
	@FXML
	private ImageView imgCliente;
	@FXML
	private ImageView imgExit;
	@FXML
	private Button btStyleClock;
	@FXML
	private Clock clock;

	private static ImageViewResizer imgClienteResizer;
	private static ImageViewResizer imgExitResizer;
	private static Timeline tmlBtClockGrow = new Timeline();
	private static Timeline tmlBtClockShrink = new Timeline();

	ScreensController mainContainer = new ScreensController();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		imgCliente.setImage(new Image("/com/servicos/estatica/belluno/style/belluno.png"));
		imgClienteResizer = new ImageViewResizer(imgCliente, 126, 70);
		imgClienteResizer.setLayoutX(16);
		imgClienteResizer.setLayoutY(6);
		imgExitResizer = new ImageViewResizer(imgExit, 70, 71);
		imgExitResizer.setLayoutX(50);
		imgExitResizer.setLayoutY(633);
		mainPane.getChildren().addAll(imgClienteResizer, imgExitResizer);

//		tmlBtClockGrow.getKeyFrames()
//				.addAll(new KeyFrame(Duration.seconds(0.3), new KeyValue(btStyleClock.translateXProperty(), -105)));
//		tmlBtClockShrink.getKeyFrames()
//				.addAll(new KeyFrame(Duration.seconds(0.3), new KeyValue(btStyleClock.translateXProperty(), 0)));

		mainContainer.loadScreen(screenInicioID, screenInicioFile);
		CurrentScreenProperty.setScreen(screenInicioID);

		mainContainer.setScreen(screenInicioID);
		centralPane.getChildren().addAll(mainContainer);

	}

	@FXML
	private void handleImgClienteAction() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/belluno/app/ClienteInfo.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Informações sobre o cliente");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgCliente.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
	}
	
	@FXML
	private void exit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente sair do sistema?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Platform.exit();
		}
	}

}
