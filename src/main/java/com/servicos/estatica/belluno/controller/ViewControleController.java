package com.servicos.estatica.belluno.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.servicos.estatica.belluno.model.CicloControle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ViewControleController implements Initializable {

	@FXML
	private Rectangle rectControle;
	@FXML
	private Button btVoltar;
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

	private CicloControle cicloControle;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rectControle.setFill(Color.TRANSPARENT);
		chkPrimeiro.setStyle("-fx-opacity: 1");
		chkSegundo.setStyle("-fx-opacity: 1");
		chkTerceiro.setStyle("-fx-opacity: 1");
		chkQuarto.setStyle("-fx-opacity: 1");
		rdFinalAberto.setStyle("-fx-opacity: 1");
		rdFinalFechado.setStyle("-fx-opacity: 1");
		rdPrimeiroAberto.setStyle("-fx-opacity: 1");
		rdPrimeiroFechado.setStyle("-fx-opacity: 1");
		rdSegundoAberto.setStyle("-fx-opacity: 1");
		rdSegundoFechado.setStyle("-fx-opacity: 1");
		rdTerceiroAberto.setStyle("-fx-opacity: 1");
		rdTerceiroFechado.setStyle("-fx-opacity: 1");
		rdQuartoAberto.setStyle("-fx-opacity: 1");
		rdQuartoFechado.setStyle("-fx-opacity: 1");
	}

	public void setContext(CicloControle cicloControle) {
		this.cicloControle = cicloControle;
		populateFields();

	}

	private void populateFields() {
		txtIdentificador.setText(cicloControle.getIdentificador());
		txtPrimeiroTotal.setText(cicloControle.getPrimeiroTotal().toString());
		txtSegundoTotal.setText(cicloControle.getSegundoTotal().toString());
		txtTerceiroTotal.setText(cicloControle.getTerceiroTotal().toString());
		txtQuartoTotal.setText(cicloControle.getQuartoTotal().toString());
		if ("S".equals(cicloControle.getPrimeiroFixo())) {
			chkPrimeiro.setSelected(true);
			txtPrimeiroAberto.clear();
			txtPrimeiroFechado.clear();
			if ("S".equals(cicloControle.getPrimeiroSempreAberto())) {
				rdPrimeiroAberto.setSelected(true);
				rdPrimeiroFechado.setSelected(false);
			} else {
				rdPrimeiroAberto.setSelected(false);
				rdPrimeiroFechado.setSelected(true);
			}
		} else {
			chkPrimeiro.setSelected(false);
			rdPrimeiroAberto.setSelected(false);
			rdPrimeiroFechado.setSelected(false);
			txtPrimeiroAberto.setText(cicloControle.getPrimeiroAberto().toString());
			txtPrimeiroFechado.setText(cicloControle.getPrimeiroFechado().toString());
		}
		if ("S".equals(cicloControle.getSegundoFixo())) {
			chkSegundo.setSelected(true);
			txtSegundoAberto.clear();
			txtSegundoFechado.clear();
			if ("S".equals(cicloControle.getSegundoSempreAberto())) {
				rdSegundoAberto.setSelected(true);
				rdSegundoFechado.setSelected(false);
			} else {
				rdSegundoAberto.setSelected(false);
				rdSegundoFechado.setSelected(true);
			}
		} else {
			chkSegundo.setSelected(false);
			rdSegundoAberto.setSelected(false);
			rdSegundoFechado.setSelected(false);
			txtSegundoAberto.setText(cicloControle.getSegundoAberto().toString());
			txtSegundoFechado.setText(cicloControle.getSegundoFechado().toString());
		}
		if ("S".equals(cicloControle.getTerceiroFixo())) {
			chkTerceiro.setSelected(true);
			txtTerceiroAberto.clear();
			txtTerceiroFechado.clear();
			if ("S".equals(cicloControle.getTerceiroSempreAberto())) {
				rdTerceiroAberto.setSelected(true);
				rdTerceiroFechado.setSelected(false);
			} else {
				rdTerceiroAberto.setSelected(false);
				rdTerceiroFechado.setSelected(true);
			}
		} else {
			chkTerceiro.setSelected(false);
			rdTerceiroAberto.setSelected(false);
			rdTerceiroFechado.setSelected(false);
			txtTerceiroAberto.setText(cicloControle.getTerceiroAberto().toString());
			txtTerceiroFechado.setText(cicloControle.getTerceiroFechado().toString());
		}
		if ("S".equals(cicloControle.getQuartoFixo())) {
			chkQuarto.setSelected(true);
			txtQuartoAberto.clear();
			txtQuartoFechado.clear();
			if ("S".equals(cicloControle.getQuartoSempreAberto())) {
				rdQuartoAberto.setSelected(true);
				rdQuartoFechado.setSelected(false);
			} else {
				rdQuartoAberto.setSelected(false);
				rdQuartoFechado.setSelected(true);
			}
		} else {
			chkQuarto.setSelected(false);
			rdQuartoAberto.setSelected(false);
			rdQuartoFechado.setSelected(false);
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
	public void voltar() {
		Stage stage = (Stage) btVoltar.getScene().getWindow();
		stage.close();
	}

}
