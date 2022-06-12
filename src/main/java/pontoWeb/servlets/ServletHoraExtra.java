package pontoWeb.servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		//CONVERTE P/ JSONSTR CONTEÚDO DA REQUEST
		String requestData = request.getReader().lines().collect(Collectors.joining());

		//EFETUA CÁLCULOS PARA OBTER TODOS OS PERÍODOS DE HORA EXTRA
		HoraExtraController horaExtraController = new HoraExtraController();
		List<Periodo> periodosHoraExtra = horaExtraController.calculaHoraExtraWeb(requestData);
		
		//CONVERTE PERÍODOS DE HORA EXTRA EM STRING
		JSON_Periodos horaextralist = new JSON_Periodos();
		for(Periodo p: periodosHoraExtra) {
			horaextralist.getPeriodos().add(p.getEntrada().toString());
			horaextralist.getPeriodos().add(p.getSaida().toString());
		}
		Gson gson = new Gson(); 
		String listaHoraExtraJSONStr = gson.toJson(horaextralist);

		
		//CONVERTE STR EM JSON
		JsonObject listaHoraExtraJSON = new Gson().fromJson(listaHoraExtraJSONStr, JsonObject.class);
		
		//ENVIA RESPOSTA DA REQUEST
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(listaHoraExtraJSON);
		out.flush();
	}

}
