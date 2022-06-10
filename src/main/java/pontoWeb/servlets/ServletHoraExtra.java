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

import pontoWeb.controller.HoraExtraController;
import pontoWeb.model.JSON_Periodos;
import pontoWeb.model.Periodo;

@WebServlet(urlPatterns = { "/horaextra" })
public class ServletHoraExtra extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletHoraExtra() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Chamou Get no /horaextra");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//string conteudo json
		String requestData = request.getReader().lines().collect(Collectors.joining());
		//calcular horaextra
		HoraExtraController heController = null;
		try {
			heController = new HoraExtraController();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Periodo> periodosAtrasos = heController.calculaHoraExtraWeb(requestData);
		//converter periodo de atrasos em string
		JSON_Periodos horaextralist = new JSON_Periodos();
		for(Periodo p: periodosAtrasos) {
			horaextralist.getPeriodos().add(p.getEntrada().toString());
			horaextralist.getPeriodos().add(p.getSaida().toString());
		}
		Gson gson = new Gson(); 
		String myjson = gson.toJson(horaextralist); // converts to json
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
