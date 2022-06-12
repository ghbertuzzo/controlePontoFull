package pontoWeb.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pontoWeb.db.ConnectionFactoryDB;
import pontoWeb.db.DAOPeriodo;
import pontoWeb.db.DAO_He_Periodo;
import pontoWeb.model.Dia;
import pontoWeb.model.Periodo;

public class HoraExtraController {

	private DiaController diaController;
	private PeriodoController periodoController;

	public HoraExtraController() {
		this.diaController = new DiaController();
		this.periodoController = new PeriodoController();
	}

	public List<Periodo> calculaHoraExtraWeb(String json) {
		// CONVERTE EM DIA DE TRABALHO
		Dia horarioTrabalhoDia = this.diaController.getConvertedDayHT(json);

		// CONVERTE EM DIA DE MARCAÇÕES FEITAS
		Dia marcacoesFeitasDia = this.diaController.getConvertedDayMF(json);

		// CALCULA O DIA DE HORA EXTRAS
		Dia horaextraDia = this.diaController.getHoraExtra(marcacoesFeitasDia, horarioTrabalhoDia);

		// RETORNA LISTA DE PERÍODOS DE HORA EXTRA
		List<Periodo> listaPeriodosHoraExtra = this.periodoController.getPeriods(horaextraDia);
		return listaPeriodosHoraExtra;
	}

	// RECEBE UM ID DE HORAEXTRA E RETORNA TODOS OS PERIODOS (PERIODOS DE HORAEXTRA) DAQUELE DIA
	public List<Periodo> getPeriods(int id_he, ConnectionFactoryDB connection) throws SQLException{
		DAO_He_Periodo dao_he_periodo = new DAO_He_Periodo(connection);
		List<Integer> list_idPeriods = dao_he_periodo.getPeriods(id_he);
		ArrayList<Periodo> listPeriods = new ArrayList<Periodo>();
		DAOPeriodo daoPeriodo = new DAOPeriodo(connection);
		for (Integer idPeriod : list_idPeriods) {
			Periodo periodo = daoPeriodo.getPeriodoByID(idPeriod);
			listPeriods.add(periodo);
		}
		return listPeriods;
	}

}
