package com.servicos.estatica.belluno.util;

import java.util.Date;

import java.time.Duration;

public class PeriodFormatter {

	public static String formatPeriod(Date ini, Date fim) {
		long periodoMillis = fim.getTime() - ini.getTime();
		Duration duration = Duration.ofMillis(periodoMillis);
		long hours = duration.toHours();
		int minutes = (int) ((duration.getSeconds() % (60 * 60)) / 60);
		int seconds = (int) (duration.getSeconds() % 60);
		return (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":"
				+ (seconds < 10 ? "0" + seconds : seconds);
	}
}
