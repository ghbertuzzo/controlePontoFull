package pontoWeb.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_Ht_Periodo {

	private ConnectionFactoryDB connection;

	public DAO_Ht_Periodo(ConnectionFactoryDB connection) throws SQLException {
		this.connection = connection;
	}

	public void insert(int idHT, int idPeriodo) throws SQLException {
		String querysql = "INSERT INTO \"schemaControlePonto\".ht_periodo(id, id_ht, id_periodo) VALUES (default, ?, ?);";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ps.setInt(1, idHT);
		ps.setInt(2, idPeriodo);
		ps.execute();
		ps.close();
	}

	public List<Integer> getPeriods(int idHT) throws SQLException {
		List<Integer> listIds = new ArrayList<Integer>();
		String querysql = "SELECT id_periodo FROM \"schemaControlePonto\".ht_periodo WHERE id_ht = ?;";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ps.setInt(1, idHT);
		ResultSet rs;
		rs = ps.executeQuery();
		while (rs.next()) {
			listIds.add(rs.getInt(1));
		}
		ps.close();
		return listIds;
	}

}
