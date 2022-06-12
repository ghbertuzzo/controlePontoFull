package pontoWeb.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_He_Periodo {

	private ConnectionFactoryDB connection;

	public DAO_He_Periodo(ConnectionFactoryDB connection) throws SQLException {
		this.connection = connection;
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
