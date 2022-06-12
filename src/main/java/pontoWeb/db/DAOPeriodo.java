package pontoWeb.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import pontoWeb.controller.PeriodoController;
import pontoWeb.model.Periodo;

public class DAOPeriodo {

	private ConnectionFactoryDB connection;

	public DAOPeriodo(ConnectionFactoryDB connection) throws SQLException {
		this.connection = connection;
	}

	public Periodo getPeriodoByID(int idPeriodo) throws SQLException {
		Periodo periodo = null;
		PeriodoController periodoController = new PeriodoController();
		String querysql = "SELECT entry, exit FROM \"schemaControlePonto\".periodo WHERE id = ?;";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ps.setInt(1, idPeriodo);
		ResultSet rs;
		rs = ps.executeQuery();
		while (rs.next()) {
			LocalTime entrada = periodoController.numberToLocalTime(rs.getInt(1));
			LocalTime saida = periodoController.numberToLocalTime(rs.getInt(2));
			periodo = new Periodo(entrada, saida);
		}
		ps.close();
		return periodo;
	}

}
