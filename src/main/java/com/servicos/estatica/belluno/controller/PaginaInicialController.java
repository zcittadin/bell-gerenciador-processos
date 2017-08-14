package com.servicos.estatica.belluno.controller;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.servicos.estatica.belluno.app.ControlledScreen;
import com.servicos.estatica.belluno.dao.LeituraDAO;
import com.servicos.estatica.belluno.dao.ProcessoDAO;
import com.servicos.estatica.belluno.mail.MailJob;
import com.servicos.estatica.belluno.modbus.ModbusRTUService;
import com.servicos.estatica.belluno.model.Leitura;
import com.servicos.estatica.belluno.model.Processo;
import com.servicos.estatica.belluno.report.builder.ProcessoReportCreator;
import com.servicos.estatica.belluno.shared.ProcessoStatusManager;
import com.servicos.estatica.belluno.util.Chronometer;
import com.servicos.estatica.belluno.util.FxDialogs;
import com.servicos.estatica.belluno.util.HoverDataChart;
import com.servicos.estatica.belluno.util.Toast;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
	private Button btNovo;
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
	@FXML
	private CheckBox chkMarcadores;

	private static Timeline scanModbusSlaves;
	private static XYChart.Series<String, Number> tempSeries;
	private static DateTimeFormatter dataHoraFormatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yy");

	private static Double temperatura = new Double(0);
	private static Double setPoint = new Double(0);
	private static Double tempMin = new Double(1900);
	private static Double tempMax = new Double(0);
	private static Integer plottedTemp = new Integer(0);
	private static Integer plottedSp = new Integer(0);
	// private static Integer scanInterval = new Integer(0);
	private static Boolean isReady = false;
	private static Boolean isRunning = false;
	private static Boolean isFinalized = false;
	private static Boolean isAdding = false;

	private static String PROC_KEY = "proc1";
	private static String TOOLTIP_CSS = "-fx-font-size: 8pt; -fx-font-weight: bold; -fx-font-style: normal; ";
	private Tooltip tooltipNovo = new Tooltip("Clique para cadastrar um novo processo");
	private Tooltip tooltipSalvar = new Tooltip("Salvar processo");
	private Tooltip tooltipCancelar = new Tooltip("Cancelar e excluir processo");
	private Tooltip tooltipReport = new Tooltip("Exportar relatório em PDF");
	private Tooltip tooltipMarks = new Tooltip("Habilitar marcadores do gráfico de linhas");
	private Tooltip tooltipSwitch = new Tooltip("Iniciar/parar registro do processo");

	final ObservableList<XYChart.Series<String, Number>> plotValuesList = FXCollections.observableArrayList();
	final List<Node> valueMarks = new ArrayList<>();

	private static List<Leitura> leituras = new ArrayList<>();
	private static Processo processo;

	SchedulerFactory schedFact = null;
	Scheduler sched = null;

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
		initTooltips();
		imgFogo.setImage(new Image("/com/servicos/estatica/belluno/style/fire.gif"));
		modService.setConnectionParams("COM7", 9600);
		modService.openConnection();
		initModbusReadSlaves();
		configLineChart();
		configureMailJob();
	}

	@FXML
	private void addProcesso() {
		isFinalized = false;
		isAdding = true;
		tempMax = new Double(0);
		tempMin = new Double(1900);
		clearLineChart();
		lblTemp.setText("000.0");
		lblChrono.setText("00:00:00");
		lblDhInicial.setText("00:00:00 - 00/00/00");
		lblTempMax.setText("000.0");
		lblTempMin.setText("000.0");
		txtProcesso.setDisable(false);
		txtProcesso.setText("");
		txtProcesso.requestFocus();
		btSalvar.setDisable(false);
		btCancelar.setDisable(false);
		btReport.setDisable(true);
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
		txtProcesso.setDisable(true);
		btNovo.setDisable(true);
		btSalvar.setDisable(true);
		btCancelar.setDisable(true);
		btReport.setDisable(true);

		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				leituras.clear();
				processo = new Processo(null, leituras, txtProcesso.getText(), 0, 0, null, null);
				processoDAO.saveProcesso(processo);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				isAdding = false;
				progressSave.setVisible(false);
				btCancelar.setDisable(false);
				lblTemp.setText("000.0");
				lblChrono.setText("00:00:00");
				try {
					sched.getContext().put("processo", processo);
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
				makeToast("Processo salvo com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				isAdding = false;
				txtProcesso.setText(null);
				txtProcesso.setDisable(true);
				btNovo.setDisable(true);
				btSalvar.setDisable(true);
				btCancelar.setDisable(true);
				progressSave.setVisible(false);
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
		isReady = true;

	}

	@FXML
	private void cancelarProcesso() {
		if (isAdding) {
			btSalvar.setDisable(true);
			btCancelar.setDisable(true);
			txtProcesso.setText("");
			txtProcesso.setDisable(true);
			isAdding = false;
			return;
		}

		if (isRunning) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Existe um processo em andamento. Encerre-o antes de cancelar.");
			alert.showAndWait();
		}

		if (isReady || isFinalized) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmar cancelamento");
			alert.setHeaderText("Os dados referentes a este processo serão perdidos. Confirmar?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				isAdding = false;
				isFinalized = true;
				isReady = false;
				isRunning = false;
				tempMax = new Double(0);
				tempMin = new Double(1900);
				imgFogo.setVisible(false);
				imgSwitch.setImage(new Image("/com/servicos/estatica/belluno/style/switch_off.png"));
				lblTemp.setText("000.0");
				lblChrono.setText("00:00:00");
				lblDhInicial.setText("00:00:00 - 00/00/00");
				lblTempMax.setText("000.0");
				lblTempMin.setText("000.0");
				btNovo.setDisable(false);
				btSalvar.setDisable(true);
				btReport.setDisable(true);
				btCancelar.setDisable(true);
				txtProcesso.setText("");
				txtProcesso.setDisable(true);
				scanModbusSlaves.stop();
				clearLineChart();
				leituraDAO.removeLeituras(processo);
				leituras.clear();
				processoDAO.removeProcesso(processo);
				makeToast("Processo removido com sucesso.");
			}
		}

	}

	@FXML
	public void saveReport() {
		Stage stage = new Stage();
		stage.initOwner(btReport.getScene().getWindow());
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"));
		fileChooser.setTitle("Salvar relatório de processo");
		fileChooser.setInitialFileName(processo.getIdentificador() + ".pdf");
		File savedFile = fileChooser.showSaveDialog(stage);
		if (savedFile != null) {
			generatePdfReport(savedFile);
		}

	}

	private void generatePdfReport(File file) {
		progressSave.setVisible(true);
		txtProcesso.setDisable(true);
		btNovo.setDisable(true);
		btCancelar.setDisable(true);
		btReport.setDisable(true);
		Task<Integer> reportTask = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				int result = ProcessoReportCreator.build(processo, file.getAbsolutePath(), lblChrono.getText());
				int maximum = 20;
				for (int i = 0; i < maximum; i++) {
					updateProgress(i, maximum);
				}
				return new Integer(result);
			}
		};

		reportTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				progressSave.setVisible(false);
				txtProcesso.setDisable(false);
				btNovo.setDisable(false);
				btCancelar.setDisable(false);
				btReport.setDisable(false);
				int r = reportTask.getValue();
				if (r != 1) {
					Toolkit.getDefaultToolkit().beep();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erro");
					alert.setHeaderText("Houve uma falha na emissão do relatório.");
					alert.showAndWait();
					return;
				}
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Concluído");
				alert.setHeaderText("Relatório emitido com sucesso. Deseja visualizar?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		Thread t = new Thread(reportTask);
		t.start();
	}

	@FXML
	public void toggleMark() {
		for (Node mark : valueMarks) {
			mark.setVisible(chkMarcadores.isSelected());
		}
	}

	@FXML
	public void toggleProcess() {
		if ((!isReady && !isRunning) || isFinalized) {
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

	@FXML
	private void adjSetPoint() {
		String sp = FxDialogs.showNumberInput("Set-point", "", "Digite o set-point:", "");
		if (sp != null && !"".equals(sp)) {
			modService.writeSingleRegister(1, 0, Integer.parseInt(sp) * 10);
		}
	}

	private void initProcess() {
		temperatura = roundToHalf(modService.readMultipleRegisters(1, 1, 1) / 10);
		plottedTemp = temperatura.intValue();
		setPoint = roundToHalf(modService.readMultipleRegisters(1, 0, 1) / 10);
		plottedSp = setPoint.intValue();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblTemp.setText(plottedTemp.toString());
				calculaMinMax();
			}
		});
		plotTemp();
		imgSwitch.setImage(new Image("/com/servicos/estatica/belluno/style/switch_on.png"));
		isReady = false;
		isRunning = true;
		scanModbusSlaves.play();
		chronoMeter.start(lblChrono);
		lblDhInicial.setText(dataHoraFormatter.format(LocalDateTime.now()));
		imgFogo.setVisible(true);
		processo.setDhInicial(Calendar.getInstance().getTime());
		processoDAO.updateDataInicial(processo);
		ProcessoStatusManager.setProcessoStatus(PROC_KEY, true);
	}

	private void finalizeProcess() {
		scanModbusSlaves.stop();
		imgSwitch.setImage(new Image("/com/servicos/estatica/belluno/style/switch_off.png"));
		btNovo.setDisable(false);
		btReport.setDisable(false);
		isRunning = false;
		isFinalized = true;
		chronoMeter.stop();
		imgFogo.setVisible(false);
		makeToast("Processo finalizado com sucesso.");
		processo.setDhFinal(Calendar.getInstance().getTime());
		processoDAO.updateDataFinal(processo);
		ProcessoStatusManager.setProcessoStatus(PROC_KEY, false);
	}

	private void saveTemp() {
		Leitura leitura = new Leitura(null, processo, Calendar.getInstance().getTime(), plottedTemp, plottedSp);
		leituras.add(leitura);
		processo.setLeituras(leituras);
		leituraDAO.saveLeitura(leitura);
	}

	private void initModbusReadSlaves() {
		scanModbusSlaves = new Timeline(new KeyFrame(Duration.millis(3000), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Task<Void> modbusTask = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						temperatura = roundToHalf(modService.readMultipleRegisters(1, 1, 1) / 10);
						plottedTemp = temperatura.intValue();
						setPoint = roundToHalf(modService.readMultipleRegisters(1, 0, 1) / 10);
						plottedSp = setPoint.intValue();
						return null;
					}
				};
				modbusTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent arg0) {

					}
				});
				modbusTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent arg0) {

					}
				});
				Thread t = new Thread(modbusTask);
				t.start();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblTemp.setText(plottedTemp.toString());
						calculaMinMax();
					}
				});
				// scanInterval++;
				// if (scanInterval == 120) {
				plotTemp();
				// scanInterval = 0;
				// }
			}
		}));
		scanModbusSlaves.setCycleCount(Timeline.INDEFINITE);
	}

	public static double roundToHalf(double d) {
		return (Math.ceil(d * 2) / 2);
	}

	private void configLineChart() {
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(1200);
		yAxis.setTickUnit(200);
		tempSeries = new XYChart.Series<String, Number>();
		plotValuesList.add(tempSeries);
		chartTemp.setData(plotValuesList);
	}

	private void configureMailJob() {
		try {
			schedFact = new StdSchedulerFactory();
			sched = schedFact.getScheduler();
			JobDetail job = JobBuilder.newJob(MailJob.class).withIdentity("myJob", "group1").build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 45 8-12,14-17 ? * MON-FRI")).build();
			sched.scheduleJob(job, trigger);
			sched.start();
		} catch (Exception e) {
			System.out.println("Erro ao enviar e-mail.");
			e.printStackTrace();
		}
	}

	private void plotTemp() {
		final XYChart.Data<String, Number> data = new XYChart.Data<>(dataHoraFormatter.format(LocalDateTime.now()),
				plottedTemp);
		Node mark = new HoverDataChart(1, plottedTemp);
		mark.setVisible(chkMarcadores.isSelected());
		data.setNode(mark);
		tempSeries.getData().add(data);
		valueMarks.add(mark);
		saveTemp();
	}

	@SuppressWarnings("unchecked")
	private void clearLineChart() {
		tempSeries.getData().clear();
		chartTemp.getData().clear();
		tempSeries = new XYChart.Series<String, Number>();
		chartTemp.getData().addAll(tempSeries);
	}

	private void calculaMinMax() {
		if (tempMin > temperatura) {
			tempMin = temperatura;
			Integer i = tempMin.intValue();
			lblTempMin.setText(i.toString());
			processo.setTempMin(tempMin.intValue());
			processoDAO.updateTemperaturaMin(processo);
		}
		if (tempMax < temperatura) {
			tempMax = roundToHalf(temperatura);
			Integer i = tempMax.intValue();
			lblTempMax.setText(i.toString());
			processo.setTempMax(tempMax.intValue());
			processoDAO.updateTemperaturaMax(processo);
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

	private void initTooltips() {
		tooltipCancelar.setStyle(TOOLTIP_CSS);
		tooltipMarks.setStyle(TOOLTIP_CSS);
		tooltipNovo.setStyle(TOOLTIP_CSS);
		tooltipReport.setStyle(TOOLTIP_CSS);
		tooltipSalvar.setStyle(TOOLTIP_CSS);
		tooltipSwitch.setStyle(TOOLTIP_CSS);
		Tooltip.install(btNovo, tooltipNovo);
		Tooltip.install(btSalvar, tooltipSalvar);
		Tooltip.install(btCancelar, tooltipCancelar);
		Tooltip.install(btReport, tooltipReport);
		Tooltip.install(imgSwitch, tooltipSwitch);
		Tooltip.install(chkMarcadores, tooltipMarks);
	}

}
