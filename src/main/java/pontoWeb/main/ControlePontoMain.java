package pontoWeb.main;

import java.sql.SQLException;

import pontoWeb.db.ConnectionFactory;
import pontoWeb.view.JanelaPrincipal;
import net.sf.jasperreports.engine.JRException;

public class ControlePontoMain {

	public static void main(String[] args) throws SQLException, JRException, ClassNotFoundException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		JanelaPrincipal start = new JanelaPrincipal(connectionFactory);
		start.setVisible(true);
	}

}
