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

import javax.swing.JTextField;

import pontoWeb.db.ConnectionFactory;
import pontoWeb.db.DAOHistorico;
import pontoWeb.model.ExtratoDia;
import pontoWeb.model.Historico;
import pontoWeb.model.Periodo;
import pontoWeb.view.JanelaPrincipal;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class HistoricoController {

	private DAOHistorico daoHistorico;
	public ConnectionFactory connection;

	public HistoricoController(ConnectionFactory connection) throws SQLException {
		this.connection = connection;
		this.daoHistorico = new DAOHistorico(this.connection);
	}

	public boolean hasARegister(List<JTextField> listJTextField) throws SQLException {
		String date = getDateFormatted(listJTextField);
		Historico historico = this.daoHistorico.getHistoricoByDate(date);
		if (historico != null)
			return true;
		return false;
	}

	public Historico returnHistoricoHasARegister(List<JTextField> listJTextField) throws SQLException {
		String date = getDateFormatted(listJTextField);
		Historico historico = this.daoHistorico.getHistoricoByDate(date);
		if (historico != null)
			return historico;
		return null;
	}

	private String getDateFormatted(List<JTextField> listJTextField) {
		return new String("20" + listJTextField.get(2).getText() + "-" + listJTextField.get(1).getText() + "-"
				+ listJTextField.get(0).getText());
	}

	public String getDateFormattedView(List<JTextField> listJTextField) {
		return new String(listJTextField.get(0).getText() + "/" + listJTextField.get(1).getText() + "/20"
				+ listJTextField.get(2).getText());
	}

	public Boolean insert(JanelaPrincipal context) throws SQLException {
		this.connection.getConnection().setAutoCommit(false);
		try {
			String date = getDateFormatted(context.listTextFieldsData);
			int idHT = context.htController.saveHorarioDeTrabalho(context.horarioDeTrabalhoView);
			int idMF = context.mfController.saveMarcacoesFeitas(context.marcacoesFeitasView);
			int idHE = context.heController.saveHoraExtra(context.horaExtraView);
			int idAT = context.atController.saveAtrasos(context.atrasoView);
			this.daoHistorico.insert(date, idHT, idMF, idHE, idAT);
			this.connection.getConnection().commit();
			System.out.println("Commit");
			return true;
		} catch (Exception e) {
			this.connection.getConnection().rollback();
			System.out.println("Rollback");
			return false;
		}
	}

	public Boolean update(JanelaPrincipal context) throws SQLException {
		this.connection.getConnection().setAutoCommit(false);
		try {
			String date = getDateFormatted(context.listTextFieldsData);
			int idAntigo = this.daoHistorico.getIdHistoricoByDate(date);
			this.daoHistorico.delete(idAntigo);
			int idHT = context.htController.saveHorarioDeTrabalho(context.horarioDeTrabalhoView);
			int idMF = context.mfController.saveMarcacoesFeitas(context.marcacoesFeitasView);
			int idHE = context.heController.saveHoraExtra(context.horaExtraView);
			int idAT = context.atController.saveAtrasos(context.atrasoView);
			this.daoHistorico.insert(date, idHT, idMF, idHE, idAT);
			this.connection.getConnection().commit();
			System.out.println("Commit");
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			this.connection.getConnection().rollback();
			System.out.println("Rollback");
			return false;
		}
	}

	public void load(Historico historico, JanelaPrincipal context) throws SQLException {
		loadHorarioDeTrabalho(historico, context);
		loadMarcacoesFeitas(historico, context);
		loadHoraExtra(historico, context);
		loadAtrasos(historico, context);
	}

	private void loadAtrasos(Historico historico, JanelaPrincipal context) throws SQLException {
		context.atrasoView.clearTable();
		List<Periodo> listPeriodsAT = context.atController.getPeriods(historico.getId_at());
		;
		for (Periodo periodo : listPeriodsAT) {
			context.atrasoView.getListEntries().add(periodo.getEntrada());
			context.atrasoView.getListExits().add(periodo.getSaida());
		}
		context.atrasoView.renderTable();
	}

	private void loadHoraExtra(Historico historico, JanelaPrincipal context) throws SQLException {
		context.horaExtraView.clearTable();
		List<Periodo> listPeriodsHE = context.heController.getPeriods(historico.getId_he());
		for (Periodo periodo : listPeriodsHE) {
			context.horaExtraView.getListEntries().add(periodo.getEntrada());
			context.horaExtraView.getListExits().add(periodo.getSaida());
		}
		context.horaExtraView.renderTable();
	}

	private void loadMarcacoesFeitas(Historico historico, JanelaPrincipal context) throws SQLException {
		context.marcacoesFeitasView.clearTable();
		List<Periodo> listPeriodsMF = context.mfController.getPeriods(historico.getId_mf());
		;
		for (Periodo periodo : listPeriodsMF) {
			context.marcacoesFeitasView.getListEntries().add(periodo.getEntrada());
			context.marcacoesFeitasView.getListExits().add(periodo.getSaida());
		}
		context.marcacoesFeitasView.renderTable();
	}

	private void loadHorarioDeTrabalho(Historico historico, JanelaPrincipal context) throws SQLException {
		context.horarioDeTrabalhoView.clearTable();
		List<Periodo> listPeriodsHT = context.htController.getPeriods(historico.getId_ht());
		for (Periodo periodo : listPeriodsHT) {
			context.horarioDeTrabalhoView.getListEntries().add(periodo.getEntrada());
			context.horarioDeTrabalhoView.getListExits().add(periodo.getSaida());
		}
		context.horarioDeTrabalhoView.renderTable();
	}

	public void exportReportHistorico(List<Historico> historicos, JanelaPrincipal context)
			throws SQLException, FileNotFoundException, JRException {
		List<ExtratoDia> listExtrato = getListExtrato(historicos, context);
		generateReport(listExtrato);
	}

	private void generateReport(List<ExtratoDia> listExtrato) throws FileNotFoundException, JRException {
		JRBeanCollectionDataSource itensJRBean = new JRBeanCollectionDataSource(listExtrato);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("CollectionBeanParam", itensJRBean);
		String reportPath = "src/main/java/pontoWeb/reports/RelatorioExtratoDia_2.jrxml";
		InputStream input = new FileInputStream(new File(reportPath));
		JasperDesign jasperDesign = JRXmlLoader.load(input);
		JasperReport jr = JasperCompileManager.compileReport(jasperDesign);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parameters, itensJRBean);
		JasperViewer.viewReport(jasperPrint,false);
	}

	private List<ExtratoDia> getListExtrato(List<Historico> historicos, JanelaPrincipal context) throws SQLException {
		PeriodoController periodoController = new PeriodoController();
		List<ExtratoDia> listExtrato = new ArrayList<ExtratoDia>();
		listExtrato.add(null);
		for (Historico historico : historicos) {
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
			List<Periodo> listPeriodsHE = context.heController.getPeriods(historico.getId_he());
			long sumDay_HE = periodoController.sumBetweenPeriods(listPeriodsHE);
			List<Periodo> listPeriodsAT = context.atController.getPeriods(historico.getId_at());
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
			ExtratoDia extratoDia = new ExtratoDia(formatador.format(historico.getDate()), sumatraso, sumhoraextra,
					total);
			listExtrato.add(extratoDia);
		}
		return listExtrato;
	}

	public List<Historico> getHistoricos() throws SQLException {
		return this.daoHistorico.getHistoricos();
	}

}
