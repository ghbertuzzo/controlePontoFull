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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pontoWeb.db.ConnectionFactoryDB;
import pontoWeb.db.DAOHistorico;
import pontoWeb.model.Dia;
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

	public void generateReportHistorico(List<Historico> historicos, ConnectionFactoryDB connection, String path)
			throws ClassNotFoundException, SQLException, FileNotFoundException, JRException {
		List<ExtratoDia> listExtrato = getListExtrato(historicos, connection);
		generateReportWeb(listExtrato, path);
	}

	public void generateReportWeb(List<ExtratoDia> listExtrato, String path) throws FileNotFoundException, JRException {
		JRBeanCollectionDataSource itensJRBean = new JRBeanCollectionDataSource(listExtrato);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("CollectionBeanParam", itensJRBean);
		InputStream input = new FileInputStream(new File(path));
		JasperDesign jasperDesign = JRXmlLoader.load(input);
		JasperReport jr = JasperCompileManager.compileReport(jasperDesign);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parameters, itensJRBean);
		JasperExportManager.exportReportToPdfFile(jasperPrint, path);
	}

	private List<ExtratoDia> getListExtrato(List<Historico> historicos, ConnectionFactoryDB connection)
			throws SQLException, ClassNotFoundException {
		PeriodoController periodoController = new PeriodoController();
		HoraExtraController heController = new HoraExtraController(connection);
		AtrasoController atController = new AtrasoController(connection);
		List<ExtratoDia> listExtrato = new ArrayList<ExtratoDia>();
		listExtrato.add(null);
		for (Historico historico : historicos) {
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
			List<Periodo> listPeriodsHE = heController.getPeriods(historico.getId_he());
			long sumDay_HE = periodoController.sumBetweenPeriods(listPeriodsHE);
			List<Periodo> listPeriodsAT = atController.getPeriods(historico.getId_at());
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

	public List<Historico> getHistoricos(ConnectionFactoryDB connection) throws SQLException {
		return this.daoHistorico.getHistoricos(connection);
	}

	public void generateReport(ConnectionFactoryDB connection, String path)
			throws SQLException, ClassNotFoundException, FileNotFoundException, JRException {
		List<Historico> listaHistorico = getHistoricos(connection);
		if (listaHistorico != null) {
			generateReportHistorico(listaHistorico, connection, path);
		}
	}

	public Boolean addHistorico(String requestData) {
		// CONVERTE EM PERÍODOS DE HORARIO DE TRABALHO e PERÍODOS DE MARCACOES FEITAS
		DiaController diaController = new DiaController();
		Dia horarioTrabalhoDia = diaController.getConvertedDayHT(requestData);
		Dia marcacoesFeitasDia = diaController.getConvertedDayMF(requestData);
		PeriodoController periodoController = new PeriodoController();
		List<Periodo> listaPeriodosHorararioTrabalho = periodoController.getPeriods(horarioTrabalhoDia);
		List<Periodo> listaPeriodosMarcacoesFeitas = periodoController.getPeriods(marcacoesFeitasDia);

		// EFETUA CÁLCULOS PARA OBTER TODOS OS PERÍODOS DE HORA EXTRA
		HoraExtraController horaExtraController = new HoraExtraController(this.connection);
		List<Periodo> listaPeriodosHoraExtra = horaExtraController.calculaHoraExtraWeb(requestData);

		// EFETUA CÁLCULOS PARA OBTER TODOS OS PERÍODOS DE ATRASO
		AtrasoController atrasoController = new AtrasoController(this.connection);
		List<Periodo> listaPeriodosAtrasos = atrasoController.calculaAtrasosWeb(requestData);

		// PEGA DATA DA REQUISIÇÃO
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(requestData, JsonObject.class);
		String date = jsonObject.get("data").getAsString();

		// SETA COMMIT COMO FALSO
		int idHT = -1;
		int idMF = -1;
		int idAT = -1;
		int idHE = -1;
		try {
			this.connection.getConnection().setAutoCommit(false);
			// ADD NOVO HORARIO DE TRABALHO
			HorarioDeTrabalhoController htController = new HorarioDeTrabalhoController(connection);
			idHT = htController.saveHorarioDeTrabalho(listaPeriodosHorararioTrabalho);
			// ADD NOVA MARCACOES FEITAS
			MarcacoesFeitasController mfController = new MarcacoesFeitasController(connection);
			idMF = mfController.saveMarcacoesFeitas(listaPeriodosMarcacoesFeitas);
			// ADD ATRASOS
			idAT = atrasoController.saveAtrasos(listaPeriodosAtrasos);
			// ADD HORAEXTRAS
			idHE = horaExtraController.saveHoraExtra(listaPeriodosHoraExtra);
			// ADD HISTORICO DIA
			DAOHistorico daoHistorico = new DAOHistorico(connection);
			daoHistorico.insert(date, idHT, idMF, idHE, idAT);
			// SE PASSOU TUDO, COMMIT
			this.connection.getConnection().commit();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public Historico getHistorico(String requestData) throws SQLException {
		// PEGA DATA DA REQUISIÇÃO
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(requestData, JsonObject.class);
		String date = jsonObject.get("data").getAsString();
		Historico historico = this.daoHistorico.getHistoricoByDate(date);
		if(historico!=null)
			return historico;
		else
			return null;
	}

}
