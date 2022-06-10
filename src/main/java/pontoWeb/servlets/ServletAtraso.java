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
import pontoWeb.model.JSON_Periodos;
import pontoWeb.model.Periodo;

@WebServlet(urlPatterns = { "/atrasos" })
public class ServletAtraso extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletAtraso() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//string conteudo json
		String requestData = request.getReader().lines().collect(Collectors.joining());
		//calcular atrasos
		AtrasoController atrasoController = null;
		try {
			atrasoController = new AtrasoController();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Periodo> periodosAtrasos = atrasoController.calculaAtrasosWeb(requestData);
		//converter periodo de atrasos em string
		JSON_Periodos atrasoslist = new JSON_Periodos();
		for(Periodo p: periodosAtrasos) {
			atrasoslist.getPeriodos().add(p.getEntrada().toString());
			atrasoslist.getPeriodos().add(p.getSaida().toString());
		}
		Gson gson = new Gson(); 
		String myjson = gson.toJson(atrasoslist); // converts to json
	    System.out.println(myjson);	
		
		//converte string em json
		JsonObject convertedObject = new Gson().fromJson(myjson, JsonObject.class);
		
		//envia response com json
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(convertedObject);
		out.flush();
	}

}
