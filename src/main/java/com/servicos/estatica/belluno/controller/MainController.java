package com.servicos.estatica.belluno.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import zan.inc.custom.components.ImageViewResizer;

public class MainController implements Initializable {

	public static String screenConsultaID = "consulta";
	public static String screenConsultaFile = "/com/servicos/estatica/resicolor/app/Consulta.fxml";
	public static String screenComparaID = "compara";
	public static String screenComparaFile = "/com/servicos/estatica/resicolor/app/Comparacoes.fxml";
	public static String screenInicioID = "inicio";
	public static String screenInicioFile = "/com/servicos/estatica/resicolor/app/PaginaInicial.fxml";

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
//	@FXML
//	private Clock clock;

//	private static ImageViewResizer imgClienteResizer;
//	private static ImageViewResizer imgExitResizer;
//	private static Timeline tmlBtClockGrow = new Timeline();
//	private static Timeline tmlBtClockShrink = new Timeline();

	ScreensController mainContainer = new ScreensController();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
