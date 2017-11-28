package com.servicos.estatica.belluno.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.servicos.estatica.belluno.app.ControlledScreen;
import com.servicos.estatica.belluno.dao.CicloControleDAO;
import com.servicos.estatica.belluno.model.CicloControle;
import com.servicos.estatica.belluno.util.Toast;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

@SuppressWarnings("rawtypes")
public class ControleController implements Initializable, ControlledScreen {

	@FXML
	private Rectangle recControle;
	@FXML
	private ComboBox comboControle;
	@FXML
	private Button btNovo;
	@FXML
	private Button btEditar;
	@FXML
	private Button btUtilizar;
	@FXML
	private Button btExcluir;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	@FXML
	private TextField txtIdentificador;
	@FXML
	private TextField txtPrimeiroTotal;
	@FXML
	private TextField txtSegundoTotal;
	@FXML
	private TextField txtTerceiroTotal;
	@FXML
	private TextField txtQuartoTotal;
	@FXML
	private TextField txtPrimeiroAberto;
	@FXML
	private TextField txtSegundoAberto;
	@FXML
	private TextField txtTerceiroAberto;
	@FXML
	private TextField txtQuartoAberto;
	@FXML
	private TextField txtPrimeiroFechado;
	@FXML
	private TextField txtSegundoFechado;
	@FXML
	private TextField txtTerceiroFechado;
	@FXML
	private TextField txtQuartoFechado;
	@FXML
	private RadioButton rdPrimeiroAberto;
	@FXML
	private RadioButton rdPrimeiroFechado;
	@FXML
	private RadioButton rdSegundoAberto;
	@FXML
	private RadioButton rdSegundoFechado;
	@FXML
	private RadioButton rdTerceiroAberto;
	@FXML
	private RadioButton rdTerceiroFechado;
	@FXML
	private RadioButton rdQuartoAberto;
	@FXML
	private RadioButton rdQuartoFechado;
	@FXML
	private RadioButton rdFinalAberto;
	@FXML
	private RadioButton rdFinalFechado;
	@FXML
	private CheckBox chkPrimeiro;
	@FXML
	private CheckBox chkSegundo;
	@FXML
	private CheckBox chkTerceiro;
	@FXML
	private CheckBox chkQuarto;
	@FXML
	private ProgressIndicator progControle;

	ToggleGroup groupPrimeiro = new ToggleGroup();
	ToggleGroup groupSegundo = new ToggleGroup();
	ToggleGroup groupTerceiro = new ToggleGroup();
	ToggleGroup groupQuarto = new ToggleGroup();
	ToggleGroup groupFinal = new ToggleGroup();

	private static ObservableList<CicloControle> ciclos = FXCollections.observableArrayList();

	private static CicloControle cicloControle;
	private static CicloControleDAO controleDAO = new CicloControleDAO();

	private static DecimalFormat decimalFormat = new DecimalFormat("#");
	private static String toastMsg;

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		recControle.setFill(Color.TRANSPARENT);
		initFields();
		organizeRadioGroups();
		populateCombo();
		comboControle.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CicloControle>() {
			public void changed(ObservableValue<? extends CicloControle> observable, CicloControle oldValue,
					CicloControle newValue) {
				cicloControle = newValue;
			}
		});
	}

	@FXML
	private void addCiclo() {
		resetCombo();
		cicloControle = null;
		disableFields(false);
	}

	@FXML
	private void saveCicloControle() {
		if ("".equals(txtIdentificador.getText().trim()) || txtIdentificador.getText() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Informe um nome de identificação para o ciclo de controle.");
			alert.showAndWait();
			txtIdentificador.requestFocus();
			return;
		}
		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				fetch(true);
				if (cicloControle == null) {
					cicloControle = new CicloControle();
					prepareCicloControle();
					controleDAO.saveCicloControle(cicloControle);
					toastMsg = "Ciclo de controle salvo com sucesso.";
				} else {
					prepareCicloControle();
					controleDAO.updateCicloControle(cicloControle);
					toastMsg = "Ciclo de controle atualizado com sucesso.";
				}
				return null;
			}
		};

		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(WorkerStateEvent arg0) {
				fetch(false);
				resetFields(true);
				comboControle.setItems(null);
				populateCombo();
				makeToast(toastMsg);
				cancelar();
			}
		});

		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				fetch(false);
				resetFields(true);
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erro");
				alert.setHeaderText("Ocorreu um erro ao salvar o ciclo de controle.");
				alert.showAndWait();
				txtIdentificador.requestFocus();
				cancelar();
			}
		});
		new Thread(saveTask).start();
	}

	private void prepareCicloControle() {
		cicloControle.setPrimeiroTotal(Integer.parseInt(txtPrimeiroTotal.getText()));
		cicloControle.setSegundoTotal(Integer.parseInt(txtSegundoTotal.getText()));
		cicloControle.setTerceiroTotal(Integer.parseInt(txtTerceiroTotal.getText()));
		cicloControle.setQuartoTotal(Integer.parseInt(txtQuartoTotal.getText()));

		if (chkPrimeiro.isSelected()) {
			cicloControle.setPrimeiroFixo("S");
			cicloControle.setPrimeiroAberto(null);
			cicloControle.setPrimeiroFechado(null);
			if (rdPrimeiroAberto.isSelected()) {
				cicloControle.setPrimeiroSempreAberto("S");
				cicloControle.setPrimeiroSempreFechado("N");
			}
			if (rdPrimeiroFechado.isSelected()) {
				cicloControle.setPrimeiroSempreAberto("N");
				cicloControle.setPrimeiroSempreFechado("S");
			}
		} else {
			cicloControle.setPrimeiroFixo("N");
			cicloControle.setPrimeiroAberto(Integer.parseInt(txtPrimeiroAberto.getText()));
			cicloControle.setPrimeiroFechado(Integer.parseInt(txtPrimeiroFechado.getText()));
			cicloControle.setPrimeiroSempreAberto(null);
			cicloControle.setPrimeiroSempreFechado(null);
		}

		if (chkSegundo.isSelected()) {
			cicloControle.setSegundoFixo("S");
			cicloControle.setSegundoAberto(null);
			cicloControle.setSegundoFechado(null);
			if (rdSegundoAberto.isSelected()) {
				cicloControle.setSegundoSempreAberto("S");
				cicloControle.setSegundoSempreFechado("N");
			}
			if (rdSegundoFechado.isSelected()) {
				cicloControle.setSegundoSempreAberto("N");
				cicloControle.setSegundoSempreFechado("S");
			}
		} else {
			cicloControle.setSegundoFixo("N");
			cicloControle.setSegundoAberto(Integer.parseInt(txtSegundoAberto.getText()));
			cicloControle.setSegundoFechado(Integer.parseInt(txtSegundoFechado.getText()));
			cicloControle.setSegundoSempreAberto(null);
			cicloControle.setSegundoSempreFechado(null);
		}

		if (chkTerceiro.isSelected()) {
			cicloControle.setTerceiroFixo("S");
			cicloControle.setTerceiroAberto(null);
			cicloControle.setTerceiroFechado(null);
			if (rdTerceiroAberto.isSelected()) {
				cicloControle.setTerceiroSempreAberto("S");
				cicloControle.setTerceiroSempreFechado("N");
			}
			if (rdTerceiroFechado.isSelected()) {
				cicloControle.setTerceiroSempreAberto("N");
				cicloControle.setTerceiroSempreFechado("S");
			}
		} else {
			cicloControle.setTerceiroFixo("N");
			cicloControle.setTerceiroAberto(Integer.parseInt(txtTerceiroAberto.getText()));
			cicloControle.setTerceiroFechado(Integer.parseInt(txtTerceiroFechado.getText()));
			cicloControle.setTerceiroSempreAberto(null);
			cicloControle.setTerceiroSempreFechado(null);
		}

		if (chkQuarto.isSelected()) {
			cicloControle.setQuartoFixo("S");
			cicloControle.setQuartoAberto(null);
			cicloControle.setQuartoFechado(null);
			if (rdQuartoAberto.isSelected()) {
				cicloControle.setQuartoSempreAberto("S");
				cicloControle.setQuartoSempreFechado("N");
			}
			if (rdQuartoFechado.isSelected()) {
				cicloControle.setQuartoSempreAberto("N");
				cicloControle.setQuartoSempreFechado("S");
			}
		} else {
			cicloControle.setQuartoFixo("N");
			cicloControle.setQuartoAberto(Integer.parseInt(txtQuartoAberto.getText()));
			cicloControle.setQuartoFechado(Integer.parseInt(txtQuartoFechado.getText()));
			cicloControle.setQuartoSempreAberto(null);
			cicloControle.setQuartoSempreFechado(null);
		}

		if (rdFinalAberto.isSelected()) {
			cicloControle.setFinalAberto("S");
			cicloControle.setFinalFechado("N");
		}
		if (rdFinalFechado.isSelected()) {
			cicloControle.setFinalAberto("N");
			cicloControle.setFinalFechado("S");
		}
		cicloControle.setIdentificador(txtIdentificador.getText());
	}

	@FXML
	private void editCicloControle() {
		disableFields(false);
		btNovo.setDisable(true);
		btExcluir.setDisable(true);
		btUtilizar.setDisable(true);
		comboControle.setDisable(true);
		txtPrimeiroTotal.setText(cicloControle.getPrimeiroTotal().toString());
		txtSegundoTotal.setText(cicloControle.getSegundoTotal().toString());
		txtTerceiroTotal.setText(cicloControle.getTerceiroTotal().toString());
		txtQuartoTotal.setText(cicloControle.getQuartoTotal().toString());
		txtIdentificador.setText(cicloControle.getIdentificador());

		if ("S".equals(cicloControle.getPrimeiroFixo())) {
			chkPrimeiro.setSelected(true);
			rdPrimeiroAberto.setDisable(false);
			rdPrimeiroFechado.setDisable(false);
			txtPrimeiroAberto.setDisable(true);
			txtPrimeiroFechado.setDisable(true);
			if ("S".equals(cicloControle.getPrimeiroSempreAberto())) {
				rdPrimeiroAberto.setSelected(true);
				rdPrimeiroFechado.setSelected(false);
			} else {
				rdPrimeiroAberto.setSelected(false);
				rdPrimeiroAberto.setSelected(true);
			}
		} else {
			chkPrimeiro.setSelected(false);
			rdPrimeiroAberto.setDisable(true);
			rdPrimeiroFechado.setDisable(true);
			txtPrimeiroAberto.setDisable(false);
			txtPrimeiroFechado.setDisable(false);
			txtPrimeiroAberto.setText(cicloControle.getPrimeiroAberto().toString());
			txtPrimeiroFechado.setText(cicloControle.getPrimeiroFechado().toString());
		}

		if ("S".equals(cicloControle.getSegundoFixo())) {
			chkSegundo.setSelected(true);
			rdSegundoAberto.setDisable(false);
			rdSegundoFechado.setDisable(false);
			txtSegundoAberto.setDisable(true);
			txtSegundoFechado.setDisable(true);
			if ("S".equals(cicloControle.getSegundoSempreAberto())) {
				rdSegundoAberto.setSelected(true);
				rdSegundoFechado.setSelected(false);
			} else {
				rdSegundoAberto.setSelected(false);
				rdSegundoFechado.setSelected(true);
			}
		} else {
			rdSegundoAberto.setDisable(true);
			rdSegundoFechado.setDisable(true);
			txtSegundoAberto.setDisable(false);
			txtSegundoFechado.setDisable(false);
			chkSegundo.setSelected(false);
			txtSegundoAberto.setText(cicloControle.getSegundoAberto().toString());
			txtSegundoFechado.setText(cicloControle.getSegundoFechado().toString());
		}

		if ("S".equals(cicloControle.getTerceiroFixo())) {
			chkTerceiro.setSelected(true);
			rdTerceiroAberto.setDisable(false);
			rdTerceiroFechado.setDisable(false);
			txtTerceiroAberto.setDisable(true);
			txtTerceiroFechado.setDisable(true);
			if ("S".equals(cicloControle.getTerceiroSempreAberto())) {
				rdTerceiroAberto.setSelected(true);
				rdTerceiroFechado.setSelected(false);
			} else {
				rdTerceiroAberto.setSelected(false);
				rdTerceiroFechado.setSelected(true);
			}
		} else {
			rdTerceiroAberto.setDisable(true);
			rdTerceiroFechado.setDisable(true);
			txtTerceiroAberto.setDisable(false);
			txtTerceiroFechado.setDisable(false);
			chkTerceiro.setSelected(false);
			txtTerceiroAberto.setText(cicloControle.getTerceiroAberto().toString());
			txtTerceiroFechado.setText(cicloControle.getTerceiroFechado().toString());
		}

		if ("S".equals(cicloControle.getQuartoFixo())) {
			chkQuarto.setSelected(true);
			rdQuartoAberto.setDisable(false);
			rdQuartoFechado.setDisable(false);
			txtQuartoAberto.setDisable(true);
			txtQuartoFechado.setDisable(true);
			if ("S".equals(cicloControle.getQuartoSempreAberto())) {
				rdQuartoAberto.setSelected(true);
				rdQuartoFechado.setSelected(false);
			} else {
				rdQuartoAberto.setSelected(false);
				rdQuartoFechado.setSelected(true);
			}
		} else {
			chkQuarto.setSelected(false);
			rdQuartoAberto.setDisable(true);
			rdQuartoFechado.setDisable(true);
			txtQuartoAberto.setDisable(false);
			txtQuartoFechado.setDisable(false);
			txtQuartoAberto.setText(cicloControle.getQuartoAberto().toString());
			txtQuartoFechado.setText(cicloControle.getQuartoFechado().toString());
		}

		if ("S".equals(cicloControle.getFinalAberto())) {
			rdFinalAberto.setSelected(true);
			rdFinalFechado.setSelected(false);
		} else {
			rdFinalAberto.setSelected(false);
			rdFinalFechado.setSelected(true);
		}

	}

	@FXML
	private void removeCicloControle() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmação");
		alert.setHeaderText("Tem certeza que deseja remover definitivamente o ciclo programado?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Task<Void> removeTask = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					fetch(true);
					controleDAO.removeCicloControle(cicloControle);
					return null;
				}
			};
			removeTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					fetch(false);
					cicloControle = null;
					makeToast("O ciclo programado foi removido com sucesso.");
					populateCombo();
					resetFields(true);
					btEditar.setDisable(true);
					btUtilizar.setDisable(true);
					btExcluir.setDisable(true);
					btSalvar.setDisable(true);
					btCancelar.setDisable(false);
				}

			});
			removeTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					fetch(false);
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erro");
					alert.setHeaderText("Ocorreu um erro ao remover o ciclo de controle.");
					alert.showAndWait();
				}
			});
			new Thread(removeTask).start();
		}
	}

	@FXML
	private void cancelar() {
		resetFields(true);
		resetCombo();
		btNovo.setDisable(false);
		btEditar.setDisable(true);
		btUtilizar.setDisable(true);
		btExcluir.setDisable(true);
		btCancelar.setDisable(false);
		comboControle.setDisable(false);
	}

	@FXML
	private void selectCicloControle() {
		btEditar.setDisable(false);
		btUtilizar.setDisable(false);
		btExcluir.setDisable(false);
	}

	@FXML
	private void togglePrimeiro() {
		txtPrimeiroAberto.setDisable(chkPrimeiro.isSelected());
		txtPrimeiroFechado.setDisable(chkPrimeiro.isSelected());
		rdPrimeiroAberto.setDisable(!chkPrimeiro.isSelected());
		rdPrimeiroFechado.setDisable(!chkPrimeiro.isSelected());
	}

	@FXML
	private void toggleSegundo() {
		txtSegundoAberto.setDisable(chkSegundo.isSelected());
		txtSegundoFechado.setDisable(chkSegundo.isSelected());
		rdSegundoAberto.setDisable(!chkSegundo.isSelected());
		rdSegundoFechado.setDisable(!chkSegundo.isSelected());
	}

	@FXML
	private void toggleTerceiro() {
		txtTerceiroAberto.setDisable(chkTerceiro.isSelected());
		txtTerceiroFechado.setDisable(chkTerceiro.isSelected());
		rdTerceiroAberto.setDisable(!chkTerceiro.isSelected());
		rdTerceiroFechado.setDisable(!chkTerceiro.isSelected());
	}

	@FXML
	private void toggleQuarto() {
		txtQuartoAberto.setDisable(chkQuarto.isSelected());
		txtQuartoFechado.setDisable(chkQuarto.isSelected());
		rdQuartoAberto.setDisable(!chkQuarto.isSelected());
		rdQuartoFechado.setDisable(!chkQuarto.isSelected());
	}

	private void disableFields(boolean b) {
		btSalvar.setDisable(b);
		btCancelar.setDisable(b);
		txtIdentificador.setDisable(b);
		txtPrimeiroTotal.setDisable(b);
		txtSegundoTotal.setDisable(b);
		txtTerceiroTotal.setDisable(b);
		txtQuartoTotal.setDisable(b);
		chkPrimeiro.setDisable(b);
		chkSegundo.setDisable(b);
		chkTerceiro.setDisable(b);
		chkQuarto.setDisable(b);
		if (chkPrimeiro.isSelected()) {
			rdPrimeiroAberto.setDisable(b);
			rdPrimeiroFechado.setDisable(b);
		} else {
			txtPrimeiroAberto.setDisable(b);
			txtPrimeiroFechado.setDisable(b);
		}
		if (chkSegundo.isSelected()) {
			rdSegundoAberto.setDisable(b);
			rdSegundoFechado.setDisable(b);
		} else {
			txtSegundoAberto.setDisable(b);
			txtSegundoFechado.setDisable(b);
		}
		if (chkTerceiro.isSelected()) {
			rdTerceiroAberto.setDisable(b);
			rdTerceiroFechado.setDisable(b);
		} else {
			txtTerceiroAberto.setDisable(b);
			txtTerceiroFechado.setDisable(b);
		}
		if (chkQuarto.isSelected()) {
			rdQuartoAberto.setDisable(b);
			rdQuartoFechado.setDisable(b);
		} else {
			txtQuartoAberto.setDisable(b);
			txtQuartoFechado.setDisable(b);
		}
		rdFinalAberto.setDisable(b);
		rdFinalFechado.setDisable(b);
		txtPrimeiroTotal.requestFocus();
	}

	private void resetFields(boolean b) {
		btSalvar.setDisable(b);
		btCancelar.setDisable(b);
		txtIdentificador.setDisable(b);
		txtPrimeiroTotal.setDisable(b);
		txtSegundoTotal.setDisable(b);
		txtTerceiroTotal.setDisable(b);
		txtQuartoTotal.setDisable(b);
		chkPrimeiro.setDisable(b);
		chkSegundo.setDisable(b);
		chkTerceiro.setDisable(b);
		chkQuarto.setDisable(b);
		rdPrimeiroAberto.setDisable(b);
		rdPrimeiroFechado.setDisable(b);
		txtPrimeiroAberto.setDisable(b);
		txtPrimeiroFechado.setDisable(b);
		rdSegundoAberto.setDisable(b);
		rdSegundoFechado.setDisable(b);
		txtSegundoAberto.setDisable(b);
		txtSegundoFechado.setDisable(b);
		rdTerceiroAberto.setDisable(b);
		rdTerceiroFechado.setDisable(b);
		txtTerceiroAberto.setDisable(b);
		txtTerceiroFechado.setDisable(b);
		rdQuartoAberto.setDisable(b);
		rdQuartoFechado.setDisable(b);
		txtQuartoAberto.setDisable(b);
		txtQuartoFechado.setDisable(b);
		rdFinalAberto.setDisable(b);
		rdFinalFechado.setDisable(b);

		txtIdentificador.clear();
		txtPrimeiroAberto.clear();
		txtPrimeiroFechado.clear();
		txtPrimeiroTotal.clear();
		txtSegundoAberto.clear();
		txtSegundoFechado.clear();
		txtSegundoTotal.clear();
		txtTerceiroAberto.clear();
		txtTerceiroFechado.clear();
		txtTerceiroTotal.clear();
		txtQuartoAberto.clear();
		txtQuartoFechado.clear();
		txtQuartoTotal.clear();

		chkPrimeiro.setSelected(!b);
		chkSegundo.setSelected(!b);
		chkTerceiro.setSelected(!b);
		chkQuarto.setSelected(!b);
	}

	private void initFields() {
		formatNumberField(txtPrimeiroTotal);
		formatNumberField(txtPrimeiroAberto);
		formatNumberField(txtPrimeiroFechado);
		formatNumberField(txtSegundoTotal);
		formatNumberField(txtSegundoAberto);
		formatNumberField(txtSegundoFechado);
		formatNumberField(txtTerceiroTotal);
		formatNumberField(txtTerceiroAberto);
		formatNumberField(txtTerceiroFechado);
		formatNumberField(txtQuartoTotal);
		formatNumberField(txtQuartoAberto);
		formatNumberField(txtQuartoFechado);

	}

	private void organizeRadioGroups() {
		rdPrimeiroAberto.setToggleGroup(groupPrimeiro);
		rdPrimeiroFechado.setToggleGroup(groupPrimeiro);
		rdSegundoAberto.setToggleGroup(groupSegundo);
		rdSegundoFechado.setToggleGroup(groupSegundo);
		rdTerceiroAberto.setToggleGroup(groupTerceiro);
		rdTerceiroFechado.setToggleGroup(groupTerceiro);
		rdQuartoAberto.setToggleGroup(groupQuarto);
		rdQuartoFechado.setToggleGroup(groupQuarto);
		rdFinalAberto.setToggleGroup(groupFinal);
		rdFinalFechado.setToggleGroup(groupFinal);
		rdPrimeiroAberto.setSelected(true);
		rdSegundoAberto.setSelected(true);
		rdTerceiroAberto.setSelected(true);
		rdQuartoAberto.setSelected(true);
		rdFinalAberto.setSelected(true);

	}

	private void formatNumberField(TextField txtField) {
		txtField.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = decimalFormat.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));
	}

	private void makeToast(String message) {
		String toastMsg = message;
		int toastMsgTime = 5000;
		int fadeInTime = 600;
		int fadeOutTime = 600;
		Stage stage = (Stage) btSalvar.getScene().getWindow();
		Toast.makeToast(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
	}

	private void fetch(boolean b) {
		btSalvar.setDisable(b);
		btCancelar.setDisable(b);
		txtIdentificador.setDisable(b);
		txtPrimeiroTotal.setDisable(b);
		txtSegundoTotal.setDisable(b);
		txtTerceiroTotal.setDisable(b);
		txtQuartoTotal.setDisable(b);
		chkPrimeiro.setDisable(b);
		chkSegundo.setDisable(b);
		chkTerceiro.setDisable(b);
		chkQuarto.setDisable(b);
		rdPrimeiroAberto.setDisable(b);
		rdPrimeiroFechado.setDisable(b);
		txtPrimeiroAberto.setDisable(b);
		txtPrimeiroFechado.setDisable(b);
		rdSegundoAberto.setDisable(b);
		rdSegundoFechado.setDisable(b);
		txtSegundoAberto.setDisable(b);
		txtSegundoFechado.setDisable(b);
		rdTerceiroAberto.setDisable(b);
		rdTerceiroFechado.setDisable(b);
		txtTerceiroAberto.setDisable(b);
		txtTerceiroFechado.setDisable(b);
		rdQuartoAberto.setDisable(b);
		rdQuartoFechado.setDisable(b);
		txtQuartoAberto.setDisable(b);
		txtQuartoFechado.setDisable(b);
		rdFinalAberto.setDisable(b);
		rdFinalFechado.setDisable(b);
		progControle.setVisible(b);

	}

	@SuppressWarnings("unchecked")
	private void populateCombo() {
		ciclos = FXCollections.observableList((List<CicloControle>) controleDAO.findCiclos());
		comboControle.setItems(ciclos);
	}

	private void resetCombo() {
		comboControle.getSelectionModel().clearSelection();
		comboControle.setPromptText("Selecione um ciclo de controle");
	}

}
