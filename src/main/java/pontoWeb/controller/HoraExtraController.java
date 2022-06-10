package pontoWeb.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import pontoWeb.db.ConnectionFactory;
import pontoWeb.db.DAOHoraExtra;
import pontoWeb.db.DAOPeriodo;
import pontoWeb.db.DAO_He_Periodo;
import pontoWeb.model.Dia;
import pontoWeb.model.Periodo;
import pontoWeb.view.HoraExtraView;
import pontoWeb.view.HorarioDeTrabalhoView;
import pontoWeb.view.JanelaPrincipal;
import pontoWeb.view.MarcacoesFeitasView;
import pontoWeb.view.TabelaView;

public class HoraExtraController {

	private JanelaPrincipal context;
	private DiaController diaController;
	private PeriodoController periodoController;
	private DAOPeriodo daoPeriodo;
	private DAO_He_Periodo daohe_periodo;
	private ConnectionFactory connection;
	
	public HoraExtraController(JanelaPrincipal context,ConnectionFactory connection) throws SQLException {
		this.connection = connection;
		this.context = context;
		this.diaController = new DiaController();
		this.periodoController = new PeriodoController();
		this.daoPeriodo = new DAOPeriodo(this.connection);
		this.daohe_periodo = new DAO_He_Periodo(this.connection);
	}

	public HoraExtraController() throws SQLException, ClassNotFoundException {
		this.diaController = new DiaController();
		this.periodoController = new PeriodoController();
	}

	public void subHoraExtra() {
		this.context.horaExtraView.setTable(calculaHoraExtra(this.context.horarioDeTrabalhoView,
				this.context.marcacoesFeitasView, this.context.horaExtraView));
		this.context.horaExtraView.renderTable();
	}

	private JTable calculaHoraExtra(HorarioDeTrabalhoView horarioDeTrabalho, MarcacoesFeitasView marcacoesFeitasView,
			HoraExtraView horaExtraView) {
		Dia horarioTrabalhoDia = new Dia();
		horarioTrabalhoDia = this.diaController.getConvertedDay(horarioDeTrabalho);
		Dia marcacoesFeitasDia = new Dia();
		marcacoesFeitasDia = this.diaController.getConvertedDay(marcacoesFeitasView);
		Dia horaExtraDia = this.diaController.getHoraExtra(marcacoesFeitasDia, horarioTrabalhoDia);
		List<Periodo> listaPeriodosHoraExtra = this.periodoController.getPeriods(horaExtraDia);
		TabelaView tableView = horaExtraView.newTable(listaPeriodosHoraExtra);
		horaExtraView.setListEntries(tableView.getListEntries());
		horaExtraView.setListExits(tableView.getListExits());
		return horaExtraView.getTable();
	}
	
	public List<Periodo> calculaHoraExtraWeb(String json) {
		Dia horarioTrabalhoDia = this.diaController.getConvertedDayHT(json);
		Dia marcacoesFeitasDia = this.diaController.getConvertedDayMF(json);
		Dia horaextraDia = this.diaController.getHoraExtra(marcacoesFeitasDia,horarioTrabalhoDia);
		List<Periodo> listaPeriodosHoraExtra = this.periodoController.getPeriods(horaextraDia);
		return listaPeriodosHoraExtra;
	}

	public int saveHoraExtra(HoraExtraView horaExtraView) throws SQLException {
		DAOHoraExtra daoHoraExtra= new DAOHoraExtra(this.connection);
		//CRIA NOVO REGISTRO DO TIPO HORAEXTRA NO BANCO 
		Integer idHE = daoHoraExtra.insert();
		//CRIA NOVOS REGISTROS DO TIPO PERIODO (PERIODOS DE HORA EXTRA) NO BANCO
		for(int i = 0; i<horaExtraView.getTableModel().getRowCount();i++) {
			int idperiodo = this.daoPeriodo.insert(this.periodoController.localTimeToNumber(horaExtraView.getListEntries().get(i)), this.periodoController.localTimeToNumber(horaExtraView.getListExits().get(i)));
			this.daohe_periodo.insert(idHE, idperiodo);
		}
		return idHE;
	}
	
	//RECEBE UM ID DE HORAEXTRA E RETORNA TODOS OS PERIODOS (PERIODOS DE HORA EXTRA) DAQUELE DIA
	public List<Periodo> getPeriods(int id_he) throws SQLException {	
		List<Integer> list_idPeriods = this.daohe_periodo.getPeriods(id_he);
		ArrayList<Periodo> listPeriods = new ArrayList<Periodo>();
		for(Integer idPeriod: list_idPeriods) {
			Periodo periodo = this.daoPeriodo.getPeriodoByID(idPeriod);
			listPeriods.add(periodo);
		}		
		return listPeriods;
	}

}
