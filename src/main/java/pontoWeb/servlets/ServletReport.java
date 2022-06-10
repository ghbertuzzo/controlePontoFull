package pontoWeb.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pontoWeb.controller.HistoricoController;
import pontoWeb.db.ConnectionFactory;
import pontoWeb.model.ExtratoDia;
import pontoWeb.model.Historico;

@WebServlet(urlPatterns = { "/export" })
public class ServletReport extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletReport() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ConnectionFactory connection = null;
		try {
			connection = new ConnectionFactory();
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HistoricoController historicoController = null;
		List<Historico> historicos = null;
		List<ExtratoDia> listExtrato = null;
		try {
			historicoController = new HistoricoController(connection);
			System.out.println("Buscando historico!");
			historicos = historicoController.getHistoricos();
			System.out.println("Captou historico!");
			if (historicos != null) {
				historicoController.exportReportHistorico(historicos);	
				System.out.println("Gerando relatório");
			}
			historicoController.generateReportWeb(listExtrato);
			System.out.println("Relatório gerado");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Erro!");
		}		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Chamou Post no /export");
		doGet(request, response);
	}

}
