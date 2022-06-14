package pontoWeb.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pontoWeb.controller.HistoricoController;
import pontoWeb.db.ConnectionFactoryDB;
import pontoWeb.model.Historico;
import pontoWeb.model.JSON_Periodos;

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
				//GERA LISTA DE PERIODOS COMPLETA
				JSON_Periodos listaPeriodos = historicoController.generateListPeriods(historico);				
				
				//CONVERTE LISTA EM STRING JSON
				Gson gson = new Gson();
				String listaPeriodosJSONStr = gson.toJson(listaPeriodos);
				
				// CONVERTE STRING EM JSON
				JsonObject listaAtrasosJSON = new Gson().fromJson(listaPeriodosJSONStr, JsonObject.class);

				// ENVIA RESPOSTA DA REQUEST
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(listaAtrasosJSON);
				out.flush();				
			} else {
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
