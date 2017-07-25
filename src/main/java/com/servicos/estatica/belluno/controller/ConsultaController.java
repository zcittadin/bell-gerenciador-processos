package com.servicos.estatica.belluno.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import com.servicos.estatica.belluno.app.ControlledScreen;
import com.servicos.estatica.belluno.dao.ProcessoDAO;
import com.servicos.estatica.belluno.model.Processo;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
	private TableColumn colSetPoint;
	@FXML
	private TableColumn colGraficos;
	@FXML
	private TableColumn colRelatorios;

	SpinnerValueFactory<Integer> valueFactory = //
			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 5);
	ToggleGroup group = new ToggleGroup();
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
		spnUltimos.getValueFactory().setValue(5);
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
		spnUltimos.getValueFactory().setValue(5);
		dtpInicio.setDisable(true);
		dtpFinal.setDisable(true);
		txtIdentificador.setDisable(false);

	}

	@FXML
	private void consultar() {
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
					System.out.println("Por periodo");
					return null;
				}
				if (rdUltimos.isSelected()) {
					processos = FXCollections.observableList((List<Processo>) processoDAO.findLastProcessos());
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

	private void consultarRecentes() {
		tblConsulta.getItems().clear();
		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				processos = FXCollections.observableList((List<Processo>) processoDAO.findLastProcessos());
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
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>("12:00:00");
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
		colSetPoint.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Processo, Double>, ObservableValue<Double>>() {
					public ObservableValue<Double> call(CellDataFeatures<Processo, Double> cell) {
						final Processo p = cell.getValue();
						final SimpleObjectProperty<Double> simpleObject = new SimpleObjectProperty<Double>(
								new Double(0));
						return simpleObject;
					}
				});
		colIdentificador.setStyle("-fx-alignment: CENTER;");
		colDhInicial.setStyle("-fx-alignment: CENTER;");
		colDhFinal.setStyle("-fx-alignment: CENTER;");
		colTempoDecorrido.setStyle("-fx-alignment: CENTER;");
		colTempMin.setStyle("-fx-alignment: CENTER;");
		colTempMax.setStyle("-fx-alignment: CENTER;");
		colSetPoint.setStyle("-fx-alignment: CENTER;");
		colGraficos.setStyle("-fx-alignment: CENTER;");
		colRelatorios.setStyle("-fx-alignment: CENTER;");
		tblConsulta.getColumns().setAll(colIdentificador, colDhInicial, colDhFinal, colTempoDecorrido, colTempMin,
				colTempMax, colSetPoint, colGraficos, colRelatorios);
	}

}
