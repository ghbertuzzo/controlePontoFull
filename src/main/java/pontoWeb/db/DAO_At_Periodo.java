package pontoWeb.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_At_Periodo {

	private ConnectionFactoryDB connection;

	public DAO_At_Periodo(ConnectionFactoryDB connection) throws SQLException {
		this.connection = connection;
	}
	
	public List<Integer> getPeriods(int idAT) throws SQLException {
		List<Integer> listIds = new ArrayList<Integer>();
		String querysql = "SELECT id_periodo FROM \"schemaControlePonto\".at_periodo WHERE id_at = ?;";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ps.setInt(1, idAT);
		ResultSet rs;
		rs = ps.executeQuery();
		while (rs.next()) {
			listIds.add(rs.getInt(1));
		}
		ps.close();
		return listIds;
	}
}
