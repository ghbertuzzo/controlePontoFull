package pontoWeb.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_He_Periodo {

	private ConnectionFactory connection;

	public DAO_He_Periodo(ConnectionFactory connection) throws SQLException {
		this.connection = connection;
	}

	public void insert(int idHe, int idPeriodo) throws SQLException {
		String querysql = "INSERT INTO \"schemaControlePonto\".he_periodo(id, id_he, id_periodo) VALUES (default, ?, ?);";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ps.setInt(1, idHe);
		ps.setInt(2, idPeriodo);
		ps.execute();
		ps.close();
	}
	
	public List<Integer> getPeriods(int idHE) throws SQLException {
		List<Integer> listIds = new ArrayList<Integer>();
		String querysql = "SELECT id_periodo FROM \"schemaControlePonto\".he_periodo WHERE id_he = ?;";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ps.setInt(1, idHE);
		ResultSet rs;
		rs = ps.executeQuery();
		while (rs.next()) {
			listIds.add(rs.getInt(1));
		}		
		ps.close();
		return listIds;
	}
}
