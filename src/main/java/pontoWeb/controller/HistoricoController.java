package pontoWeb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pontoWeb.db.ConnectionFactoryDB;
import pontoWeb.db.DAOHistorico;
import pontoWeb.model.ExtratoDia;
import pontoWeb.model.Historico;
import pontoWeb.model.Periodo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class HistoricoController {

	private DAOHistorico daoHistorico;
	public ConnectionFactoryDB connection;

	public HistoricoController(ConnectionFactoryDB connection) throws SQLException {
		this.connection = connection;
		this.daoHistorico = new DAOHistorico(this.connection);
	}

	public void generateReportHistorico(List<Historico> historicos, ConnectionFactoryDB connection) throws ClassNotFoundException, SQLException, FileNotFoundException, JRException {
		List<ExtratoDia> listExtrato = getListExtrato(historicos, connection);
		generateReportWeb(listExtrato);
	}
	
	public void generateReportWeb(List<ExtratoDia> listExtrato) throws FileNotFoundException, JRException {
		JRBeanCollectionDataSource itensJRBean = new JRBeanCollectionDataSource(listExtrato);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("CollectionBeanParam", itensJRBean);
		String reportPath = "C:\\Users\\MoriInfo\\Documents\\Softwares Dev\\controlePontoFull\\src\\main\\java\\pontoWeb\\reports\\RelatorioExtratoDia_2.jrxml";
		InputStream input = new FileInputStream(new File(reportPath));
		JasperDesign jasperDesign = JRXmlLoader.load(input);
		JasperReport jr = JasperCompileManager.compileReport(jasperDesign);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parameters, itensJRBean);
		JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\MoriInfo\\Documents\\Softwares Dev\\controlePontoFull\\src\\main\\java\\pontoWeb\\reports\\report.pdf");
		System.out.println("Report gerado!");
	}

	private List<ExtratoDia> getListExtrato(List<Historico> historicos, ConnectionFactoryDB connection) throws SQLException, ClassNotFoundException {
		PeriodoController periodoController = new PeriodoController();
		HoraExtraController heController = new HoraExtraController();
		AtrasoController atController = new AtrasoController();
		List<ExtratoDia> listExtrato = new ArrayList<ExtratoDia>();
		listExtrato.add(null);
		for (Historico historico : historicos) {
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
			List<Periodo> listPeriodsHE = heController.getPeriods(historico.getId_he(),connection);
			long sumDay_HE = periodoController.sumBetweenPeriods(listPeriodsHE);
			List<Periodo> listPeriodsAT = atController.getPeriods(historico.getId_at(),connection);
			long sumDay_AT = periodoController.sumBetweenPeriods(listPeriodsAT);
			LocalTime localtime_he = periodoController.numberToLocalTime((int) sumDay_HE);
			String sumhoraextra = dtf.format(localtime_he);
			LocalTime localtime_at = periodoController.numberToLocalTime((int) sumDay_AT);
			String sumatraso = dtf.format(localtime_at);

			long diference = ChronoUnit.MINUTES.between(localtime_he, localtime_at);
			String total;
			if (diference <= 0) {
				LocalTime localtime_dif = periodoController.numberToLocalTime((int) Math.abs(diference));
				total = dtf.format(localtime_dif);
			} else {
				LocalTime localtime_dif = periodoController.numberToLocalTime((int) diference);
				total = dtf.format(localtime_dif);
				total = "(" + total + ")";
			}
			ExtratoDia extratoDia = new ExtratoDia(formatador.format(historico.getDate()), sumatraso, sumhoraextra,	total);
			listExtrato.add(extratoDia);
		}
		return listExtrato;
	}

	public List<Historico> getHistoricos(ConnectionFactoryDB connection) throws SQLException {
		return this.daoHistorico.getHistoricos(connection);
	}

	public void generateReport(ConnectionFactoryDB connection) throws SQLException, ClassNotFoundException, FileNotFoundException, JRException {
		List<Historico> listaHistorico = getHistoricos(connection);
		if (listaHistorico != null) {
			generateReportHistorico(listaHistorico, connection);	
		}
	}

}
