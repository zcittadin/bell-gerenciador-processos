package com.servicos.estatica.belluno.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

import javax.swing.text.NumberFormatter;

import com.servicos.estatica.belluno.app.ControlledScreen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControleController implements Initializable, ControlledScreen {

	@FXML
	private Rectangle recControle;
	@FXML
	private ComboBox comboControle;
	@FXML
	private Button btNovo;
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

	ToggleGroup groupPrimeiro = new ToggleGroup();
	ToggleGroup groupSegundo = new ToggleGroup();
	ToggleGroup groupTerceiro = new ToggleGroup();
	ToggleGroup groupQuarto = new ToggleGroup();
	ToggleGroup groupFinal = new ToggleGroup();

	private static DecimalFormat decimalFormat = new DecimalFormat("#");

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		recControle.setFill(Color.TRANSPARENT);

		initFields();
		organizeRadioGroups();

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

}
