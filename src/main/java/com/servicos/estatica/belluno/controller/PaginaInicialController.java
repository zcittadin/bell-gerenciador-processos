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
import com.servicos.estatica.belluno.util.Chronometer;
import com.servicos.estatica.belluno.util.HoverDataChart;
import com.servicos.estatica.belluno.util.Toast;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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
	@FXML
	private Label lblDhInicial;
	@FXML
	private Label lblTempMin;
	@FXML
	private Label lblTempMax;
	@FXML
	private ProgressIndicator progressSave;

	private static Timeline chartAnimation;
	private static Timeline scanModbusSlaves;
	private static XYChart.Series<String, Number> tempSeries;
	private static DateTimeFormatter dataHoraFormatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yy");

	private static Double temperatura = new Double(0);
	private static Double tempMin = new Double(300);
	private static Double tempMax = new Double(0);
	private static Boolean isReady = false;
	private static Boolean isRunning = false;

	final ObservableList<XYChart.Series<String, Number>> plotValuesList = FXCollections.observableArrayList();
	final List<Node> valueMarks = new ArrayList<>();

	private static List<Leitura> leituras = new ArrayList<>();
	private static Processo processo;

	final Chronometer chronoMeter = new Chronometer();
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
		modService.setConnectionParams("COM5", 9600);
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

		progressSave.setVisible(true);

		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				processo = new Processo(null, leituras, txtProcesso.getText(), 0, 0, null, null);
				processoDAO.saveProcesso(processo);
				return null;
			}
		};

		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				progressSave.setVisible(false);
				lblTemp.setText("000.0");
				lblChrono.setText("00:00:00");
				makeToast("Processo salvo com sucesso.");
			}
		});

		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				progressSave.setVisible(false);
			}
		});
		Thread t = new Thread(saveTask);
		t.start();

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
		if (!isReady && !isRunning) {
			Toolkit.getDefaultToolkit().beep();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Informe um identificador antes de iniciar o registro do processo.");
			alert.showAndWait();
			txtProcesso.requestFocus();
			imgSwitch.setCursor(Cursor.OPEN_HAND);
			return;
		}
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
		chronoMeter.start(lblChrono);
		lblDhInicial.setText(dataHoraFormatter.format(LocalDateTime.now()));
		imgFogo.setVisible(true);
		// dadosParciaisTimeLine.play();
		// chronoMeter.start(lblCronometro);
		// produtoService.updateDataInicial(Integer.parseInt(lblLote.getText()));
		processoDAO.updateDataInicial(processo);
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
		chronoMeter.stop();
		txtProcesso.clear();
		txtProcesso.setDisable(false);
		lblTemp.setText("");
		lblChrono.setText("");
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
						calculaMinMax();
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
		tempSeries.getData().add(new XYChart.Data<>(dataHoraFormatter.format(LocalDateTime.now()), 20));
		plotValuesList.add(tempSeries);
		chartTemp.setData(plotValuesList);

	}

	private void plotTemp() {
		final XYChart.Data<String, Number> data = new XYChart.Data<>(dataHoraFormatter.format(LocalDateTime.now()),
				temperatura);
		Node mark = new HoverDataChart(1, temperatura);
		if (!MarkLineChartProperty.getMark())
			mark.setVisible(Boolean.FALSE);
		valueMarks.add(mark);
		data.setNode(mark);
		tempSeries.getData().add(data);
		saveTemp();
	}

	private void calculaMinMax() {
		if (tempMin > temperatura) {
			tempMin = temperatura;
			lblTempMin.setText(tempMin.toString());
			// produtoService.updateTemperaturaMin(Integer.parseInt(lblLote.getText()),
			// tempMin);
		}
		if (tempMax < temperatura) {
			tempMax = temperatura;
			lblTempMax.setText(tempMax.toString());
			// produtoService.updateTemperaturaMax(Integer.parseInt(lblLote.getText()),
			// tempMax);
		}
	}

	private void makeToast(String message) {
		String toastMsg = message;
		int toastMsgTime = 5000;
		int fadeInTime = 600;
		int fadeOutTime = 600;
		Stage stage = (Stage) btSalvar.getScene().getWindow();
		Toast.makeToast(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
	}

}
