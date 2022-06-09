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

import pontoWeb.controller.AtrasoController;
import pontoWeb.model.Periodo;

@WebServlet(urlPatterns = { "/atrasos" })
public class ServletAtraso extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletAtraso() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Chamou Get no /atrasos");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//string conteudo json
		String requestData = request.getReader().lines().collect(Collectors.joining());
		//calcular atrasos
		AtrasoController atrasoController = new AtrasoController();
		List<Periodo> periodosAtrasos = atrasoController.calculaAtrasosWeb(requestData);
		System.err.println("Chegou ak");
		
		//formatar string no padrão json
		
		
		//converte string em json
		JsonObject convertedObject = new Gson().fromJson(requestData, JsonObject.class);
		
		//envia response com json
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(convertedObject);
		out.flush();
	}

}
