package pontoWeb.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import pontoWeb.controller.HistoricoController;
import pontoWeb.db.ConnectionFactoryDB;

@WebServlet(urlPatterns = { "/export" })
public class ServletReport extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private HistoricoController historicoController;
	private ConnectionFactoryDB connection;

	public ServletReport() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String caminho = request.getSession().getServletContext().getRealPath("WEB-INF/classes/pontoWeb/reports" + File.separator + "RelatorioExtratoDia_2.jrxml");
		try {
			this.connection = new ConnectionFactoryDB();
			this.historicoController = new HistoricoController(connection);
			this.historicoController.generateReport(this.connection, caminho, request);
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType("application/pdf");
		response.setHeader("Content-Type", "application/pdf");
		response.addHeader("Content-Disposition", "attachment; filename=report.pdf");
		caminho = request.getSession().getServletContext().getRealPath("WEB-INF/classes/pontoWeb/reports/");
		File filePDF = new File(caminho + "myreport.pdf");
		FileInputStream fis = new FileInputStream(filePDF);
		ServletOutputStream os = response.getOutputStream();

		try {
			response.setContentLength((int) filePDF.length());
			int byteRead = 0;
			while ((byteRead = fis.read()) != -1) {
				os.write(byteRead);
			}
			os.flush();
		} catch (Exception excp) {
			excp.printStackTrace();
		} finally {
			os.close();
			fis.close();
		}

	}

}
