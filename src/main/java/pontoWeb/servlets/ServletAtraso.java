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
import pontoWeb.db.ConnectionFactoryDB;
import pontoWeb.model.JSON_Periodos;
import pontoWeb.model.Periodo;

@WebServlet(urlPatterns = { "/atrasos" })
public class ServletAtraso extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AtrasoController atrasoController;
	private ConnectionFactoryDB connection;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// CRIA UMA CONEXÃO COM O BANCO
		try {
			this.connection = new ConnectionFactoryDB();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// CONVERTE P/ JSONSTR CONTEÚDO DA REQUEST
		String requestData = request.getReader().lines().collect(Collectors.joining());

		// EFETUA CÁLCULOS PARA OBTER TODOS OS PERÍODOS DE ATRASO
		this.atrasoController = new AtrasoController(this.connection);
		List<Periodo> periodosAtrasos = this.atrasoController.calculaAtrasosWeb(requestData);

		// CONVERTE PERÍODOS DE ATRASO EM STRING
		JSON_Periodos atrasoslist = new JSON_Periodos();
		for (Periodo p : periodosAtrasos) {
			atrasoslist.getPeriodos().add(p.getEntrada().toString());
			atrasoslist.getPeriodos().add(p.getSaida().toString());
		}
		Gson gson = new Gson();
		String listaAtrasosJSONStr = gson.toJson(atrasoslist);

		// CONVERTE STR EM JSON
		JsonObject listaAtrasosJSON = new Gson().fromJson(listaAtrasosJSONStr, JsonObject.class);

		// ENVIA RESPOSTA DA REQUEST
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(listaAtrasosJSON);
		out.flush();
	}

}
