package com.servicos.estatica.belluno.report.builder;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.util.Date;

import com.servicos.estatica.belluno.model.Processo;
import com.servicos.estatica.belluno.report.template.ProcessoReportTemplate;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.exception.DRException;

public class ProcessoReportCreator {

	public static int build(Processo processo, String path, String periodo) {
		
		TextColumnBuilder<Date> dtProcessoColumn = col.column("Horário", "dtProc", type.timeHourToSecondType());
		TextColumnBuilder<Double> tempColumn = col.column("Temperatura lida (ºC)", "temp", type.doubleType());
		TextColumnBuilder<Double> setPointColumn = col.column("Set-point (ºC)", "sp", type.doubleType());

		try {
			JasperPdfExporterBuilder pdfExporter = export.pdfExporter(path);
			report().setTemplate(ProcessoReportTemplate.reportTemplate)
					.title(ProcessoReportTemplate.createHeaderComponent(processo),
							ProcessoReportTemplate.createSeparatorComponent(),
							ProcessoReportTemplate.createDadosComponent(processo, periodo),
							ProcessoReportTemplate.createSeparatorComponent(),
							ProcessoReportTemplate.createChartComponent(processo),
							ProcessoReportTemplate.createSeparatorComponent())
					.setDataSource(processo.getLeituras()).columns(dtProcessoColumn, tempColumn, setPointColumn)
					.summary(ProcessoReportTemplate.createEmissaoComponent())
					.pageFooter(ProcessoReportTemplate.footerComponent).toPdf(pdfExporter);
			return 1;
		} catch (DRException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
