package com.servicos.estatica.belluno.controller;

import java.awt.Toolkit;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.servicos.estatica.belluno.app.ControlledScreen;
import com.servicos.estatica.belluno.dao.LeituraDAO;
import com.servicos.estatica.belluno.dao.ProcessoDAO;
import com.servicos.estatica.belluno.modbus.ModbusRTUService;
import com.servicos.estatica.belluno.model.Leitura;
import com.servicos.estatica.belluno.model.Processo;
import com.servicos.estatica.belluno.properties.MarkLineChartProperty;
import com.servicos.estatica.belluno.util.HoverDataChart;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class PaginaInicialController implements Initializable, ControlledScreen {

	@FXML
	private LineChart<String, Number> chartTemp;
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	@FXML
	private ImageView imgSwitch;
	@FXML
	private ImageView imgFogo;
	@FXML
	private TextField txtProcesso;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	@FXML
	private Button btReport;
	@FXML
	private Label lblTemp;
	@FXML
	private Label lblChrono;

	private static Timeline chartAnimation;
	private static Timeline scanModbusSlaves;
	private static XYChart.Series<String, Number> tempSeries;
	private static DateTimeFormatter horasFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");

	private static Double temperatura = new Double(0);
	private static Boolean isReady = false;
	private static Boolean isRunning = false;

	final ObservableList<XYChart.Series<String, Number>> plotValuesList = FXCollections.observableArrayList();
	final List<Node> valueMarks = new ArrayList<>();

	private static List<Leitura> leituras = new ArrayList<>();
	private static Processo processo;
	private static LeituraDAO leituraDAO = new LeituraDAO();
	private static ProcessoDAO processoDAO = new ProcessoDAO();
	private static ModbusRTUService modService = new ModbusRTUService();

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		imgFogo.setImage(new Image("/com/servicos/estatica/belluno/style/fire.gif"));
		modService.setConnectionParams("COM9", 9600);
		modService.openConnection();
		initModbusReadSlaves();
		configLineChart();
	}

	@FXML
	private void salvarProcesso() {
		if ("".equals(txtProcesso.getText().trim())) {
			Toolkit.getDefaultToolkit().beep();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Informe um identificador antes de iniciar o registro do processo.");
			alert.showAndWait();
			txtProcesso.requestFocus();
			return;
		}

		processo = new Processo(null, leituras, txtProcesso.getText(), 0, 0, null);
		processoDAO.saveProcesso(processo);

		txtProcesso.setDisable(true);
		btSalvar.setDisable(true);
		isReady = true;

	}

	@FXML
	private void cancelarProcesso() {

	}

	@FXML
	private void generateReport() {

	}

	@FXML
	public void toggleProcess() {
		if (isReady && !isRunning) {
			initProcess();
		} else if (!isReady && !isRunning) {
			imgSwitch.setCursor(Cursor.OPEN_HAND);
			return;
		} else if (!isReady && isRunning) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Encerramento");
			alert.setHeaderText("Deseja realmente finalizar o processo em andamento?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				finalizeProcess();
			}
		}
		imgSwitch.setCursor(Cursor.OPEN_HAND);
	}

	@FXML
	public void switchIsPressing() {
		imgSwitch.setCursor(Cursor.CLOSED_HAND);
	}

	private void initProcess() {
		plotTemp();
		imgSwitch.setImage(new Image("/com/servicos/estatica/belluno/style/switch_on.png"));
		// Tooltip.install(imgSwitch, TOOLTIP_SWITCH_ANDAMENTO);
		// lblHorario.setText(horasFormatter.format(LocalDateTime.now()));
		isReady = false;
		isRunning = true;
		scanModbusSlaves.play();
		chartAnimation.play();
		imgFogo.setVisible(true);
		// dadosParciaisTimeLine.play();
		// chronoMeter.start(lblCronometro);
		// produtoService.updateDataInicial(Integer.parseInt(lblLote.getText()));
	}

	private void finalizeProcess() {
		// produtoService.updateDataFinal(Integer.parseInt(lblLote.getText()));
		// Platform.runLater(new Runnable() {
		// @Override
		// public void run() {
		// lblStatus.setTextFill(Color.web(LBL_STATUS_FINALIZADO_COLOR));
		// lblStatus.setText(LBL_STATUS_FINALIZADO);
		// }
		// });
		// lblStatus.setOpacity(1);
		scanModbusSlaves.stop();
		chartAnimation.stop();
		// dadosParciaisTimeLine.stop();
		imgSwitch.setImage(new Image("/com/servicos/estatica/belluno/style/switch_off.png"));
		// Tooltip.install(imgSwitch, TOOLTIP_SWITCH_FINALIZADO);
		btSalvar.setDisable(false);
		isRunning = false;
		txtProcesso.clear();
		txtProcesso.setDisable(false);
		imgFogo.setVisible(false);
		// ProcessoStatusManager.setProcessoStatus(NOME_REATOR, isRunning);
		// chronoMeter.stop();
		// makeToast(TOASTER_FINALIZADO_SUCESSO);
	}

	private void saveTemp() {
		Leitura leitura = new Leitura(null, processo, Calendar.getInstance().getTime(), temperatura, 250);
		leituras.add(leitura);
		processo.setLeituras(leituras);
		leituraDAO.saveLeitura(leitura);
	}

	private void initModbusReadSlaves() {
		scanModbusSlaves = new Timeline(new KeyFrame(Duration.millis(3000), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				temperatura = modService.readMultipleRegisters(1, 0, 1);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblTemp.setText(temperatura.toString());
					}
				});
			}
		}));
		scanModbusSlaves.setCycleCount(Timeline.INDEFINITE);
	}

	private void configLineChart() {
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(1050);
		yAxis.setTickUnit(100);

		chartAnimation = new Timeline();
		chartAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(3000), (ActionEvent actionEvent) -> plotTemp()));
		chartAnimation.setCycleCount(Animation.INDEFINITE);

		tempSeries = new XYChart.Series<String, Number>();
		tempSeries.getData().add(new XYChart.Data<>(horasFormatter.format(LocalDateTime.now()), 20));
		plotValuesList.add(tempSeries);
		chartTemp.setData(plotValuesList);

	}

	private void plotTemp() {
		final XYChart.Data<String, Number> data = new XYChart.Data<>(horasFormatter.format(LocalDateTime.now()),
				temperatura);
		Node mark = new HoverDataChart(1, temperatura);
		if (!MarkLineChartProperty.getMark())
			mark.setVisible(Boolean.FALSE);
		valueMarks.add(mark);
		data.setNode(mark);
		tempSeries.getData().add(data);
		saveTemp();
	}

}
