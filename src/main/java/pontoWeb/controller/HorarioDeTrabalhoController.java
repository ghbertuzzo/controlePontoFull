package pontoWeb.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pontoWeb.db.ConnectionFactory;
import pontoWeb.db.DAOHorarioDeTrabalho;
import pontoWeb.db.DAOPeriodo;
import pontoWeb.db.DAO_Ht_Periodo;
import pontoWeb.model.Periodo;
import pontoWeb.view.HorarioDeTrabalhoView;

public class HorarioDeTrabalhoController {	
	
	private DAOPeriodo daoPeriodo;
	private DAO_Ht_Periodo daoht_periodo;
	private ConnectionFactory connection;
	
	public HorarioDeTrabalhoController(ConnectionFactory connection) throws SQLException {
		this.connection = connection;
		this.daoPeriodo = new DAOPeriodo(this.connection);
		this.daoht_periodo = new DAO_Ht_Periodo(this.connection);
	}

	public int saveHorarioDeTrabalho(HorarioDeTrabalhoView horarioDeTrabalhoView) throws SQLException {
		PeriodoController periodoController = new PeriodoController();
		DAOHorarioDeTrabalho daoHt = new DAOHorarioDeTrabalho(this.connection);
		//CRIA NOVO REGISTRO DO TIPO HORARIODETRABALHO NO BANCO 
		Integer idHT = daoHt.insert();
		//CRIA NOVOS REGISTROS DO TIPO PERIODO (PERIODOS DE HORARIODETRABALHO) NO BANCO
		for(int i = 0; i<horarioDeTrabalhoView.getTableModel().getRowCount();i++) {
			int idperiodo = this.daoPeriodo.insert(periodoController.localTimeToNumber(horarioDeTrabalhoView.getListEntries().get(i)), periodoController.localTimeToNumber(horarioDeTrabalhoView.getListExits().get(i)));
			this.daoht_periodo.insert(idHT, idperiodo);
		}	
		return idHT;		
	}

	//RECEBE UM ID DE HORARIO DE TRABALHO E RETORNA TODOS OS PERIODOS (PERIODOS DE HORARIO DE TRABALHO) DAQUELE DIA
	public List<Periodo> getPeriods(int id_ht) throws SQLException {	
		List<Integer> list_idPeriods = this.daoht_periodo.getPeriods(id_ht);
		ArrayList<Periodo> listPeriods = new ArrayList<Periodo>();
		for(Integer idPeriod: list_idPeriods) {
			Periodo periodo = this.daoPeriodo.getPeriodoByID(idPeriod);
			listPeriods.add(periodo);
		}		
		return listPeriods;
	}

}
