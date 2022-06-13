package pontoWeb.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pontoWeb.controller.AtrasoController;
import pontoWeb.controller.HistoricoController;
import pontoWeb.controller.HoraExtraController;
import pontoWeb.controller.HorarioDeTrabalhoController;
import pontoWeb.controller.MarcacoesFeitasController;
import pontoWeb.db.ConnectionFactoryDB;
import pontoWeb.model.Historico;
import pontoWeb.model.JSON_Periodos;
import pontoWeb.model.Periodo;

@WebServlet(urlPatterns = { "/carregar" })
public class ServletCarregar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ConnectionFactoryDB connection;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Historico historico = null;
		List<Periodo> listaPeriodosHorararioTrabalho = null;
		List<Periodo> listaPeriodosMarcacoesFeitas = null;
		List<Periodo> listaPeriodosHoraExtra = null;
		List<Periodo> listaPeriodosAtrasos = null;
		// CRIA UMA CONEXÃO COM O BANCO
		try {
			this.connection = new ConnectionFactoryDB();
			// CONVERTE P/ JSONSTR CONTEÚDO DA REQUEST
			String requestData = request.getReader().lines().collect(Collectors.joining());
			
			//BUSCA HISTÓRICO
			HistoricoController historicoController = new HistoricoController(this.connection);
			historico = historicoController.getHistorico(requestData);
			
			//SE EXISTE HISTORICO FAZ:
			if(historico!=null) {
				//BUSCA PERIODOS DE HT DO DIA
				HorarioDeTrabalhoController htController = new HorarioDeTrabalhoController(connection);
				listaPeriodosHorararioTrabalho = htController.getPeriods(historico.getId_ht());
				
				//BUSCA PERIODOS DE MF DO DIA
				MarcacoesFeitasController mfController = new MarcacoesFeitasController(connection);
				listaPeriodosMarcacoesFeitas = mfController.getPeriods(historico.getId_mf());
				
				//BUSCA PERIODOS DE ATRASO DO DIA
				AtrasoController atController = new AtrasoController(connection);
				listaPeriodosAtrasos = atController.getPeriods(historico.getId_at());
				
				//BUSCA PERIODOS DE HORAEXTRA DO DIA
				HoraExtraController heController = new HoraExtraController(connection);
				listaPeriodosHoraExtra = heController.getPeriods(historico.getId_he());
				
				// CONVERTE PERÍODOS EM STRING
				JSON_Periodos listaPeriodos = new JSON_Periodos();
				for (Periodo p : listaPeriodosHorararioTrabalho) {
					listaPeriodos.getPeriodos().add(p.getEntrada().toString());
					listaPeriodos.getPeriodos().add(p.getSaida().toString());
				}
				//ADD SEPARADOR (FIM HT)
				listaPeriodos.getPeriodos().add("-");
				for (Periodo p : listaPeriodosMarcacoesFeitas) {
					listaPeriodos.getPeriodos().add(p.getEntrada().toString());
					listaPeriodos.getPeriodos().add(p.getSaida().toString());
				}
				//ADD SEPARADOR (FIM MF)
				listaPeriodos.getPeriodos().add("-");				
				for (Periodo p : listaPeriodosAtrasos) {
					listaPeriodos.getPeriodos().add(p.getEntrada().toString());
					listaPeriodos.getPeriodos().add(p.getSaida().toString());
				}
				//ADD SEPARADOR (FIM AT)
				listaPeriodos.getPeriodos().add("-");
				for (Periodo p : listaPeriodosHoraExtra) {
					listaPeriodos.getPeriodos().add(p.getEntrada().toString());
					listaPeriodos.getPeriodos().add(p.getSaida().toString());
				}				
				Gson gson = new Gson();
				String listaPeriodosJSONStr = gson.toJson(listaPeriodos);
				
				// CONVERTE STR EM JSON
				JsonObject listaAtrasosJSON = new Gson().fromJson(listaPeriodosJSONStr, JsonObject.class);

				// ENVIA RESPOSTA DA REQUEST
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(listaAtrasosJSON);
				out.flush();				
			}else {
				JSON_Periodos listaPeriodos = new JSON_Periodos();
				listaPeriodos.getPeriodos().add("vazio");
				Gson gson = new Gson();
				String listaPeriodosJSONStr = gson.toJson(listaPeriodos);
				
				// CONVERTE STR EM JSON
				JsonObject listaAtrasosJSON = new Gson().fromJson(listaPeriodosJSONStr, JsonObject.class);

				// ENVIA RESPOSTA DA REQUEST
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(listaAtrasosJSON);
				out.flush();
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}

}
