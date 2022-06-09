package pontoWeb.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pontoWeb.db.ConnectionFactory;
import pontoWeb.db.DAOMarcacoesFeitas;
import pontoWeb.db.DAOPeriodo;
import pontoWeb.db.DAO_Mf_Periodo;
import pontoWeb.model.Periodo;
import pontoWeb.view.MarcacoesFeitasView;

public class MarcacoesFeitasController {	
	
	private DAOPeriodo daoPeriodo;
	private DAO_Mf_Periodo daomf_periodo;
	private ConnectionFactory connection;
	
	public MarcacoesFeitasController(ConnectionFactory connection) throws SQLException {
		this.connection = connection;
		this.daoPeriodo = new DAOPeriodo(this.connection);	
		this.daomf_periodo = new DAO_Mf_Periodo(this.connection);
	}

	public int saveMarcacoesFeitas(MarcacoesFeitasView marcacoesFeitasView) throws SQLException {
		PeriodoController periodoController = new PeriodoController();
		DAOMarcacoesFeitas daoMarcacoesFeitas = new DAOMarcacoesFeitas(this.connection);
		//CRIA NOVO REGISTRO DO TIPO MARCACOESFEITAS NO BANCO 
		Integer idMF = daoMarcacoesFeitas.insert();
		//CRIA NOVOS REGISTROS DO TIPO PERIODO (PERIODOS DE MARCACOESFEITAS) NO BANCO
		for(int i = 0; i<marcacoesFeitasView.getTableModel().getRowCount();i++) {
			int idperiodo = this.daoPeriodo.insert(periodoController.localTimeToNumber(marcacoesFeitasView.getListEntries().get(i)), periodoController.localTimeToNumber(marcacoesFeitasView.getListExits().get(i)));
			this.daomf_periodo.insert(idMF, idperiodo);
		}
		return idMF;
	}
	
	//RECEBE UM ID DE MARCACOES FEITA E RETORNA TODOS OS PERIODOS (PERIODOS DE MARCACOES FEITA) DAQUELE DIA
	public List<Periodo> getPeriods(int id_mf) throws SQLException {
		List<Integer> list_idPeriods = this.daomf_periodo.getPeriods(id_mf);
		ArrayList<Periodo> listPeriods = new ArrayList<Periodo>();
		for(Integer idPeriod: list_idPeriods) {
			Periodo periodo = this.daoPeriodo.getPeriodoByID(idPeriod);
			listPeriods.add(periodo);
		}		
		return listPeriods;
	}

}
