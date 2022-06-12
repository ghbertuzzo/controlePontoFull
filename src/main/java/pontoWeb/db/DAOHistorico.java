package pontoWeb.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pontoWeb.model.Historico;

public class DAOHistorico {

	private ConnectionFactoryDB connection;
	
	public DAOHistorico(ConnectionFactoryDB connection) {
		this.connection = connection;
	}
	
	public List<Historico> getHistoricos(ConnectionFactoryDB connection) throws SQLException {
		this.connection = connection;
		List<Historico> historicos = new ArrayList<Historico>();
		String querysql = "SELECT date, id, id_ht, id_mf, id_he, id_at	FROM \"schemaControlePonto\".historico ORDER BY date;";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ResultSet rs;
		rs = ps.executeQuery();
		while (rs.next()) {
			Historico historico = new Historico(rs.getDate(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),rs.getInt(6));
			historicos.add(historico);
		}
		ps.close();
		return historicos;
	}
	
}
