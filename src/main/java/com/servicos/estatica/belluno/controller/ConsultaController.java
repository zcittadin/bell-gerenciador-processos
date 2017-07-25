package com.servicos.estatica.belluno.controller;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.servicos.estatica.belluno.app.ControlledScreen;
import com.servicos.estatica.belluno.dao.LeituraDAO;
import com.servicos.estatica.belluno.dao.ProcessoDAO;
import com.servicos.estatica.belluno.model.Leitura;
import com.servicos.estatica.belluno.model.Processo;
import com.servicos.estatica.belluno.report.builder.ProcessoReportCreator;
import com.servicos.estatica.belluno.util.PeriodFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("rawtypes")
public class ConsultaController implements Initializable, ControlledScreen {

	@FXML
	private Rectangle recConsulta;
	@FXML
	private RadioButton rdIdentificador;
	@FXML
	private RadioButton rdPeriodo;
	@FXML
	private RadioButton rdUltimos;
	@FXML
	private TextField txtIdentificador;
	@FXML
	private DatePicker dtpInicio;
	@FXML
	private DatePicker dtpFinal;
	@FXML
	private Spinner<Integer> spnUltimos;
	@FXML
	private TableView tblConsulta;
	@FXML
	private TableColumn colIdentificador;
	@FXML
	private TableColumn colDhInicial;
	@FXML
	private TableColumn colDhFinal;
	@FXML
	private TableColumn colTempoDecorrido;
	@FXML
	private TableColumn colTempMin;
	@FXML
	private TableColumn colTempMax;
	@FXML
	private TableColumn colGraficos;
	@FXML
	private TableColumn colRelatorios;
	@FXML
	private ProgressIndicator progForm;

	SpinnerValueFactory<Integer> valueFactory = //
			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 10);

	ToggleGroup group = new ToggleGroup();

	private static LeituraDAO leituraDAO = new LeituraDAO();
	private static ProcessoDAO processoDAO = new ProcessoDAO();
	private static ObservableList<Processo> processos = FXCollections.observableArrayList();

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		spnUltimos.setValueFactory(valueFactory);
		recConsulta.setFill(Color.TRANSPARENT);
		rdIdentificador.setToggleGroup(group);
		rdPeriodo.setToggleGroup(group);
		rdUltimos.setToggleGroup(group);
		consultarRecentes();

	}

	@FXML
	private void selectUltimos() {
		spnUltimos.setDisable(false);
		dtpInicio.getEditor().setText(null);
		dtpFinal.getEditor().setText(null);
		txtIdentificador.setText(null);
		dtpInicio.setDisable(true);
		dtpFinal.setDisable(true);
		txtIdentificador.setDisable(true);
	}

	@FXML
	private void selectPorPeriodo() {
		spnUltimos.setDisable(true);
		spnUltimos.getValueFactory().setValue(10);
		txtIdentificador.setText(null);
		dtpInicio.setDisable(false);
		dtpFinal.setDisable(false);
		txtIdentificador.setDisable(true);
	}

	@FXML
	private void selectPorIdentificador() {
		dtpInicio.getEditor().setText(null);
		dtpFinal.getEditor().setText(null);
		spnUltimos.setDisable(true);
		spnUltimos.getValueFactory().setValue(10);
		dtpInicio.setDisable(true);
		dtpFinal.setDisable(true);
		txtIdentificador.setDisable(false);

	}

	@FXML
	private void consultar() {
		if (!validateFields())
			return;
		tblConsulta.getItems().clear();
		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (rdIdentificador.isSelected()) {
					processos = FXCollections.observableList(
							(List<Processo>) processoDAO.findByIdentificadorProcessos(txtIdentificador.getText()));
					return null;
				}
				if (rdPeriodo.isSelected()) {
					processos = FXCollections.observableList((List<Processo>) processoDAO
							.findByPeriodo(dtpInicio.getValue().toString(), dtpFinal.getValue().toString()));
					return null;
				}
				if (rdUltimos.isSelected()) {
					processos = FXCollections
							.observableList((List<Processo>) processoDAO.findLastProcessos(spnUltimos.getValue()));
					return null;
				}
				return null;
			}
		};

		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				if (!processos.isEmpty()) {
					populateTable();
				}
				// progressProcessos.setVisible(false);
				// tbProcessos.setDisable(false);
			}
		});
		Thread t = new Thread(searchTask);
		t.start();
	}

	private Boolean validateFields() {
		if (rdPeriodo.isSelected()) {
			if ((dtpInicio.getValue() == null) || (dtpFinal.getValue() == null)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Atenção");
				alert.setHeaderText("Informe as datas de início e fim do processo.");
				alert.showAndWait();
				dtpInicio.requestFocus();
				return false;
			}
		}
		if (rdIdentificador.isSelected()) {
			if (txtIdentificador.getText() == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Atenção");
				alert.setHeaderText("Informe um identificador para a consulta.");
				alert.showAndWait();
				txtIdentificador.requestFocus();
				return false;
			}
			if (txtIdentificador.getText().trim().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Atenção");
				alert.setHeaderText("Informe um identificador para a consulta.");
				alert.showAndWait();
				txtIdentificador.requestFocus();
				return false;
			}
		}
		return true;

	}

	private void consultarRecentes() {
		tblConsulta.getItems().clear();
		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				processos = FXCollections.observableList((List<Processo>) processoDAO.findLastProcessos(5));
				return null;
			}
		};

		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				if (!processos.isEmpty()) {
					populateTable();
				}
				// progressProcessos.setVisible(false);
				// tbProcessos.setDisable(false);
			}
		});
		Thread t = new Thread(searchTask);
		t.start();

	}

	@SuppressWarnings("unchecked")
	private void populateTable() {
		tblConsulta.setItems(processos);

		colIdentificador.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Processo, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Processo, String> cell) {
						final Processo p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getIdentificador());
						return simpleObject;
					}
				});
		colDhInicial.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Processo, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Processo, String> cell) {
						final Processo p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss").format(p.getDhInicial()));
						return simpleObject;
					}
				});
		colDhFinal.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Processo, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Processo, String> cell) {
						final Processo p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(p.getDhFinal()));
						return simpleObject;
					}
				});
		colTempoDecorrido.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Processo, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Processo, String> cell) {
						final Processo p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								PeriodFormatter.formatPeriod(p.getDhInicial(), p.getDhFinal()));
						return simpleObject;
					}
				});
		colTempMin.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Processo, Double>, ObservableValue<Double>>() {
					public ObservableValue<Double> call(CellDataFeatures<Processo, Double> cell) {
						final Processo p = cell.getValue();
						final SimpleObjectProperty<Double> simpleObject = new SimpleObjectProperty<Double>(
								p.getTempMin());
						return simpleObject;
					}
				});
		colTempMax.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Processo, Double>, ObservableValue<Double>>() {
					public ObservableValue<Double> call(CellDataFeatures<Processo, Double> cell) {
						final Processo p = cell.getValue();
						final SimpleObjectProperty<Double> simpleObject = new SimpleObjectProperty<Double>(
								p.getTempMax());
						return simpleObject;
					}
				});
		colGraficos.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		colRelatorios.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

		Callback<TableColumn<Processo, String>, TableCell<Processo, String>> cellGraficoFactory = //
				new Callback<TableColumn<Processo, String>, TableCell<Processo, String>>() {
					@Override
					public TableCell call(final TableColumn<Processo, String> param) {
						final TableCell<Processo, String> cell = new TableCell<Processo, String>() {

							final Button btn = new Button();

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(event -> {

										try {
											Stage stage;
											Parent root;
											stage = new Stage();
											URL url = getClass()
													.getResource("/com/servicos/estatica/belluno/app/ChartScreen.fxml");
											FXMLLoader fxmlloader = new FXMLLoader();
											fxmlloader.setLocation(url);
											fxmlloader.setBuilderFactory(new JavaFXBuilderFactory());
											root = (Parent) fxmlloader.load(url.openStream());
											stage.setScene(new Scene(root));
											stage.setTitle("Visualização gráfica do processo");
											stage.initModality(Modality.APPLICATION_MODAL);
											stage.initOwner(txtIdentificador.getScene().getWindow());
											stage.setResizable(Boolean.FALSE);
											((ChartScreenController) fxmlloader.getController())
													.setContext(getTableView().getItems().get(getIndex()));
											stage.showAndWait();
										} catch (IOException e) {
											System.err.println("Erro ao carregar FXML!");
											e.printStackTrace();
										}

									});
									btn.setStyle(
											"-fx-graphic: url('com/servicos/estatica/belluno/style/chart_curve.png');");
									btn.setCursor(Cursor.HAND);
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
		colGraficos.setCellFactory(cellGraficoFactory);

		Callback<TableColumn<Processo, String>, TableCell<Processo, String>> cellReportFactory = //
				new Callback<TableColumn<Processo, String>, TableCell<Processo, String>>() {
					@Override
					public TableCell call(final TableColumn<Processo, String> param) {
						final TableCell<Processo, String> cell = new TableCell<Processo, String>() {

							final Button btn = new Button();

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(event -> {
										Processo processo = getTableView().getItems().get(getIndex());
										saveReport(processo);
									});
									btn.setStyle("-fx-graphic: url('com/servicos/estatica/belluno/style/report.png');");
									btn.setCursor(Cursor.HAND);
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
		colRelatorios.setCellFactory(cellReportFactory);

		colIdentificador.setStyle("-fx-alignment: CENTER;");
		colDhInicial.setStyle("-fx-alignment: CENTER;");
		colDhFinal.setStyle("-fx-alignment: CENTER;");
		colTempoDecorrido.setStyle("-fx-alignment: CENTER;");
		colTempMin.setStyle("-fx-alignment: CENTER;");
		colTempMax.setStyle("-fx-alignment: CENTER;");
		colGraficos.setStyle("-fx-alignment: CENTER;");
		colRelatorios.setStyle("-fx-alignment: CENTER;");
		tblConsulta.getColumns().setAll(colIdentificador, colDhInicial, colDhFinal, colTempoDecorrido, colTempMin,
				colTempMax, colGraficos, colRelatorios);
	}

	public void saveReport(Processo processo) {
		List<Leitura> leituras = leituraDAO.findLeiturasByProcesso(processo);
		processo.setLeituras(leituras);
		Stage stage = new Stage();
		stage.initOwner(tblConsulta.getScene().getWindow());
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"));
		fileChooser.setTitle("Salvar relatório de processo");
		fileChooser.setInitialFileName(processo.getIdentificador() + ".pdf");
		File savedFile = fileChooser.showSaveDialog(stage);
		if (savedFile != null) {
			generatePdfReport(savedFile, processo);
		}
	}

	private void generatePdfReport(File file, Processo processo) {
		progForm.setVisible(true);
		// btReport.setDisable(Boolean.TRUE);
		Task<Integer> reportTask = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				int result = ProcessoReportCreator.build(processo, file.getAbsolutePath(),
						PeriodFormatter.formatPeriod(processo.getDhInicial(), processo.getDhFinal()));
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
				progForm.setVisible(false);
				// btReport.setDisable(Boolean.FALSE);
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

}
