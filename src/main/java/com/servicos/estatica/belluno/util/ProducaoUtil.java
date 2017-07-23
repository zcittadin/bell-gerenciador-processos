package com.servicos.estatica.belluno.util;

import com.servicos.estatica.belluno.model.Leitura;
import com.servicos.estatica.belluno.model.Processo;

public class ProducaoUtil {

	public static double getTempMin(Processo processo) {
		double min = 1900;
		for (Leitura leitura : processo.getLeituras()) {
			if (leitura.getTemp() < min) {
				min = leitura.getTemp();
			}
		}
		return min;
	}

	public static double getTempMax(Processo processo) {
		double max = 0;
		for (Leitura leitura : processo.getLeituras()) {
			if (leitura.getTemp() >= max) {
				max = leitura.getTemp();
			}
		}
		return max;
	}
}
