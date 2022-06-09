package pontoWeb.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

import pontoWeb.db.ConnectionFactory;
import pontoWeb.db.DAOAtraso;
import pontoWeb.db.DAOPeriodo;
import pontoWeb.db.DAO_At_Periodo;
import pontoWeb.model.Dia;
import pontoWeb.model.Periodo;
import pontoWeb.view.AtrasoView;
import pontoWeb.view.HorarioDeTrabalhoView;
import pontoWeb.view.JanelaPrincipal;
import pontoWeb.view.MarcacoesFeitasView;
import pontoWeb.view.TabelaView;

public class AtrasoController {

	private JanelaPrincipal context;
	private DiaController diaController;
	private PeriodoController periodoController;
	private DAOPeriodo daoPeriodo;
	private DAO_At_Periodo daoat_periodo;
	private ConnectionFactory connection;
	
	public AtrasoController(JanelaPrincipal context, ConnectionFactory connection) throws SQLException {
		this.connection = connection;
		this.context = context;
		this.diaController = new DiaController();
		this.periodoController = new PeriodoController();
		this.daoPeriodo = new DAOPeriodo(this.connection);
		this.daoat_periodo = new DAO_At_Periodo(this.connection);
	}
	
	public AtrasoController() {
		this.diaController = new DiaController();
		this.periodoController = new PeriodoController();
	}

	public void subAtraso() {
		this.context.atrasoView.setTable(calculaAtrasos(this.context.horarioDeTrabalhoView,this.context.marcacoesFeitasView,this.context.atrasoView));
		this.context.atrasoView.renderTable();
	}
	
	private JTable calculaAtrasos(HorarioDeTrabalhoView horarioTrabalhoView, MarcacoesFeitasView marcacoesFeitasView, AtrasoView atrasoView) {
		Dia horarioTrabalhoDia = new Dia();		
		horarioTrabalhoDia = this.diaController.getConvertedDay(horarioTrabalhoView);
		Dia marcacoesFeitasDia = new Dia();
		marcacoesFeitasDia = this.diaController.getConvertedDay(marcacoesFeitasView);
		Dia atrasoDia = this.diaController.getAtrasos(horarioTrabalhoDia, marcacoesFeitasDia);
		List<Periodo> listaPeriodosAtrasados = this.periodoController.getPeriods(atrasoDia);
		TabelaView tableView = atrasoView.newTable(listaPeriodosAtrasados);
		atrasoView.setListEntries(tableView.getListEntries());
		atrasoView.setListExits(tableView.getListExits());
		return atrasoView.getTable();
	}
	
	public List<Periodo> calculaAtrasosWeb(String json) {
		Dia horarioTrabalhoDia = this.diaController.getConvertedDayHT(json);
		Dia marcacoesFeitasDia = this.diaController.getConvertedDayMF(json);
		Dia atrasoDia = this.diaController.getAtrasos(horarioTrabalhoDia, marcacoesFeitasDia);
		List<Periodo> listaPeriodosAtrasados = this.periodoController.getPeriods(atrasoDia);
		return listaPeriodosAtrasados;
	}

	public int saveAtrasos(AtrasoView atrasoView) throws SQLException {		
		DAOAtraso daoAtraso = new DAOAtraso(this.connection);
		//CRIA NOVO REGISTRO DO TIPO ATRASO NO BANCO 
		Integer idAt = daoAtraso.insert();
		//CRIA NOVOS REGISTROS DO TIPO PERIODO (PERIODOS DE ATRASO) NO BANCO
		for(int i = 0; i<atrasoView.getTableModel().getRowCount();i++) {
			int idperiodo = this.daoPeriodo.insert(this.periodoController.localTimeToNumber(atrasoView.getListEntries().get(i)), this.periodoController.localTimeToNumber(atrasoView.getListExits().get(i)));
			this.daoat_periodo.insert(idAt, idperiodo);
		}
		return idAt;
	}
	
	//RECEBE UM ID DE ATRASO E RETORNA TODOS OS PERIODOS (PERIODOS DE ATRASO) DAQUELE DIA
	public List<Periodo> getPeriods(int id_at) throws SQLException {
		List<Integer> list_idPeriods = this.daoat_periodo.getPeriods(id_at);
		ArrayList<Periodo> listPeriods = new ArrayList<Periodo>();
		for(Integer idPeriod: list_idPeriods) {
			Periodo periodo = this.daoPeriodo.getPeriodoByID(idPeriod);
			listPeriods.add(periodo);
		}		
		return listPeriods;
	}
}
