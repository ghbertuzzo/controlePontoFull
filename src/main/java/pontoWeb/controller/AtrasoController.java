package pontoWeb.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pontoWeb.db.ConnectionFactoryDB;
import pontoWeb.db.DAOPeriodo;
import pontoWeb.db.DAO_At_Periodo;
import pontoWeb.model.Dia;
import pontoWeb.model.Periodo;

public class AtrasoController {

	private DiaController diaController;
	private PeriodoController periodoController;

	public AtrasoController() {
		this.diaController = new DiaController();
		this.periodoController = new PeriodoController();
	}

	public List<Periodo> calculaAtrasosWeb(String json) {
		// CONVERTE EM DIA DE TRABALHO
		Dia horarioTrabalhoDia = this.diaController.getConvertedDayHT(json);

		// CONVERTE EM DIA DE MARCAÇÕES FEITAS
		Dia marcacoesFeitasDia = this.diaController.getConvertedDayMF(json);

		// CALCULA O DIA DE HORA EXTRAS
		Dia atrasoDia = this.diaController.getAtrasos(horarioTrabalhoDia, marcacoesFeitasDia);

		// RETORNA LISTA DE PERÍODOS DE ATRASO
		List<Periodo> listaPeriodosAtrasados = this.periodoController.getPeriods(atrasoDia);
		return listaPeriodosAtrasados;
	}

	// RECEBE UM ID DE ATRASO E RETORNA TODOS OS PERIODOS (PERIODOS DE ATRASO)DAQUELE DIA
	public List<Periodo> getPeriods(int id_at, ConnectionFactoryDB connection) throws SQLException {
		DAO_At_Periodo dao_at_periodo = new DAO_At_Periodo(connection);
		List<Integer> list_idPeriods = dao_at_periodo.getPeriods(id_at);
		ArrayList<Periodo> listPeriods = new ArrayList<Periodo>();
		DAOPeriodo daoPeriodo = new DAOPeriodo(connection);
		for (Integer idPeriod : list_idPeriods) {
			Periodo periodo = daoPeriodo.getPeriodoByID(idPeriod);
			listPeriods.add(periodo);
		}
		return listPeriods;
	}
}
